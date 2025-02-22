<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ page import="model.javaBeans.Utente, model.DAO.UtenteDAO"%>
<%@ page import="model.javaBeans.Prodotto, model.DAO.ProdottoDAO"%>
<%@ page import="java.sql.SQLException" %>

<%
    // Verifica della sessione
    Utente utente = (Utente) session.getAttribute("utente");
    if (utente == null || utente.getTipo() != 1) {
        response.sendRedirect(request.getContextPath() + "/view/AccessoVietato.jsp");
        return;
    }
    
 // Ottieni il numero di utenti dal database
    UtenteDAO utenteDAO = new UtenteDAO();
    int numeroUtenti = 0;
    try {
        numeroUtenti = utenteDAO.getUtenteCount();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    ProdottoDAO prodottoDAO = new ProdottoDAO();
    int numProdotti = 0;
    try {
        numProdotti = prodottoDAO.getProdottiCount();
    } catch (SQLException e) {
        e.printStackTrace();
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
    	<a href="<%= request.getContextPath() %>/view/ListaUtenti.jsp" class="dashboard-item">
            <div class="icon user">
                <i class="fas fa-users"></i>
            </div>
            <div class="info">
                <h3>Utenti</h3>
                <p><%= numeroUtenti %></p>
            </div>
        </a>
        <a href="<%= request.getContextPath() %>/view/ListaProdotti.jsp" class="dashboard-item">
            <div class="icon lista">
                <i class="fas fa-clipboard-list"></i>
            </div>
            <div class="info">
                <h3>Lista Prodotti</h3>
                <p><%= numProdotti %></p>
            </div>
        </a>
        <a href="<%= request.getContextPath() %>/view/AccountUser.jsp" class="dashboard-item">
            <div class="icon user">
                <i class="fa-solid fa-user-pen"></i>
            </div>
            <div class="info">
                <h3>Vedi o modifica account</h3>
            </div>
        </a>
        <a href="<%= request.getContextPath() %>/view/CaricamentoProdotto.jsp" class="dashboard-item">
            <div class="icon prodotto">
                <i class="fa-solid fa-plus"></i>
            </div>
            <div class="info">
                <h3>Aggiungi Prodotto</h3>
            </div>
        </a>
        <a href="<%= request.getContextPath() %>/view/ListaOrdiniAdmin.jsp" class="dashboard-item">
            <div class="icon lista">
            	<i class="fa-solid fa-bag-shopping"></i>
            </div>
            <div class="info">
                <h3>Lista Ordini</h3>
                <p></p>
            </div>
        </a>
         <a href="<%= request.getContextPath() %>/view/ListaProdotti.jsp" class="dashboard-item">
            <div class="icon prodotto">
               <i class="fa-solid fa-pen-to-square"></i>
            </div>
            <div class="info">
                <h3>Modifica o elimina prodotti</h3>
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
