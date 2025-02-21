package model.DAO;

import model.javaBeans.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class AcquistoDAO {

	public int AddAcquisto(int IDOrdine, int IDProdotto, String nome, int quantita, byte[] immagine, float prezzoAcquistato, float IVAAcquistato) throws SQLException {
	    Connection con = null;
	    PreparedStatement stmt = null;
	    ResultSet generatedKeys = null;
	    int IDAcquisto = -1;

	    try {
	        con = ConDB.getConnection();
	        String insertSQL = "INSERT INTO Acquisto (IDOrdine, IDProdotto, nome, quantita, immagine, prezzoAq, ivaAq) VALUES (?, ?, ?, ?, ?, ?, ?)";
	        stmt = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
	        stmt.setInt(1, IDOrdine);
	        stmt.setInt(2, IDProdotto);
	        stmt.setString(3, nome);
	        stmt.setInt(4, quantita);
	        stmt.setBytes(5, immagine); // Utilizza setBytes per l'immagine
	        stmt.setFloat(6, prezzoAcquistato);
	        stmt.setFloat(7, IVAAcquistato);

	        int affectedRows = stmt.executeUpdate();
	        if (affectedRows == 0) {
	            throw new SQLException("Errore creazione acquisto");
	        }

	        generatedKeys = stmt.getGeneratedKeys();
	        if (generatedKeys.next()) {
	            IDAcquisto = generatedKeys.getInt(1);
	        } else {
	            throw new SQLException("Creating purchase failed, no ID obtained.");
	        }

	        con.commit();
	    } catch (SQLException e) {
	        if (con != null) {
	            try {
	                con.rollback();
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	        }
	        throw e;
	    } finally {
	        if (generatedKeys != null) generatedKeys.close();
	        if (stmt != null) stmt.close();
	        if (con != null) ConDB.releaseConnection(con);
	    }

	    return IDAcquisto;
	}

	
	
	public List <Acquisto> getAcquistiByOrderId(int IDOrdine) throws SQLException {
    	List<Acquisto> acquisti=new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = ConDB.getConnection();
            String selectSQL = "SELECT * FROM acquisto WHERE IDOrdine = ?";
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setInt(1, IDOrdine);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
            	//prendiamo l'immagine dal database sotto forma di array di byte e con la successiva funzione la trasformiamo l'array di byte in una stringa
            	byte [] img = resultSet.getBytes("immagine");
            	if(img==null) {
            		continue;
            	}
            	String immagine =Base64.getEncoder().encodeToString(img);


                Acquisto acquisto = new Acquisto(
                		resultSet.getInt("IDAcquisto"),
                		resultSet.getInt("IDOrdine"),
                		resultSet.getInt("IDProdotto"),
                		resultSet.getString("nome"),
                		resultSet.getInt("quantita"),
                    immagine,
                    resultSet.getFloat("prezzoAq"),
                    resultSet.getInt("ivaAq")
                );
                acquisti.add(acquisto);
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return acquisti;
    }
	
	 public void deleteAcquistiByOrderId(int orderId, Connection con) throws SQLException {
	        PreparedStatement stmt = null;

	        try {
	            String query = "DELETE FROM Acquisto WHERE IDOrdine = ?";
	            stmt = con.prepareStatement(query);
	            stmt.setInt(1, orderId);
	            stmt.executeUpdate();
	        } finally {
	            if (stmt != null) stmt.close();
	        }
	    }
	
	
}
