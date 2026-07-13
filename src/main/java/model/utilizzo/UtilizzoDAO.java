package model.utilizzo;

import model.ConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UtilizzoDAO {

    public UtilizzoDAO() {}

    public void doSave(UtilizzoBean utilizzo) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        // Query per inserire il ponte tra Utente e Carta
        String query = "INSERT INTO utilizzo (FK_utente, FK_metodopagamento) VALUES (?, ?)";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);

            statement.setString(1, utilizzo.getFkUtente());
            statement.setLong(2, utilizzo.getFkMetodoPagamento());

            statement.executeUpdate();
            
        } finally {
            try {
                if (statement != null) statement.close();
            } finally {
                ConnectionPool.releaseConnection(connection);
            }
        }
    }
}