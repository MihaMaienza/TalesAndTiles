package model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class CartItemDAO {
	
	public synchronized static int AddCartItem(int IDCarrello, int IDProdotto) throws SQLException {
	    Connection con = null;
	    PreparedStatement stmt = null;
	    ResultSet generatedKeys = null;
	    int IDCartItem = -1;

	    try {
	        con = ConDB.getConnection();

	        // Controlla se il prodotto è già presente nel carrello
	        String checkSQL = "SELECT IDCartItem, quantita FROM cartitem WHERE IDCarrello = ? AND IDProdotto = ?";
	        stmt = con.prepareStatement(checkSQL);
	        stmt.setInt(1, IDCarrello);
	        stmt.setInt(2, IDProdotto);
	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	            // Prodotto già presente nel carrello, aumenta la quantità
	            int currentQuantity = rs.getInt("quantita");
	            int newQuantity = currentQuantity + 1;
	            int IDCartItemExisting = rs.getInt("IDCartItem");

	            String updateSQL = "UPDATE cartitem SET quantita = ? WHERE IDCartItem = ?";
	            stmt = con.prepareStatement(updateSQL);
	            stmt.setInt(1, newQuantity);
	            stmt.setInt(2, IDCartItemExisting);

	            int affectedRows = stmt.executeUpdate();
	            if (affectedRows == 0) {
	                throw new SQLException("Updating product quantity failed, no rows affected.");
	            }

	            IDCartItem = IDCartItemExisting;
	        } else {
	            // Prodotto non presente nel carrello, aggiungi nuovo elemento
	            String insertSQL = "INSERT INTO cartitem (IDCarrello, IDProdotto, quantita) VALUES (?, ?, ?)";
	            stmt = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
	            stmt.setInt(1, IDCarrello);
	            stmt.setInt(2, IDProdotto);
	            stmt.setInt(3, 1);

	            int affectedRows = stmt.executeUpdate();
	            if (affectedRows == 0) {
	                throw new SQLException("Creating product failed, no rows affected.");
	            }

	            generatedKeys = stmt.getGeneratedKeys();
	            if (generatedKeys.next()) {
	                IDCartItem = generatedKeys.getInt(1);
	            } else {
	                throw new SQLException("Creating product failed, no ID obtained.");
	            }
	        }

	        con.commit();
	    } finally {
	        if (generatedKeys != null) generatedKeys.close();
	        if (stmt != null) stmt.close();
	        if (con != null) ConDB.releaseConnection(con);
	    }

	    return IDCartItem;
	}

	
	public List<CartItem> getProductsByCarrelloID(int IDCarrello) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<CartItem> cartItems = new ArrayList<>();

        try {
            connection = ConDB.getConnection();
            String selectSQL = "SELECT * FROM cartitem WHERE IDCarrello = ?";
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setInt(1, IDCarrello);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int IDCartItem = resultSet.getInt("IDCartItem");
                int IDProdotto = resultSet.getInt("IDProdotto");
                int quantita = resultSet.getInt("quantita");
                CartItem cartItem = new CartItem(IDCartItem, IDProdotto, IDCarrello,quantita);
                cartItems.add(cartItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
            } finally {
                if (connection != null) ConDB.releaseConnection(connection);
            }
        }
        return cartItems;
    }
	
	public void SvuotaCarrello(int IDCarrello) throws SQLException {
		//svuota completamente il carrello
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = ConDB.getConnection();
            String deleteSQL = "DELETE FROM cartitem WHERE IDCarrello = ?";
            preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, IDCarrello);

            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } finally {
                if (connection != null) {
                    ConDB.releaseConnection(connection);
                }
            }
        }
    }
	
	public void removeProductFromCartByProductId(int IDProdotto, int IDCarrello) throws SQLException {
		//rimuove un cart item in base all'id del prodotto e del carrello
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = ConDB.getConnection();
            String deleteSQL = "DELETE FROM cartitem WHERE IDProdotto = ? AND IDCarrello = ?";
            preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, IDProdotto);
            preparedStatement.setInt(2, IDCarrello);

            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } finally {
                if (connection != null) {
                    ConDB.releaseConnection(connection);
                }
            }
        }
    }
	
	
	public void removeProductIfQuantity(int IDProdotto, int IDCarrello) throws SQLException {
		//se il cartItem ha quantita 1, il cart item viene eliminato, altrimenti viene diminuita la quantita di 1
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;

	    try {
	        connection = ConDB.getConnection();
	        
	        // Controlla la quantità attuale del prodotto nel carrello
	        String selectSQL = "SELECT quantita FROM cartitem WHERE IDProdotto = ? AND IDCarrello = ?";
	        preparedStatement = connection.prepareStatement(selectSQL);
	        preparedStatement.setInt(1, IDProdotto);
	        preparedStatement.setInt(2, IDCarrello);
	        resultSet = preparedStatement.executeQuery();
	        
	        if (resultSet.next()) {
	            int currentQuantity = resultSet.getInt("quantita");
	            
	            if (currentQuantity > 1) {
	                // Riduci la quantità di 1
	                String updateSQL = "UPDATE cartitem SET quantita = quantita - 1 WHERE IDProdotto = ? AND IDCarrello = ?";
	                preparedStatement = connection.prepareStatement(updateSQL);
	                preparedStatement.setInt(1, IDProdotto);
	                preparedStatement.setInt(2, IDCarrello);
	                preparedStatement.executeUpdate();
	            } else {
	                // Elimina il record se la quantità è 1
	                String deleteSQL = "DELETE FROM cartitem WHERE IDProdotto = ? AND IDCarrello = ?";
	                preparedStatement = connection.prepareStatement(deleteSQL);
	                preparedStatement.setInt(1, IDProdotto);
	                preparedStatement.setInt(2, IDCarrello);
	                preparedStatement.executeUpdate();
	            }
	        }

	        connection.commit();
	    } catch (SQLException e) {
	        if (connection != null) {
	            connection.rollback();
	        }
	        e.printStackTrace();
	        throw e;
	    } finally {
	        try {
	            if (resultSet != null) resultSet.close();
	            if (preparedStatement != null) preparedStatement.close();
	        } finally {
	            if (connection != null) ConDB.releaseConnection(connection);
	        }
	    }
	}
	
	
	public synchronized void addCartNoLogItems(int IDCarrello, List<CartNoLog> cartNoLogItems) throws SQLException {
        Connection connection = null;
        PreparedStatement checkStmt = null;
        PreparedStatement insertStmt = null;
        PreparedStatement updateStmt = null;

        try {
            connection = ConDB.getConnection();
            connection.setAutoCommit(false); // Disabilita l'auto-commit per la transazione

            // Query per verificare se un CartItem esiste già
            String checkSQL = "SELECT quantita FROM cartitem WHERE IDCarrello = ? AND IDProdotto = ?";
            checkStmt = connection.prepareStatement(checkSQL);

            // Query per inserire un nuovo CartItem
            String insertSQL = "INSERT INTO cartitem (IDCarrello, IDProdotto, quantita) VALUES (?, ?, ?)";
            insertStmt = connection.prepareStatement(insertSQL);

            // Query per aggiornare la quantità di un CartItem esistente
            String updateSQL = "UPDATE cartitem SET quantita = quantita + ? WHERE IDCarrello = ? AND IDProdotto = ?";
            updateStmt = connection.prepareStatement(updateSQL);

            for (CartNoLog cartNoLogItem : cartNoLogItems) {
                checkStmt.setInt(1, IDCarrello);
                checkStmt.setInt(2, cartNoLogItem.getIDProdotto());
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    // Il CartItem esiste, aggiorna la quantità
                    updateStmt.setInt(1, cartNoLogItem.getQuantita());
                    updateStmt.setInt(2, IDCarrello);
                    updateStmt.setInt(3, cartNoLogItem.getIDProdotto());
                    updateStmt.executeUpdate();
                } else {
                    // Il CartItem non esiste, inserisci un nuovo CartItem
                    insertStmt.setInt(1, IDCarrello);
                    insertStmt.setInt(2, cartNoLogItem.getIDProdotto());
                    insertStmt.setInt(3, cartNoLogItem.getQuantita());
                    insertStmt.executeUpdate();
                }
                rs.close();
            }

            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (checkStmt != null) checkStmt.close();
                if (insertStmt != null) insertStmt.close();
                if (updateStmt != null) updateStmt.close();
            } finally {
                if (connection != null) ConDB.releaseConnection(connection);
            }
        }
    }
	
	public int countItemsInCartByUsername(String username) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int itemCount = 0;

        try {
            connection = ConDB.getConnection();
            String selectSQL = "SELECT SUM(ci.quantita) FROM cartitem ci JOIN carrello c ON ci.IDCarrello = c.IDCarrello WHERE c.username = ?";
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                itemCount = resultSet.getInt(1);
            }
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
            } finally {
                if (connection != null) ConDB.releaseConnection(connection);
            }
        }

        return itemCount;
    }
	
	public List<Prodotto> getProductsByCartItems(List<CartItem> cartItems) throws SQLException {
	    List<Prodotto> prodotti = new ArrayList<>();
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;

	    try {
	        connection = ConDB.getConnection();
	        String query = "SELECT * FROM prodotto WHERE ID = ?";
	        preparedStatement = connection.prepareStatement(query);

	        for (CartItem cartItem : cartItems) {
	            preparedStatement.setInt(1, cartItem.getIDProdotto());
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

	public int getQuantityByProductIdAndCarrelloId(int IDProdotto, int IDCarrello) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int quantity = 0;

        try {
            connection = ConDB.getConnection();
            String query = "SELECT quantita FROM cartitem WHERE IDProdotto = ? AND IDCarrello = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, IDProdotto);
            preparedStatement.setInt(2, IDCarrello);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                quantity = resultSet.getInt("quantita");
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) ConDB.releaseConnection(connection);
        }

        return quantity;
    }
	
	public synchronized void updateCartItemQuantity(int IDCarrello, int IDProdotto, int quantita) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = ConDB.getConnection();
            String updateSQL = "UPDATE cartitem SET quantita = ? WHERE IDCarrello = ? AND IDProdotto = ?";
            stmt = con.prepareStatement(updateSQL);
            stmt.setInt(1, quantita);
            stmt.setInt(2, IDCarrello);
            stmt.setInt(3, IDProdotto);

            stmt.executeUpdate();
            con.commit();
        } finally {
            if (stmt != null) stmt.close();
            if (con != null) ConDB.releaseConnection(con);
        }
    }
	
	
	public float calculateTotalPrice(int cartId, String[] productIds) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        float totalPrice = 0;

        try {
            con = ConDB.getConnection();
            String query = "SELECT p.prezzo, ci.quantita FROM CartItem ci " +
                           "JOIN Prodotto p ON ci.IDProdotto = p.ID " +
                           "WHERE ci.IDCarrello = ? AND ci.IDProdotto = ?";
            stmt = con.prepareStatement(query);

            for (String productIdStr : productIds) {
                int productId = Integer.parseInt(productIdStr);
                stmt.setInt(1, cartId);
                stmt.setInt(2, productId);
                rs = stmt.executeQuery();

                if (rs.next()) {
                    float price = rs.getFloat("prezzo");
                    int quantity = rs.getInt("quantita");
                    totalPrice += price * quantity;
                }
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (con != null) ConDB.releaseConnection(con);
        }

        return totalPrice;
    }

