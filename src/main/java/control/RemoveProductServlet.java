package control;

import model.DAO.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@SuppressWarnings("serial")
@WebServlet("/RemoveProductServlet")
public class RemoveProductServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int productId = Integer.parseInt(request.getParameter("id"));

        ProdottoDAO prodottoDAO = new ProdottoDAO();
        CartItemDAO cartItemDAO = new CartItemDAO();

        try {
            // Rimuovi il prodotto dai carrelli
            List<Integer> carrelli = cartItemDAO.getCarrelliByProductId(productId);
            for (int carrelloId : carrelli) {
                cartItemDAO.removeProductFromCartByProductId(productId, carrelloId);
            }

            // Rimuovi il prodotto dal database
            prodottoDAO.removeProductById(productId);

            // Reindirizza con messaggio di successo
            response.sendRedirect(request.getContextPath() + "/view/ListaProdotti.jsp?message=success");
        } catch (SQLException e) {
            e.printStackTrace();
            // Reindirizza con messaggio di errore
            response.sendRedirect(request.getContextPath() + "/view/ListaProdotti.jsp?message=error");
        }
    }
}
