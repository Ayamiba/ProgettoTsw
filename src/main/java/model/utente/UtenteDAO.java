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

public class UtenteDAO implements DAOInterface<UtenteBean, String> {
	
	public UtenteDAO() {}
	
	@Override
	public UtenteBean doRetrieveByKey(String email) throws SQLException {
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
				utente = new UtenteBean();
				utente.setEmail(resultSet.getString("email"));
				utente.setNome(resultSet.getString("nome"));
				utente.setCognome(resultSet.getString("cognome"));
				utente.setPassword(resultSet.getString("password"));
				utente.setDataNascita(resultSet.getDate("data_nascita"));
				utente.setTipo(resultSet.getString("tipo"));
			}
		} finally {
			try {
				if (resultSet != null) resultSet.close();
			} finally {
				try {
					if (statement != null) statement.close();
				} finally {
					ConnectionPool.releaseConnection(connection);
				}
			}
		}
		return utente;
	}
	
	@Override
	public List<UtenteBean> doRetrieveAll() throws SQLException { //usa una lista di utenti
		List<UtenteBean> utenti = new ArrayList<>();
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		String query = "SELECT email, nome, cognome, password, data_nascita, tipo FROM Utente";

		try {
			connection = ConnectionPool.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				UtenteBean utente = new UtenteBean();
				utente.setEmail(resultSet.getString("email"));
				utente.setNome(resultSet.getString("nome"));
				utente.setCognome(resultSet.getString("cognome"));
				utente.setPassword(resultSet.getString("password"));
				utente.setDataNascita(resultSet.getDate("data_nascita"));
				utente.setTipo(resultSet.getString("tipo"));
				utenti.add(utente);
			}
		} finally {
			try {
				if (resultSet != null) resultSet.close();
			} finally {
				try {
					if (statement != null) statement.close();
				} finally {
					ConnectionPool.releaseConnection(connection);
				}
			}
		}
		return utenti;
	}

	@Override
	public void doSave(UtenteBean utente) throws SQLException { //permette di fare l'insert
		Connection connection = null;
		PreparedStatement statement = null;

		String query = "INSERT INTO Utente (nome, cognome, email, password, data_nascita, tipo) VALUES (?, ?, ?, ?, ?, ?)";

		try {
			connection = ConnectionPool.getConnection();
			statement = connection.prepareStatement(query);

			statement.setString(3, utente.getEmail());
			statement.setString(1, utente.getNome());
			statement.setString(2, utente.getCognome());
			statement.setString(4, utente.getPassword());
			statement.setDate(5, utente.getDataNascita());
			statement.setString(6, utente.getTipo());

			statement.executeUpdate();
		} finally {
			try {
				if (statement != null) statement.close();
			} finally {
				ConnectionPool.releaseConnection(connection);
			}
		}
	}

	@Override
	public void doUpdate(UtenteBean utente) throws SQLException { //aggiorna i dati dell'utente
		Connection connection = null;
		PreparedStatement statement = null;

		String query = "UPDATE Utente SET nome = ?, cognome = ?, password = ?, data_nascita = ?, tipo = ? WHERE email = ?";

		try {
			connection = ConnectionPool.getConnection();
			statement = connection.prepareStatement(query);

			statement.setString(1, utente.getNome());
			statement.setString(2, utente.getCognome());
			statement.setString(3, utente.getPassword());
			statement.setDate(4, utente.getDataNascita());
			statement.setString(5, utente.getTipo());
			statement.setString(6, utente.getEmail());

			statement.executeUpdate();
		} finally {
			try {
				if (statement != null) statement.close();
			} finally {
				ConnectionPool.releaseConnection(connection);
			}
		}
	}

	@Override
	public void doDelete(String email) throws SQLException { // cancella il record e le sue dipendenze
		Connection connection = null;
		PreparedStatement stmtRecensioni = null;
		PreparedStatement stmtOrdini = null;
		PreparedStatement stmtTracce = null;
		PreparedStatement stmtUtente = null;

		// 1. Elimina le recensioni legate all'utente o agli ordini associati alle sue tracce
		String deleteRecensioni = "DELETE FROM Recensione WHERE FK_utente = ? OR FK_ordine IN "
				+ "(SELECT ID_ordine FROM Ordine WHERE FK_traccia IN "
				+ "(SELECT ID_traccia FROM TracciaAudio WHERE FK_utente = ?))";

		// 2. Elimina gli ordini che referenziano le tracce dell'utente
		String deleteOrdini = "DELETE FROM Ordine WHERE FK_traccia IN "
				+ "(SELECT ID_traccia FROM TracciaAudio WHERE FK_utente = ?)";

		// 3. Elimina le tracce audio dell'utente
		String deleteTracce = "DELETE FROM TracciaAudio WHERE FK_utente = ?";

		// 4. Elimina l'utente
		String deleteUtente = "DELETE FROM Utente WHERE email = ?";

		try {
			connection = ConnectionPool.getConnection();
			connection.setAutoCommit(false); // Avvia la transazione

			// 1. Cancella Recensioni
			stmtRecensioni = connection.prepareStatement(deleteRecensioni);
			stmtRecensioni.setString(1, email);
			stmtRecensioni.setString(2, email);
			stmtRecensioni.executeUpdate();

			// 2. Cancella Ordini vincolati
			stmtOrdini = connection.prepareStatement(deleteOrdini);
			stmtOrdini.setString(1, email);
			stmtOrdini.executeUpdate();

			// 3. Cancella Tracce Audio
			stmtTracce = connection.prepareStatement(deleteTracce);
			stmtTracce.setString(1, email);
			stmtTracce.executeUpdate();

			// 4. Cancella Utente
			stmtUtente = connection.prepareStatement(deleteUtente);
			stmtUtente.setString(1, email);
			stmtUtente.executeUpdate();

			connection.commit(); // Conferma tutte le eliminazioni
		} catch (SQLException e) {
			if (connection != null) {
				try {
					connection.rollback(); // Annulla la transazione se qualcosa va in errore
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			throw e;
		} finally {
			try { if (stmtRecensioni != null) stmtRecensioni.close(); } catch (Exception e) {}
			try { if (stmtOrdini != null) stmtOrdini.close(); } catch (Exception e) {}
			try { if (stmtTracce != null) stmtTracce.close(); } catch (Exception e) {}
			try { if (stmtUtente != null) stmtUtente.close(); } catch (Exception e) {}
			if (connection != null) {
				try { connection.setAutoCommit(true); } catch (Exception e) {}
				ConnectionPool.releaseConnection(connection);
			}
		}
	}
	
	//questo metodo restituisce l'utente se esso è presente, null se non lo è
	public UtenteBean doRetrieveByLogin(String email, String password) throws SQLException {
		System.out.println("--- [DEBUG DAO START] ---");
		System.out.println("Email ricevuta dal form: '" + email + "'");
		System.out.println("Password ricevuta dal form: '" + password + "'");

		if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
			System.out.println("DEBUG DAO: Email o Password vuote! Esco subito.");
			return null;
		}
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		UtenteBean utente = null;

		String query = "SELECT * FROM Utente WHERE email = ? AND password = ?"; //restituisce la riga corrispondente

		try {
			connection = ConnectionPool.getConnection();
			statement = connection.prepareStatement(query);
			
			statement.setString(1, email);
			statement.setString(2, password);

			resultSet = statement.executeQuery();

			// Se nel DB c'è una riga entrerà nell if
			if (resultSet.next()) { //se la select produce una riga in output mettiamo a true la variabile
				utente = new UtenteBean();
				utente.setEmail(resultSet.getString("email"));
				utente.setNome(resultSet.getString("nome"));
				utente.setCognome(resultSet.getString("cognome"));
				utente.setPassword(resultSet.getString("password"));
				utente.setDataNascita(resultSet.getDate("data_nascita"));
				utente.setTipo(resultSet.getString("tipo"));
			}
			
		} finally {
			try {
				if (resultSet != null) resultSet.close();
			} finally {
				try {
					if (statement != null) statement.close();
				} finally {
					ConnectionPool.releaseConnection(connection);
				}
			}
		}

		return utente; // Ritornerà l'utente se viene trovato, 'null' se non trovato
	}
	
	public void doUpdateTipo(String email, String nuovoTipo) throws SQLException {
		Connection connection = null;
		PreparedStatement statement = null;
		
		// Modifichiamo solo il ruolo dell'utente
		String query = "UPDATE Utente SET tipo = ? WHERE email = ?";
		
		try {
			connection = ConnectionPool.getConnection();
			statement = connection.prepareStatement(query);
			statement.setString(1, nuovoTipo);
			statement.setString(2, email);
			
			statement.executeUpdate();
			
		} finally {
			try { if (statement != null) statement.close(); } finally {
				ConnectionPool.releaseConnection(connection);
			}
		}
	}

	// NUOVO METODO: Recupera solo gli utenti di tipo 'professionista'
	public List<UtenteBean> doRetrieveProfessionisti() throws SQLException {
		List<UtenteBean> professionisti = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		String query = "SELECT email, nome, cognome, password, data_nascita, tipo FROM Utente WHERE tipo = 'professionista'";

		try {
			connection = ConnectionPool.getConnection();
			statement = connection.prepareStatement(query);
			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				UtenteBean utente = new UtenteBean();
				utente.setEmail(resultSet.getString("email"));
				utente.setNome(resultSet.getString("nome"));
				utente.setCognome(resultSet.getString("cognome"));
				utente.setPassword(resultSet.getString("password"));
				utente.setDataNascita(resultSet.getDate("data_nascita"));
				utente.setTipo(resultSet.getString("tipo"));
				professionisti.add(utente);
			}
		} finally {
			try {
				if (resultSet != null) resultSet.close();
			} finally {
				try {
					if (statement != null) statement.close();
				} finally {
					ConnectionPool.releaseConnection(connection);
				}
			}
		}
		return professionisti;
	}
	
}