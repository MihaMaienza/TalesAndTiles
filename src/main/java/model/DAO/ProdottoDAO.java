package model.DAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.Base64;
import java.util.HashMap;

public class ProdottoDAO {

	public Prodotto findById(int id) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Prodotto prodotto = null;

        try {
            connection = ConDB.getConnection();
            String selectSQL = "SELECT * FROM prodotto WHERE id = ?";
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
            	byte [] img = resultSet.getBytes("immagine");

            	String immagine =Base64.getEncoder().encodeToString(img);
                prodotto = new Prodotto(
                    resultSet.getInt("id"),
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
        return prodotto;
    }

    public List<Prodotto> getAllProdotti() throws SQLException, ClassNotFoundException {
    	List<Prodotto> prodotti = new ArrayList<>();
        try (Connection element = ConDB.getConnection();
             PreparedStatement stmt = element.prepareStatement("SELECT * FROM prodotto WHERE eliminato = 0");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
            	//prendiamo l'immagine dal database sotto forma di array di byte e con la successiva funzione la trasformiamo l'array di byte in una stringa
            	byte [] img = rs.getBytes("immagine");
            	if(img==null) {
            		continue;
            	}
            	String immagine =Base64.getEncoder().encodeToString(img);


                Prodotto prodotto = new Prodotto(
                    rs.getInt("ID"),
                    rs.getString("nome"),
                    rs.getFloat("prezzo"),
                    rs.getInt("IVA"),
                    rs.getString("tipo"),
                    immagine,
                    rs.getInt("disponibilita"),
                    rs.getString("descrizione"),
                    rs.getString("condizione"),
                    rs.getInt("annoPubblicazione")
                );
                prodotti.add(prodotto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prodotti;
    }


    public List <Prodotto> getAllGames() throws SQLException {
    	List<Prodotto> prodotti=new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = ConDB.getConnection();
            String selectSQL = "SELECT p.* " +
                    "FROM Prodotto p " +
                    "WHERE p.tipo = 'gioco' AND eliminato = 0";

            preparedStatement = connection.prepareStatement(selectSQL);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
            	//prendiamo l'immagine dal database sotto forma di array di byte e con la successiva funzione la trasformiamo l'array di byte in una stringa
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
        return prodotti;
    }


    public List <Prodotto> getAllLibro() throws SQLException {
    	List<Prodotto> prodotti=new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = ConDB.getConnection();
            String selectSQL = "SELECT p.* " +
                    "FROM Prodotto p " +
                    "WHERE p.tipo = 'libro' AND eliminato = 0";

            preparedStatement = connection.prepareStatement(selectSQL);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
            	//prendiamo l'immagine dal database sotto forma di array di byte e con la successiva funzione la trasformiamo l'array di byte in una stringa
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
        return prodotti;
    }

//metodo ausiliario per funzionamento searchbar
    public List<Prodotto> searchProdotti(String query) throws SQLException {
        List<Prodotto> prodotti = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = ConDB.getConnection();
            String sql = "SELECT * FROM prodotto WHERE nome LIKE ? AND eliminato = 0";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "%" + query + "%");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                byte[] img = resultSet.getBytes("immagine");
                String immagine = (img != null) ? Base64.getEncoder().encodeToString(img) : null;

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
        } finally {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        }

        return prodotti;
    }

    public List <Prodotto> getAllProductByCondizione(String condizione) throws SQLException {
    	List<Prodotto> prodotti=new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = ConDB.getConnection();
            String selectSQL = "SELECT * FROM prodotto WHERE condizione = ? AND eliminato = 0";

            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, condizione);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
            	//prendiamo l'immagine dal database sotto forma di array di byte e con la successiva funzione la trasformiamo l'array di byte in una stringa
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
        return prodotti;
    }


 // Nuovo metodo per ottenere il numero totale di prodotti
    public int getProdottiCount() throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int count = 0;

