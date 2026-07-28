package model.prodotto;

import model.ConnectionPool;
import model.DAOInterface;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProdottoDAO implements DAOInterface<ProdottoBean, Integer> {

    public ProdottoDAO() {}

    @Override
    public ProdottoBean doRetrieveByKey(Integer idProdotto) throws SQLException {
        if (idProdotto == null) return null;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ProdottoBean prodotto = null;

        String query = "SELECT ID_prodotto, nome, prezzo, descrizione, immagine FROM Prodotto WHERE ID_prodotto = ?";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, idProdotto); 
            
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                prodotto = new ProdottoBean();
                prodotto.setIdProdotto(resultSet.getInt("ID_prodotto"));
                prodotto.setNome(resultSet.getString("nome"));
                prodotto.setPrezzo(resultSet.getFloat("prezzo"));
                prodotto.setDescrizione(resultSet.getString("descrizione"));
                prodotto.setImmagine(resultSet.getString("immagine"));
            }
        } finally {
            try { if (resultSet != null) resultSet.close(); } finally {
                try { if (statement != null) statement.close(); } finally {
                    ConnectionPool.releaseConnection(connection);
                }
            }
        }
        return prodotto;
    }

    @Override
    public List<ProdottoBean> doRetrieveAll() throws SQLException {
        List<ProdottoBean> prodotti = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        String query = "SELECT ID_prodotto, nome, prezzo, descrizione, immagine FROM Prodotto";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                ProdottoBean prodotto = new ProdottoBean();
                prodotto.setIdProdotto(resultSet.getInt("ID_prodotto"));
                prodotto.setNome(resultSet.getString("nome"));
                prodotto.setPrezzo(resultSet.getFloat("prezzo"));
                prodotto.setDescrizione(resultSet.getString("descrizione"));
                prodotto.setImmagine(resultSet.getString("immagine"));
                prodotti.add(prodotto);
            }
        } finally {
            try { if (resultSet != null) resultSet.close(); } finally {
                try { if (statement != null) statement.close(); } finally {
                    ConnectionPool.releaseConnection(connection);
                }
            }
        }
        return prodotti;
    }
    
    public List<ProdottoBean> doRetrieveByCategoria(String nomeCategoria) throws SQLException {
        List<ProdottoBean> prodotti = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        String query = "";
        
        // Aggiunto DISTINCT per eliminare i duplicati e controlli per i valori minuscoli della JSP
        if ("Effetto".equalsIgnoreCase(nomeCategoria) || "effetto".equalsIgnoreCase(nomeCategoria)) {
            query = "SELECT DISTINCT Prodotto.* FROM Prodotto " +
                    "JOIN Tipologia ON Prodotto.ID_prodotto = Tipologia.FK_prodotto " +
                    "JOIN Categoria ON Tipologia.FK_categoria = Categoria.nome " +
                    "WHERE Categoria.effetto = 1";
                    
        } else if ("Studio Tool".equalsIgnoreCase(nomeCategoria) || "studio_tool".equalsIgnoreCase(nomeCategoria)) {
            query = "SELECT DISTINCT Prodotto.* FROM Prodotto " +
                    "JOIN Tipologia ON Prodotto.ID_prodotto = Tipologia.FK_prodotto " +
                    "JOIN Categoria ON Tipologia.FK_categoria = Categoria.nome " +
                    "WHERE Categoria.studio_tool = 1";
                    
        } else if ("bundle".equalsIgnoreCase(nomeCategoria)) {
            // Se cerchi i bundle, cerca la parola "bundle" nel nome o nella descrizione
            query = "SELECT DISTINCT Prodotto.* FROM Prodotto WHERE nome LIKE '%bundle%' OR descrizione LIKE '%bundle%'";
        } else {
            return prodotti; 
        }
        
        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                ProdottoBean prodotto = new ProdottoBean();
                
                prodotto.setIdProdotto(resultSet.getInt("ID_prodotto"));
                prodotto.setNome(resultSet.getString("nome")); 
                prodotto.setPrezzo(resultSet.getFloat("prezzo"));
                prodotto.setDescrizione(resultSet.getString("descrizione"));
                prodotto.setImmagine(resultSet.getString("immagine"));
                
                prodotti.add(prodotto);
            }
        } finally {
            try { if (resultSet != null) resultSet.close(); } finally {
                try { if (statement != null) statement.close(); } finally {
                    ConnectionPool.releaseConnection(connection);
                }
            }
        }
        
        return prodotti;
    }
    
    public List<ProdottoBean> doRetrieveByPrezzoMax(float prezzoMax) throws SQLException {
        List<ProdottoBean> prodotti = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        String query = "SELECT * FROM Prodotto WHERE prezzo <= ?";
        
        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            statement.setFloat(1, prezzoMax); 
            
            resultSet = statement.executeQuery();
            
            while(resultSet.next()) {
            	ProdottoBean prodotto = new ProdottoBean();
    			prodotto.setIdProdotto(resultSet.getInt("ID_prodotto"));
    			prodotto.setNome(resultSet.getString("nome"));
    			prodotto.setPrezzo(resultSet.getFloat("prezzo"));
    			prodotto.setDescrizione(resultSet.getString("descrizione"));
    			prodotto.setImmagine(resultSet.getString("immagine"));
    			prodotti.add(prodotto);
            }
        } finally {
        	try { if (resultSet != null) resultSet.close(); } finally {
				try { if (statement != null) statement.close(); } finally {
					ConnectionPool.releaseConnection(connection);
				}
			}
        }
        return prodotti;
    }
    
    public List<ProdottoBean> doRetrieveByCategoriaAndPrezzo(String nomeCategoria, float prezzoMax) throws SQLException {
        List<ProdottoBean> prodotti = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        String query = "";
        
        // Aggiunto DISTINCT e controlli per la JSP
        if ("Effetto".equalsIgnoreCase(nomeCategoria) || "effetto".equalsIgnoreCase(nomeCategoria)) {
            query = "SELECT DISTINCT Prodotto.* FROM Prodotto " +
                    "JOIN Tipologia ON Prodotto.ID_prodotto = Tipologia.FK_prodotto " +
                    "JOIN Categoria ON Tipologia.FK_categoria = Categoria.nome " +
                    "WHERE Categoria.effetto = 1 AND Prodotto.prezzo <= ?";
                    
        } else if ("Studio Tool".equalsIgnoreCase(nomeCategoria) || "studio_tool".equalsIgnoreCase(nomeCategoria)) {
            query = "SELECT DISTINCT Prodotto.* FROM Prodotto " +
                    "JOIN Tipologia ON Prodotto.ID_prodotto = Tipologia.FK_prodotto " +
                    "JOIN Categoria ON Tipologia.FK_categoria = Categoria.nome " +
                    "WHERE Categoria.studio_tool = 1 AND Prodotto.prezzo <= ?";
                    
        } else if ("bundle".equalsIgnoreCase(nomeCategoria)) {
            query = "SELECT DISTINCT Prodotto.* FROM Prodotto WHERE (nome LIKE '%bundle%' OR descrizione LIKE '%bundle%') AND prezzo <= ?";
        } else {
            return prodotti;
        }
        
        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            statement.setFloat(1, prezzoMax);
            
            resultSet = statement.executeQuery();
            
            while(resultSet.next()) {
                ProdottoBean prodotto = new ProdottoBean();
                
                prodotto.setIdProdotto(resultSet.getInt("ID_prodotto"));
                prodotto.setNome(resultSet.getString("nome"));
                prodotto.setPrezzo(resultSet.getFloat("prezzo"));
                prodotto.setDescrizione(resultSet.getString("descrizione"));
                prodotto.setImmagine(resultSet.getString("immagine"));
                prodotti.add(prodotto);
            }
        } finally {
            try { if (resultSet != null) resultSet.close(); } finally {
                try { if (statement != null) statement.close(); } finally {
                    ConnectionPool.releaseConnection(connection);
                }
            }
        }
        return prodotti;
    }
    
    // Metodo per trovare un prodotto partendo dal suo nome esatto
    public ProdottoBean doRetrieveByName(String nome) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ProdottoBean prodotto = null;

        String query = "SELECT * FROM Prodotto WHERE nome = ?";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, nome);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                prodotto = new ProdottoBean();
                prodotto.setIdProdotto(resultSet.getInt("ID_prodotto"));
    			prodotto.setNome(resultSet.getString("nome"));
    			prodotto.setPrezzo(resultSet.getFloat("prezzo"));
    			prodotto.setDescrizione(resultSet.getString("descrizione"));
    			prodotto.setImmagine(resultSet.getString("immagine"));
            }
        } finally {
            try { if (resultSet != null) resultSet.close(); } finally {
                try { if (statement != null) statement.close(); } finally {
                    ConnectionPool.releaseConnection(connection);
                }
            }
        }
        return prodotto;
    }
    
    public List<ProdottoBean> doRetrieveByNomeLike(String nome) throws SQLException {
        List<ProdottoBean> prodotti = new ArrayList<>();
        if (nome == null || nome.trim().isEmpty()) return prodotti;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        String query = "SELECT * FROM Prodotto WHERE LOWER(nome) LIKE LOWER(?)";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, "%" + nome.trim() + "%");
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ProdottoBean prodotto = new ProdottoBean();
                prodotto.setIdProdotto(resultSet.getInt("ID_prodotto"));
                prodotto.setNome(resultSet.getString("nome"));
                prodotto.setPrezzo(resultSet.getFloat("prezzo"));
                prodotto.setDescrizione(resultSet.getString("descrizione"));
                prodotto.setImmagine(resultSet.getString("immagine"));
                prodotti.add(prodotto);
            }
        } finally {
            try { if (resultSet != null) resultSet.close(); } finally {
                try { if (statement != null) statement.close(); } finally {
                    ConnectionPool.releaseConnection(connection);
                }
            }
        }
        return prodotti;
    }
    
    // Metodo per estrarre tutti i prodotti acquistati all'interno di uno specifico ordine
    public List<ProdottoBean> doRetrieveProdottiByOrdine(int idOrdine) throws java.sql.SQLException {
        java.sql.Connection connection = null;
        java.sql.PreparedStatement statement = null;
        java.sql.ResultSet resultSet = null;
        List<ProdottoBean> prodotti = new java.util.ArrayList<>();

        String query = "SELECT p.ID_prodotto, p.nome, p.prezzo, p.descrizione, p.immagine " +
                       "FROM Prodotto p " +
                       "JOIN Contenuto c ON p.ID_prodotto = c.FK_prodotto " +
                       "WHERE c.FK_ordine = ? " +
                       "ORDER BY c.posizione_catena ASC";

        try {
            connection = model.ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, idOrdine);
            
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ProdottoBean prodotto = new ProdottoBean();
                prodotto.setIdProdotto(resultSet.getInt("ID_prodotto"));
                prodotto.setNome(resultSet.getString("nome"));
                prodotto.setPrezzo(resultSet.getFloat("prezzo"));
                prodotto.setDescrizione(resultSet.getString("descrizione"));
                prodotto.setImmagine(resultSet.getString("immagine"));
                
                prodotti.add(prodotto);
            }
        } finally {
            try { if (resultSet != null) resultSet.close(); } finally {
                try { if (statement != null) statement.close(); } finally {
                    model.ConnectionPool.releaseConnection(connection);
                }
            }
        }
        return prodotti;
    }
    
 // Gestisce tutte le combinazioni di filtri (Categoria, SottoCategoria, Prezzo)
    public List<ProdottoBean> doRetrieveByAllFilters(String categoria, String sottoCategoria, float prezzoMax) throws SQLException {
        List<ProdottoBean> prodotti = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        // Query base con DISTINCT e le tre JOIN per navigare il database
        StringBuilder query = new StringBuilder(
            "SELECT DISTINCT p.ID_prodotto, p.nome, p.prezzo, p.descrizione, p.immagine " +
            "FROM Prodotto p " +
            "LEFT JOIN Tipologia t ON p.ID_prodotto = t.FK_prodotto " +
            "LEFT JOIN Categoria c ON t.FK_categoria = c.nome " +
            "WHERE 1=1"
        );

        // 1. Filtro Categoria Principale
        if (categoria != null && !categoria.trim().isEmpty()) {
            if ("Effetto".equalsIgnoreCase(categoria)) {
                query.append(" AND c.effetto = 1");
            } else if ("Studio Tool".equalsIgnoreCase(categoria)) {
                query.append(" AND c.studio_tool = 1");
            } else if ("bundle".equalsIgnoreCase(categoria)) {
                query.append(" AND (p.nome LIKE '%bundle%' OR p.descrizione LIKE '%bundle%')");
            }
        }

        // 2. Filtro Sotto-Categoria (amplificatore, riverbero, ecc.)
        if (sottoCategoria != null && !sottoCategoria.trim().isEmpty()) {
            query.append(" AND c.nome = ?");
        }

        // 3. Filtro Prezzo
        if (prezzoMax >= 0) {
            query.append(" AND p.prezzo <= ?");
        }

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query.toString());

            int paramIndex = 1;

            // Riempiamo i punti interrogativi in modo dinamico
            if (sottoCategoria != null && !sottoCategoria.trim().isEmpty()) {
                statement.setString(paramIndex++, sottoCategoria);
            }

            if (prezzoMax >= 0) {
                statement.setFloat(paramIndex++, prezzoMax);
            }

            resultSet = statement.executeQuery();

            while(resultSet.next()) {
                ProdottoBean prodotto = new ProdottoBean();
                prodotto.setIdProdotto(resultSet.getInt("ID_prodotto"));
                prodotto.setNome(resultSet.getString("nome"));
                prodotto.setPrezzo(resultSet.getFloat("prezzo"));
                prodotto.setDescrizione(resultSet.getString("descrizione"));
                prodotto.setImmagine(resultSet.getString("immagine"));
                prodotti.add(prodotto);
            }
        } finally {
            try { if (resultSet != null) resultSet.close(); } finally {
                try { if (statement != null) statement.close(); } finally {
                    ConnectionPool.releaseConnection(connection);
                }
            }
        }
        return prodotti;
    }
    
 // Metodo per ottenere tutti i prodotti acquistati in un determinato ordine
    public List<ProdottoBean> doRetrieveByOrdine(int idOrdine) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<ProdottoBean> prodotti = new ArrayList<>();

        String query = "SELECT p.* FROM Prodotto p " +
                       "JOIN contenuto c ON p.ID_prodotto = c.FK_prodotto " +
                       "WHERE c.FK_ordine = ?";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, idOrdine);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ProdottoBean prodotto = new ProdottoBean();
                prodotto.setIdProdotto(resultSet.getInt("ID_prodotto"));
                prodotto.setNome(resultSet.getString("nome"));
                prodotto.setPrezzo(resultSet.getFloat("prezzo"));
                prodotto.setDescrizione(resultSet.getString("descrizione"));
                prodotto.setImmagine(resultSet.getString("immagine"));
                prodotti.add(prodotto);
            }
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {}
            if (statement != null) try { statement.close(); } catch (SQLException e) {}
            ConnectionPool.releaseConnection(connection);
        }
        return prodotti;
    }
    
    @Override
    public void doSave(ProdottoBean prodotto) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        String query = "INSERT INTO Prodotto (ID_prodotto, nome, prezzo, descrizione, immagine) VALUES (?, ?, ?, ?, ?)";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);

            statement.setInt(1, prodotto.getIdProdotto());
            statement.setString(2, prodotto.getNome());
            statement.setFloat(3, prodotto.getPrezzo());
            statement.setString(4, prodotto.getDescrizione());
            statement.setString(5, prodotto.getImmagine());

            statement.executeUpdate();
        } finally {
            try { if (statement != null) statement.close(); } finally {
                ConnectionPool.releaseConnection(connection);
            }
        }
    }

    @Override
    public void doUpdate(ProdottoBean prodotto) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        String query = "UPDATE Prodotto SET nome = ?, prezzo = ?, descrizione = ?, immagine = ? WHERE ID_prodotto = ?";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);

            statement.setString(1, prodotto.getNome());
            statement.setFloat(2, prodotto.getPrezzo());
            statement.setString(3, prodotto.getDescrizione());
            statement.setString(4, prodotto.getImmagine());
            statement.setInt(5, prodotto.getIdProdotto());

            statement.executeUpdate();
        } finally {
            try { if (statement != null) statement.close(); } finally {
                ConnectionPool.releaseConnection(connection);
            }
        }
    }

    @Override
    public void doDelete(Integer idProdotto) throws SQLException {
        if (idProdotto == null) return;

        Connection connection = null;
        PreparedStatement statement = null;

        String query = "DELETE FROM Prodotto WHERE ID_prodotto = ?";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);

            statement.setInt(1, idProdotto);
            statement.executeUpdate();
        } finally {
            try { if (statement != null) statement.close(); } finally {
                ConnectionPool.releaseConnection(connection);
            }
        }
    }
}