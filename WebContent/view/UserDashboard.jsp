<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ page import="model.javaBeans.Utente, model.DAO.UtenteDAO"%>
<%@ page import="model.javaBeans.CartItem, model.DAO.CartItemDAO" %>
<%@ page import="model.javaBeans.WishlistItem, model.DAO.WishlistItemDAO" %>

<%@ page import="model.DAO.OrdineDAO" %>
<%@ page import="java.sql.SQLException" %>

<%
    // Verifica della sessione
    Utente utente = (Utente) session.getAttribute("utente");
    if (utente == null || utente.getTipo() != 0) {
        response.sendRedirect(request.getContextPath() + "/view/AccessoVietato.jsp");
        return;
    }

    // Ottieni il numero di elementi nella wishlist
    int wishlistCount = 0;
    int cartItemCount = 0;
    int orderItemCount = 0;
    
    if (utente != null) {
    	WishlistItemDAO wishlistItemDAO = new WishlistItemDAO();
        CartItemDAO cartItemDAO = new CartItemDAO();
        OrdineDAO ordineDAO = new OrdineDAO();
        
        
        try {
        	int IDWishlist = (Integer) session.getAttribute("IDWishlist");
            wishlistCount = wishlistItemDAO.countItemsInWishlistByUsername(utente.getUsername());
            cartItemCount = cartItemDAO.countItemsInCartByUsername(utente.getUsername());
            orderItemCount = ordineDAO.countOrdersByUsername(utente.getUsername());
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/AdminDashboard.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>

    <div class="dashboard">
        <a href="<%= request.getContextPath() %>/view/AccountUser.jsp" class="dashboard-item">
            <div class="icon user">
                <i class="fa-solid fa-user-pen"></i>
            </div>
            <div class="info">
                <h3>Vedi o modifica account</h3>
            </div>
        </a>
         <a href="<%= request.getContextPath() %>/view/Wishlist.jsp" class="dashboard-item">
            <div class="icon prodotto">
            	<i class="fa-solid fa-heart"></i>
            </div>
            <div class="info">
                <h3>Wishlist</h3>
                 <p><%= wishlistCount %></p>
                <p></p>
            </div>
        </a>
         <a href="<%= request.getContextPath() %>/view/Carrello.jsp" class="dashboard-item">
            <div class="icon lista">
            	<i class="fa-solid fa-cart-shopping"></i>
            </div>
            <div class="info">
                <h3>Carrello</h3>
                <p><%= cartItemCount %></p>
            </div>
        </a>
        <a href="<%= request.getContextPath() %>/index.jsp" class="dashboard-item">
            <div class="icon ritorno">
               <i class="fa-solid fa-house"></i>
            </div>
            <div class="info">
                <h3>Torna alla home</h3>
            </div>
        </a>
      <a href="<%= request.getContextPath() %>/view/ListaOrdini.jsp" class="dashboard-item">
            <div class="icon lista">
            	<i class="fa-solid fa-bag-shopping"></i>
            </div>
            <div class="info">
                <h3>Lista Ordini</h3>
                <p><%= orderItemCount %></p>
            </div>
        </a>
        <form action="<%= request.getContextPath() %>/LogoutServlet" method="post" class="dashboard-item logout-form">
            <div class="icon ritorno" onclick="document.querySelector('.logout-form').submit();">
               <i class="fa-solid fa-right-from-bracket"></i>
            </div>
            <div class="info">
                <h3>Logout</h3>
            </div>
        </form>
    </div>
</body>
</html>
