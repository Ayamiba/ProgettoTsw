package model.utente;


import model.ConnectionPool;
import model.DAOInterface;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UtenteDAO implements DAOInteface<UtenteBean, String> {
	
	public UtenteDAO() {}
	
	@Override
	public UtenteBean doRetrieveByKey(String email) {
		 Connection connection = null;
	        PreparedStatement statement = null;
	        ResultSet resultSet = null;
	        UtenteBean utente = null;
	        
	        String query="SELECT email, nome, cognome, password, data_nascita, tipo FROM Utente WHERE email = ?";
	        
	        try {
	        	connection = ConnectionPool.getConnection();
	        	statement = connection.prepareStatement(query);
	        	
	        	statement.setString(1, email);
	        	resultSet= statement.executeQuery();
	        	
	        	if(resultSet.next()) {
	        		utente = new UtenteBean(
	        				resultSet.getString("email"),
	        				resultSet.getString("nome"),
	        				resultSet.getString("cognome"),
	        				resultSet.getString("password"),
	        				resultSet.getTimestamp("data_nascita"),
	        				resultSet.getString("tipo")
	        				);
	        	}
	        } finally {}
	        
	}
}