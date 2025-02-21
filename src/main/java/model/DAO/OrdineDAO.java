package model.DAO;

import model.javaBeans.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrdineDAO {

	public int AddOrdine(String username, float prezzoTotale, Date dataConsegna, Date dataOrdine) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;
        int IDOrdine = -1;

        try {
            con = ConDB.getConnection();
            String insertSQL = "INSERT INTO Ordine (username, prezzoTotale, dataConsegna, dataOrdine) VALUES (?, ?, ?, ?)";
            stmt = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, username);
            stmt.setFloat(2, prezzoTotale);
            stmt.setDate(3, new java.sql.Date(dataConsegna.getTime()));
            stmt.setDate(4, new java.sql.Date(dataOrdine.getTime()));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Errore creazione acquisto");
            }

            generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                IDOrdine = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creazione ordine fallita, nessun ID ottenuto.");
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

        return IDOrdine;
    }
	
	public List<Ordine> getOrdersByUsername(String username) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Ordine> ordini = new ArrayList<>();

        try {
            con = ConDB.getConnection();
            String selectSQL = "SELECT * FROM Ordine WHERE username = ?";
            stmt = con.prepareStatement(selectSQL);
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int IDOrdine = rs.getInt("ID");
                float prezzoTotale = rs.getFloat("prezzoTotale");
                Date dataConsegna = rs.getDate("dataConsegna");
                Date dataOrdine = rs.getDate("dataOrdine");
                String nomeConsegna = rs.getString("nomeConsegna");
                String cognomeConsegna = rs.getString("cognomeConsegna");
                int cap = rs.getInt("cap");
                String via = rs.getString("via");
                String citta = rs.getString("citta");
                String numCarta = rs.getString("numCarta"); 

                Ordine ordine = new Ordine(IDOrdine, username, prezzoTotale, dataConsegna, dataOrdine, nomeConsegna, cognomeConsegna, cap, via, citta, numCarta);
                ordini.add(ordine);
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (con != null) ConDB.releaseConnection(con);
        }

        return ordini;
    }
	
	public void updateOrderWithDetails(int orderId, String nome, String cognome, String cap, String via, String citta, String numCarta) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = ConDB.getConnection();
            con.setAutoCommit(false); // Assicurati che l'auto-commit sia disabilitato

            String updateSQL = "UPDATE Ordine SET nomeConsegna = ?, cognomeConsegna = ?, cap = ?, via = ?, citta = ?, numCarta = ? WHERE ID = ?";
            stmt = con.prepareStatement(updateSQL);
            stmt.setString(1, nome);
            stmt.setString(2, cognome);
            stmt.setString(3, cap);
            stmt.setString(4, via);
            stmt.setString(5, citta);
            stmt.setString(6, numCarta);
            stmt.setInt(7, orderId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                con.commit(); // Conferma la transazione se almeno una riga è stata aggiornata
            } else {
                con.rollback(); // Annulla la transazione se nessuna riga è stata aggiornata
            }
        } catch (SQLException e) {
            if (con != null) {
                con.rollback(); // Annulla la transazione in caso di errore
            }
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (con != null) ConDB.releaseConnection(con);
        }
    }
	
	public Ordine getOrderById(int IDOrdine) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Ordine ordine = null;

        try {
            con = ConDB.getConnection();
            String selectSQL = "SELECT * FROM Ordine WHERE ID = ?";
            stmt = con.prepareStatement(selectSQL);
            stmt.setInt(1, IDOrdine);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String username = rs.getString("username");
                float prezzoTotale = rs.getFloat("prezzoTotale");
                Date dataConsegna = rs.getDate("dataConsegna");
                Date dataOrdine = rs.getDate("dataOrdine");
                String nomeConsegna = rs.getString("nomeConsegna");
                String cognomeConsegna = rs.getString("cognomeConsegna");
                int cap = rs.getInt("cap");
                String via = rs.getString("via");
                String citta = rs.getString("citta");
                String numCarta = rs.getString("numCarta");

                ordine = new Ordine(IDOrdine, username, prezzoTotale, dataConsegna, dataOrdine, nomeConsegna, cognomeConsegna, cap, via, citta,numCarta);
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (con != null) ConDB.releaseConnection(con);
        }

        return ordine;
    }
	
	public void deleteOrderById(int orderId, Connection con) throws SQLException {
        PreparedStatement stmt = null;

        try {
            String query = "DELETE FROM Ordine WHERE ID = ?";
            stmt = con.prepareStatement(query);
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) stmt.close();
        }
    }
	
	public void saveOrderPDF(int orderId, byte[] pdfData, Connection con) throws SQLException {
	    String query = "UPDATE ordine SET pdf = ? WHERE ID = ?";
	    try (PreparedStatement stmt = con.prepareStatement(query)) {
	        stmt.setBytes(1, pdfData);
	        stmt.setInt(2, orderId);
	        stmt.executeUpdate();
	    }
	}
	
	public byte[] getOrderPDF(int orderId) throws SQLException {
	    String sql = "SELECT pdf FROM ordine WHERE ID = ?";
	    try (Connection con = ConDB.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setInt(1, orderId);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                return rs.getBytes("pdf");
	            } else {
	                return null;
	            }
	        }
	    }
	}
	
	public List<Ordine> getAllOrders() {
        List<Ordine> ordini = new ArrayList<>();
        try (Connection con = ConDB.getConnection()) {
            String query = "SELECT * FROM ordine";
            try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    Ordine ordine = new Ordine(
                        rs.getInt("ID"),
                        rs.getString("username"),
                        rs.getFloat("prezzoTotale"),
                        rs.getDate("dataConsegna"),
                        rs.getDate("dataOrdine"),
                        rs.getString("nomeConsegna"),
                        rs.getString("cognomeConsegna"),
                        rs.getInt("cap"),
                        rs.getString("via"),
                        rs.getString("citta"),
                        rs.getString("numCarta")
                    );
                    ordini.add(ordine);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ordini;
    }
	
	public int countOrdersByUsername(String username) {
        String query = "SELECT COUNT(*) FROM Ordine WHERE username = ?";
        try (Connection con = ConDB.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
	
	public int countTotalOrders() {
        String query = "SELECT COUNT(*) FROM Ordine";
        try (Connection con = ConDB.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
	
}
