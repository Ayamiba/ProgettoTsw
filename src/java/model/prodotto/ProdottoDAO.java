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