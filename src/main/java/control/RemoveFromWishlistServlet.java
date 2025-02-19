package control;

import model.DAO.*;
import model.javaBeans.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("unused")
@WebServlet("/RemoveFromWishlistServlet")
public class RemoveFromWishlistServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utente");
        int IDWishlist = (int) session.getAttribute("IDWishlist");
        boolean isLoggedIn = (utente != null);
        
        // Leggi il productId dal body della richiesta
        @SuppressWarnings("resource")
		int IDProdotto = new Gson().fromJson(request.getReader(), RemoveFromWishlistRequest.class).productId;

        try {
            if (isLoggedIn) {
                WishlistItemDAO wishlistItemDAO = new WishlistItemDAO();
                wishlistItemDAO.removeProductFromWishlistByProductId(IDProdotto, IDWishlist);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            response.setContentType("application/json");
            response.getWriter().write("{\"status\": \"success\"}");
        } catch (SQLException e) {
            e.printStackTrace();
            response.setContentType("application/json");
            response.getWriter().write("{\"status\": \"error\"}");
        }
    }

    private static class RemoveFromWishlistRequest {
        int productId;
    }
}
