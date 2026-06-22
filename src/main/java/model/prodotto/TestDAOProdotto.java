package model.prodotto;

import model.ConnectionPool;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TestDAOProdotto {
	//compilazione javac model\ConnectionPool.java model\DAOInterface.java model\prodotto\ProdottoBean.java model\prodotto\ProdottoDAO.java model\prodotto\TestDAOProdotto.java
	//esecuzione java -cp ".;C:\lib\mysql-connector-j-8.0.33.jar" model.prodotto.TestDAOProdotto
    public static void main(String[] args) {
        System.out.println("=== INIZIO TEST PRODOTTO DAO ===");

        // --- INIZIALIZZAZIONE POOL CON CATCH OBBLIGATORIO ---
        System.out.println("\n--- Inizializzazione ConnectionPool ---");
        try {
            ConnectionPool.init(5); 
            System.out.println("[SUCCESS] ConnectionPool inizializzato correttamente!");
        } catch (SQLException e) {
            System.out.println("[ERRORE CRITICO] Inizializzazione del Pool Fallita: " + e.getMessage());
            System.out.println("=== FINE TEST (INTERROTTO) ===");
            return;
        }

        // --- VERIFICA STRUTTURALE CONNESSIONE ---
        try (Connection testConn = ConnectionPool.getConnection()) {
            if (testConn != null && !testConn.isClosed()) {
                System.out.println("[SUCCESS] Connessione di test al database riuscita!");
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE CRITICO] Impossibile recuperare una connessione integra!");
            return; 
        }

        ProdottoDAO prodottoDAO = new ProdottoDAO();

        // 1. TEST doRetrieveByKey
        int idCerca = 2; // int primitivo che sfrutta l'autoboxing automatico verso Integer
        System.out.println("\n--- 1. Test doRetrieveByKey (ID: " + idCerca + ") ---");
        try {
            ProdottoBean prodotto = prodottoDAO.doRetrieveByKey(idCerca);
            if (prodotto != null) {
                System.out.println("[SUCCESS] Prodotto individuato:");
                System.out.println(" -> Nome: " + prodotto.getNome());
                System.out.println(" -> Prezzo: €" + prodotto.getPrezzo());
                System.out.println(" -> Descrizione: " + prodotto.getDescrizione());
            } else {
                System.out.println("[FAIL] Prodotto cercato non trovato.");
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE] Eccezione in doRetrieveByKey: " + e.getMessage());
        }

        // 2. TEST doRetrieveAll
        System.out.println("\n--- 2. Test doRetrieveAll ---");
        try {
            List<ProdottoBean> catalogoProdotti = prodottoDAO.doRetrieveAll();
            System.out.println("[INFO] Totale articoli presenti a catalogo: " + catalogoProdotti.size());
            for (ProdottoBean p : catalogoProdotti) {
                System.out.println(" -> ID: " + p.getIdProdotto() + " | " + p.getNome() + " (€" + p.getPrezzo() + ")");
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE] Eccezione in doRetrieveAll: " + e.getMessage());
        }

        // 3. TEST doUpdate
        int idModifica = 1;
        System.out.println("\n--- 3. Test doUpdate (Modifica prezzo articolo ID: " + idModifica + ") ---");
        try {
            ProdottoBean prodottoDaModificare = prodottoDAO.doRetrieveByKey(idModifica);
            if (prodottoDaModificare != null) {
                System.out.println("[INFO] Prezzo corrente: €" + prodottoDaModificare.getPrezzo());
                prodottoDaModificare.setPrezzo(24.99f); // Modifichiamo il prezzo floating-point
                prodottoDAO.doUpdate(prodottoDaModificare);
                
                ProdottoBean prodottoVerifica = prodottoDAO.doRetrieveByKey(idModifica);
                System.out.println("[SUCCESS] Nuovo prezzo registrato a database: €" + prodottoVerifica.getPrezzo());
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE] Eccezione in doUpdate: " + e.getMessage());
        }

        // 4. TEST doDelete
        int idElimina = 6;
        System.out.println("\n--- 4. Test doDelete (ID: " + idElimina + ") ---");
        try {
            prodottoDAO.doDelete(idElimina);
            ProdottoBean prodottoEliminato = prodottoDAO.doRetrieveByKey(idElimina);
            if (prodottoEliminato == null) {
                System.out.println("[SUCCESS] Articolo rimosso correttamente dal catalogo database.");
            } else {
                System.out.println("[FAIL] L'articolo è ancora presente nel sistema.");
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE] Eccezione in doDelete: " + e.getMessage());
        }

        System.out.println("\n=== FINE TEST PRODOTTO DAO ===");
    }
}