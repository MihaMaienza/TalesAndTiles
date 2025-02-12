package model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CarrelloDAO {
	
	 public synchronized static int CreaCarrello(String username) throws SQLException {
	        

	        Connection con = null;
	        PreparedStatement stmt = null;
	        ResultSet generatedKeys = null;
	        int IDCarrello = -1;

	        try {
	            con = ConDB.getConnection();
	            stmt = con.prepareStatement("INSERT INTO carrello (Username)VALUES (?)", Statement.RETURN_GENERATED_KEYS);
	           
	            stmt.setString(1, username);

				int affectedRows = stmt.executeUpdate();
				if (affectedRows == 0) {
				    throw new SQLException("Creating product failed, no rows affected.");
				}

				generatedKeys = stmt.getGeneratedKeys();
				if (generatedKeys.next()) {
				    IDCarrello = generatedKeys.getInt(1);
				} else {
				    throw new SQLException("Creating product failed, no ID obtained.");
				}

				con.commit();
	        } finally {
	            if (generatedKeys != null) generatedKeys.close();
	            if (stmt != null) stmt.close();
	            if (con != null) ConDB.releaseConnection(con);
	        }

	        return IDCarrello;
	    }
	 
	 public Carrello getCarrelloByUsername(String username) throws SQLException {
	        Connection connection = null;
	        PreparedStatement preparedStatement = null;
	        ResultSet resultSet = null;
	        Carrello carrello = null;

	        try {
	            connection = ConDB.getConnection();
	            String selectSQL = "SELECT * FROM carrello WHERE username = ?";
	            preparedStatement = connection.prepareStatement(selectSQL);
	            preparedStatement.setString(1, username);
	            resultSet = preparedStatement.executeQuery();

	            if (resultSet.next()) {
	                int IDCarrello = resultSet.getInt("IDCarrello");
	                carrello= new Carrello(IDCarrello,username);
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
	        return carrello;
	    }
	} 
	
