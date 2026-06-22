package model.ordine;

import model.ConnectionPool;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

//compilazione javac model\ConnectionPool.java model\DAOInterface.java model\ordine\OrdineBean.java model\ordine\OrdineDAO.java model\ordine\TestDAOOrdine.java
//esecuzione: java -cp ".;C:\lib\mysql-connector-j-8.0.33.jar" model.ordine.TestDAOOrdine

public class TestDAOOrdine {

    public static void main(String[] args) {
        System.out.println("=== INIZIO TEST ORDINE DAO ===");

        // --- INIZIALIZZAZIONE POOL CON CATCH ---
        System.out.println("\n--- Inizializzazione ConnectionPool ---");
        try {
            ConnectionPool.init(5); 
            System.out.println("[SUCCESS] ConnectionPool inizializzato correttamente!");
        } catch (SQLException e) {
            System.out.println("[ERRORE CRITICO] Inizializzazione del Pool Fallita: " + e.getMessage());
            System.out.println("=== FINE TEST (INTERROTTO) ===");
            return;
        }

        // --- VERIFICA CONNESSIONE ---
        try (Connection testConn = ConnectionPool.getConnection()) {
            if (testConn != null && !testConn.isClosed()) {
                System.out.println("[SUCCESS] Connessione stabilita con successo!");
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE CRITICO] Impossibile ottenere una connessione valida!");
            return; 
        }

        OrdineDAO ordineDAO = new OrdineDAO();

        // 1. TEST doRetrieveByKey
        int idCerca = 1; // Primitivo int -> passerà al metodo come Integer tramite Autoboxing
        System.out.println("\n--- 1. Test doRetrieveByKey (ID: " + idCerca + ") ---");
        try {
            OrdineBean ordine = ordineDAO.doRetrieveByKey(idCerca);
            if (ordine != null) {
                System.out.println("[SUCCESS] Ordine trovato:");
                System.out.println(" -> Data: " + ordine.getDataOrdine());
                System.out.println(" -> Totale: €" + ordine.getTotale());
                System.out.println(" -> Stato: " + ordine.getStato());
                System.out.println(" -> Descrizione: " + ordine.getDescrizione());
                System.out.println(" -> Traccia Associata (FK): " + ordine.getfKTraccia());
            } else {
                System.out.println("[FAIL] Ordine non trovato.");
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE] Errore in doRetrieveByKey: " + e.getMessage());
        }

        // 2. TEST doRetrieveAll
        System.out.println("\n--- 2. Test doRetrieveAll ---");
        try {
            List<OrdineBean> tuttiGliOrdini = ordineDAO.doRetrieveAll();
            System.out.println("[INFO] Totale ordini nel database: " + tuttiGliOrdini.size());
            for (OrdineBean o : tuttiGliOrdini) {
                System.out.println(" -> ID " + o.getIdOrdine() + " | Totale: €" + o.getTotale() + " | Stato: " + o.getStato());
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE] Errore in doRetrieveAll: " + e.getMessage());
        }

        // 3. TEST doUpdate
        int idModifica = 3;
        System.out.println("\n--- 3. Test doUpdate (Modifica stato ordine ID: " + idModifica + ") ---");
        try {
            OrdineBean ordineDaModificare = ordineDAO.doRetrieveByKey(idModifica);
            if (ordineDaModificare != null) {
                System.out.println("[INFO] Stato attuale: " + ordineDaModificare.getStato());
                ordineDaModificare.setStato("Completato"); // Aggiorniamo lo stato
                ordineDAO.doUpdate(ordineDaModificare);
                
                OrdineBean ordineVerifica = ordineDAO.doRetrieveByKey(idModifica);
                System.out.println("[SUCCESS] Nuovo stato salvato nel DB: " + ordineVerifica.getStato());
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE] Errore in doUpdate: " + e.getMessage());
        }

        // 4. TEST doDelete
        int idElimina = 6;
        System.out.println("\n--- 4. Test doDelete (ID: " + idElimina + ") ---");
        try {
            ordineDAO.doDelete(idElimina);
            OrdineBean ordineEliminato = ordineDAO.doRetrieveByKey(idElimina);
            if (ordineEliminato == null) {
                System.out.println("[SUCCESS] Ordine eliminato con successo dal database.");
            } else {
                System.out.println("[FAIL] L'ordine è ancora presente.");
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE] Errore in doDelete: " + e.getMessage());
        }

        System.out.println("\n=== FINE TEST ORDINE DAO ===");
    }
}