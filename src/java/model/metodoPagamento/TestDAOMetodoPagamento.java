package model.metodoPagamento;

import model.ConnectionPool;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

//compilare i file con javac model\ConnectionPool.java model\DAOInterface.java model\metodoPagamento\MetodoPagamentoBean.java model\metodoPagamento\MetodoPagamentoDAO.java model\metodoPagamento\TestDAOMetodoPagamento.java
//usare il jar esterno con java -cp ".;C:\lib\mysql-connector-j-8.0.33.jar" model.metodoPagamento.TestDAOMetodoPagamento
public class TestDAOMetodoPagamento {
	
    public static void main(String[] args) {
        System.out.println("=== INIZIO TEST METODO PAGAMENTO DAO ===");

        // --- INIZIALIZZAZIONE POOL CON GESTIONE ECCEZIONE ---
        System.out.println("\n--- Inizializzazione ConnectionPool ---");
        try {
            ConnectionPool.init(5); 
            System.out.println("[SUCCESS] ConnectionPool inizializzato correttamente!");
        } catch (SQLException e) {
            System.out.println("[ERRORE CRITICO] Inizializzazione del Pool Fallita: " + e.getMessage());
            System.out.println("=== FINE TEST (INTERROTTO) ===");
            return;
        }

        // --- VERIFICA EFFETTIVA DELLA CONNESSIONE ---
        try (Connection testConn = ConnectionPool.getConnection()) {
            if (testConn != null && !testConn.isClosed()) {
                System.out.println("[SUCCESS] Connessione al database stabilita correttamente!");
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE CRITICO] Impossibile estrarre una connessione valida!");
            return; 
        }

        MetodoPagamentoDAO metodoDAO = new MetodoPagamentoDAO();

        // 1. TEST doRetrieveByKey
        long cartaCerca = 1111222233334444L; // Usiamo la L alla fine per identificare una costante long primitiva
        System.out.println("\n--- 1. Test doRetrieveByKey (Carta: " + cartaCerca + ") ---");
        try {
            MetodoPagamentoBean metodo = metodoDAO.doRetrieveByKey(cartaCerca);
            if (metodo != null) {
                System.out.println("[SUCCESS] Metodo di pagamento trovato:");
                System.out.println(" -> Intestatario: " + metodo.getNome() + " " + metodo.getCognome());
                System.out.println(" -> CVV: " + metodo.getCvv());
            } else {
                System.out.println("[FAIL] Carta di pagamento non trovata.");
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE] Errore in doRetrieveByKey: " + e.getMessage());
        }

        // 2. TEST doRetrieveAll
        System.out.println("\n--- 2. Test doRetrieveAll ---");
        try {
            List<MetodoPagamentoBean> tuttiIMetodi = metodoDAO.doRetrieveAll();
            System.out.println("[INFO] Totale carte di pagamento registrate nel sistema: " + tuttiIMetodi.size());
            for (MetodoPagamentoBean m : tuttiIMetodi) {
                System.out.println(" -> Carta: " + m.getNumeroCarta() + " | Intestata a: " + m.getNome() + " " + m.getCognome());
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE] Errore in doRetrieveAll: " + e.getMessage());
        }

        // 3. TEST doUpdate (Modifica del cognome o dell'intestazione di una carta esistente)
        long cartaModifica = 3333444455556666L;
        System.out.println("\n--- 3. Test doUpdate (Modifica cognome carta: " + cartaModifica + ") ---");
        try {
            MetodoPagamentoBean cartaDaModificare = metodoDAO.doRetrieveByKey(cartaModifica);
            if (cartaDaModificare != null) {
                System.out.println("[INFO] Cognome attuale: " + cartaDaModificare.getCognome());
                cartaDaModificare.setCognome("Neri-Rossi"); // Cambiamo il cognome
                metodoDAO.doUpdate(cartaDaModificare);
                
                MetodoPagamentoBean cartaVerifica = metodoDAO.doRetrieveByKey(cartaModifica);
                System.out.println("[SUCCESS] Cognome aggiornato sul database: " + cartaVerifica.getCognome());
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE] Errore in doUpdate: " + e.getMessage());
        }

        // 4. TEST doDelete (Cancellazione del record temporaneo)
        long cartaElimina = 1234567812345678L;
        System.out.println("\n--- 4. Test doDelete (Carta: " + cartaElimina + ") ---");
        try {
            metodoDAO.doDelete(cartaElimina);
            MetodoPagamentoBean cartaEliminata = metodoDAO.doRetrieveByKey(cartaElimina);
            if (cartaEliminata == null) {
                System.out.println("[SUCCESS] Metodo di pagamento eliminato correttamente.");
            } else {
                System.out.println("[FAIL] La carta risulta ancora presente nel database.");
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE] Errore in doDelete: " + e.getMessage());
        }

        System.out.println("\n=== FINE TEST METODO PAGAMENTO DAO ===");
    }
}