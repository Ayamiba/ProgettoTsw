package model.tracciaAudio;

import model.ConnectionPool;
import model.DAOInterface;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TracciaAudioDAO implements DAOInterface<TracciaAudioBean, Integer> {

    public TracciaAudioDAO() {}

    @Override
    public TracciaAudioBean doRetrieveByKey(Integer idTraccia) throws SQLException { //restituisce la traccia in base alla primary key
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        TracciaAudioBean traccia = null;

        String query = "SELECT ID_traccia, nome_file, percorso_file, FK_utente, `check` FROM TracciaAudio WHERE ID_traccia = ?";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, idTraccia);
            
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                traccia = new TracciaAudioBean();
                traccia.setIdTraccia(resultSet.getInt("ID_traccia"));
                traccia.setNomeFile(resultSet.getString("nome_file"));
                traccia.setPercorsoFile(resultSet.getString("percorso_file"));
                traccia.setfKUtente(resultSet.getString("FK_utente"));
                traccia.setCheck(resultSet.getBoolean("check"));
            }
        } finally {
            try { if (resultSet != null) resultSet.close(); } finally {
                try { if (statement != null) statement.close(); } finally {
                    ConnectionPool.releaseConnection(connection);
                }
            }
        }
        return traccia;
    }

    @Override
    public List<TracciaAudioBean> doRetrieveAll() throws SQLException { //restituisce tutte le tracce
        List<TracciaAudioBean> tracce = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        String query = "SELECT ID_traccia, nome_file, percorso_file, FK_utente, `check` FROM TracciaAudio";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                TracciaAudioBean traccia = new TracciaAudioBean();
                traccia.setIdTraccia(resultSet.getInt("ID_traccia"));
                traccia.setNomeFile(resultSet.getString("nome_file"));
                traccia.setPercorsoFile(resultSet.getString("percorso_file"));
                traccia.setfKUtente(resultSet.getString("FK_utente"));
                traccia.setCheck(resultSet.getBoolean("check"));
                tracce.add(traccia);
            }
        } finally {
            try { if (resultSet != null) resultSet.close(); } finally {
                try { if (statement != null) statement.close(); } finally {
                    ConnectionPool.releaseConnection(connection);
                }
            }
        }
        return tracce;
    }

    @Override
    public void doSave(TracciaAudioBean traccia) throws SQLException { //fa una insert con la nuova traccia
        Connection connection = null;
        PreparedStatement statement = null;

        String query = "INSERT INTO TracciaAudio (ID_traccia, nome_file, percorso_file, `check`, FK_utente) VALUES (?, ?, ?, ?, ?)";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);

            statement.setInt(1, traccia.getIdTraccia());
            statement.setString(2, traccia.getNomeFile());
            statement.setString(3, traccia.getPercorsoFile());
            statement.setBoolean(4, traccia.isCheck());
            statement.setString(5, traccia.getfKUtente());

            statement.executeUpdate();
        } finally {
            try { if (statement != null) statement.close(); } finally {
                ConnectionPool.releaseConnection(connection);
            }
        }
    }

    @Override
    public void doUpdate(TracciaAudioBean traccia) throws SQLException { //fa l'update passando l'oggetto traccia
        Connection connection = null;
        PreparedStatement statement = null;

        String query = "UPDATE TracciaAudio SET nome_file = ?, percorso_file = ?, `check` = ?, FK_utente = ? WHERE ID_traccia = ?";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);

            statement.setString(1, traccia.getNomeFile());
            statement.setString(2, traccia.getPercorsoFile());
            statement.setBoolean(3, traccia.isCheck());
            statement.setString(4, traccia.getfKUtente());
            statement.setInt(5, traccia.getIdTraccia());

            statement.executeUpdate();
        } finally {
            try { if (statement != null) statement.close(); } finally {
                ConnectionPool.releaseConnection(connection);
            }
        }
    }

    @Override
    public void doDelete(Integer idTraccia) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        String query = "DELETE FROM TracciaAudio WHERE ID_traccia = ?";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);

            statement.setInt(1, idTraccia);
            statement.executeUpdate();
        } finally {
            try { if (statement != null) statement.close(); } finally {
                ConnectionPool.releaseConnection(connection);
            }
        }
    }

    public List<TracciaAudioBean> doRetrieveByUtente(String emailUtente) throws SQLException { //restituisce tutte le tracce di un utente
        List<TracciaAudioBean> tracce = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        String query = "SELECT ID_traccia, nome_file, percorso_file, FK_utente, `check` FROM TracciaAudio WHERE FK_utente = ?";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, emailUtente);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                TracciaAudioBean traccia = new TracciaAudioBean();
                traccia.setIdTraccia(resultSet.getInt("ID_traccia"));
                traccia.setNomeFile(resultSet.getString("nome_file"));
                traccia.setPercorsoFile(resultSet.getString("percorso_file"));
                traccia.setfKUtente(resultSet.getString("FK_utente"));
                traccia.setCheck(resultSet.getBoolean("check"));
                tracce.add(traccia);
            }
        } finally {
            try { if (resultSet != null) resultSet.close(); } finally {
                try { if (statement != null) statement.close(); } finally {
                    ConnectionPool.releaseConnection(connection);
                }
            }
        }
        return tracce;
    }
    
    public void doUpdateCheck(boolean check, int id) throws SQLException{
    	Connection connection = null;
        PreparedStatement statement = null;

        String query ="UPDATE TracciaAudio SET `check`= ? WHERE ID_traccia = ?";
        
        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);

            statement.setBoolean(1, check);
            statement.setInt(2, id);
            statement.executeUpdate();
        } finally {
            try { if (statement != null) statement.close(); } finally {
                ConnectionPool.releaseConnection(connection);
            }
        }
        }
}