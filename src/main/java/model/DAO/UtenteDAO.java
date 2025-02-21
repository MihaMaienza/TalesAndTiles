package model.DAO;

import model.javaBeans.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UtenteDAO {

    public void save(Utente utente) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = ConDB.getConnection();
            String insertSQL = "INSERT INTO utente (username, pwd, nome, cognome, email, dataNascita, nomeCarta, cognomeCarta, numCarta, dataScadenza, CVV, cap, via, citta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, utente.getUsername());
            preparedStatement.setString(2, utente.getPwd());
            preparedStatement.setString(3, utente.getNome());
            preparedStatement.setString(4, utente.getCognome());
            preparedStatement.setString(5, utente.getEmail());

            if (utente.getDataNascita() != null) {
                preparedStatement.setDate(6, new java.sql.Date(utente.getDataNascita().getTime()));
            } else {
                preparedStatement.setNull(6, java.sql.Types.DATE);
            }

            preparedStatement.setString(7, utente.getNomeCarta());
            preparedStatement.setString(8, utente.getCognomeCarta());
            preparedStatement.setString(9, utente.getNumCarta());

            if (utente.getDataNascita() != null) {
                preparedStatement.setDate(10, new java.sql.Date(utente.getDataScadenza().getTime()));
            } else {
                preparedStatement.setNull(10, java.sql.Types.DATE);
            }


            preparedStatement.setString(11, utente.getCVV());
            preparedStatement.setString(12, utente.getCap());
            preparedStatement.setString(13, utente.getVia());
            preparedStatement.setString(14, utente.getCitta());

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

    public void update(Utente utente) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = ConDB.getConnection();
            String updateSQL = "UPDATE utente SET nome=?, cognome=?, email=?, dataNascita=?, nomeCarta=?, cognomeCarta=?, numCarta=?, dataScadenza=?, CVV=?, cap=?, via=?, citta=?";

            if (utente.getPwd() != null && !utente.getPwd().isEmpty()) {
                updateSQL += ", pwd=?";
            }

            updateSQL += " WHERE username=?";

            preparedStatement = connection.prepareStatement(updateSQL);
            preparedStatement.setString(1, utente.getNome());
            preparedStatement.setString(2, utente.getCognome());
            preparedStatement.setString(3, utente.getEmail());

            if (utente.getDataNascita() != null) {
                preparedStatement.setDate(4, new java.sql.Date(utente.getDataNascita().getTime()));
            } else {
                preparedStatement.setNull(4, java.sql.Types.DATE);
            }

            preparedStatement.setString(5, utente.getNomeCarta());
            preparedStatement.setString(6, utente.getCognomeCarta());
            preparedStatement.setString(7, utente.getNumCarta());

            if (utente.getDataScadenza() != null) {
                preparedStatement.setDate(8, new java.sql.Date(utente.getDataScadenza().getTime()));
            } else {
                preparedStatement.setNull(8, java.sql.Types.DATE);
            }

            preparedStatement.setString(9, utente.getCVV());
            preparedStatement.setString(10, utente.getCap());
            preparedStatement.setString(11, utente.getVia());
            preparedStatement.setString(12, utente.getCitta());

            int index = 13;
            if (utente.getPwd() != null && !utente.getPwd().isEmpty()) {
                preparedStatement.setString(index++, utente.getPwd());
            }

            preparedStatement.setString(index, utente.getUsername());

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


    public Utente findByUsername(String username) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = ConDB.getConnection();
            String selectSQL = "SELECT * FROM utente WHERE username = ?";
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String pwd = resultSet.getString("pwd");
                String nome = resultSet.getString("nome");
                String cognome = resultSet.getString("cognome");
                String email = resultSet.getString("email");
                Date dataNascita = resultSet.getDate("dataNascita");
                String nomeCarta = resultSet.getString("nomeCarta");
                String cognomeCarta = resultSet.getString("cognomeCarta");
                String numCarta = resultSet.getString("numCarta");
                Date dataScadenza = resultSet.getDate("dataScadenza");
                String CVV = resultSet.getString("CVV");
                String cap = resultSet.getString("cap");
                String via = resultSet.getString("via");
                String citta = resultSet.getString("citta");
                int tipo= resultSet.getInt("tipo");

                return new Utente(username, pwd, nome, cognome, email, dataNascita, nomeCarta, cognomeCarta, numCarta, dataScadenza, CVV, cap, via, citta,tipo);
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
        return null;
    }

public boolean isEmailInUse(String email) throws SQLException {
    String query = "SELECT COUNT(*) FROM utente WHERE email = ?";
    try (Connection connection = ConDB.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setString(1, email);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        }
    }
    return false;
}

public boolean isUsernameInUse(String username) throws SQLException {
    String query = "SELECT COUNT(*) FROM utente WHERE username = ?";
    try (Connection connection = ConDB.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setString(1, username);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        }
    }
    return false;
}

public boolean isPasswordValid(String password) {
    return password != null && password.length() >= 8;
}
 // Nuovo metodo per ottenere il numero totale di utenti
    public int getUtenteCount() throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int count = 0;

        try {
            connection = ConDB.getConnection();
            String countSQL = "SELECT COUNT(*) FROM utente";
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

    // Nuovo metodo per ottenere la lista di tutti gli utenti
    public List<Utente> getAllUtenti() throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Utente> utenti = new ArrayList<>();

        try {
            connection = ConDB.getConnection();
            String selectSQL = "SELECT * FROM utente";
            preparedStatement = connection.prepareStatement(selectSQL);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String pwd = resultSet.getString("pwd");
                String nome = resultSet.getString("nome");
                String cognome = resultSet.getString("cognome");
                String email = resultSet.getString("email");
                Date dataNascita = resultSet.getDate("dataNascita");
                String nomeCarta = resultSet.getString("nomeCarta");
                String cognomeCarta = resultSet.getString("cognomeCarta");
                String numCarta = resultSet.getString("numCarta");
                Date dataScadenza = resultSet.getDate("dataScadenza");
                String CVV = resultSet.getString("CVV");
                String cap = resultSet.getString("cap");
                String via = resultSet.getString("via");
                String citta = resultSet.getString("citta");
                int tipo = resultSet.getInt("tipo");

                Utente utente = new Utente(username, pwd, nome, cognome, email, dataNascita, nomeCarta, cognomeCarta, numCarta, dataScadenza, CVV, cap, via, citta, tipo);
                utenti.add(utente);
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
        return utenti;
    }
}


