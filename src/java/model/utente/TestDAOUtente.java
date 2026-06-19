package model.utente;

import model.ConnectionPool;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
// per il test di utente complilare con javac model\ConnectionPool.java model\DAOInterface.java model\utente\UtenteBean.java model\utente\UtenteDAO.java model\utente\TestDAOUtente.java
//e fare java -cp ".;C:\lib\mysql-connector-j-8.0.33.jar" model.utente.TestDAOUtente
public class TestDAOUtente {

    public static void main(String[] args) {
        System.out.println("=== INIZIO TEST UTENTE DAO ===");

        // --- INIZIALIZZAZIONE POOL CON GESTIONE ECCEZIONE ---
        System.out.println("\n--- Inizializzazione ConnectionPool ---");
        try {
            // Passiamo 5 come dimensione iniziale del pool, gestendo la SQLException 
            ConnectionPool.init(5); 
            System.out.println("[SUCCESS] ConnectionPool inizializzato con successo!");
        } catch (SQLException e) {
            System.out.println("[ERRORE CRITICO] Fallita l'inizializzazione del Pool: " + e.getMessage());
            System.out.println("=== FINE TEST (INTERROTTO) ===");
            return;
        }

        // --- VERIFICA EFFETTIVA DELLA CONNESSIONE ---
        System.out.println("\n--- Verifica Connessione dal Pool ---");
        try (Connection testConn = ConnectionPool.getConnection()) {
            if (testConn != null && !testConn.isClosed()) {
                System.out.println("[SUCCESS] Connessione al database stabilita correttamente!");
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE CRITICO] Impossibile estrarre una connessione valida!");
            System.out.println("Dettagli errore: " + e.getMessage());
            System.out.println("=== FINE TEST (INTERROTTO) ===");
            return; 
        }
        // Se la connessione funziona, procediamo con i test del DAO
        UtenteDAO utenteDAO = new UtenteDAO();

        // 1. TEST doRetrieveByKey
        System.out.println("\n--- 1. Test doRetrieveByKey fatto su giulia.bianchi---");
        try {
            UtenteBean utente = utenteDAO.doRetrieveByKey("giulia.bianchi@email.it");
            if (utente != null) {
                System.out.println("[SUCCESS] Utente trovato tramite chiave:");
                System.out.println(" -> Nome: " + utente.getNome());
                System.out.println(" -> Cognome: " + utente.getCognome());
                System.out.println(" -> Data di Nascita: " + utente.getDataNascita());
                System.out.println(" -> Ruolo/Tipo: " + utente.getTipo());
            } else {
                System.out.println("[FAIL] Utente non trovato nel DB.");
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE] Errore in doRetrieveByKey: " + e.getMessage());
        }

        // 2. TEST doLogin
        System.out.println("\n--- 2. Test doLogin (Utente Corretto) ---");
        try {
            UtenteBean loggedUser = utenteDAO.doLogin("luca.verdi@email.it", "verdiPass!");
            if (loggedUser != null) {
                System.out.println("[SUCCESS] Login effettuato! Benvenuto " + loggedUser.getNome() + " Nato il: " + loggedUser.getDataNascita() + " [Ruolo: " + loggedUser.getTipo() + "]");
            } else {
                System.out.println("[FAIL] Login fallito: credenziali errate.");
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE] Errore in doLogin: " + e.getMessage());
        }

        // 3. TEST doRetrieveAll
        System.out.println("\n--- 3. Test doRetrieveAll ---");
        try {
            List<UtenteBean> listaUtenti = utenteDAO.doRetrieveAll();
            System.out.println("[INFO] Totale utenti registrati nel DB: " + listaUtenti.size());
            for (UtenteBean u : listaUtenti) {
                System.out.println(" -> " + u.getEmail() + " | " + u.getNome() + " " + u.getCognome() + " | Nato il: " + u.getDataNascita() + " [" + u.getTipo() + "]");
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE] Errore in doRetrieveAll: " + e.getMessage());
        }

        // 4. TEST doUpdate
        System.out.println("\n--- 4. Test doUpdate ---");
        try {
            UtenteBean utenteDaModificare = utenteDAO.doRetrieveByKey("elena.neri@email.it");
            if (utenteDaModificare != null) {
                System.out.println("[INFO] Tipo attuale: " + utenteDaModificare.getTipo());
                utenteDaModificare.setTipo("utente registrato");
                utenteDAO.doUpdate(utenteDaModificare);
                
                // Verifica la modifica
                UtenteBean utenteVerifica = utenteDAO.doRetrieveByKey("elena.neri@email.it");
                System.out.println("[SUCCESS] Tipo aggiornato sul DB: " + utenteVerifica.getNome() + " ora è: [" + utenteVerifica.getTipo() + "]");
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE] Errore in doUpdate: " + e.getMessage());
        }

        // 5. TEST doDelete
        System.out.println("\n--- 5. Test doDelete ---");
        try {
            utenteDAO.doDelete("test.save@email.it");
            UtenteBean utenteCancellato = utenteDAO.doRetrieveByKey("test.save@email.it");
            if (utenteCancellato == null) {
                System.out.println("[SUCCESS] Utente 'test.save@email.it' eliminato correttamente.");
            } else {
                System.out.println("[FAIL] L'utente è ancora presente nel database.");
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE] Errore in doDelete: " + e.getMessage());
        }

        System.out.println("\n=== FINE TEST UTENTE DAO ===");
    }
}