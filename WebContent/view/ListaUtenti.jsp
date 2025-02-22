<%@ page session="true" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, model.javaBeans.Utente, model.DAO.UtenteDAO"%>

<%
	Utente utente = (Utente) session.getAttribute("utente");
	if (utente == null || utente.getTipo() != 1) {
	response.sendRedirect(request.getContextPath() + "/view/AccessoVietato.jsp");
	return;
	}
    UtenteDAO utenteDAO = new UtenteDAO();
    List<Utente> utenti = utenteDAO.getAllUtenti(); // Ottieni tutti gli utenti
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lista Utenti</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/ListaUtenti.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
    <h1>Lista Utenti</h1>
    <div class="actions">
        <button class="btn back" onclick="window.location.href='<%= request.getContextPath() %>/view/AdminDashboard.jsp'">Torna alla Dashboard</button>
    </div>
    <div class="user-table">
        <table>
            <thead>
                <tr>
                    <th>Icona</th>
                    <th>Username</th>
                    <th>Nome</th>
                    <th>Cognome</th>
                    <th>Email</th>
                </tr>
            </thead>
            <tbody>
                <% for (Utente user : utenti) { %>
                    <tr>
                        <td><i class="fas fa-user"></i></td>
                        <td><%= user.getUsername() %></td>
                        <td><%= user.getNome() %></td>
                        <td><%= user.getCognome() %></td>
                        <td><%= user.getEmail() %></td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    </div>
</body>
</html>
