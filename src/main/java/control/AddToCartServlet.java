package control;

import model.DAO.*;
import model.javaBeans.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/AddToCartServlet")
public class AddToCartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utente");

        int IDProdotto = Integer.parseInt(request.getParameter("IDProdotto"));

        ProdottoDAO prodottoDAO = new ProdottoDAO();
        Prodotto prodotto;
        try {
            prodotto = prodottoDAO.findById(IDProdotto);
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("error");
            return;
        }

        if (utente != null) {
            // Utente loggato
            int IDCarrello = (int) session.getAttribute("IDCarrello");
            try {
                CartItemDAO cartItemDAO = new CartItemDAO();
                int quantitaInCarrello = cartItemDAO.getQuantityByProductIdAndCarrelloId(IDProdotto, IDCarrello);

                if (quantitaInCarrello < prodotto.getDisponibilita()) {
                    @SuppressWarnings({ "unused", "static-access" })
                    int IDCartItem = cartItemDAO.AddCartItem(IDCarrello, IDProdotto);
                    response.getWriter().write("success");
                } else {
                    response.getWriter().write("not_available");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                response.getWriter().write("error");
            }
        } else {
            // Utente non loggato
            @SuppressWarnings("unchecked")
            List<CartNoLog> cartNoLog = (List<CartNoLog>) session.getAttribute("cartNoLog");
            if (cartNoLog == null) {
                cartNoLog = new ArrayList<>();
            }

            boolean productExists = false;
            for (CartNoLog item : cartNoLog) {
                if (item.getIDProdotto() == IDProdotto) {
                    if (item.getQuantita() < prodotto.getDisponibilita()) {
                        item.setQuantita(item.getQuantita() + 1);
                        productExists = true;
                        break;
                    } else {
                        response.getWriter().write("not_available");
                        return;
                    }
                }
            }

            if (!productExists) {
                cartNoLog.add(new CartNoLog(IDProdotto, 1));
            }

            session.setAttribute("cartNoLog", cartNoLog);
            response.getWriter().write("success");
        }
    }
}
