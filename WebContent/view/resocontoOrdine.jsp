<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, model.javaBeans.Ordine, model.DAO.OrdineDAO, model.javaBeans.Acquisto, model.DAO.AcquistoDAO, model.javaBeans.Utente " %>
<%@ page import="com.google.gson.Gson" %>

<%
    int orderId = Integer.parseInt(request.getParameter("orderId"));
    OrdineDAO ordineDAO = new OrdineDAO();
    AcquistoDAO acquistoDAO = new AcquistoDAO();
    Ordine ordine = ordineDAO.getOrderById(orderId);
    List<Acquisto> acquisti = acquistoDAO.getAcquistiByOrderId(orderId);
    Utente utente = (Utente) session.getAttribute("utente");

    // Ottieni gli ID dei prodotti
    List<Integer> productIds = new ArrayList<>();
    for (Acquisto acquisto : acquisti) {
        productIds.add(acquisto.getIDProdotto());
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Resoconto Ordine</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/resocontoOrdine.css">
    <script>
        var contextPath = '<%= request.getContextPath() %>';
        var orderId = <%= orderId %>;
        var productIds = <%= new Gson().toJson(productIds) %>;
    </script>
    <script src="<%= request.getContextPath() %>/scripts/resocontoOrdine.js" defer></script>
    <meta name="viewport" content="width=device-width, initial-scale=1" charset="UTF-8">
</head>
<body>
    <div class="container">
        <h1>Resoconto dell'Ordine</h1>

        <div class="order-summary">
            <div class="shipping-address">
                <h2>In consegna a</h2>
                <p id="address-display"><%= (utente.getNome() != null && utente.getCognome() != null && utente.getVia() != null && utente.getCap() != null && utente.getCitta() != null) ? utente.getNome() + " " + utente.getCognome() + ", " + utente.getVia() + ", " + utente.getCap() + ", " + utente.getCitta() : "Nessun indirizzo salvato" %></p>
                <button onclick="editAddress()">Modifica</button>
                <div id="address-form" style="display: none;">
                    <input type="text" id="nome" placeholder="Nome" value="<%= utente.getNome() != null ? utente.getNome() : "" %>">
                    <input type="text" id="cognome" placeholder="Cognome" value="<%= utente.getCognome() != null ? utente.getCognome() : "" %>">
                    <input type="text" id="via" placeholder="Via" value="<%= utente.getVia() != null ? utente.getVia() : "" %>">
                    <input type="text" id="cap" placeholder="CAP" value="<%= utente.getCap() != null ? utente.getCap() : "" %>">
                    <input type="text" id="citta" placeholder="Città" value="<%= utente.getCitta() != null ? utente.getCitta() : "" %>">
                    <button onclick="saveAddress()">Salva</button>
                </div>
            </div>

            <div class="payment-method">
                <h2>Pagamento con</h2>
                <p id="card-display"><%= (utente.getNomeCarta() != null && utente.getCognomeCarta() != null && utente.getNumCarta() != null) ? "Mastercard " + utente.getNumCarta().substring(utente.getNumCarta().length() - 4) : "Nessuna carta salvata" %></p>
                <button onclick="editCard()">Modifica</button>
                <div id="card-form" style="display: none;">
                    <input type="text" id="nome-carta" placeholder="Nome sulla Carta" value="<%= utente.getNomeCarta() != null ? utente.getNomeCarta() : "" %>">
                    <input type="text" id="cognome-carta" placeholder="Cognome sulla Carta" value="<%= utente.getCognomeCarta() != null ? utente.getCognomeCarta() : "" %>">
                    <input type="text" id="numero-carta" placeholder="Numero Carta" value="<%= utente.getNumCarta() != null ? utente.getNumCarta() : "" %>">
                    <input type="text" id="cvv" placeholder="CVV" value="<%= utente.getCVV() != null ? utente.getCVV() : "" %>">
                    <input type="text" id="data-scadenza" placeholder="Data Scadenza (MM/AA)" value="<%= utente.getDataScadenza() != null ? new java.text.SimpleDateFormat("MM/yy").format(utente.getDataScadenza()) : "" %>">
                    <button onclick="saveCard()">Salva</button>
                </div>
            </div>

            <div class="delivery-date">
                <h2>In arrivo il <%= ordine.getDataConsegna() %></h2>
            </div>

            <div class="products">
                <h2>Prodotti Acquistati</h2>
                <% for (Acquisto acquisto : acquisti) { %>
                    <div class="product">
                        <div class="product-image">
                            <img src="data:image/png;base64,<%= acquisto.getImmagine() %>" alt="Immagine Prodotto" />
                        </div>
                        <div class="product-details">
                            <p><strong>Nome:</strong> <%= acquisto.getNome() %></p>
                            <p><strong>Quantità:</strong> <%= acquisto.getQuantita() %></p>
                            <p><strong>Prezzo:</strong> €<%= acquisto.getPrezzoAq() %></p>
                            <p><strong>IVA:</strong> €<%= acquisto.getDiCuiIva() %></p>
                        </div>
                    </div>
                <% } %>
            </div>

            <div class="order-total">
                <h2>Totale ordine: €<%= ordine.getPrezzoTotale() %></h2>
            </div>

            <div class="buttons">
                <button class="finalize" onclick="finalizePurchase()">Finalizza Pagamento</button>
                <button class="cancel" onclick="cancelOrder()">Annulla Ordine</button>
            </div>
        </div>
    </div>

    <div id="flashMessage" class="flash-message"></div>
</body>
</html>
