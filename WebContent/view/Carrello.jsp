<%@ page session="true" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.*, model.javaBeans.Prodotto,model.DAO.ProdottoDAO,model.DAO.CartItemDAO, model.javaBeans.CartItem"%>
<%@ page import="model.DAO.CartNoLogDAO, model.javaBeans.CartNoLog"%>
<%@ page import="model.javaBeans.Utente, model.DAO.UtenteDAO"%>

<%
List<Prodotto> prodotti = new ArrayList<>();
List<Integer> quantitaList = new ArrayList<>();
Utente utente = (Utente) session.getAttribute("utente");
CartItemDAO cartItemDAO = new CartItemDAO();
CartNoLogDAO cartNoLogDAO = new CartNoLogDAO();

boolean isLoggedIn = (utente != null);

if (isLoggedIn) {
    int IDCarrello = (int) session.getAttribute("IDCarrello");
    List<CartItem> cartItems = cartItemDAO.getProductsByCarrelloID(IDCarrello); // Ottieni tutti i prodotti nel carrello
    prodotti = cartItemDAO.getProductsByCartItems(cartItems);
    for (CartItem cartItem : cartItems) {
        quantitaList.add(cartItem.getQuantita());
    }
} else {
    @SuppressWarnings("unchecked")
    List<CartNoLog> cartNoLogItems = (List<CartNoLog>) session.getAttribute("cartNoLog");
    if (cartNoLogItems != null) {
        prodotti = cartNoLogDAO.getProductsByCartNoLog(cartNoLogItems);
        for (CartNoLog cartNoLog : cartNoLogItems) {
            quantitaList.add(cartNoLog.getQuantita());
        }
    }
}
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Il mio carrello</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/Carrello.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <script>
        var contextPath = '<%= request.getContextPath() %>';
        var isLoggedIn = <%= isLoggedIn %>;
    </script>
    <script src="<%= request.getContextPath() %>/scripts/Carrello.js" defer></script>
    
</head>
<body>
    <h1>Il mio carrello</h1>
    <div class="actions">
        <button class="btn back" onclick="window.location.href='<%= request.getContextPath() %>/index.jsp'">Torna alla Dashboard</button>
        <% if (!prodotti.isEmpty()) { %>
            <button class="btn empty-cart" onclick="confirmEmptyCart()">Svuota Carrello</button>
            <span class="total-price">Totale: €<span id="totalPrice">0.00</span></span>
            <button class="btn purchase" onclick="proceedToPurchase()" >Procedi all'acquisto</button>
        <% } %>
    </div>
    <div class="product-table">
        <% if (prodotti.isEmpty()) { %>
            <p>Nessun elemento presente nel tuo carrello.</p>
        <% } else { %>
            <table>
                <thead>
                    <tr>
                        <th>Seleziona</th>
                        <th>Immagine</th>
                        <th>Nome</th>
                        <th>Prezzo</th>
                        <th>Quantita</th>
                        <th>Condizione</th>
                        <th>Disponibilità</th>
                        <th></th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <% for (int i = 0; i < prodotti.size(); i++) { 
                        Prodotto prodotto = prodotti.get(i);
                        int quantita = quantitaList.get(i);
                    %>
                        <tr data-id="<%= prodotto.getId() %>" data-price="<%= prodotto.getPrezzo() %>" data-availability="<%= prodotto.getDisponibilita() %>">
                            <td>
                                <label>
                                    <input type="checkbox" class="product-checkbox" data-id="<%= prodotto.getId() %>" checked>
                                    <span class="checkbox-container"></span>
                                </label>
                            </td>
                            <td>
                                <img src="data:image/png;base64,<%= prodotto.getImmagine() %>" alt="Immagine Prodotto" class="product-image"/>
                            </td>
                            <td><%= prodotto.getNome() %></td>
                            <td>€ <%= prodotto.getPrezzo() %></td>
                            <td>
                                <button class="btn decrement" data-id="<%= prodotto.getId() %>">-</button>
                                <input type="text" class="quantity" data-id="<%= prodotto.getId() %>" value="<%= quantita %>" readonly>
                                <button class="btn increment" data-id="<%= prodotto.getId() %>">+</button>
                            </td>
                            <td><%= prodotto.getCondizione() %></td>
                            <td><%= prodotto.getDisponibilita() %></td>
                            <td>
                                <button class="btn edit" data-id="<%= prodotto.getId() %>">Vai a prodotto</button>
                            </td>    
                            <td>
                                <button class="btn delete" data-id="<%= prodotto.getId() %>">Rimuovi</button>
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        <% } %>
    </div>

    <!-- Modal per visualizzare l'immagine ingrandita -->
    <div id="imageModal" class="modal">
        <span class="close">&times;</span>
        <img class="modal-content" id="modalImage">
    </div>

    <!-- Div per mostrare i messaggi flash -->
    <div id="flashMessage" class="flash-message"></div>
</body>
</html>
