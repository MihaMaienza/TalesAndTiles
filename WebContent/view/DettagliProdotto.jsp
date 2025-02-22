<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ page import="java.util.*, model.javaBeans.Prodotto, model.javaBeans.WishlistItem, model.DAO.*" %>
<%@ page import="model.javaBeans.Utente" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<!--=============== CSS ===============-->
<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/DettagliProdotto.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/PagineRicerca.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/card.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

<title>Dettagli Prodotto</title>
</head>
<body>

<jsp:include page="navbar.jsp" />
<jsp:include page="register.jsp" />
<div class="product-details">
    <%
        int prodottoId = Integer.parseInt(request.getParameter("id"));
        ProdottoDAO prodottoDAO = new ProdottoDAO();
        Map<String, Object> prodottoDettagli = prodottoDAO.getProductDetails(prodottoId);


        boolean isInWishlist = false;
        WishlistItemDAO wishlistItemDAO = new WishlistItemDAO();
        Utente utente = (Utente) session.getAttribute("utente");
        int IDWishlist=-1;
        if (utente != null) {
        	IDWishlist = (Integer)session.getAttribute("IDWishlist");
            WishlistItem wishlistItem = wishlistItemDAO.findWishlistItemByProductId(IDWishlist, prodottoId);
            isInWishlist = (wishlistItem != null);
        }
        
        
        if (prodottoDettagli != null) {
    %>
    <div class="product-image">
        <img src="data:image/png;base64,<%= prodottoDettagli.get("immagine") %>" alt="Immagine Prodotto"/>
    </div>
    <div class="product-info">
        <h1><%= prodottoDettagli.get("nome") %></h1>
        <h2>Prezzo: &euro; <%= prodottoDettagli.get("prezzo") %></h2>
        <h3>Disponibilità: <%= prodottoDettagli.get("disponibilita") %></h3>
        <h3>Condizione: <%= prodottoDettagli.get("condizione") %></h3>
        <h3>Anno di Pubblicazione: <%= prodottoDettagli.get("annoPubblicazione") %></h3>
        <p>Descrizione: <%= prodottoDettagli.get("descrizione") %></p>

        <% if ("gioco".equalsIgnoreCase((String) prodottoDettagli.get("tipo"))) { %>
            <h3>Età: <%= prodottoDettagli.get("eta") %></h3>
            <h3>Casa Produttrice: <%= prodottoDettagli.get("casaProduttrice") %></h3>
			<h3>Genere: <%= prodottoDettagli.get("nomeGenere") %></h3>
        <% } else if ("libro".equalsIgnoreCase((String) prodottoDettagli.get("tipo"))) { %>
            <h3>Scrittore: <%= prodottoDettagli.get("scrittore") %></h3>
            <h3>Editore: <%= prodottoDettagli.get("editore") %></h3>
            <h3>ISBN: <%= prodottoDettagli.get("isbn") %></h3>
			<h3>Genere: <%= prodottoDettagli.get("nomeGenere") %></h3>
        <% } %>
    </div>
    <div class="action-buttons">
        <button class="add-to-cart-btn" data-product-id="<%= prodottoDettagli.get("ID") %>"><i class="ri-shopping-cart-2-fill"></i></button>
        <button class="add-to-wishlist" data-product-id="<%= prodottoDettagli.get("ID") %>"><i class="ri-heart-add-fill"></i></button>
    </div>
    <%
        } else {
            out.println("<p>Prodotto non trovato.</p>");
        }
    %>
</div>

<div class="titolo">
    <h1>Altri prodotti correlati</h1>
</div>
<div class="containerP">
    <% 
    List<Prodotto> correlati = null;
    if ("gioco".equalsIgnoreCase((String) prodottoDettagli.get("tipo"))) {
        int idGenere = prodottoDettagli.get("idGenere") != null ? Integer.parseInt(prodottoDettagli.get("idGenere").toString()) : -1;
        correlati = prodottoDAO.getAllProductsByIdGenereGioco(idGenere);
    } else {
        int idGenere = prodottoDettagli.get("idGenere") != null ? Integer.parseInt(prodottoDettagli.get("idGenere").toString()) : -1;
        correlati = prodottoDAO.getAllProductsByIdGenereLibro(idGenere);
    }

    if (correlati != null) {
        int i = 0;
        for (Prodotto p : correlati) { 
            if (p.getId() == prodottoId) {
                // Salta il prodotto corrente
                continue;
            }
            if (i >= 4) {
                break;
            }
    %>
    <div class="card">
        <div class="imgBx">
            <a href="<%= request.getContextPath() %>/view/DettagliProdotto.jsp?id=<%= p.getId() %>" class="card-link">
                <img src="data:image/png;base64,<%= p.getImmagine() %>" alt="Immagine Prodotto"/>
            </a>
        </div>
        <div class="contentBx">
            <h2><%= p.getNome() %></h2>
            <div class="wish">
                <h3>Add to the wishlist: </h3>
                <button class="add-to-wishlist" data-product-id="<%= p.getId() %>">
           	 		<i class="<%= isInWishlist ? "ri-dislike-fill" : "ri-heart-add-fill" %>"></i>
        		</button>
            </div>
            <div class="price">
                <h2>&euro; <%= p.getPrezzo() %></h2>
            </div>
            <button class="add-to-cart-btn" data-product-id="<%= p.getId() %>">Add to cart</button>
        </div>
    </div>
    <% 
            i++;
        }
    } 
    %>
</div>

<jsp:include page="footer.jsp" />

<!-- Includi il file JavaScript esterno -->
<script>
    const contextPath = '<%= request.getContextPath() %>';
    const isLoggedIn = <%= (utente != null) %>;
</script>
<script src="${pageContext.request.contextPath}/scripts/AddToCart.js"></script>
<script src="${pageContext.request.contextPath}/scripts/AddToWishlist.js"></script>

<!-- Div per mostrare i messaggi flash -->
<div id="flashMessage" class="flash-message"></div>
</body>
</html>
