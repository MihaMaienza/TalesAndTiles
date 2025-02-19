package control;

import model.DAO.*;
import model.javaBeans.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/UpdateCartQuantityServlet")
public class UpdateCartQuantityServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utente");
        boolean isLoggedIn = (utente != null);

        JsonObject jsonObject = JsonParser.parseReader(request.getReader()).getAsJsonObject();
        int IDProdotto = jsonObject.get("productId").getAsInt();
        int quantita = jsonObject.get("quantity").getAsInt();

        try {
            if (isLoggedIn) {
                int IDCarrello = (int) session.getAttribute("IDCarrello");
                CartItemDAO cartItemDAO = new CartItemDAO();
                cartItemDAO.updateCartItemQuantity(IDCarrello, IDProdotto, quantita);
            } else {
                @SuppressWarnings("unchecked")
                List<CartNoLog> cartNoLogItems = (List<CartNoLog>) session.getAttribute("cartNoLog");
                if (cartNoLogItems != null) {
                    CartNoLogDAO cartNoLogDAO = new CartNoLogDAO();
                    cartNoLogDAO.updateCartNoLogItemQuantity(cartNoLogItems, IDProdotto, quantita);
                    session.setAttribute("cartNoLog", cartNoLogItems);
                }
            }
            response.setContentType("application/json");
            response.getWriter().write("{\"status\": \"success\"}");
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\": \"error\"}");
        }
    }
}
