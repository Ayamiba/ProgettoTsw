package model.ordine;

import model.ConnectionPool;
import model.DAOInterface;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrdineDAO implements DAOInterface<OrdineBean, Integer> {

    public OrdineDAO() {}

    @Override
    public OrdineBean doRetrieveByKey(Integer idOrdine) throws SQLException {
        if (idOrdine == null) return null;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        OrdineBean ordine = null;

        String query = "SELECT ID_ordine, data_ordine, totale, stato, descrizione, FK_traccia, FK_metodo_pagamento FROM Ordine WHERE ID_ordine = ?";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, idOrdine); // Autoboxing da Integer a int automatico
            
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                ordine = new OrdineBean();
                ordine.setIdOrdine(resultSet.getInt("ID_ordine"));
                ordine.setDataOrdine(resultSet.getDate("data_ordine"));
                ordine.setTotale(resultSet.getFloat("totale"));
                ordine.setStato(resultSet.getString("stato"));
                ordine.setDescrizione(resultSet.getString("descrizione"));
                ordine.setfKTraccia(resultSet.getInt("FK_traccia"));
                ordine.setfKMetodoPagamento(resultSet.getLong("FK_metodo_pagamento"));
            }
        } finally {
            try { if (resultSet != null) resultSet.close(); } finally {
                try { if (statement != null) statement.close(); } finally {
                    ConnectionPool.releaseConnection(connection);
                }
            }
        }
        return ordine;
    }

    @Override
    public List<OrdineBean> doRetrieveAll() throws SQLException {
        List<OrdineBean> ordini = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        String query = "SELECT ID_ordine, data_ordine, totale, stato, descrizione, FK_traccia, FK_metodo_pagamento FROM Ordine";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                OrdineBean ordine = new OrdineBean();
                ordine.setIdOrdine(resultSet.getInt("ID_ordine"));
                ordine.setDataOrdine(resultSet.getDate("data_ordine"));
                ordine.setTotale(resultSet.getFloat("totale"));
                ordine.setStato(resultSet.getString("stato"));
                ordine.setDescrizione(resultSet.getString("descrizione"));
                ordine.setfKTraccia(resultSet.getInt("FK_traccia"));
                ordine.setfKMetodoPagamento(resultSet.getLong("FK_metodo_pagamento"));
                ordini.add(ordine);
            }
        } finally {
            try { if (resultSet != null) resultSet.close(); } finally {
                try { if (statement != null) statement.close(); } finally {
                    ConnectionPool.releaseConnection(connection);
                }
            }
        }
        return ordini;
    }

    @Override
    public void doSave(OrdineBean ordine) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        String query = "INSERT INTO Ordine (ID_ordine, data_ordine, totale, stato, descrizione, FK_traccia, FK_metodo_pagamento) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);

            statement.setInt(1, ordine.getIdOrdine());
            statement.setDate(2, ordine.getDataOrdine());
            statement.setFloat(3, ordine.getTotale());
            statement.setString(4, ordine.getStato());
            statement.setString(5, ordine.getDescrizione());
            statement.setInt(6, ordine.getfKTraccia());
            statement.setLong(7, ordine.getfKMetodoPagamento());

            statement.executeUpdate();
        } finally {
            try { if (statement != null) statement.close(); } finally {
                ConnectionPool.releaseConnection(connection);
            }
        }
    }

    @Override
    public void doUpdate(OrdineBean ordine) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        String query = "UPDATE Ordine SET data_ordine = ?, totale = ?, stato = ?, descrizione = ?, FK_traccia = ?, FK_metodo_pagamento = ? WHERE ID_ordine = ?";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);

            statement.setDate(1, ordine.getDataOrdine());
            statement.setFloat(2, ordine.getTotale());
            statement.setString(3, ordine.getStato());
            statement.setString(4, ordine.getDescrizione());
            statement.setInt(5, ordine.getfKTraccia());
            statement.setInt(6, ordine.getIdOrdine());
            statement.setLong(7, ordine.getfKMetodoPagamento());

            statement.executeUpdate();
        } finally {
            try { if (statement != null) statement.close(); } finally {
                ConnectionPool.releaseConnection(connection);
            }
        }
    }

    @Override
    public void doDelete(Integer idOrdine) throws SQLException {
        if (idOrdine == null) return;

        Connection connection = null;
        PreparedStatement statement = null;

        String query = "DELETE FROM Ordine WHERE ID_ordine = ?";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);

            statement.setInt(1, idOrdine);
            statement.executeUpdate();
        } finally {
            try { if (statement != null) statement.close(); } finally {
                ConnectionPool.releaseConnection(connection);
            }
        }
    }
}