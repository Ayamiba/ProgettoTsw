package model.tracciaAudio;

import model.ConnectionPool;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
//compilare con javac model\ConnectionPool.java model\DAOInterface.java model\tracciaAudio\TracciaAudioBean.java model\tracciaAudio\TracciaAudioDAO.java model\tracciaAudio\TestDAOTracciaAudio.java
//eseguire con java -cp ".;C:\lib\mysql-connector-j-8.0.33.jar" model.tracciaAudio.TestDAOTracciaAudio
public class TestDAOTracciaAudio {

    public static void main(String[] args) {
        System.out.println("=== INIZIO TEST TRACCIA AUDIO DAO ===");

        // --- INIZIALIZZAZIONE POOL CON GESTIONE ECCEZIONE ---
        System.out.println("\n--- Inizializzazione ConnectionPool ---");
        try {
            ConnectionPool.init(5); 
            System.out.println("[SUCCESS] ConnectionPool inizializzato con successo!");
        } catch (SQLException e) {
            System.out.println("[ERRORE CRITICO] Fallita l'inizializzazione del Pool: " + e.getMessage());
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

        TracciaAudioDAO tracciaDAO = new TracciaAudioDAO();

        // 1. TEST doRetrieveByKey
        System.out.println("\n--- 1. Test doRetrieveByKey (ID: 1) ---");
        try {
            TracciaAudioBean traccia = tracciaDAO.doRetrieveByKey(1);
            if (traccia != null) {
                System.out.println("[SUCCESS] Traccia trovata:");
                System.out.println(" -> Nome File: " + traccia.getNomeFile());
                System.out.println(" -> Percorso File: " + traccia.getPercorsoFile());
                System.out.println(" -> Caricata da (FK): " + traccia.getfKUtente());
                System.out.println(" -> Approvata (check): " + traccia.isCheck());
            } else {
                System.out.println("[FAIL] Traccia audio non trovata.");
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE] Errore in doRetrieveByKey: " + e.getMessage());
        }

        // 2. TEST doRetrieveByUtente
        System.out.println("\n--- 2. Test doRetrieveByUtente (mario.rossi@email.it) ---");
        try {
            List<TracciaAudioBean> tracceMario = tracciaDAO.doRetrieveByUtente("mario.rossi@email.it");
            System.out.println("[INFO] Tracce trovate per Mario: " + tracceMario.size());
            for (TracciaAudioBean t : tracceMario) {
                System.out.println(" -> ID " + t.getIdTraccia() + ": " + t.getNomeFile());
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE] Errore in doRetrieveByUtente: " + e.getMessage());
        }

        // 3. TEST doRetrieveAll
        System.out.println("\n--- 3. Test doRetrieveAll ---");
        try {
            List<TracciaAudioBean> tutteLeTracce = tracciaDAO.doRetrieveAll();
            System.out.println("[INFO] Totale tracce audio nel sistema: " + tutteLeTracce.size());
            for (TracciaAudioBean t : tutteLeTracce) {
                System.out.println(" -> ID " + t.getIdTraccia() + " | " + t.getNomeFile() + " | Caricata da: " + t.getfKUtente());
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE] Errore in doRetrieveAll: " + e.getMessage());
        }

        // 4. TEST doUpdate
        System.out.println("\n--- 4. Test doUpdate (Approva traccia ID: 2) ---");
        try {
            TracciaAudioBean tracciaDaModificare = tracciaDAO.doRetrieveByKey(2);
            if (tracciaDaModificare != null) {
                System.out.println("[INFO] Stato check prima: " + tracciaDaModificare.isCheck());
                tracciaDaModificare.setCheck(true);
                tracciaDAO.doUpdate(tracciaDaModificare);
                
                TracciaAudioBean tracciaVerifica = tracciaDAO.doRetrieveByKey(2);
                System.out.println("[SUCCESS] Stato check dopo l'update: " + tracciaVerifica.isCheck());
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE] Errore in doUpdate: " + e.getMessage());
        }

        // 5. TEST doDelete
        System.out.println("\n--- 5. Test doDelete (ID: 6) ---");
        try {
            tracciaDAO.doDelete(6);
            TracciaAudioBean tracciaEliminata = tracciaDAO.doRetrieveByKey(6);
            if (tracciaEliminata == null) {
                System.out.println("[SUCCESS] Traccia ID 6 eliminata correttamente.");
            } else {
                System.out.println("[FAIL] La traccia è ancora presente.");
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE] Errore in doDelete: " + e.getMessage());
        }

        System.out.println("\n=== FINE TEST TRACCIA AUDIO DAO ===");
    }
}