/*    public void removeProductFromCart(int cartId, int productId) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = ConDB.getConnection();
            String query = "DELETE FROM CartItem WHERE IDCarrello = ? AND IDProdotto = ?";
            stmt = con.prepareStatement(query);
            stmt.setInt(1, cartId);
            stmt.setInt(2, productId);

            stmt.executeUpdate();
        } finally {
            if (stmt != null) stmt.close();
            if (con != null) ConDB.releaseConnection(con);
        }
    }*/
    
	public void removeProductFromCartByOrder(int cartId, int productId, Connection con) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String deleteSQL = "DELETE FROM CartItem WHERE IDCarrello = ? AND IDProdotto = ?";
            stmt = con.prepareStatement(deleteSQL);
            stmt.setInt(1, cartId);
            stmt.setInt(2, productId);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) stmt.close();
        }
    }
 // Metodo per aggiornare le quantità dei prodotti nei carrelli degli altri utenti
    public void updateCartItemsForAllUsers(int productId, Connection con) throws SQLException {
        String query = "SELECT IDCarrello, quantita FROM CartItem WHERE IDProdotto = ?";
        String updateQuery = "UPDATE CartItem SET quantita = ? WHERE IDCarrello = ? AND IDProdotto = ?";
        String deleteQuery = "DELETE FROM CartItem WHERE IDCarrello = ? AND IDProdotto = ?";
        String availabilityQuery = "SELECT disponibilita FROM Prodotto WHERE ID = ?";

        try (PreparedStatement ps = con.prepareStatement(query);
             PreparedStatement updatePs = con.prepareStatement(updateQuery);
             PreparedStatement deletePs = con.prepareStatement(deleteQuery);
             PreparedStatement availabilityPs = con.prepareStatement(availabilityQuery)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            availabilityPs.setInt(1, productId);
            ResultSet availabilityRs = availabilityPs.executeQuery();
            int disponibilita = 0;
            if (availabilityRs.next()) {
                disponibilita = availabilityRs.getInt("disponibilita");
            }

            while (rs.next()) {
                int cartId = rs.getInt("IDCarrello");
                int quantita = rs.getInt("quantita");

                if (disponibilita > 0) {
                    int newQuantita = Math.min(quantita, disponibilita);
                    updatePs.setInt(1, newQuantita);
                    updatePs.setInt(2, cartId);
                    updatePs.setInt(3, productId);
                    updatePs.executeUpdate();
                } else {
                    deletePs.setInt(1, cartId);
                    deletePs.setInt(2, productId);
                    deletePs.executeUpdate();
                }
            }
        }
    }
    public List<Integer> getCarrelliByProductId(int productId) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Integer> carrelloIds = new ArrayList<>();

        try {
            connection = ConDB.getConnection();
            String selectSQL = "SELECT DISTINCT IDCarrello FROM CartItem WHERE IDProdotto = ?";
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setInt(1, productId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                carrelloIds.add(resultSet.getInt("IDCarrello"));
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) ConDB.releaseConnection(connection);
        }

        return carrelloIds;
    }
	
}
