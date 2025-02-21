package model.DAO;

import model.javaBeans.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class WishlistDAO {
	
	 public synchronized static int CreaWishlist(String username) throws SQLException {
	        

	        Connection con = null;
	        PreparedStatement stmt = null;
	        ResultSet generatedKeys = null;
	        int IDWishlist = -1;

	        try {
	            con = ConDB.getConnection();
	            stmt = con.prepareStatement("INSERT INTO wishlist (Username)VALUES (?)", Statement.RETURN_GENERATED_KEYS);
	           
	            stmt.setString(1, username);

				int affectedRows = stmt.executeUpdate();
				if (affectedRows == 0) {
				    throw new SQLException("Creating product failed, no rows affected.");
				}

				generatedKeys = stmt.getGeneratedKeys();
				if (generatedKeys.next()) {
				    IDWishlist = generatedKeys.getInt(1);
				} else {
				    throw new SQLException("Creating product failed, no ID obtained.");
				}

				con.commit();
	        } finally {
	            if (generatedKeys != null) generatedKeys.close();
	            if (stmt != null) stmt.close();
	            if (con != null) ConDB.releaseConnection(con);
	        }

	        return IDWishlist;
	    }
	 
	 public Wishlist getWishlistByUsername(String username) throws SQLException {
	        Connection connection = null;
	        PreparedStatement preparedStatement = null;
	        ResultSet resultSet = null;
	        Wishlist wishlist = null;

	        try {
	            connection = ConDB.getConnection();
	            String selectSQL = "SELECT * FROM Wishlist WHERE username = ?";
	            preparedStatement = connection.prepareStatement(selectSQL);
	            preparedStatement.setString(1, username);
	            resultSet = preparedStatement.executeQuery();

	            if (resultSet.next()) {
	                int IDWishlist = resultSet.getInt("IDWishlist");
	                wishlist= new Wishlist(IDWishlist,username);
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
	        return wishlist;
	    }
	} 
	