        try {
            connection = ConDB.getConnection();
            String countSQL = "SELECT COUNT(*) FROM prodotto WHERE eliminato = 0";
            preparedStatement = connection.prepareStatement(countSQL);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } finally {
                if (connection != null) {
                    ConDB.releaseConnection(connection);
                }
            }
        }
        return count;
    }


    public List<Prodotto> getAllProdottiSorted(String sortOrder) throws SQLException {
        List<Prodotto> prodotti = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = ConDB.getConnection();
            String selectSQL = "SELECT * FROM prodotto WHERE eliminato = 0 ORDER BY prezzo " + ("desc".equalsIgnoreCase(sortOrder) ? "DESC" : "ASC");

            preparedStatement = connection.prepareStatement(selectSQL);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                // prendiamo l'immagine dal database sotto forma di array di byte e con la successiva funzione la trasformiamo in una stringa
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
        return prodotti;
			}

			public void removeProductById(int productId) throws SQLException {
				    Connection connection = null;
				    PreparedStatement preparedStatement = null;

				    try {
				        connection = ConDB.getConnection();
				        String updateSQL = "UPDATE prodotto SET eliminato = 1 WHERE ID = ?";
				        preparedStatement = connection.prepareStatement(updateSQL);
				        preparedStatement.setInt(1, productId);

				        preparedStatement.executeUpdate();
				        connection.commit();
				    } catch (SQLException e) {
				        if (connection != null) {
				            connection.rollback();
				        }
				        e.printStackTrace();
				        throw e;
				    } finally {
				        if (preparedStatement != null) {
				            preparedStatement.close();
				        }
				        if (connection != null) {
				            ConDB.releaseConnection(connection);
				        }
				    }
				}
			
	public Map<String, Object> getProductDetails(int productId) throws SQLException {
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    Map<String, Object> productDetails = new HashMap<>();
	
	    try {
	        connection = ConDB.getConnection();
	
	        // Recupera le informazioni di base dal prodotto
	        String productQuery = "SELECT * FROM prodotto WHERE ID = ?";
	        preparedStatement = connection.prepareStatement(productQuery);
	        preparedStatement.setInt(1, productId);
	        resultSet = preparedStatement.executeQuery();
	
	        if (resultSet.next()) {
	            // Codifica l'immagine in Base64
	            byte[] img = resultSet.getBytes("immagine");
	            String immagine = Base64.getEncoder().encodeToString(img);
	
	            // Aggiungi i dettagli base del prodotto alla mappa
	            productDetails.put("ID", resultSet.getInt("ID"));
	            productDetails.put("nome", resultSet.getString("nome"));
	            productDetails.put("prezzo", resultSet.getDouble("prezzo"));
	            productDetails.put("IVA", resultSet.getInt("IVA"));
	            productDetails.put("tipo", resultSet.getString("tipo"));
	            productDetails.put("immagine", immagine);
	            productDetails.put("disponibilita", resultSet.getInt("disponibilita"));
	            productDetails.put("descrizione", resultSet.getString("descrizione"));
	            productDetails.put("condizione", resultSet.getString("condizione"));
	            productDetails.put("annoPubblicazione", resultSet.getInt("annoPubblicazione"));
	            productDetails.put("eliminato", resultSet.getInt("eliminato"));
	
	            // Controlla il tipo per aggiungere i dettagli specifici
	            String tipo = resultSet.getString("tipo");
	            if ("gioco".equalsIgnoreCase(tipo)) {
	                // Recupera dettagli dalla tabella gioco
	                String gameQuery = "SELECT * FROM gioco WHERE idProdotto = ?";
	                preparedStatement = connection.prepareStatement(gameQuery);
	                preparedStatement.setInt(1, productId);
	                ResultSet gameResultSet = preparedStatement.executeQuery();
	
	                if (gameResultSet.next()) {
	                    int idGenere = gameResultSet.getInt("idGenere");
	                    String nomeGenere = GenereGiocoDAO.getNomeGenereGioco(idGenere);
	
	                    productDetails.put("eta", gameResultSet.getInt("eta"));
	                    productDetails.put("casaProduttrice", gameResultSet.getString("casaProduttrice"));
	                    productDetails.put("idGenere", idGenere);
	                    productDetails.put("nomeGenere", nomeGenere);
	                }
	            } else if ("libro".equalsIgnoreCase(tipo)) {
	                // Recupera dettagli dalla tabella libro
	                String bookQuery = "SELECT * FROM libro WHERE idProdotto = ?";
	                preparedStatement = connection.prepareStatement(bookQuery);
	                preparedStatement.setInt(1, productId);
	                ResultSet bookResultSet = preparedStatement.executeQuery();
	
	                if (bookResultSet.next()) {
	                    int idGenere = bookResultSet.getInt("idGenere");
	                    String nomeGenere = GenereLibroDAO.getNomeGenereLibro(idGenere);
	
	                    productDetails.put("scrittore", bookResultSet.getString("scrittore"));
	                    productDetails.put("editore", bookResultSet.getString("editore"));
	                    productDetails.put("isbn", bookResultSet.getString("isbn"));
	                    productDetails.put("idGenere", idGenere);
	                    productDetails.put("nomeGenere", nomeGenere);
	                }
	            }
	        }
	
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw e;
	    } finally {
	        if (resultSet != null) {
	            resultSet.close();
	        }
	        if (preparedStatement != null) {
	            preparedStatement.close();
	        }
	        if (connection != null) {
	            ConDB.releaseConnection(connection);
	        }
	    }
	
	    return productDetails;
	}
	
	public List<Prodotto> getAllProductsByIdGenereGioco(int idGenereGioco) throws SQLException {
	    List<Prodotto> prodotti = new ArrayList<>();
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	
	    try {
	        connection = ConDB.getConnection();
	
	        // Query per ottenere i prodotti associati a un dato idGenereGioco con eliminato = 0
	        String selectSQL = "SELECT p.* FROM prodotto p " +
	                           "JOIN gioco g ON p.ID = g.idProdotto " +
	                           "WHERE g.idGenere = ? AND p.eliminato = 0";
	        preparedStatement = connection.prepareStatement(selectSQL);
	        preparedStatement.setInt(1, idGenereGioco);
	        resultSet = preparedStatement.executeQuery();
	
	        while (resultSet.next()) {
	            // Ottieni l'immagine e convertila in Base64
	            byte[] img = resultSet.getBytes("immagine");
	            String immagine = (img != null) ? Base64.getEncoder().encodeToString(img) : null;
	
	            // Crea un oggetto Prodotto
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
	    } finally {
	        // Chiudi le risorse
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
	    return prodotti;
	}
	
	
	public List<Prodotto> getAllProductsByIdGenereLibro(int idGenereLibro) throws SQLException {
	    List<Prodotto> prodotti = new ArrayList<>();
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	
	    try {
	        connection = ConDB.getConnection();
	
	        // Query per ottenere i prodotti associati a un dato idGenereLibro con eliminato = 0
	        String selectSQL = "SELECT p.* FROM prodotto p " +
	                           "JOIN libro l ON p.ID = l.idProdotto " +
	                           "WHERE l.idGenere = ? AND p.eliminato = 0";
	        preparedStatement = connection.prepareStatement(selectSQL);
	        preparedStatement.setInt(1, idGenereLibro);
	        resultSet = preparedStatement.executeQuery();
	
	        while (resultSet.next()) {
	            // Ottieni l'immagine e convertila in Base64
	            byte[] img = resultSet.getBytes("immagine");
	            String immagine = (img != null) ? Base64.getEncoder().encodeToString(img) : null;
	
	            // Crea un oggetto Prodotto
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
	    } finally {
	        // Chiudi le risorse
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
	    return prodotti;
	}
	
	public void updateProductAvailability(int productId, int quantity, Connection con) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String updateSQL = "UPDATE Prodotto SET disponibilita = disponibilita - ? WHERE ID = ?";
            stmt = con.prepareStatement(updateSQL);
            stmt.setInt(1, quantity);
            stmt.setInt(2, productId);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) stmt.close();
        }
    }


}
