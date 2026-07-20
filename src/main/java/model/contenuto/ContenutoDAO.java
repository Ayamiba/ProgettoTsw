package model.contenuto;

import model.ConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContenutoDAO {

    public ContenutoDAO() {}

  
    public void doSave(ContenutoBean contenuto) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        String query = "INSERT INTO Contenuto (FK_ordine, FK_prodotto, posizione_catena) VALUES (?, ?, ?)";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);

            statement.setInt(1, contenuto.getFkOrdine());
            statement.setInt(2, contenuto.getFkProdotto());
            statement.setInt(3, contenuto.getPosizioneCatena());

            statement.executeUpdate();
            
        } finally {
            try { if (statement != null) statement.close(); } finally {
                ConnectionPool.releaseConnection(connection);
            }
        }
    }

 
    public List<ContenutoBean> doRetrieveByOrdine(int idOrdine) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<ContenutoBean> listaContenuti = new ArrayList<>();

        String query = "SELECT ID_riga_contenuto, FK_ordine, FK_prodotto, posizione_catena FROM Contenuto WHERE FK_ordine = ? ORDER BY posizione_catena ASC";

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, idOrdine);
            
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ContenutoBean contenuto = new ContenutoBean();
                contenuto.setIdRigaContenuto(resultSet.getInt("ID_riga_contenuto"));
                contenuto.setFkOrdine(resultSet.getInt("FK_ordine"));
                contenuto.setFkProdotto(resultSet.getInt("FK_prodotto"));
                contenuto.setPosizioneCatena(resultSet.getInt("posizione_catena"));
                
                listaContenuti.add(contenuto);
            }
        } finally {
            try { if (resultSet != null) resultSet.close(); } finally {
                try { if (statement != null) statement.close(); } finally {
                    ConnectionPool.releaseConnection(connection);
                }
            }
        }
        return listaContenuti;
    }
    
    
    
 // Metodo per svuotare completamente il carrello di un utente dopo l'acquisto
    public void doEmptyCarrello(String emailUtente) throws java.sql.SQLException {
        java.sql.Connection connection = null;
        java.sql.PreparedStatement statement = null;
        
        String query = "DELETE FROM Carrello WHERE FK_utente = ?";
        
        try {
            connection = model.ConnectionPool.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, emailUtente);
            statement.executeUpdate();
        } finally {
            try { if (statement != null) statement.close(); } finally {
                model.ConnectionPool.releaseConnection(connection);
            }
        }
    }
}