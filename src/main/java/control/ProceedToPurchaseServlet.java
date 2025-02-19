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
import java.sql.SQLException;
import java.util.*;

@WebServlet("/ProceedToPurchaseServlet")
public class ProceedToPurchaseServlet extends HttpServlet {
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
        String[] productIds = gson.fromJson(requestBody.get("productIds"), String[].class);

        if (productIds == null || productIds.length == 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        ProdottoDAO prodottoDAO = new ProdottoDAO();
        CartItemDAO cartItemDAO = new CartItemDAO();
        OrdineDAO ordineDAO = new OrdineDAO();
        AcquistoDAO acquistoDAO = new AcquistoDAO();

        try {
            // Creare un nuovo ordine
            java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
            Calendar cal = Calendar.getInstance();
            cal.setTime(currentDate);
            cal.add(Calendar.DAY_OF_MONTH, 5);
            java.sql.Date deliveryDate = new java.sql.Date(cal.getTimeInMillis());

            int IDCarrello = (int) session.getAttribute("IDCarrello");
            float totalPrice = cartItemDAO.calculateTotalPrice(IDCarrello, productIds);

            int ordineId = ordineDAO.AddOrdine(utente.getUsername(), totalPrice, deliveryDate, currentDate);

            // Aggiungere i prodotti selezionati all'ordine
            for (String productIdStr : productIds) {
                int productId = Integer.parseInt(productIdStr);
                Prodotto prodotto = prodottoDAO.findById(productId);
                int quantity = cartItemDAO.getQuantityByProductIdAndCarrelloId(productId, IDCarrello);
                
                // Decodifica l'immagine Base64 in un array di byte
                byte[] immagine = Base64.getDecoder().decode(prodotto.getImmagine());

                acquistoDAO.AddAcquisto(ordineId, prodotto.getId(), prodotto.getNome(), quantity, immagine, prodotto.getPrezzo(), prodotto.getIVA());
            }

            response.setContentType("application/json");
            response.getWriter().write("{\"status\": \"success\", \"orderId\": " + ordineId + "}");
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write("{\"status\": \"error\"}");
        }
    }
}
