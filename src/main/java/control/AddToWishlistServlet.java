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

@WebServlet("/AddToWishlistServlet")
public class AddToWishlistServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer IDWishlist = (Integer) session.getAttribute("IDWishlist");

        if (IDWishlist == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":\"unauthorized\"}");
            return;
        }

        int IDProdotto = Integer.parseInt(request.getParameter("IDProdotto"));

        WishlistItemDAO wishlistItemDAO = new WishlistItemDAO();
        try {
            WishlistItem existingItem = wishlistItemDAO.findWishlistItemByProductId(IDWishlist, IDProdotto);
            if (existingItem != null) {
                response.setContentType("application/json");
                response.getWriter().write("{\"status\":\"already_exists\"}");
            } else {
                wishlistItemDAO.AddWishlistItem(IDWishlist, IDProdotto);
                response.setContentType("application/json");
                response.getWriter().write("{\"status\":\"success\"}");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":\"error\"}");
        }
    }
}
