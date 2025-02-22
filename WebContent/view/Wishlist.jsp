<%@ page session="true" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.*, model.javaBeans.Prodotto, model.DAO.WishlistItemDAO, model.javaBeans.WishlistItem"%>
<%@ page import="model.javaBeans.Utente"%>

<%
List<Prodotto> prodotti = new ArrayList<>();
Utente utente = (Utente) session.getAttribute("utente");

if (utente == null) {
response.sendRedirect(request.getContextPath() + "/view/AccessoVietato.jsp");
return;
}

if (utente != null) {
    int IDWishlist = (int) session.getAttribute("IDWishlist");
    WishlistItemDAO wishlistItemDAO = new WishlistItemDAO();
    List<WishlistItem> wishlistItems = wishlistItemDAO.getProductsByIDWishlist(IDWishlist); // Ottieni tutti gli elementi della wishlist
    prodotti = wishlistItemDAO.getProductsByWishlistItems(wishlistItems); // Usa la funzione per ottenere i prodotti
}
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>La mia wishlist</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/Wishlist.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <script>
        var contextPath = '<%= request.getContextPath() %>';
        var isLoggedIn = <%= utente != null %>;
    </script>
    <script src="<%= request.getContextPath() %>/scripts/Wishlist.js" defer></script>
    <style>
        .flash-message {
            position: fixed;
            top: 20px;
            right: 20px;
            padding: 10px 20px;
            background-color: #4CAF50; /* Verde per il successo */
            color: white;
            border-radius: 5px;
            display: none;
            z-index: 1000;
        }
        .flash-message.error {
            background-color: #f44336; /* Rosso per l'errore */
        }
    </style>
</head>
<body>
    <h1>La mia wishlist</h1>
    <div class="actions">
        <button class="btn back" onclick="window.location.href='<%= request.getContextPath() %>/index.jsp'">Torna alla Dashboard</button>
        <% if (!prodotti.isEmpty()) { %>
            <button class="btn empty-cart" onclick="confirmEmptyCart()">Svuota Wishlist</button>
        <% } %>
    </div>
    <div class="product-table">
        <% if (prodotti.isEmpty()) { %>
            <p>Nessun elemento presente nella tua wishlist.</p>
        <% } else { %>
            <table>
                <thead>
                    <tr>
                        <th>Immagine</th>
                        <th>Nome</th>
                        <th>Prezzo</th>
                        <th>Condizione</th>
                        <th>Azioni</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Prodotto prodotto : prodotti) { %>
                        <tr data-id="<%= prodotto.getId() %>">
                            <td>
                                <img src="data:image/png;base64,<%= prodotto.getImmagine() %>" alt="Immagine Prodotto" class="product-image"/>
                            </td>
                            <td><%= prodotto.getNome() %></td>
                            <td>€ <%= prodotto.getPrezzo() %></td>
                            <td><%= prodotto.getCondizione() %></td>
                            <td>
                                <button class="btn edit" data-id="<%= prodotto.getId() %>">Vai a prodotto</button>
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
