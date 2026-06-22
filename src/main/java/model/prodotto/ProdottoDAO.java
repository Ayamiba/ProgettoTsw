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

        String query = "SELECT ID_prodotto, nome, prezzo, descrizione FROM Prodotto WHERE ID_prodotto = ?";

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

        String query = "SELECT ID_prodotto, nome, prezzo, descrizione FROM Prodotto";

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
        
        // 1. Il "Bivio" di Java basato sulla tua idea dei TINYINT
        String query = "";
        
        if ("Effetti".equalsIgnoreCase(nomeCategoria)) {
            // Se l'utente clicca "Effetti", cerchiamo dove la colonna effetto è = 1
            query = "SELECT Prodotto.* FROM Prodotto " +
                    "JOIN Tipologia ON Prodotto.ID_prodotto = Tipologia.FK_prodotto " +
                    "JOIN Categoria ON Tipologia.FK_categoria = Categoria.nome " +
                    "WHERE Categoria.effetto = 1";
                    
        } else if ("StudioTool".equalsIgnoreCase(nomeCategoria)) {
            // Se l'utente clicca "StudioTool", cerchiamo dove la colonna studio_tool è = 1
            query = "SELECT Prodotto.* FROM Prodotto " +
                    "JOIN Tipologia ON Prodotto.ID_prodotto = Tipologia.FK_prodotto " +
                    "JOIN Categoria ON Tipologia.FK_categoria = Categoria.nome " +
                    "WHERE Categoria.studio_tool = 1";
                    
        } else {
            // Se la categoria non è nessuna delle due (o c'è un errore), restituisci una lista vuota per sicurezza
            return prodotti; 
        }
        
        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            
            // Non serve più lo statement.setString() perché l'1 è già scritto fisso nella query!
            resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                ProdottoBean prodotto = new ProdottoBean();
                
                prodotto.setIdProdotto(resultSet.getInt("ID_prodotto"));
                prodotto.setNome(resultSet.getString("nome")); 
                prodotto.setPrezzo(resultSet.getFloat("prezzo"));
                prodotto.setDescrizione(resultSet.getString("descrizione"));
                
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
            statement.setFloat(1, prezzoMax); // Usiamo setFloat invece di setString!
            
            resultSet = statement.executeQuery();
            
            while(resultSet.next()) {
            	ProdottoBean prodotto = new ProdottoBean();
    			
    			prodotto.setIdProdotto(resultSet.getInt("ID_prodotto"));
    			prodotto.setNome(resultSet.getString("nome"));
    			prodotto.setPrezzo(resultSet.getFloat("prezzo"));
    			prodotto.setDescrizione(resultSet.getString("descrizione"));
    			prodotti.add(prodotto);
            }
        } finally {
        	try {
				if (resultSet != null) {
					resultSet.close();
					}
			}
			finally {
				try {
					if (statement != null) {
						statement.close();
						}
				}
				finally {
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
        
        // 1. Usiamo la stessa logica dei TINYINT per evitare problemi con i nomi
        String query = "";
        
        if ("Effetti".equalsIgnoreCase(nomeCategoria)) {
            query = "SELECT Prodotto.* FROM Prodotto " +
                    "JOIN Tipologia ON Prodotto.ID_prodotto = Tipologia.FK_prodotto " +
                    "JOIN Categoria ON Tipologia.FK_categoria = Categoria.nome " +
                    "WHERE Categoria.effetto = 1 AND Prodotto.prezzo <= ?";
                    
        } else if ("StudioTool".equalsIgnoreCase(nomeCategoria)) {
            query = "SELECT Prodotto.* FROM Prodotto " +
                    "JOIN Tipologia ON Prodotto.ID_prodotto = Tipologia.FK_prodotto " +
                    "JOIN Categoria ON Tipologia.FK_categoria = Categoria.nome " +
                    "WHERE Categoria.studio_tool = 1 AND Prodotto.prezzo <= ?";
                    
        } else {
            // Se la categoria non esiste, ritorno vuoto
            return prodotti;
        }
        
        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            
            // Attenzione: ora c'è solo UN punto interrogativo nella query (quello del prezzo).
            // Il TINYINT (1) lo abbiamo già scritto a mano nella query sopra.
            // Quindi usiamo setFloat(1, prezzoMax) e non più setFloat(2)
            statement.setFloat(1, prezzoMax);
            
            resultSet = statement.executeQuery();
            
            while(resultSet.next()) {
                ProdottoBean prodotto = new ProdottoBean();
                
                prodotto.setIdProdotto(resultSet.getInt("ID_prodotto"));
                prodotto.setNome(resultSet.getString("nome"));
                prodotto.setPrezzo(resultSet.getFloat("prezzo"));
                prodotto.setDescrizione(resultSet.getString("descrizione"));
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
    @Override
    public void doSave(ProdottoBean prodotto) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        String query = "INSERT INTO Prodotto (ID_prodotto, nome, prezzo, descrizione) VALUES (?, ?, ?, ?)";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);

            statement.setInt(1, prodotto.getIdProdotto());
            statement.setString(2, prodotto.getNome());
            statement.setFloat(3, prodotto.getPrezzo());
            statement.setString(4, prodotto.getDescrizione());

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

        String query = "UPDATE Prodotto SET nome = ?, prezzo = ?, descrizione = ? WHERE ID_prodotto = ?";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);

            statement.setString(1, prodotto.getNome());
            statement.setFloat(2, prodotto.getPrezzo());
            statement.setString(3, prodotto.getDescrizione());
            statement.setInt(4, prodotto.getIdProdotto());

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