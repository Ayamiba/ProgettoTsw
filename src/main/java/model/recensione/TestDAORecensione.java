package model.recensione;

import model.ConnectionPool;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TestDAORecensione {

    public static void main(String[] args) {
        System.out.println("=== INIZIO TEST RECENSIONE DAO ===");

        // --- GESTIONE OBBLIGATORIA POOL ---
        System.out.println("\n--- Inizializzazione ConnectionPool ---");
        try {
            ConnectionPool.init(5); 
            System.out.println("[SUCCESS] ConnectionPool caricato correttamente!");
        } catch (SQLException e) {
            System.out.println("[ERRORE CRITICO] Inizializzazione del Pool Fallita: " + e.getMessage());
            return;
        }

        try (Connection testConn = ConnectionPool.getConnection()) {
            if (testConn != null && !testConn.isClosed()) {
                System.out.println("[SUCCESS] Connessione al database riuscita!");
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE CRITICO] Impossibile recuperare una connessione valida!");
            return; 
        }

        RecensioneDAO recensioneDAO = new RecensioneDAO();

        // 1. TEST doRetrieveByKey
        int idOrdineCerca = 1; // Sfrutta l'autoboxing verso Integer
        System.out.println("\n--- 1. Test doRetrieveByKey (Ordine ID: " + idOrdineCerca + ") ---");
        try {
            RecensioneBean recensione = recensioneDAO.doRetrieveByKey(idOrdineCerca);
            if (recensione != null) {
                System.out.println("[SUCCESS] Recensione trovata:");
                System.out.println(" -> Voto: " + recensione.getVoto() + " stelle");
                System.out.println(" -> Commento: " + recensione.getCommento());
                System.out.println(" -> Data: " + recensione.getDataRecensione());
            } else {
                System.out.println("[FAIL] Nessuna recensione trovata per l'ordine specificato.");
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE] Eccezione in doRetrieveByKey: " + e.getMessage());
        }

        // 2. TEST doRetrieveAll
        System.out.println("\n--- 2. Test doRetrieveAll ---");
        try {
            List<RecensioneBean> tutteLeRecensioni = recensioneDAO.doRetrieveAll();
            System.out.println("[INFO] Numero totale recensioni a sistema: " + tutteLeRecensioni.size());
            for (RecensioneBean r : tutteLeRecensioni) {
                System.out.println(" -> Ordine ID: " + r.getFkOrdine() + " | Voto: " + r.getVoto() + "★ | Commento: " + r.getCommento());
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE] Eccezione in doRetrieveAll: " + e.getMessage());
        }

        // 3. TEST doUpdate
        int idOrdineModifica = 2;
        System.out.println("\n--- 3. Test doUpdate (Modifica commento ordine ID: " + idOrdineModifica + ") ---");
        try {
            RecensioneBean recDaModificare = recensioneDAO.doRetrieveByKey(idOrdineModifica);
            if (recDaModificare != null) {
                System.out.println("[INFO] Commento vecchio: " + recDaModificare.getCommento());
                
                recDaModificare.setCommento("Commento aggiornato: Prodotto davvero eccezionale sotto ogni aspetto!");
                recDaModificare.setVoto(5); // Aggiorniamo anche il voto portandolo a 5 stelle
                
                recensioneDAO.doUpdate(recDaModificare);
                
                RecensioneBean recVerifica = recensioneDAO.doRetrieveByKey(idOrdineModifica);
                System.out.println("[SUCCESS] Nuovi dati a DB -> Voto: " + recVerifica.getVoto() + "★ | Commento: " + recVerifica.getCommento());
            } else {
                System.out.println("[FAIL] Impossibile aggiornare: recensione dell'ordine " + idOrdineModifica + " non trovata.");
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE] Eccezione in doUpdate: " + e.getMessage());
        }

        // 4. TEST doDelete
        int idOrdineElimina = 5;
        System.out.println("\n--- 4. Test doDelete (Eliminazione recensione ordine ID: " + idOrdineElimina + ") ---");
        try {
            recensioneDAO.doDelete(idOrdineElimina);
            
            RecensioneBean recEliminata = recensioneDAO.doRetrieveByKey(idOrdineElimina);
            if (recEliminata == null) {
                System.out.println("[SUCCESS] Recensione dell'ordine " + idOrdineElimina + " rimossa con successo.");
            } else {
                System.out.println("[FAIL] Errore: la recensione risulta ancora presente.");
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE] Eccezione in doDelete: " + e.getMessage());
        }

        System.out.println("\n=== FINE TEST RECENSIONE DAO ===");
    }
}