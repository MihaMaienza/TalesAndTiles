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
import com.google.gson.Gson;

import java.util.List;

@WebServlet("/RemoveFromCartServlet")
public class RemoveFromCartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utente");
        boolean isLoggedIn = (utente != null);
        @SuppressWarnings("resource")
		int IDProdotto = new Gson().fromJson(request.getReader(), RemoveFromCartRequest.class).productId;

        try {
            if (isLoggedIn) {
                int IDCarrello = (int) session.getAttribute("IDCarrello");
                CartItemDAO cartItemDAO = new CartItemDAO();
                cartItemDAO.removeProductFromCartByProductId(IDProdotto, IDCarrello);
            } else {
                @SuppressWarnings("unchecked")
                List<CartNoLog> cartNoLogItems = (List<CartNoLog>) session.getAttribute("cartNoLog");
                cartNoLogItems.removeIf(item -> item.getIDProdotto() == IDProdotto);
                session.setAttribute("cartNoLog", cartNoLogItems);
            }
            response.setContentType("application/json");
            response.getWriter().write("{\"status\": \"success\"}");
        } catch (SQLException e) {
            e.printStackTrace();
            response.setContentType("application/json");
            response.getWriter().write("{\"status\": \"error\"}");
        }
    }

    private static class RemoveFromCartRequest {
        int productId;
    }
}
