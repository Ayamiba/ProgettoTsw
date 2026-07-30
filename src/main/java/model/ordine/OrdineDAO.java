package model.ordine;

import model.ConnectionPool;
import model.DAOInterface;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrdineDAO implements DAOInterface<OrdineBean, Integer> {

    public OrdineDAO() {}

    @Override
    public OrdineBean doRetrieveByKey(Integer idOrdine) throws SQLException {
        if (idOrdine == null) return null;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        OrdineBean ordine = null;

        String query = "SELECT ID_ordine, data_ordine, totale, stato, descrizione, FK_traccia, FK_metodo_pagamento, file_consegnato FROM Ordine WHERE ID_ordine = ?";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, idOrdine); // Autoboxing da Integer a int automatico
            
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                ordine = new OrdineBean();
                ordine.setIdOrdine(resultSet.getInt("ID_ordine"));
                ordine.setDataOrdine(resultSet.getDate("data_ordine"));
                ordine.setTotale(resultSet.getFloat("totale"));
                ordine.setStato(resultSet.getString("stato"));
                ordine.setDescrizione(resultSet.getString("descrizione"));
                ordine.setfKTraccia(resultSet.getInt("FK_traccia"));
                ordine.setfKMetodoPagamento(resultSet.getLong("FK_metodo_pagamento"));
                ordine.setFileConsegnato(resultSet.getString("file_consegnato"));
            }
        } finally {
            try { if (resultSet != null) resultSet.close(); } finally {
                try { if (statement != null) statement.close(); } finally {
                    ConnectionPool.releaseConnection(connection);
                }
            }
        }
        return ordine;
    }

    @Override
    public List<OrdineBean> doRetrieveAll() throws SQLException {
        List<OrdineBean> ordini = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        String query = "SELECT ID_ordine, data_ordine, totale, stato, descrizione, FK_traccia, FK_metodo_pagamento, file_consegnato FROM Ordine";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                OrdineBean ordine = new OrdineBean();
                ordine.setIdOrdine(resultSet.getInt("ID_ordine"));
                ordine.setDataOrdine(resultSet.getDate("data_ordine"));
                ordine.setTotale(resultSet.getFloat("totale"));
                ordine.setStato(resultSet.getString("stato"));
                ordine.setDescrizione(resultSet.getString("descrizione"));
                ordine.setfKTraccia(resultSet.getInt("FK_traccia"));
                ordine.setfKMetodoPagamento(resultSet.getLong("FK_metodo_pagamento"));
                ordine.setFileConsegnato(resultSet.getString("file_consegnato"));
                ordini.add(ordine);
            }
        } finally {
            try { if (resultSet != null) resultSet.close(); } finally {
                try { if (statement != null) statement.close(); } finally {
                    ConnectionPool.releaseConnection(connection);
                }
            }
        }
        return ordini;
    }
    
 // Metodo per estrarre tutti gli ordini di uno specifico utente (unendo Ordine e TracciaAudio)
    public List<OrdineBean> doRetrieveByUtente(String emailUtente) throws java.sql.SQLException {
        java.sql.Connection connection = null;
        java.sql.PreparedStatement statement = null;
        java.sql.ResultSet resultSet = null;
        List<OrdineBean> ordini = new java.util.ArrayList<>();

        // Join tra Ordine e TracciaAudio per risalire alla mail dell'utente
        String query = "SELECT o.* FROM Ordine o " +
                       "JOIN TracciaAudio t ON o.FK_traccia = t.ID_traccia " +
                       "WHERE t.FK_utente = ? ORDER BY o.data_ordine DESC";

        try {
            connection = model.ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, emailUtente);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                OrdineBean ordine = new OrdineBean();
                ordine.setIdOrdine(resultSet.getInt("ID_ordine"));
                ordine.setDataOrdine(resultSet.getDate("data_ordine"));
                ordine.setTotale(resultSet.getFloat("totale"));
                ordine.setStato(resultSet.getString("stato"));
                ordine.setDescrizione(resultSet.getString("descrizione"));
                ordine.setfKTraccia(resultSet.getInt("FK_traccia"));
                ordine.setfKMetodoPagamento(resultSet.getLong("FK_metodo_pagamento"));
                ordine.setFileConsegnato(resultSet.getString("file_consegnato"));
                ordini.add(ordine);
            }
        } finally {
            try { if (resultSet != null) resultSet.close(); } finally {
                try { if (statement != null) statement.close(); } finally {
                    model.ConnectionPool.releaseConnection(connection);
                }
            }
        }
        return ordini;
    }
    
 // Metodo per permettere al professionista di prendere in carico un ordine
    public boolean accettaOrdine(int idOrdine, String emailProfessionista) throws java.sql.SQLException {
        java.sql.Connection connection = null;
        java.sql.PreparedStatement statement = null;
        int result = 0;

        // La query aggiorna l'ordine se è ancora 'In attesa'
        String query = "UPDATE ordine SET stato = 'In Lavorazione', FK_email_professionista = ? WHERE ID_ordine = ? AND stato = 'In attesa'";

        try {
            connection = model.ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            
            statement.setString(1, emailProfessionista);
            statement.setInt(2, idOrdine);
            
            result = statement.executeUpdate();
            
        } finally {
            try { if (statement != null) statement.close(); } finally {
                model.ConnectionPool.releaseConnection(connection);
            }
        }
        
        // Ritorna true se l'aggiornamento è andato a buon fine (almeno 1 riga modificata)
        return result > 0;
    }
 // Metodo per recuperare l'email del cliente a partire dall'ID dell'ordine
    public String doRetrieveEmailClienteByOrdine(int idOrdine) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String emailCliente = "Email Sconosciuta"; // Valore di fallback

        // Join tra Ordine e la tabella ponte Utilizzo per trovare l'utente
        String query = "SELECT u.FK_utente FROM Ordine o " +
                       "JOIN utilizzo u ON o.FK_metodo_pagamento = u.FK_metodopagamento " +
                       "WHERE o.ID_ordine = ?";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, idOrdine);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                emailCliente = resultSet.getString("FK_utente");
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            ConnectionPool.releaseConnection(connection);
        }
        return emailCliente;
    }
 // 2. Estrae gli ordini di uno specifico professionista in base allo stato
    public List<OrdineBean> doRetrieveByProfessionistaAndStato(String emailProfessionista, String stato) throws java.sql.SQLException {
        java.sql.Connection connection = null;
        java.sql.PreparedStatement statement = null;
        java.sql.ResultSet resultSet = null;
        List<OrdineBean> ordini = new java.util.ArrayList<>();

        String query = "SELECT * FROM ordine WHERE FK_email_professionista = ? AND stato = ? ORDER BY data_ordine DESC";

        try {
            connection = model.ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            
            // Passiamo l'email (String) invece di un ID intero
            statement.setString(1, emailProfessionista);
            statement.setString(2, stato);
            
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                OrdineBean ordine = new OrdineBean();
                ordine.setIdOrdine(resultSet.getInt("ID_ordine"));
                ordine.setDataOrdine(resultSet.getDate("data_ordine"));
                ordine.setTotale(resultSet.getFloat("totale"));
                ordine.setStato(resultSet.getString("stato"));
                ordine.setDescrizione(resultSet.getString("descrizione"));
                ordine.setfKTraccia(resultSet.getInt("FK_traccia"));
                ordine.setfKMetodoPagamento(resultSet.getLong("FK_metodo_pagamento"));
                ordine.setfkEmailProfessionista(resultSet.getString("FK_email_professionista"));
                ordine.setFileConsegnato(resultSet.getString("file_consegnato"));
                
                ordini.add(ordine);
            }
        } finally {
            try { if (resultSet != null) resultSet.close(); } finally {
                try { if (statement != null) statement.close(); } finally {
                    model.ConnectionPool.releaseConnection(connection);
                }
            }
        }
        return ordini;
    }
    
 // Metodo per estrarre tutti gli ordini liberi che non sono ancora stati presi da nessuno
    public List<OrdineBean> doRetrieveOrdiniInAttesa() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<OrdineBean> ordini = new ArrayList<>();

        // Cerca ordini In attesa dove non c'è nessuna email assegnata
        String query = "SELECT * FROM Ordine WHERE stato = 'In attesa' AND (FK_email_professionista IS NULL OR FK_email_professionista = '' OR FK_email_professionista = 'NULL') ORDER BY data_ordine ASC";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                OrdineBean ordine = new OrdineBean();
                ordine.setIdOrdine(resultSet.getInt("ID_ordine"));
                ordine.setDataOrdine(resultSet.getDate("data_ordine"));
                ordine.setTotale(resultSet.getFloat("totale"));
                ordine.setStato(resultSet.getString("stato"));
                ordine.setDescrizione(resultSet.getString("descrizione"));
                ordine.setfKTraccia(resultSet.getInt("FK_traccia"));
                ordine.setfKMetodoPagamento(resultSet.getLong("FK_metodo_pagamento"));
                ordine.setfkEmailProfessionista(resultSet.getString("FK_email_professionista"));
                ordine.setFileConsegnato(resultSet.getString("file_consegnato"));
                
                ordini.add(ordine);
            }
        } finally {
            try { if (resultSet != null) resultSet.close(); } finally {
                try { if (statement != null) statement.close(); } finally {
                    ConnectionPool.releaseConnection(connection);
                }
            }
        }
        return ordini;
    }
    
 // Metodo per segnare un ordine come Completato
    public boolean completaOrdine(int idOrdine, String nomeFileConsegnato) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        int result = 0;

        String query = "UPDATE Ordine SET stato = 'Completato', file_consegnato=? WHERE ID_ordine = ?";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(2, idOrdine);
            statement.setString(1, nomeFileConsegnato);
            
            result = statement.executeUpdate();
        } finally {
            try { if (statement != null) statement.close(); } finally {
                ConnectionPool.releaseConnection(connection);
            }
        }
        return result > 0;
    }

    @Override
    public void doSave(OrdineBean ordine) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        String query = "INSERT INTO Ordine (ID_ordine, data_ordine, totale, stato, descrizione, FK_traccia, FK_metodo_pagamento, file_consegnato) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);

            statement.setInt(1, ordine.getIdOrdine());
            statement.setDate(2, ordine.getDataOrdine());
            statement.setFloat(3, ordine.getTotale());
            statement.setString(4, ordine.getStato());
            statement.setString(5, ordine.getDescrizione());
            statement.setInt(6, ordine.getfKTraccia());
            statement.setLong(7, ordine.getfKMetodoPagamento());
            statement.setString(8, ordine.getFileConsegnato());

            statement.executeUpdate();
        } finally {
            try { if (statement != null) statement.close(); } finally {
                ConnectionPool.releaseConnection(connection);
            }
        }
    }
    
 // Cambiamo il tipo di ritorno in int per restituire l'ID generato
    public int doSaveGetId(OrdineBean ordine) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;
        int idGenerato = -1;

        // Rimuoviamo ID_ordine dalla INSERT perché è AUTO_INCREMENT
        String query = "INSERT INTO Ordine (data_ordine, totale, stato, descrizione, FK_traccia, FK_metodo_pagamento, file_consegnato) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            connection = ConnectionPool.getConnection();
            // Aggiungiamo Statement.RETURN_GENERATED_KEYS per farci restituire l'ID
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            statement.setDate(1, ordine.getDataOrdine());
            statement.setFloat(2, ordine.getTotale());
            statement.setString(3, ordine.getStato());
            statement.setString(4, ordine.getDescrizione());
            statement.setInt(5, ordine.getfKTraccia());
            statement.setLong(6, ordine.getfKMetodoPagamento());
            statement.setString(7, ordine.getFileConsegnato());

            statement.executeUpdate();

            // Recuperiamo l'ID appena creato da MySQL
            generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                idGenerato = generatedKeys.getInt(1);
                ordine.setIdOrdine(idGenerato); // Aggiorniamo subito il Bean
            }
            
        } finally {
            try { if (generatedKeys != null) generatedKeys.close(); } finally {
                try { if (statement != null) statement.close(); } finally {
                    ConnectionPool.releaseConnection(connection);
                }
            }
        }
        return idGenerato;
    }

    @Override
    public void doUpdate(OrdineBean ordine) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        // Abbiamo aggiunto FK_email_professionista = ? prima del WHERE
        String query = "UPDATE Ordine SET data_ordine = ?, totale = ?, stato = ?, descrizione = ?, FK_traccia = ?, FK_metodo_pagamento = ?, file_consegnato = ?, FK_email_professionista = ? WHERE ID_ordine = ?";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);

            statement.setDate(1, ordine.getDataOrdine());
            statement.setFloat(2, ordine.getTotale());
            statement.setString(3, ordine.getStato());
            statement.setString(4, ordine.getDescrizione());
            statement.setInt(5, ordine.getfKTraccia());
            statement.setLong(6, ordine.getfKMetodoPagamento());
            statement.setString(7, ordine.getFileConsegnato());
            
            // Parametro 8: Passiamo l'email del professionista. 
            // Se nel Bean è "null", Java inserirà automaticamente "NULL" nel database!
            statement.setString(8, ordine.getfkEmailProfessionista());
            
            // Parametro 9: L'ID dell'ordine (WHERE)
            statement.setInt(9, ordine.getIdOrdine());

            statement.executeUpdate();
        } finally {
            try { if (statement != null) statement.close(); } finally {
                ConnectionPool.releaseConnection(connection);
            }
        }
    }
    @Override
    public void doDelete(Integer idOrdine) throws SQLException {
        if (idOrdine == null) return;

        Connection connection = null;
        PreparedStatement statement = null;

        String query = "DELETE FROM Ordine WHERE ID_ordine = ?";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);

            statement.setInt(1, idOrdine);
            statement.executeUpdate();
        } finally {
            try { if (statement != null) statement.close(); } finally {
                ConnectionPool.releaseConnection(connection);
            }
        }
    }
    
    //questo metodo ci restituisce la lista contenente gli ordini presi dal database con le date in quel range
    public List <OrdineBean> doRetrieveByIntervalloData(java.sql.Date dataInizio, java.sql.Date dataFine) throws SQLException {
    	Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<OrdineBean> lista = new ArrayList<>();

        String query = "SELECT * FROM Ordine WHERE data_ordine BETWEEN ? AND ? ORDER BY data_ordine DESC";
        
        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            statement.setDate(1, dataInizio);
            statement.setDate(2, dataFine);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                OrdineBean ordine = new OrdineBean();
                ordine.setIdOrdine(resultSet.getInt("ID_ordine"));
                ordine.setDataOrdine(resultSet.getDate("data_ordine"));
                ordine.setTotale(resultSet.getFloat("totale"));
                ordine.setStato(resultSet.getString("stato"));
                ordine.setDescrizione(resultSet.getString("descrizione"));
                ordine.setfKTraccia(resultSet.getInt("FK_traccia"));
                ordine.setfKMetodoPagamento(resultSet.getLong("FK_metodo_pagamento"));
                ordine.setFileConsegnato(resultSet.getString("file_consegnato"));
                lista.add(ordine);
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            ConnectionPool.releaseConnection(connection);
        }
        return lista;
    }
    
    //questo metodo permette di ottenere una lista contenente gli ordini fatti da un dato utente (passando l'id) usando la struttura del nostro database, quindi passando per metodo pagamento
    public List<OrdineBean> doRetrieveByCliente(String idCliente) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<OrdineBean> lista = new ArrayList<>();
        
        //la query unisce ordine e metodo pagamento per risalire all'utente che ha fatto l ordine
        String query = "SELECT * FROM Ordine o " +
                "INNER JOIN metodopagamento m ON o.FK_metodo_pagamento = m.numero_carta " + 
                "WHERE m.FK_utente = ?";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, idCliente); 
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                OrdineBean ordine = new OrdineBean();
                ordine.setIdOrdine(resultSet.getInt("ID_ordine"));
                ordine.setDataOrdine(resultSet.getDate("data_ordine"));
                ordine.setTotale(resultSet.getFloat("totale"));
                ordine.setStato(resultSet.getString("stato"));
                ordine.setDescrizione(resultSet.getString("descrizione"));
                ordine.setfKTraccia(resultSet.getInt("FK_traccia"));
                ordine.setfKMetodoPagamento(resultSet.getLong("FK_metodo_pagamento"));
                ordine.setFileConsegnato(resultSet.getString("file_consegnato"));
                lista.add(ordine);
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            ConnectionPool.releaseConnection(connection);
        }
        return lista;
    }
    
    
    }