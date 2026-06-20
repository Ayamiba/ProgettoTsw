package model.metodoPagamento;

import model.ConnectionPool;
import model.DAOInterface;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MetodoPagamentoDAO implements DAOInterface<MetodoPagamentoBean, Long> {

    public MetodoPagamentoDAO() {}

    @Override
    public MetodoPagamentoBean doRetrieveByKey(Long numeroCarta) throws SQLException {
        if (numeroCarta == null) return null; // Evita problemi a monte

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        MetodoPagamentoBean metodo = null;

        String query = "SELECT numero_carta, cvv, nome, cognome FROM MetodoPagamento WHERE numero_carta = ?";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            statement.setLong(1, numeroCarta); // L'autoboxing converte Long in long automaticamente
            
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                metodo = new MetodoPagamentoBean();
                metodo.setNumeroCarta(resultSet.getLong("numero_carta"));
                metodo.setCvv(resultSet.getInt("cvv"));
                metodo.setNome(resultSet.getString("nome"));
                metodo.setCognome(resultSet.getString("cognome"));
            }
        } finally {
            try { if (resultSet != null) resultSet.close(); } finally {
                try { if (statement != null) statement.close(); } finally {
                    ConnectionPool.releaseConnection(connection);
                }
            }
        }
        return metodo;
    }

    @Override
    public List<MetodoPagamentoBean> doRetrieveAll() throws SQLException {
        List<MetodoPagamentoBean> metodi = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        String query = "SELECT numero_carta, cvv, nome, cognome FROM MetodoPagamento";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                MetodoPagamentoBean metodo = new MetodoPagamentoBean();
                metodo.setNumeroCarta(resultSet.getLong("numero_carta"));
                metodo.setCvv(resultSet.getInt("cvv"));
                metodo.setNome(resultSet.getString("nome"));
                metodo.setCognome(resultSet.getString("cognome"));
                metodi.add(metodo);
            }
        } finally {
            try { if (resultSet != null) resultSet.close(); } finally {
                try { if (statement != null) statement.close(); } finally {
                    ConnectionPool.releaseConnection(connection);
                }
            }
        }
        return metodi;
    }

    @Override
    public void doSave(MetodoPagamentoBean metodo) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        String query = "INSERT INTO MetodoPagamento (numero_carta, cvv, nome, cognome) VALUES (?, ?, ?, ?)";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);

            statement.setLong(1, metodo.getNumeroCarta());
            statement.setInt(2, metodo.getCvv());
            statement.setString(3, metodo.getNome());
            statement.setString(4, metodo.getCognome());

            statement.executeUpdate();
        } finally {
            try { if (statement != null) statement.close(); } finally {
                ConnectionPool.releaseConnection(connection);
            }
        }
    }

    @Override
    public void doUpdate(MetodoPagamentoBean metodo) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        String query = "UPDATE MetodoPagamento SET cvv = ?, nome = ?, cognome = ? WHERE numero_carta = ?";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);

            statement.setInt(1, metodo.getCvv());
            statement.setString(2, metodo.getNome());
            statement.setString(3, metodo.getCognome());
            statement.setLong(4, metodo.getNumeroCarta());

            statement.executeUpdate();
        } finally {
            try { if (statement != null) statement.close(); } finally {
                ConnectionPool.releaseConnection(connection);
            }
        }
    }

    @Override
    public void doDelete(Long numeroCarta) throws SQLException {
        if (numeroCarta == null) return;

        Connection connection = null;
        PreparedStatement statement = null;

        String query = "DELETE FROM MetodoPagamento WHERE numero_carta = ?";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);

            statement.setLong(1, numeroCarta);
            statement.executeUpdate();
        } finally {
            try { if (statement != null) statement.close(); } finally {
                ConnectionPool.releaseConnection(connection);
            }
        }
    }
}