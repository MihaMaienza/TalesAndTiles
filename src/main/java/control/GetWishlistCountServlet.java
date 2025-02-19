package control;

import model.DAO.*;
import model.javaBeans.*;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/GetWishlistCountServlet")
public class GetWishlistCountServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utente");
        WishlistItemDAO wishlistItemDAO = new WishlistItemDAO();
        int itemCount = 0;

        if (utente != null) {
            // Utente loggato
            try {
                itemCount = wishlistItemDAO.countItemsInWishlistByUsername(utente.getUsername());
            } catch (SQLException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
        }

        response.setContentType("application/json");
        response.getWriter().write("{\"count\": " + itemCount + "}");
    }
}
