package model.DAO;

import model.javaBeans.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class CartNoLogDAO {
	public List<Prodotto> getProductsByCartNoLog(List<CartNoLog> cartNoLog) throws SQLException {
	    List<Prodotto> prodotti = new ArrayList<>();
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;

	    try {
	        connection = ConDB.getConnection();
	        String query = "SELECT * FROM prodotto WHERE ID = ?";
	        preparedStatement = connection.prepareStatement(query);

	        for (CartNoLog cartNoLogs : cartNoLog) {
	            preparedStatement.setInt(1, cartNoLogs.getIDProdotto());
	            resultSet = preparedStatement.executeQuery();

	            if (resultSet.next()) {
	            	
	            	byte [] img = resultSet.getBytes("immagine");
	            	if(img==null) {
	            		continue;
	            	}
	            	String immagine =Base64.getEncoder().encodeToString(img);
	            	
	            	Prodotto prodotto = new Prodotto(
	                		resultSet.getInt("ID"),
	                		resultSet.getString("nome"),
	                		resultSet.getFloat("prezzo"),
	                		resultSet.getInt("IVA"),
	                		resultSet.getString("tipo"),
	                    immagine,
	                    resultSet.getInt("disponibilita"),
	                    resultSet.getString("descrizione"),
	                    resultSet.getString("condizione"),
	                    resultSet.getInt("annoPubblicazione")
	                );
	                prodotti.add(prodotto);
	            }
	        }
	    } finally {
	        if (resultSet != null) resultSet.close();
	        if (preparedStatement != null) preparedStatement.close();
	        if (connection != null) ConDB.releaseConnection(connection);
	    }

	    return prodotti;
	}
	
	public int getQuantityByProductId(List<CartNoLog> cartNoLogItems, int IDProdotto) {
        for (CartNoLog item : cartNoLogItems) {
            if (item.getIDProdotto() == IDProdotto) {
                return item.getQuantita();
            }
        }
        return 0; // Se il prodotto non è trovato nella lista, ritorna 0
    }
	
	public void updateCartNoLogItemQuantity(List<CartNoLog> cartNoLogItems, int IDProdotto, int quantita) {
        for (CartNoLog cartNoLog : cartNoLogItems) {
            if (cartNoLog.getIDProdotto() == IDProdotto) {
                cartNoLog.setQuantita(quantita);
                break;
            }
        }
    }
}
