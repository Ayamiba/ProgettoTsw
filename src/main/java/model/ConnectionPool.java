package model;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class ConnectionPool {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/saendwave"
            + "?useUnicode=true&useJDBCCompliantTimezoneShift=true"
            + "&useLegacyDatetimecode=false&serverTimezone=UTC";

    private static final String USERNAME = "root";
    private static final String PASSWORD = "Ayamiba997!";

    private static int INITIAL_POOL_SIZE = 5; //InitialePoolSize è il numero minimo di connessioni aperte
    private static final int MAX_POOL_SIZE = 20; //MaxPoolSize è il numero massimo di connessioni apribili

    private static List<Connection> freeDbConnections; //Lista Di connessioni aperte che verrà riempita
    private static int currentPoolSize = 0; //Teniamo il conto delle connessioni aperte
    private static boolean initialized = false; //Variabile di controllo se le connessioni sono state già inizializzate

    public static synchronized void init(int poolSize) throws SQLException {
        if (initialized) { //Controlliamo se è stata inizializzata la connessione
            return;
        }

        INITIAL_POOL_SIZE = poolSize > 0 ? poolSize : INITIAL_POOL_SIZE; //Controlliamo l'inizializzazione e salviamo in InitialPoolSize

        freeDbConnections = new LinkedList<>(); //Inserimento delle connessioni nella lista
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); //Caricamento dei Driver peri l DataBase

            for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
                freeDbConnections.add(createDBConnection()); //Creazione delle connessioni in base ad InitialPoolSize
            }
            currentPoolSize = INITIAL_POOL_SIZE; //Aggiorna il numero di connessioni aperte correntemente
            initialized = true; //Aggiorniamo initialized per indicare che l'inizializzazione è già avvenuta
        } catch (ClassNotFoundException e) {
            initialized = false;
            throw new SQLException("MySQL JDBC Driver not found or failed to load.", e); //Se l'inizializzazione fallisce allora stampa questo errore
        } catch (SQLException e) {
            initialized = false;
            throw e;
        }
    }

    private static Connection createDBConnection() throws SQLException { //Metodo per creare le singole Connessioni
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        connection.setAutoCommit(true); //Salva i dati quando viene stabilita la connessione
        return connection;
    }

    public static synchronized Connection getConnection() throws SQLException { //Metodo chiamato quando eseguiamo una query
        if (!initialized || freeDbConnections == null) { //Verifica che l'inizializzazione sia avvenuta e ci siano connessioni libere, altrimenti errore
            throw new SQLException("[ConnectionPool] Pool is not initialized.");
        }

        if (freeDbConnections.isEmpty()) { //Verifica se non abbiamo connessioni libere
            if (currentPoolSize < MAX_POOL_SIZE) { //Verifica se possiamo aprire altre connessioni, se sì le crea, altrimenti stampare che non ce ne sono disponibili
                Connection conn = createDBConnection();
                currentPoolSize++;
                return conn;
            } else {
                throw new SQLException("[ConnectionPool] Pool exhausted: max connections reached (" + MAX_POOL_SIZE + ").");
            }
        }

        Connection conn = freeDbConnections.remove(0); //Se ci sono connessioni disponibili ne occupa una

        try {
            if (!conn.isValid(1)) { //Controlla se la connessione è valida
                try {
                    conn.close(); //Se non è valida, chiudila
                } catch (SQLException ignored) {
                }
                currentPoolSize--; //Abbassa il contatore delle connessioni correnti
                return getConnection();
            }
        } catch (SQLException e) {
            currentPoolSize--;
            return getConnection();
        }

        return conn;
    }

    public static synchronized void releaseConnection(Connection connection) {
        if (connection != null && initialized) { //Verifica che la connessione sia valida ed inizializzata
            try {
                if (!connection.isClosed() && connection.isValid(1)) { //Verifica che la connessione sia aperta valida, se sì aggiungila alla lista
                    freeDbConnections.add(connection);
                } else {
                    connection.close(); //Altrimenti chiudila
                    currentPoolSize--;
                }
            } catch (SQLException ignored) {
            }
        } else if (connection != null) { //Se la connessione è nulla chiudila
            try {
                connection.close();
            } catch (SQLException ignored) {
            }
            currentPoolSize--;
        }
    }

    public static synchronized boolean isInitialized() { //Controlla se al pool è stata effettivamente avviata. 
    	                                                // Synchronized consente la lettura in caso di un thread che sta modificando in quel momento, leggendo quindi il valore corretto
        return initialized;
    }

    public static synchronized void shutdown() {
        if (!initialized) { //Se non è stato inizializzato interrompi
            return;
        }

        for (Connection conn : freeDbConnections) { //Per ogni connessione attiva, chiudila
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException ignored) {
            }
        }
        if (freeDbConnections != null) { // Svuota la lista di tutti gli oggetti Connection ormai inutilizzati
            freeDbConnections.clear(); 
        }
        initialized = false; //Aggiorniamo indicando che non c'è nessuna inizializzazione
        currentPoolSize = 0; //Impostiamo a 0 le connessioni correnti

        releaseResources();
    }

    public static void releaseResources() { //
        java.util.Enumeration<Driver> drivers = DriverManager.getDrivers(); //Richiediamo e troviamo tutti i driver attualmente attivi
        while (drivers.hasMoreElements()) { //Per ogni driver nell'elenco lo scolleghiamo e liberiamo risorse
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException ignored) {
            }
        }

        try {
            Class<?> clazz = Class.forName("com.mysql.cj.jdbc.AbandonedConnectionCleanupThread");
            clazz.getMethod("checkedShutdown").invoke(null);
        } catch (Exception ignored) {
        }
    }
}