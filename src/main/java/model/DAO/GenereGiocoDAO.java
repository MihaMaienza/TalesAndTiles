package model.DAO;

import model.javaBeans.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenereGiocoDAO {
	
    public static List<GenereGioco> getGenereGioco() throws SQLException {
        List<GenereGioco> genereGiochi = new ArrayList<>();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = ConDB.getConnection();
            stmt = con.prepareStatement("SELECT ID, nome FROM generegioco");
            rs = stmt.executeQuery();

            while (rs.next()) {
                GenereGioco genereGioco = new GenereGioco(rs.getInt("ID"), rs.getString("nome"));
                genereGiochi.add(genereGioco);
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } finally {
                if (con != null) ConDB.releaseConnection(con);
            }
        }

        return genereGiochi;
    }
    
    public static String getNomeGenereGioco(int idGenere) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String nomeGenere = null;

        try {
            connection = ConDB.getConnection();
            String query = "SELECT nome FROM genereGioco WHERE ID = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idGenere);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                nomeGenere = resultSet.getString("nome");
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) ConDB.releaseConnection(connection);
        }

        return nomeGenere;
    }


}
