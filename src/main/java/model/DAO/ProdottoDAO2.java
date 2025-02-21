package model.DAO;

import model.javaBeans.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ProdottoDAO2 {


    public synchronized static boolean checkIfProductExists(String id) throws SQLException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean exists = false;

        try {
            con = ConDB.getConnection();
            stmt = con.prepareStatement("SELECT 1 FROM prodotto WHERE ID = ?");
            stmt.setString(1, id);
            rs = stmt.executeQuery();
            exists = rs.next();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException sqlException) {
                System.out.println(sqlException);
            } finally {
                if (con != null) ConDB.releaseConnection(con);
            }
        }

        return exists;
    }


    public synchronized static int saveProduct(String nome, float prezzo, int iva, String tipo, String imagePath, int disponibilita, String descrizione, String condizione, int annoPubblicazione, String fasciaEta, String casaProduttrice, int idGenereGioco, String editore, String scrittore, String isbn, int idGenereLibro) throws SQLException {


        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;
        int productId = -1;

        try {
            con = ConDB.getConnection();
            con.setAutoCommit(false); // Avvia una transazione

            // Inserimento nella tabella "prodotto"
            stmt = con.prepareStatement(
                "INSERT INTO prodotto (nome, prezzo, IVA, tipo, immagine, disponibilita, descrizione, condizione, annoPubblicazione) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", 
                Statement.RETURN_GENERATED_KEYS);

            File file = new File(imagePath);
            try (FileInputStream fis = new FileInputStream(file)) {
                stmt.setString(1, nome);
                stmt.setFloat(2, prezzo);
                stmt.setInt(3, iva);
                stmt.setString(4, tipo);
                stmt.setBinaryStream(5, fis, (int) file.length());
                stmt.setInt(6, disponibilita);
                stmt.setString(7, descrizione);
                stmt.setString(8, condizione);
                stmt.setInt(9, annoPubblicazione);

                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating product failed, no rows affected.");
                }

                generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    productId = generatedKeys.getInt(1); // Ottieni l'ID generato per il prodotto
                } else {
                    throw new SQLException("Creating product failed, no ID obtained.");
                }
            } catch (FileNotFoundException e) {
                System.out.println(e);
                return -1;
            } catch (IOException e) {
                System.out.println(e);
                return -1;
            }

            // Inserimento nella tabella specifica (gioco o libro)
            if ("gioco".equals(tipo)) {
                stmt = con.prepareStatement(
                    "INSERT INTO gioco (eta, casaProduttrice, idProdotto, idGenere) VALUES (?, ?, ?, ?)");

                stmt.setString(1, fasciaEta);
                stmt.setString(2, casaProduttrice);
                stmt.setInt(3, productId); // Usa l'ID prodotto generato
                stmt.setObject(4, idGenereGioco); // Usa setObject per gestire NULL

                stmt.executeUpdate();
            } else if ("libro".equals(tipo)) {
                stmt = con.prepareStatement(
                    "INSERT INTO libro (scrittore, editore, isbn, idProdotto, idGenere) VALUES (?, ?, ?, ?, ?)");

                stmt.setString(1, scrittore);
                stmt.setString(2, editore);
                stmt.setString(3, isbn);
                stmt.setInt(4, productId); // Usa l'ID prodotto generato
                stmt.setObject(5, idGenereLibro); // Usa setObject per gestire NULL

                stmt.executeUpdate();
            }

            // Conferma la transazione
            con.commit();
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback(); // Rollback in caso di errore
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            throw e; // Propaga l'eccezione
        } finally {
            if (generatedKeys != null) generatedKeys.close();
            if (stmt != null) stmt.close();
            if (con != null) ConDB.releaseConnection(con);
        }

        return productId; // Restituisci l'ID del prodotto
    }
    
    public static boolean updateProduct(int productId, String nome, float prezzo, int iva, String tipo, 
            String imagePath, int disponibilita, String descrizione, 
            String condizione, int annoPubblicazione, String fasciaEta, 
            String casaProduttrice, Integer idGenereGioco, String editore, 
            String scrittore, String isbn, Integer idGenereLibro) throws Exception {
    		Connection con = null;
    		PreparedStatement stmt = null;

    		try {
    				con = ConDB.getConnection();
    				con.setAutoCommit(false);

    				// Aggiorna dettagli base del prodotto
    				String query = "UPDATE prodotto SET nome = ?, prezzo = ?, IVA = ?, disponibilita = ?, " +
    						"descrizione = ?, condizione = ?, annoPubblicazione = ?" +
    						(imagePath != null ? ", immagine = ?" : "") +
    						" WHERE ID = ?";
    				stmt = con.prepareStatement(query);

    				stmt.setString(1, nome);
    				stmt.setFloat(2, prezzo);
    				stmt.setInt(3, iva);
    				stmt.setInt(4, disponibilita);
    				stmt.setString(5, descrizione);
    				stmt.setString(6, condizione);
    				stmt.setInt(7, annoPubblicazione);
    				if (imagePath != null) {
    					File file = new File(imagePath);
    					FileInputStream fis = new FileInputStream(file);
    					stmt.setBinaryStream(8, fis, (int) file.length());
    					stmt.setInt(9, productId);
    				} else {
    					stmt.setInt(8, productId);
    				}

    				stmt.executeUpdate();

    				// Aggiorna dettagli specifici
    				if ("gioco".equalsIgnoreCase(tipo)) {
						stmt = con.prepareStatement("UPDATE gioco SET eta = ?, casaProduttrice = ?, idGenere = ? WHERE idProdotto = ?");
						stmt.setString(1, fasciaEta);
						stmt.setString(2, casaProduttrice);
						stmt.setObject(3, idGenereGioco);
						stmt.setInt(4, productId);
					} else if ("libro".equalsIgnoreCase(tipo)) {
						stmt = con.prepareStatement("UPDATE libro SET scrittore = ?, editore = ?, isbn = ?, idGenere = ? WHERE idProdotto = ?");
						stmt.setString(1, scrittore);
						stmt.setString(2, editore);
						stmt.setString(3, isbn);
						stmt.setObject(4, idGenereLibro);
						stmt.setInt(5, productId);
					}
					
					stmt.executeUpdate();
					con.commit();
					return true;
					} catch (Exception e) {
						if (con != null) con.rollback();
						throw e;
					} finally {
						if (stmt != null) stmt.close();
						if (con != null) ConDB.releaseConnection(con);
					}
    }
}
