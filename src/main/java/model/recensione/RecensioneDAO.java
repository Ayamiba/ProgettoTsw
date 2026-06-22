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

        String query = "SELECT FK_ordine, voto, commento, data_recensione FROM Recensione WHERE FK_ordine = ?";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, fkOrdine); // Autoboxing da Integer a int automatico
            
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                recensione = new RecensioneBean();
                recensione.setFkOrdine(resultSet.getInt("FK_ordine"));
                recensione.setVoto(resultSet.getInt("voto"));
                recensione.setCommento(resultSet.getString("commento"));
                recensione.setDataRecensione(resultSet.getDate("data_recensione"));
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

        String query = "SELECT FK_ordine, voto, commento, data_recensione FROM Recensione";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                RecensioneBean recensione = new RecensioneBean();
                recensione.setFkOrdine(resultSet.getInt("FK_ordine"));
                recensione.setVoto(resultSet.getInt("voto"));
                recensione.setCommento(resultSet.getString("commento"));
                recensione.setDataRecensione(resultSet.getDate("data_recensione"));
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

    @Override
    public void doSave(RecensioneBean recensione) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        String query = "INSERT INTO Recensione (FK_ordine, voto, commento, data_recensione) VALUES (?, ?, ?, ?)";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);

            statement.setInt(1, recensione.getFkOrdine());
            statement.setInt(2, recensione.getVoto());
            statement.setString(3, recensione.getCommento());
            statement.setDate(4, recensione.getDataRecensione());

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

        String query = "UPDATE Recensione SET voto = ?, commento = ?, data_recensione = ? WHERE FK_ordine = ?";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);

            statement.setInt(1, recensione.getVoto());
            statement.setString(2, recensione.getCommento());
            statement.setDate(3, recensione.getDataRecensione());
            statement.setInt(4, recensione.getFkOrdine());

            statement.executeUpdate();
        } finally {
            try { if (statement != null) statement.close(); } finally {
                ConnectionPool.releaseConnection(connection);
            }
        }
    }

    @Override
    public void doDelete(Integer fkOrdine) throws SQLException {
        if (fkOrdine == null) return;

        Connection connection = null;
        PreparedStatement statement = null;

        String query = "DELETE FROM Recensione WHERE FK_ordine = ?";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);

            statement.setInt(1, fkOrdine);
            statement.executeUpdate();
        } finally {
            try { if (statement != null) statement.close(); } finally {
                ConnectionPool.releaseConnection(connection);
            }
        }
    }
}