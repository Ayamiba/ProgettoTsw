package model.categoria;

import model.ConnectionPool;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TestDAOCategoria {

    public static void main(String[] args) {
        System.out.println("=== INIZIO TEST CATEGORIA DAO ===");

        // --- GESTIONE OBBLIGATORIA ECCEZIONI POOL ---
        System.out.println("\n--- Inizializzazione ConnectionPool ---");
        try {
            ConnectionPool.init(5); 
            System.out.println("[SUCCESS] ConnectionPool caricato correttamente!");
        } catch (SQLException e) {
            System.out.println("[ERRORE CRITICO] Inizializzazione del Pool Fallita: " + e.getMessage());
            System.out.println("=== FINE TEST (INTERROTTO) ===");
            return;
        }

        try (Connection testConn = ConnectionPool.getConnection()) {
            if (testConn != null && !testConn.isClosed()) {
                System.out.println("[SUCCESS] Connessione di test al database riuscita!");
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE CRITICO] Impossibile recuperare una connessione valida!");
            return; 
        }

        CategoriaDAO categoriaDAO = new CategoriaDAO();

        // 1. TEST doRetrieveByKey
        String nomeCerca = "VST Instrument";
        System.out.println("\n--- 1. Test doRetrieveByKey (Nome: '" + nomeCerca + "') ---");
        try {
            CategoriaBean categoria = categoriaDAO.doRetrieveByKey(nomeCerca);
            if (categoria != null) {
                System.out.println("[SUCCESS] Categoria individuata:");
                System.out.println(" -> Nome: " + categoria.getNome());
                System.out.println(" -> Is Studio Tool: " + categoria.getStudioTool());
                System.out.println(" -> Is Effetto: " + categoria.getEffetto());
                System.out.println(" -> Tipo: " + categoria.getTipo());
            } else {
                System.out.println("[FAIL] Categoria non trovata.");
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE] Eccezione in doRetrieveByKey: " + e.getMessage());
        }

        // 2. TEST doRetrieveAll
        System.out.println("\n--- 2. Test doRetrieveAll ---");
        try {
            List<CategoriaBean> tutteLeCategorie = categoriaDAO.doRetrieveAll();
            System.out.println("[INFO] Totale categorie presenti a database: " + tutteLeCategorie.size());
            for (CategoriaBean c : tutteLeCategorie) {
                System.out.println(" -> Categoria: " + c.getNome() + " | Tipo: " + c.getTipo());
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE] Eccezione in doRetrieveAll: " + e.getMessage());
        }

        // 3. TEST doUpdate
        String nomeModifica = "Reverb Effect";
        System.out.println("\n--- 3. Test doUpdate (Modifica campi di: '" + nomeModifica + "') ---");
        try {
            CategoriaBean catDaModificare = categoriaDAO.doRetrieveByKey(nomeModifica);
            if (catDaModificare != null) {
                System.out.println("[INFO] Stato attuale -> Tipo: " + catDaModificare.getTipo() + " | StudioTool: " + catDaModificare.getStudioTool());
                
                // Modifichiamo i parametri della categoria di test
                catDaModificare.setTipo("Plugin Ambientale");
                catDaModificare.setStudioTool(true); // Impostiamo a true per verificare l'aggiornamento del booleano
                
                categoriaDAO.doUpdate(catDaModificare);
                
                // Verifica sul database se la modifica è persistita
                CategoriaBean catVerifica = categoriaDAO.doRetrieveByKey(nomeModifica);
                System.out.println("[SUCCESS] Nuovi dati salvati a DB -> Tipo: " + catVerifica.getTipo() + " | StudioTool: " + catVerifica.getStudioTool());
            } else {
                System.out.println("[FAIL] Impossibile eseguire l'update: categoria '" + nomeModifica + "' non trovata.");
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE] Eccezione in doUpdate: " + e.getMessage());
        }

        // 4. TEST doDelete
        String nomeElimina = "Test Category";
        System.out.println("\n--- 4. Test doDelete (Eliminazione di: '" + nomeElimina + "') ---");
        try {
            categoriaDAO.doDelete(nomeElimina);
            
            // Tentiamo di recuperarla per verificare l'effettiva scomparsa
            CategoriaBean catEliminata = categoriaDAO.doRetrieveByKey(nomeElimina);
            if (catEliminata == null) {
                System.out.println("[SUCCESS] Categoria '" + nomeElimina + "' rimossa con successo dal database.");
            } else {
                System.out.println("[FAIL] Errore: la categoria risulta ancora presente sul database.");
            }
        } catch (SQLException e) {
            System.out.println("[ERRORE] Eccezione in doDelete: " + e.getMessage());
        }

        System.out.println("\n=== FINE TEST CATEGORIA DAO ===");
    }
}