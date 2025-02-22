<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page import="org.apache.catalina.filters.ExpiresFilter.XServletOutputStream" %>
<%@ page import="java.util.*, model.javaBeans.Prodotto, model.DAO.ProdottoDAO" %>
<%@ page import="model.javaBeans.Utente" %>
<%@ page import="java.util.Base64" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<!--=============== CSS ===============-->
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/PagineRicerca.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/card.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
<style>
/* Dropdown per l'ordinamento */
.sort-dropdown {
    display: inline-block;
    position: relative;
    max-width: 200px;
    margin: 10px;
}

.sort-dropdown select {
    appearance: none;
    -webkit-appearance: none;
    -moz-appearance: none;
    width: 100%;
    height: 40px;
    padding: 0 15px;
    background: linear-gradient(to right, #6a11cb, #2575fc);
    border: none;
    border-radius: 20px;
    font-size: 16px;
    color: black;
    cursor: pointer;
    outline: none;
    transition: background 0.3s ease, box-shadow 0.3s ease;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.sort-dropdown select:hover {
    background: linear-gradient(to right, #5a0db2, #1a64d6);
}

.sort-dropdown select:focus {
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}

.sort-dropdown::after {
    content: '\25BC'; /* Freccia verso il basso */
    position: absolute;
    top: 50%;
    right: 15px;
    transform: translateY(-50%);
    pointer-events: none;
    color: #fff;
    font-size: 12px;
}

.sort-dropdown select::-ms-expand {
    display: none;
}
</style>
<title>Catalogo TalesAndTiles</title>
</head>
<body>

<jsp:include page="navbar.jsp" />
<jsp:include page="register.jsp" />

<%
    String condizione = request.getParameter("condizione");
    ProdottoDAO prodottoDAO = new ProdottoDAO();
    Utente utente = (Utente) session.getAttribute("utente");
%>

<div class="titolo">
    <h1>Catalogo Prodotti Usati</h1>
    <!-- Dropdown per l'ordinamento -->
    <div class="sort-dropdown">
        <select id="sortOrder" onchange="sortProducts()">
            <option value="asc">Prezzo: Crescente</option>
            <option value="desc">Prezzo: Decrescente</option>
        </select>
    </div>
</div>
<div class="containerP" id="product-container">
    <%
        List<Prodotto> prodotti = prodottoDAO.getAllProductByCondizione(condizione);

        for (Prodotto p : prodotti) {

    %>
    <div class="card" data-price="<%= p.getPrezzo() %>">
        <div class="imgBx">
            <a href="<%= request.getContextPath() %>/view/DettagliProdotto.jsp?id=<%= p.getId() %>" class="card-link">
            <img src="data:image/png;base64,<%= p.getImmagine() %>" alt="Immagine Prodotto"/></a>
        </div>
        <div class="contentBx">
            <h2><%= p.getNome() %></h2>
            <div class="wish">
                <h3>Add to the wishlist: </h3>
                <span class="add-to-wishlist card-wishlist-btn" data-product-id="<%= p.getId() %>">
                    <i class="ri-heart-add-fill"></i>
                </span>
            </div>
            <div class="price">
                <h2> &euro; <%= p.getPrezzo() %></h2>
            </div>
            <button class="add-to-cart-btn" data-product-id="<%= p.getId() %>">Add to cart</button>
        </div>
    </div>
    <%
        }
    %>
</div>

<jsp:include page="footer.jsp" />

<!-- Includi il file JavaScript esterno -->
<script>
    const contextPath = '<%= request.getContextPath() %>';
    const isLoggedIn = <%= (utente != null) %>;

    function sortProducts() {
        var container = document.getElementById('product-container');
        var cards = Array.from(container.getElementsByClassName('card'));
        var sortOrder = document.getElementById('sortOrder').value;

        cards.sort(function(a, b) {
            var priceA = parseFloat(a.getAttribute('data-price'));
            var priceB = parseFloat(b.getAttribute('data-price'));
            if (sortOrder === 'asc') {
                return priceA - priceB;
            } else {
                return priceB - priceA;
            }
        });

        // Rimuovi tutte le carte dal container
        while (container.firstChild) {
            container.removeChild(container.firstChild);
        }

        // Aggiungi le carte ordinate al container
        cards.forEach(function(card) {
            container.appendChild(card);
        });
    }
</script>
<script src="${pageContext.request.contextPath}/scripts/main.js"></script>
<script src="${pageContext.request.contextPath}/scripts/AddToCart.js"></script>

<!-- Div per mostrare i messaggi flash -->
<div id="flashMessage" class="flash-message"></div>
</body>
</html>
