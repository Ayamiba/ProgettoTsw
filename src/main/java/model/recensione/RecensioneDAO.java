package model.recensione;

import model.ConnectionPool;
import model.DAOInterface;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RecensioneDAO implements DAOInterface<RecensioneBean, Integer> {

    public RecensioneDAO() {}

    @Override
    public RecensioneBean doRetrieveByKey(Integer fkOrdine) throws SQLException {
        if (fkOrdine == null) return null;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        RecensioneBean recensione = null;

        String query = "SELECT ID_recensione, FK_ordine, FK_prodotto, FK_utente, voto, commento, data_recensione, tipo FROM Recensione WHERE ID_recensione = ?";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, fkOrdine); // Autoboxing da Integer a int automatico
            
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                recensione = new RecensioneBean();
                recensione.setIdRecensione(resultSet.getInt("ID_recensione"));   
                recensione.setFkOrdine(resultSet.getInt("FK_ordine"));
                recensione.setFkProdotto(resultSet.getInt("FK_prodotto"));
                recensione.setVoto(resultSet.getInt("voto"));
                recensione.setCommento(resultSet.getString("commento"));
                recensione.setDataRecensione(resultSet.getDate("data_recensione"));
                recensione.setFkUtente(resultSet.getString("FK_utente"));
                		
            }
        } finally {
            try { if (resultSet != null) resultSet.close(); } finally {
                try { if (statement != null) statement.close(); } finally {
                    ConnectionPool.releaseConnection(connection);
                }
            }
        }
        return recensione;
    }

    @Override
    public List<RecensioneBean> doRetrieveAll() throws SQLException {
        List<RecensioneBean> recensioni = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        String query = "SELECT ID_recensione, FK_ordine, FK_prodotto, FK_utente, voto, commento, data_recensione, tipo FROM Recensione";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                RecensioneBean recensione = new RecensioneBean();
                recensione.setIdRecensione(resultSet.getInt("ID_recensione"));   
                recensione.setFkOrdine(resultSet.getInt("FK_ordine"));
                recensione.setFkProdotto(resultSet.getInt("FK_prodotto"));
                recensione.setVoto(resultSet.getInt("voto"));
                recensione.setCommento(resultSet.getString("commento"));
                recensione.setDataRecensione(resultSet.getDate("data_recensione"));
                recensione.setFkUtente(resultSet.getString("FK_utente"));
                recensioni.add(recensione);
            }
        } finally {
            try { if (resultSet != null) resultSet.close(); } finally {
                try { if (statement != null) statement.close(); } finally {
                    ConnectionPool.releaseConnection(connection);
                }
            }
        }
        return recensioni;
    }
    
 // Recupera sia le recensioni del prodotto specifico sia le recensioni degli ordini per il filtro
    public List<RecensioneBean> doRetrieveByIdProdotto(int idProdotto) throws SQLException {
        List<RecensioneBean> recensioni = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        String query = "SELECT ID_recensione, FK_ordine, FK_prodotto, FK_utente, voto, commento, data_recensione, tipo "
                     + "FROM Recensione WHERE FK_prodotto = ? ORDER BY data_recensione DESC";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, idProdotto);
            
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
            	RecensioneBean recensione = new RecensioneBean();
            	recensione.setIdRecensione(resultSet.getInt("ID_recensione"));   
            	recensione.setFkOrdine(resultSet.getInt("FK_ordine"));
                recensione.setFkProdotto(resultSet.getInt("FK_prodotto"));
                recensione.setVoto(resultSet.getInt("voto"));
                recensione.setCommento(resultSet.getString("commento"));
                recensione.setDataRecensione(resultSet.getDate("data_recensione"));
                recensione.setFkUtente(resultSet.getString("FK_utente"));
                recensioni.add(recensione);
            }
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {}
            if (statement != null) try { statement.close(); } catch (SQLException e) {}
            ConnectionPool.releaseConnection(connection);
        }
        return recensioni;
    }

    public List<RecensioneBean> doRetrieveByIdOrdine(int idOrdine) throws SQLException {
        List<RecensioneBean> recensioni = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        String query = "SELECT ID_recensione, FK_ordine, FK_prodotto, FK_utente voto, commento, data_recensione, tipo "
                     + "FROM Recensione WHERE FK_ordine = ? ORDER BY data_recensione DESC";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, idOrdine);
            
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
            	RecensioneBean recensione = new RecensioneBean();
            	recensione.setIdRecensione(resultSet.getInt("ID_recensione"));   
            	recensione.setFkOrdine(resultSet.getInt("FK_ordine"));
                recensione.setFkProdotto(resultSet.getInt("FK_prodotto"));
                recensione.setVoto(resultSet.getInt("voto"));
                recensione.setCommento(resultSet.getString("commento"));
                recensione.setDataRecensione(resultSet.getDate("data_recensione"));
                recensione.setFkUtente(resultSet.getString("FK_utente"));
                recensioni.add(recensione);
            }
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {}
            if (statement != null) try { statement.close(); } catch (SQLException e) {}
            ConnectionPool.releaseConnection(connection);
        }
        return recensioni;
    }

    public List<RecensioneBean> doRetrieveByIdUtente(int idUtente) throws SQLException {
        List<RecensioneBean> recensioni = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        String query = "SELECT ID_recensione, FK_ordine, FK_prodotto, FK_utente, voto, commento, data_recensione, tipo "
                     + "FROM Recensione WHERE FK_utente = ? ORDER BY data_recensione DESC";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, idUtente);
            
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
            	RecensioneBean recensione = new RecensioneBean();
            	recensione.setIdRecensione(resultSet.getInt("ID_recensione"));   
            	recensione.setFkOrdine(resultSet.getInt("FK_ordine"));
                recensione.setFkProdotto(resultSet.getInt("FK_prodotto"));
                recensione.setVoto(resultSet.getInt("voto"));
                recensione.setCommento(resultSet.getString("commento"));
                recensione.setDataRecensione(resultSet.getDate("data_recensione"));
                recensione.setFkUtente(resultSet.getString("FK_utente"));
                recensioni.add(recensione);
            }
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {}
            if (statement != null) try { statement.close(); } catch (SQLException e) {}
            ConnectionPool.releaseConnection(connection);
        }
        return recensioni;
    }
    
    public List<RecensioneBean> doRetrieveByProdotto() throws SQLException {
        List<RecensioneBean> recensioniProdotto = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        //quando FK_ordine è null la recensione è riferita al prodotto
        String query = "SELECT ID_recensione, FK_ordine, FK_prodotto, FK_utente, voto, commento, data_recensione, tipo FROM Recensione WHERE FK_ordine IS NULL";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
            	RecensioneBean recensione = new RecensioneBean();
            	recensione.setIdRecensione(resultSet.getInt("ID_recensione"));   
            	recensione.setFkOrdine(resultSet.getInt("FK_ordine"));
                recensione.setFkProdotto(resultSet.getInt("FK_prodotto"));
                recensione.setVoto(resultSet.getInt("voto"));
                recensione.setCommento(resultSet.getString("commento"));
                recensione.setDataRecensione(resultSet.getDate("data_recensione"));
                recensione.setFkUtente(resultSet.getString("FK_utente"));
            }
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {}
            if (statement != null) try { statement.close(); } catch (SQLException e) {}
            ConnectionPool.releaseConnection(connection);
        }
        return recensioniProdotto;
    }

    public List<RecensioneBean> doRetrieveByOrdine() throws SQLException {
        List<RecensioneBean> recensioniOrdine = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        //quando FK_ordine è null la recensione è riferita al prodotto
        String query = "SELECT ID_recensione, FK_ordine, FK_prodotto, FK_utente, voto, commento, data_recensione, tipo FROM Recensione WHERE FK_prodotto IS NULL";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
            	RecensioneBean recensione = new RecensioneBean();
            	recensione.setIdRecensione(resultSet.getInt("ID_recensione"));   
            	recensione.setFkOrdine(resultSet.getInt("FK_ordine"));
                recensione.setFkProdotto(resultSet.getInt("FK_prodotto"));
                recensione.setVoto(resultSet.getInt("voto"));
                recensione.setCommento(resultSet.getString("commento"));
                recensione.setDataRecensione(resultSet.getDate("data_recensione"));
                recensione.setFkUtente(resultSet.getString("FK_utente"));
            }
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {}
            if (statement != null) try { statement.close(); } catch (SQLException e) {}
            ConnectionPool.releaseConnection(connection);
        }
        return recensioniOrdine;
    }

 // 1. Controlla se l'utente ha già recensito l'ordine
    public boolean esisteRecensioneOrdine(String emailUtente, int idOrdine) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        boolean esiste = false;
        
        // Cerca una recensione legata a quell'utente e a quell'ordine specifico
        String query = "SELECT COUNT(*) FROM Recensione WHERE FK_utente = ? AND FK_ordine = ? AND tipo = 'ordine'";
        
        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, emailUtente);
            statement.setInt(2, idOrdine);
            rs = statement.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                esiste = true; // Ha già recensito!
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (statement != null) statement.close(); } catch (Exception e) {}
            ConnectionPool.releaseConnection(connection);
        }
        return esiste;
    }

    // 2. Controlla se l'utente ha già recensito il prodotto
    public boolean esisteRecensioneProdotto(String emailUtente, int idProdotto) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        boolean esiste = false;
        
        // Cerca una recensione legata a quell'utente e a quel prodotto specifico
        String query = "SELECT COUNT(*) FROM Recensione WHERE FK_utente = ? AND FK_prodotto = ? AND tipo = 'prodotto'";
        
        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, emailUtente);
            statement.setInt(2, idProdotto);
            rs = statement.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                esiste = true; // Ha già recensito!
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (statement != null) statement.close(); } catch (Exception e) {}
            ConnectionPool.releaseConnection(connection);
        }
        return esiste;
    }
    
    public List<RecensioneBean> doRetrieveRecensioniByProfessionista(String emailProfessionista) {
        List<RecensioneBean> recensioni = new ArrayList<>();
        // Prende le recensioni di tipo 'ordine' dove l'email del professionista nell'ordine corrisponde
        String query = "SELECT r.* FROM Recensione r JOIN Ordine o ON r.FK_ordine = o.ID_ordine " +
                       "WHERE o.FK_email_professionista = ? AND r.tipo = 'ordine' ORDER BY r.data_recensione DESC";
        
        try (Connection con = ConnectionPool.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            
            ps.setString(1, emailProfessionista);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RecensioneBean r = new RecensioneBean();
                    r.setIdRecensione(rs.getInt("ID_recensione"));
                    r.setVoto(rs.getInt("voto"));
                    r.setCommento(rs.getString("commento"));
                    r.setDataRecensione(rs.getDate("data_recensione"));
                    r.setFkUtente(rs.getString("FK_utente")); // L'email del cliente che ha recensito
                    recensioni.add(r);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recensioni;
    }
    
    @Override
    public void doSave(RecensioneBean recensione) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        String query = "INSERT INTO Recensione (FK_ordine, FK_prodotto, FK_utente, voto, commento, data_recensione, tipo) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);

          //Suddivido i casi in cui una foreign key è null o intero
            if (recensione.getFkOrdine() == null) {
                statement.setNull(1, java.sql.Types.INTEGER);
            } else {
                statement.setInt(1, recensione.getFkOrdine());
            }
            if (recensione.getFkProdotto() == null) {
                statement.setNull(2, java.sql.Types.INTEGER);
            } else {
                statement.setInt(2, recensione.getFkProdotto());
            }
            statement.setString(3, recensione.getFkUtente());
            statement.setInt(4, recensione.getVoto());
            statement.setString(5, recensione.getCommento());
            statement.setDate(6, recensione.getDataRecensione());
            statement.setString(7, recensione.getTipo());

            statement.executeUpdate();
        } finally {
            try { if (statement != null) statement.close(); } finally {
                ConnectionPool.releaseConnection(connection);
            }
        }
    }

    @Override
    public void doUpdate(RecensioneBean recensione) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        String query = "UPDATE Recensione SET FK_ordine = ?, FK_prodotto = ?, FK_utente= ?, voto = ?, commento = ?, data_recensione = ?, tipo = ? WHERE ID_recensione = ?";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            
            //Suddivido i casi in cui una foreign key è null o intero
            if (recensione.getFkOrdine() == null) {
                statement.setNull(1, java.sql.Types.INTEGER);
            } else {
                statement.setInt(1, recensione.getFkOrdine());
            }
            if (recensione.getFkProdotto() == null) {
                statement.setNull(1, java.sql.Types.INTEGER);
            } else {
                statement.setInt(1, recensione.getFkProdotto());
            }
            statement.setString(3, recensione.getFkUtente());
            statement.setInt(4, recensione.getVoto());
            statement.setString(5, recensione.getCommento());
            statement.setDate(6, recensione.getDataRecensione());
            statement.setString(7, recensione.getTipo());

            statement.executeUpdate();
        } finally {
            try { if (statement != null) statement.close(); } finally {
                ConnectionPool.releaseConnection(connection);
            }
        }
    }

    @Override
    public void doDelete(Integer idRecensione) throws SQLException {
        if (idRecensione == null) return;

        Connection connection = null;
        PreparedStatement statement = null;

        String query = "DELETE FROM Recensione WHERE ID_recensione = ?";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);

            statement.setInt(1, idRecensione);
            statement.executeUpdate();
        } finally {
            try { if (statement != null) statement.close(); } finally {
                ConnectionPool.releaseConnection(connection);
            }
        }
    }
}