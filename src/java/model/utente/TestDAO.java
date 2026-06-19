package model.utente;

import model.ConnectionPool;
import java.sql.SQLException;

public class TestDAO {
    public static void main(String[] args) {
        System.out.println("=== INIZIO TEST UTENTE DAO ===");
        
        try {
            // 1. INIZIALIZZAZIONE DEL POOL
            // Questo passo è fondamentale! Nel main dobbiamo svegliare il pool a mano.
            ConnectionPool.init(5); 
            System.out.println("[OK] ConnectionPool inizializzato correttamente.");

            // 2. ISTANZA DEL DAO
            UtenteDAO utenteDAO = new UtenteDAO();
            
            // 3. ESECUZIONE DELLA QUERY
            // Inserisci qui un'email che esiste DAVVERO sul tuo database locale
            String emailTest = "admin@saendwave.it"; 
            System.out.println("[INFO] Tento il recupero dell'utente con email: " + emailTest);
            
            UtenteBean utente = utenteDAO.doRetrieveByKey(emailTest);
            
            // 4. VERIFICA DEI RISULTATI
            if (utente != null) {
                System.out.println("[SUCCESS] Connessione riuscita e utente trovato!");
                System.out.println(" -> Nome: " + utente.getNome());
                System.out.println(" -> Cognome: " + utente.getCognome());
                System.out.println(" -> Ruolo/Tipo: " + utente.getTipo());
            } else {
                System.out.println("[WARNING] La connessione funziona, ma l'email '" + emailTest + "' non esiste nel database (il DAO ha restituito null).");
            }

        } catch (SQLException e) {
            System.err.println("[ERRORE CRITICO] Il test è fallito! Impossibile connettersi al DB.");
            e.printStackTrace();
        }
        
        System.out.println("=== FINE TEST ===");
    }
}