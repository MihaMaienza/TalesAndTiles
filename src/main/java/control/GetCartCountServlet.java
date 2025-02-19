package control;

import model.DAO.*;
import model.javaBeans.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/GetCartCountServlet")
public class GetCartCountServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utente");
        CartItemDAO cartItemDAO = new CartItemDAO();
        int itemCount = 0;
        if (utente != null) {
            // Utente loggato
            try {
                itemCount = cartItemDAO.countItemsInCartByUsername(utente.getUsername());
            } catch (SQLException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
        } else {
            // Utente non loggato
            @SuppressWarnings("unchecked")
			List<CartNoLog> cartNoLog = (List<CartNoLog>) session.getAttribute("cartNoLog");
            if (cartNoLog != null) {
                for (CartNoLog item : cartNoLog) {
                    itemCount += item.getQuantita();
                }
            }
        }

        response.setContentType("application/json");
        response.getWriter().write("{\"count\": " + itemCount + "}");
    }
}
