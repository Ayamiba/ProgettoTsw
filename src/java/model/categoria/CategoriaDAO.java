package model.categoria;

import model.ConnectionPool;
import model.DAOInterface;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO implements DAOInterface<CategoriaBean, String> {

    public CategoriaDAO() {}

    @Override
    public CategoriaBean doRetrieveByKey(String nome) throws SQLException {
        if (nome == null || nome.trim().isEmpty()) return null;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        CategoriaBean categoria = null;

        String query = "SELECT nome, studio_tool, effetto, tipo FROM Categoria WHERE nome = ?";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, nome);
            
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                categoria = new CategoriaBean();
                categoria.setNome(resultSet.getString("nome"));
                categoria.setStudioTool(resultSet.getBoolean("studio_tool")); 
                categoria.setEffetto(resultSet.getBoolean("effetto"));
                categoria.setTipo(resultSet.getString("tipo"));
            }
        } finally {
            try { if (resultSet != null) resultSet.close(); } finally {
                try { if (statement != null) statement.close(); } finally {
                    ConnectionPool.releaseConnection(connection);
                }
            }
        }
        return categoria;
    }

    @Override
    public List<CategoriaBean> doRetrieveAll() throws SQLException {
        List<CategoriaBean> categorie = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        String query = "SELECT nome, studio_tool, effetto, tipo FROM Categoria";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                CategoriaBean categoria = new CategoriaBean();
                categoria.setNome(resultSet.getString("nome"));
                categoria.setStudioTool(resultSet.getBoolean("studio_tool")); // Utilizza setStudioTool in camelCase
                categoria.setEffetto(resultSet.getBoolean("effetto"));
                categoria.setTipo(resultSet.getString("tipo"));
                categorie.add(categoria);
            }
        } finally {
            try { if (resultSet != null) resultSet.close(); } finally {
                try { if (statement != null) statement.close(); } finally {
                    ConnectionPool.releaseConnection(connection);
                }
            }
        }
        return categorie;
    }

    @Override
    public void doSave(CategoriaBean categoria) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        String query = "INSERT INTO Categoria (nome, studio_tool, effetto, tipo) VALUES (?, ?, ?, ?)";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);

            statement.setString(1, categoria.getNome());
            statement.setBoolean(2, categoria.getStudioTool());
            statement.setBoolean(3, categoria.getEffetto());
            statement.setString(4, categoria.getTipo());

            statement.executeUpdate();
        } finally {
            try { if (statement != null) statement.close(); } finally {
                ConnectionPool.releaseConnection(connection);
            }
        }
    }

    @Override
    public void doUpdate(CategoriaBean categoria) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        String query = "UPDATE Categoria SET studio_tool = ?, effetto = ?, tipo = ? WHERE nome = ?";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);

            statement.setBoolean(1, categoria.getStudioTool());
            statement.setBoolean(2, categoria.getEffetto());
            statement.setString(3, categoria.getTipo());
            statement.setString(4, categoria.getNome());

            statement.executeUpdate();
        } finally {
            try { if (statement != null) statement.close(); } finally {
                ConnectionPool.releaseConnection(connection);
            }
        }
    }

    @Override
    public void doDelete(String nome) throws SQLException {
        if (nome == null || nome.trim().isEmpty()) return;

        Connection connection = null;
        PreparedStatement statement = null;

        String query = "DELETE FROM Categoria WHERE nome = ?";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);

            statement.setString(1, nome);
            statement.executeUpdate();
        } finally {
            try { if (statement != null) statement.close(); } finally {
                ConnectionPool.releaseConnection(connection);
            }
        }
    }
}