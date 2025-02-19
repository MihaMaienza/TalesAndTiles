package control;

import model.DAO.*;
import model.javaBeans.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/CancelOrderServlet")
public class CancelOrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utente");
        boolean isLoggedIn = (utente != null);

        if (!isLoggedIn) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Gson gson = new Gson();
        JsonObject requestBody = gson.fromJson(request.getReader(), JsonObject.class);
        int orderId = requestBody.get("orderId").getAsInt();

        AcquistoDAO acquistoDAO = new AcquistoDAO();
        OrdineDAO ordineDAO = new OrdineDAO();
        Connection con = null;

        try {
            con = ConDB.getConnection();
            con.setAutoCommit(false); // Inizia la transazione

            // Rimuovere gli acquisti associati all'ordine
            acquistoDAO.deleteAcquistiByOrderId(orderId, con);

            // Rimuovere l'ordine
            ordineDAO.deleteOrderById(orderId, con);

            con.commit(); // Conferma la transazione

            response.setContentType("application/json");
            response.getWriter().write("{\"status\": \"success\"}");
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback(); // Annulla la transazione in caso di errore
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write("{\"status\": \"error\"}");
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true); // Ripristina il comportamento di commit automatico
                    ConDB.releaseConnection(con);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
