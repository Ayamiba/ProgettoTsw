package model.carrello;

import model.ConnectionPool;
import model.DAOInterface;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.prodotto.*;

public class CarrelloDAO{ //non implemento DAOInterface perchè ci serve un solo metodo
	
	public CarrelloDAO() { } 
	
	public List<ProdottoBean> doRetrieveByCarrello (String emailUtente) throws SQLException{
		List<ProdottoBean> prodottiCarrello = new ArrayList<>();
		
		if (emailUtente == null || emailUtente.trim().isEmpty()) {
	        return prodottiCarrello;
	    }
		
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        String query = "SELECT p.ID_prodotto, p.nome, p.prezzo, p.descrizione " +
                "FROM Prodotto p " +
                "JOIN Carrello c ON p.ID_prodotto = c.FK_prodotto " +
                "WHERE c.FK_utente = ?";
        
        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, emailUtente);
            
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ProdottoBean prodotto = new ProdottoBean();
                prodotto.setIdProdotto(resultSet.getInt("ID_prodotto"));
                prodotto.setNome(resultSet.getString("nome"));
                prodotto.setPrezzo(resultSet.getFloat("prezzo"));
                prodotto.setDescrizione(resultSet.getString("descrizione"));
                
                prodottiCarrello.add(prodotto);
            }
        } finally {
            try { if (resultSet != null) resultSet.close(); } finally {
                try { if (statement != null) statement.close(); } finally {
                    ConnectionPool.releaseConnection(connection);
                }
            }
        }
        return prodottiCarrello;
    }
	
	public void doSave(String emailUtente, int idProdotto) throws SQLException {
	    if (emailUtente == null || emailUtente.trim().isEmpty() || idProdotto <= 0) {
	        return; // Controllo di sicurezza preventivo
	    }

	    Connection connection = null;
	    PreparedStatement statement = null;

	    // Specifichiamo le colonne FK_utente e FK_prodotto in base alle chiavi esterne del DB
	    String query = "INSERT INTO Carrello (FK_utente, FK_prodotto) VALUES (?, ?)";

	    try {
	        connection = ConnectionPool.getConnection();
	        statement = connection.prepareStatement(query);
	        
	        statement.setString(1, emailUtente);
	        statement.setInt(2, idProdotto);

	        // Esegue l'inserimento nel database
	        statement.executeUpdate();
	        
	    } finally {
	        try {
	            if (statement != null) statement.close();
	        } finally {
	            ConnectionPool.releaseConnection(connection);
	        }
	    }
	}
	public static void doDelete(int idProdotto) throws SQLException {
		Connection connection = null;
	    PreparedStatement statement = null;
	    String query = "DELETE from CARRELLO where FK_prodotto=?";
	    try {
	        connection = ConnectionPool.getConnection();
	        statement = connection.prepareStatement(query);
	        
	        statement.setInt(1, idProdotto);

	        // Esegue l'inserimento nel database
	        statement.executeUpdate();
	        
	    } finally {
	        try {
	            if (statement != null) statement.close();
	        } finally {
	            ConnectionPool.releaseConnection(connection);
	        }
	    }
	}
	
	public synchronized boolean doDeleteByUtenteAndProdotto(String emailUtente, int idProdotto) throws SQLException {
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    int result = 0;

	    String deleteSQL = "DELETE FROM carrello WHERE FK_utente = ? AND FK_prodotto = ?";

	    try {
	        connection = ConnectionPool.getConnection();
	        preparedStatement = connection.prepareStatement(deleteSQL);
	        preparedStatement.setString(1, emailUtente);
	        preparedStatement.setInt(2, idProdotto);

	        result = preparedStatement.executeUpdate();
	    } finally {
	        try {
	            if (preparedStatement != null) preparedStatement.close();
	        } finally {
	            ConnectionPool.releaseConnection(connection);
	        }
	    }
	    return (result != 0);
	}
	
	public void doEmptyCarrello(String emailUtente) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        
        String query = "DELETE FROM Carrello WHERE FK_utente = ?";
        
        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, emailUtente);
            statement.executeUpdate();
        } finally {
            try { if (statement != null) statement.close(); } finally {
                ConnectionPool.releaseConnection(connection);
            }
        }
    }
}