package model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class WishlistItemDAO {
	
	public int AddWishlistItem(int IDWishlist, int IDProdotto) throws SQLException {
	    Connection con = null;
	    PreparedStatement stmt = null;
	    ResultSet generatedKeys = null;
	    int IDWishlistItem = -1;

	    try {
	        con = ConDB.getConnection();

	        // Controlla se il prodotto è già presente nella wishlist
	        String checkSQL = "SELECT IDWishlistItem FROM WishlistItem WHERE IDWishlist = ? AND IDProdotto = ?";
	        stmt = con.prepareStatement(checkSQL);
	        stmt.setInt(1, IDWishlist);
	        stmt.setInt(2, IDProdotto);
	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	            // Prodotto già presente nella wishlist, non fare nulla
	            IDWishlistItem = rs.getInt("IDWishlistItem");
	        } else {
	            // Prodotto non presente nella wishlist, aggiungi nuovo elemento
	            String insertSQL = "INSERT INTO WishlistItem (IDWishlist, IDProdotto) VALUES (?, ?)";
	            stmt = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
	            stmt.setInt(1, IDWishlist);
	            stmt.setInt(2, IDProdotto);

	            int affectedRows = stmt.executeUpdate();
	            if (affectedRows == 0) {
	                throw new SQLException("Creating product failed, no rows affected.");
	            }

	            generatedKeys = stmt.getGeneratedKeys();
	            if (generatedKeys.next()) {
	                IDWishlistItem = generatedKeys.getInt(1);
	            } else {
	                throw new SQLException("Creating product failed, no ID obtained.");
	            }
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

	    return IDWishlistItem;
	}
	
	 public WishlistItem findWishlistItemByProductId(int IDWishlist, int IDProdotto) throws SQLException {
	        Connection connection = ConDB.getConnection();
	        String query = "SELECT * FROM WishlistItem WHERE IDWishlist = ? AND IDProdotto = ?";
	        PreparedStatement statement = connection.prepareStatement(query);
	        statement.setInt(1, IDWishlist);
	        statement.setInt(2, IDProdotto);
	        ResultSet resultSet = statement.executeQuery();

	        if (resultSet.next()) {
	            return new WishlistItem(
	                resultSet.getInt("IDWishlistItem"),
	                resultSet.getInt("IDWishlist"),
	                resultSet.getInt("IDProdotto")
	            );
	        } else {
	            return null;
	        }
	    }


	
	public List<WishlistItem> getProductsByIDWishlist(int IDWishlist) throws SQLException {
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    List<WishlistItem> wishlistItems = new ArrayList<>();

	    try {
	        connection = ConDB.getConnection();
	        String selectSQL = "SELECT * FROM WishlistItem WHERE IDWishlist = ?";
	        preparedStatement = connection.prepareStatement(selectSQL);
	        preparedStatement.setInt(1, IDWishlist);
	        resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {
	            int IDWishlistItem = resultSet.getInt("IDWishlistItem");
	            int IDProdotto = resultSet.getInt("IDProdotto");
	            WishlistItem wishlistItem = new WishlistItem(IDWishlistItem, IDWishlist, IDProdotto);
	            wishlistItems.add(wishlistItem);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw e;
	    } finally {
	        if (resultSet != null) {
	            try {
	                resultSet.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        if (preparedStatement != null) {
	            try {
	                preparedStatement.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        if (connection != null) {
	            ConDB.releaseConnection(connection);
	        }
	    }
	    return wishlistItems;
	}

	
	public void SvuotaWishlist(int IDWishlist) throws SQLException {
		//svuota completamente il carrello
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = ConDB.getConnection();
            String deleteSQL = "DELETE FROM WishlistItem WHERE IDWishlist = ?";
            preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, IDWishlist);

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
	
	public void removeProductFromWishlistByProductId(int IDProdotto, int IDWishlist) throws SQLException {
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;

	    try {
	        connection = ConDB.getConnection();
	        String deleteSQL = "DELETE FROM WishlistItem WHERE IDProdotto = ? AND IDWishlist = ?";
	        preparedStatement = connection.prepareStatement(deleteSQL);
	        preparedStatement.setInt(1, IDProdotto);
	        preparedStatement.setInt(2, IDWishlist);

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
	
	
	public int countItemsInWishlistByUsername(String username) throws SQLException {
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    int itemCount = 0;

	    try {
	        connection = ConDB.getConnection();
	        String selectSQL = "SELECT COUNT(wi.IDProdotto) FROM WishlistItem wi JOIN Wishlist w ON wi.IDWishlist = w.IDWishlist WHERE w.username = ?";
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

	
	public List<Prodotto> getProductsByWishlistItems(List<WishlistItem> wishlistItems) throws SQLException {
	    List<Prodotto> prodotti = new ArrayList<>();
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;

	    try {
	        connection = ConDB.getConnection();
	        String query = "SELECT * FROM prodotto WHERE ID = ?";
	        preparedStatement = connection.prepareStatement(query);

	        for (WishlistItem wishlistItem : wishlistItems) {
	            preparedStatement.setInt(1, wishlistItem.getIDProdotto());
	            resultSet = preparedStatement.executeQuery();

	            if (resultSet.next()) {
	                byte[] img = resultSet.getBytes("immagine");
	                if (img == null) {
	                    continue;
	                }
	                String immagine = Base64.getEncoder().encodeToString(img);

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
}	
	