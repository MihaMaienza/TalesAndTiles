<%@ page session="true" %>
<%@ page import="java.util.*, model.javaBeans.Prodotto, model.DAO.ProdottoDAO" %>
<%@ page import="java.util.*, model.DAO.CartItemDAO, model.javaBeans.Utente, java.sql.SQLException, model.javaBeans.CartNoLog" %>
<%@ page import="model.javaBeans.GenereGioco, model.DAO.GenereGiocoDAO " %>
<%@ page import="model.javaBeans.GenereLibro, model.DAO.GenereLibroDAO " %>
<%@ page import="model.javaBeans.WishlistItem, model.DAO.WishlistItemDAO" %>
<%
    Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
    if (isLoggedIn == null) {
        isLoggedIn = false;
    }
    int cartItemCount = 0;
    int wishlistItemCount = 0;
    Utente utente = (Utente) session.getAttribute("utente");
    int utenteTipo = (utente != null) ? utente.getTipo() : 0;

    if (isLoggedIn) {
        CartItemDAO cartItemDAO = new CartItemDAO();
        WishlistItemDAO wishlistItemDAO = new WishlistItemDAO();
        try {
        	wishlistItemCount = wishlistItemDAO.countItemsInWishlistByUsername(utente.getUsername());
        	cartItemCount = cartItemDAO.countItemsInCartByUsername(utente.getUsername());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } else {
        @SuppressWarnings("unchecked")
        List<CartNoLog> cartNoLog = (List<CartNoLog>) session.getAttribute("cartNoLog");
        if (cartNoLog != null) {
            for (CartNoLog item : cartNoLog) {
                cartItemCount += item.getQuantita();
            }
        }
    }
%>
<%
    // Ottieni la lista dei generi gioco dal database usando il DAO
    List<GenereGioco> generiGioco = new ArrayList<>();
    try {
        generiGioco = GenereGiocoDAO.getGenereGioco(); // Chiamata alla tua funzione esistente
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    // Ottieni la lista dei generi gioco dal database usando il DAO
    List<GenereLibro> generiLibro = new ArrayList<>();
    try {
        generiLibro = GenereLibroDAO.getGenereLibro(); // Chiamata alla tua funzione esistente
    } catch (SQLException e) {
        e.printStackTrace();
    }
%>

<!--=============== REMIXICONS ===============-->
<link href="https://cdn.jsdelivr.net/npm/remixicon@3.2.0/fonts/remixicon.css" rel="stylesheet">

<!--=============== CSS ===============-->
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/navbar.css">

<!--=============== HEADER ===============-->
<header class="header">
    <nav class="nav container">
        <div class="nav__data">
            <a href="${pageContext.request.contextPath}/index.jsp" class="nav__logo">
                <img src="${pageContext.request.contextPath}/images/Tales&Tiles_LOGO.png">TalesAndTiles
            </a>

            <div class="nav__toggle" id="nav-toggle">
                <i class="ri-menu-line nav__burger"></i>
                <i class="ri-close-line nav__close"></i>
            </div>
        </div>

        <!--=============== NAV MENU ===============-->
        <div class="nav__menu" id="nav-menu">
            <ul class="nav__list">
     
                <!--<li class="search-wrapper">
                    <span class="icon"><ion-icon name="search-outline"></ion-icon></span>
                    <input type="search" id="search" placeholder="Search" onkeyup="searchProducts()">
                </li>  -->
                 <!--=============== DROPDOWN 1 ===============-->
                <li class="dropdown__item">
                    <div class="nav__link">
                        <a href="${pageContext.request.contextPath}/view/PaginaGiochi.jsp">Giochi</a> <i class="ri-arrow-down-s-line dropdown__arrow"></i>
                    </div>

                    <ul class="dropdown__menu">
                        <% for (GenereGioco genere : generiGioco) { %>
                            <li>
                                <a href="<%= request.getContextPath() %>/view/PaginaGenereGioco.jsp?idGenere=<%= genere.getId() %>&tipo=gioco" class="dropdown__link">
                                    <i class="ri-gamepad-line"></i> <%= genere.getNome() %>
                                </a>
                            </li>
                        <% } %>
                    </ul>
                </li>

                <!--=============== DROPDOWN 2 ===============-->
                <li class="dropdown__item">
                    <div class="nav__link">
                        <a href="${pageContext.request.contextPath}/view/PaginaLibri.jsp">Libri</a> <i class="ri-arrow-down-s-line dropdown__arrow"></i>
                    </div>

                    <ul class="dropdown__menu">
                        <% for (GenereLibro genereLibro : generiLibro) { %>
                            <li>
                                <a href="<%= request.getContextPath() %>/view/PaginaGenereGioco.jsp?idGenere=<%= genereLibro.getId() %>&tipo=libro" class="dropdown__link">
                                    <i class="ri-gamepad-line"></i> <%= genereLibro.getNome() %>
                                </a>
                            </li>
                        <% } %>
                    </ul>
                </li>


                <li><a href="${pageContext.request.contextPath}/view/PaginaUsatoNuovo.jsp?condizione=usato" class="nav__link">Usato</a></li>

                <li><a href="${pageContext.request.contextPath}/view/Contact.jsp" class="nav__link">Contatti</a></li>

                <!--=============== DROPDOWN 2 ===============-->
                
                <li class="dropdown__item">
                    <div class="nav__link">
                        Utente <i class="ri-arrow-down-s-line dropdown__arrow"></i>
                    </div>
	
                    <ul class="dropdown__menu">
                        <li>
                            <% if(!isLoggedIn) { %>
                            <a href="#" class="dropdown__link btnLogin-popup" id="accountLink">
                                <i class="ri-user-line"></i> Account
                            </a>
                            <%} if(isLoggedIn) {
                            		if(utenteTipo == 1){%>
	                            <a href="${pageContext.request.contextPath}/view/AdminDashboard.jsp" class="dropdown__link" id="accountLink">
	                                <i class="ri-user-line"></i> Account
	                            </a>
	                            <%}if(utenteTipo == 0){%>
	                            <a href="${pageContext.request.contextPath}/view/UserDashboard.jsp" class="dropdown__link" id="accountLink">
	                                <i class="ri-user-line"></i> Account
	                            </a>
	                            <% }} %>
                        </li>
<% if(isLoggedIn) { %>
                        <li>
                            <a href="${pageContext.request.contextPath}/view/ListaOrdini.jsp" class="dropdown__link">
                                <i class="ri-truck-line"></i> I miei ordini
                            </a>
                        </li>
                        <% } %>
                    </ul>
                </li>
                
                
                <li class="nav__item nav__search-icon-container">
                    <i class="ri-search-line nav__search-icon" id="search-icon"></i>
                </li>

                <li>
                    <li><a href="${pageContext.request.contextPath}/view/Wishlist.jsp" class="nav__link wishlist-link">
                        <i class="ri-heart-line"></i>
                        <span class="wishlist-count"><%= wishlistItemCount %></span></span>
                    </a>
                </li>

                <li><a href="${pageContext.request.contextPath}/view/Carrello.jsp" class="nav__link">
                    <i class="ri-shopping-cart-line"></i>
                    <span class="cart-count"><%= cartItemCount %></span> <!-- Aggiungi questa riga per mostrare il numero di articoli nel carrello -->
                </a></li>

            </ul>
        </div>
    </nav>
</header>
<!-- Div per mostrare i messaggi flash -->
<div id="flashMessage" class="flash-message"></div>


<!-- Div per la barra di ricerca popup -->
<div class="search-popup" id="search-popup">
    <input type="search" id="search" placeholder="Cerca prodotti..." oninput="searchProducts()" autofocus>
    <i class="ri-close-line" id="close-search"></i>
    <div id="search-results" class="search-results"></div> <!-- Aggiungi un contenitore per i risultati -->
</div>


<script type="module" src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js"></script>
<script nomodule src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.js"></script>
<!--=============== MAIN JS ===============-->
<script src="${pageContext.request.contextPath}/scripts/main.js"></script>

<script>
document.addEventListener("DOMContentLoaded", function() {
    var isLoggedIn = <%= isLoggedIn %>;
    var utenteTipo = <%= utenteTipo %>;
    var accountLink = document.getElementById("accountLink");

    if (!isLoggedIn) {
        accountLink.addEventListener('click', function(event) {
            event.preventDefault();
            document.getElementById("myWrapper").classList.add('active-popup');
        });

    } else {
        accountLink.href = (utenteTipo == 1) 
            ? "${pageContext.request.contextPath}/view/AdminDashboard.jsp" 
            : "${pageContext.request.contextPath}/view/UserDashboard.jsp"; // Imposta il link alla pagina dell'account utente
    }

    const iconClose = document.querySelector('.icon-close');

    iconClose.addEventListener('click', () => {
        document.getElementById("myWrapper").classList.remove('active-popup');
    });

    // Logica per chiudere la modale cliccando fuori
    window.onclick = function(event) {
        if (event.target == document.getElementById("myWrapper")) {
            document.getElementById("myWrapper").classList.remove('active-popup');
        }
    }
});
</script>