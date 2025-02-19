package control;

import model.DAO.*;
import model.javaBeans.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

@WebServlet("/FinalizePurchaseServlet")
public class FinalizePurchaseServlet extends HttpServlet {
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
        String nome = requestBody.get("nome").getAsString();
        String cognome = requestBody.get("cognome").getAsString();
        String via = requestBody.get("via").getAsString();
        String cap = requestBody.get("cap").getAsString();
        String citta = requestBody.get("citta").getAsString();
        String numCarta = requestBody.get("numCarta").getAsString();

        // Conserva solo le ultime quattro cifre del numero della carta
        if (numCarta.length() > 4) {
            numCarta = numCarta.substring(numCarta.length() - 4);
        }

        OrdineDAO ordineDAO = new OrdineDAO();
        AcquistoDAO acquistoDAO = new AcquistoDAO();
        CartItemDAO cartItemDAO = new CartItemDAO();
        ProdottoDAO prodottoDAO = new ProdottoDAO();

        ServletContext context = request.getServletContext();
        String fontPath = context.getRealPath("/resources/");

        try (Connection con = ConDB.getConnection()) {
            con.setAutoCommit(false);

            ordineDAO.updateOrderWithDetails(orderId, nome, cognome, cap, via, citta, numCarta);

            List<Acquisto> acquisti = acquistoDAO.getAcquistiByOrderId(orderId);

            for (Acquisto acquisto : acquisti) {
                int productId = acquisto.getIDProdotto();
                int quantity = acquisto.getQuantita();

                cartItemDAO.removeProductFromCartByOrder((int) session.getAttribute("IDCarrello"), productId, con);
                prodottoDAO.updateProductAvailability(productId, quantity, con);
                
             // Aggiorna le quantità dei prodotti nei carrelli degli altri utenti
                cartItemDAO.updateCartItemsForAllUsers(productId, con);
            }

            byte[] pdfBytes = generateOrderPDF(orderId, ordineDAO, acquistoDAO, fontPath);
            ordineDAO.saveOrderPDF(orderId, pdfBytes, con);

            con.commit();

            // Restituisce l'URL del PDF generato
            String pdfUrl = request.getContextPath() + "/ViewOrderPDFServlet?orderId=" + orderId;

            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("status", "success");
            jsonResponse.addProperty("pdfUrl", pdfUrl);

            response.setContentType("application/json");
            response.getWriter().write(jsonResponse.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write("{\"status\": \"error\"}");
        }
    }

    private byte[] generateOrderPDF(int orderId, OrdineDAO ordineDAO, AcquistoDAO acquistoDAO, String fontPath) throws IOException, SQLException {
        Ordine ordine = ordineDAO.getOrderById(orderId);
        List<Acquisto> acquisti = acquistoDAO.getAcquistiByOrderId(orderId);

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        // Carica i font dai file
        Path helveticaPath = Paths.get(fontPath, "Helvetica.ttf");
        Path helveticaBoldPath = Paths.get(fontPath, "Helvetica-Bold.ttf");
        PDType0Font helvetica = PDType0Font.load(document, Files.newInputStream(helveticaPath));
        PDType0Font helveticaBold = PDType0Font.load(document, Files.newInputStream(helveticaBoldPath));

        // Carica il logo
        String logoPath = fontPath.replace("resources", "images/Tales&Tiles_LOGO.png");
        PDImageXObject logo = PDImageXObject.createFromFile(logoPath, document);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            // Aggiungi il logo
            contentStream.drawImage(logo, 100, 680, 100, 100);  // Regola la coordinata y per spostare il logo più in basso

            // Aggiungi il nome dell'azienda accanto al logo
            contentStream.beginText();
            contentStream.setFont(helveticaBold, 24);
            contentStream.newLineAtOffset(220, 720);  // Sposta il testo più a destra rispetto al logo
            contentStream.showText("TalesAndTiles");
            contentStream.endText();

            // Aggiungi il titolo della fattura sotto il logo
            contentStream.beginText();
            contentStream.setFont(helveticaBold, 20);
            contentStream.newLineAtOffset(100, 660);  // Regola la coordinata y per posizionare il titolo sotto il logo
            contentStream.showText("Fattura Ordine #" + ordine.getIDOrdine());
            contentStream.endText();

            // Aggiungi i dettagli dell'ordine
            contentStream.beginText();
            contentStream.setFont(helvetica, 12);
            contentStream.newLineAtOffset(100, 620);  // Sposta i dettagli più in basso
            contentStream.showText("Nome: " + ordine.getNomeConsegna());
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("Cognome: " + ordine.getCognomeConsegna());
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("Indirizzo: " + ordine.getVia() + ", " + ordine.getCap() + ", " + ordine.getCitta());
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("Data Ordine: " + ordine.getDataOrdine());
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("Data Consegna: " + ordine.getDataConsegna());
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("Prezzo Totale: €" + ordine.getPrezzoTotale());
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("Mastercard: XXXX-XXXX-XXXX-" + ordine.getNumCarta());
            contentStream.endText();

            // Aggiungi l'intestazione dei prodotti acquistati
            contentStream.beginText();
            contentStream.setFont(helveticaBold, 16);
            contentStream.newLineAtOffset(100, 510);  // Sposta l'intestazione dei prodotti più in basso
            contentStream.showText("Prodotti Acquistati:");
            contentStream.endText();

            // Aggiungi i prodotti acquistati con le immagini
            int yPosition = 490;
            for (Acquisto acquisto : acquisti) {
                // Decodifica l'immagine Base64 in un array di byte
                byte[] imageBytes = Base64.getDecoder().decode(acquisto.getImmagine());
                PDImageXObject productImage = PDImageXObject.createFromByteArray(document, imageBytes, acquisto.getNome());
                contentStream.drawImage(productImage, 100, yPosition - 60, 50, 50);

                contentStream.beginText();
                contentStream.setFont(helvetica, 12);
                contentStream.newLineAtOffset(160, yPosition - 30);  // Allinea il testo del prodotto centrato rispetto all'immagine
                contentStream.showText(acquisto.getNome() + " - Quantità: " + acquisto.getQuantita() + " - Prezzo: €" + acquisto.getPrezzoAq());
                contentStream.endText();
                yPosition -= 70;
            }
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        document.save(byteArrayOutputStream);
        document.close();

        return byteArrayOutputStream.toByteArray();
    }
}
