<%@ page session="true" %>
<%@ page import="model.javaBeans.Utente"%>
<%
    Utente utente = (Utente) session.getAttribute("utente");
    if (utente == null) {
        response.sendRedirect(request.getContextPath() + "/view/AccessoVietato.jsp");
        return;
    }
    String numCarta = (utente != null && utente.getNumCarta() != null) ? utente.getNumCarta() : "";
    String dashboardUrl = (utente != null && utente.getTipo() == 1) ? request.getContextPath() + "/view/AdminDashboard.jsp" : request.getContextPath() + "/view/UserDashboard.jsp";
%>
<!DOCTYPE html>
<html>
<head>
    <title>Account Utente</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/AccountUser.css">
    <script>
        // Funzione per censurare il numero della carta
        function censuraNumeroCarta(numCarta) {
            if (numCarta == null || numCarta.length < 4) {
                return "informazione non disponibile";
            } else {
                return "**** **** **** " + numCarta.substring(numCarta.length - 4);
            }
        }

        // Funzione per aggiornare il numero della carta nella pagina
        function aggiornaNumeroCarta() {
            var numCarta = "<%= numCarta %>";
            document.getElementById("numeroCarta").innerText = censuraNumeroCarta(numCarta);
        }
    </script>
</head>
<body onload="aggiornaNumeroCarta()">
<div class="account-container">
    <h1>Benvenuto: <%= utente != null && utente.getNome() != null ? utente.getNome() : "informazione non disponibile" %> <%= utente != null && utente.getCognome() != null ? utente.getCognome() : "informazione non disponibile" %>!</h1>
    <div class="account-info">
        <p>Email: <%= utente != null && utente.getEmail() != null ? utente.getEmail() : "informazione non disponibile" %></p>
        <p>Username: <%= utente != null ? utente.getUsername() : "informazione non disponibile" %></p>
        <p>Nome: <%= utente != null && utente.getNome() != null ? utente.getNome() : "informazione non disponibile" %></p>
        <p>Cognome: <%= utente != null && utente.getCognome() != null ? utente.getCognome() : "informazione non disponibile" %></p>
        <p>Data di Nascita: <%= utente != null && utente.getDataNascita() != null ? utente.getDataNascita() : "informazione non disponibile" %></p>
        <p>Nome Carta: <%= utente != null && utente.getNomeCarta() != null ? utente.getNomeCarta() : "informazione non disponibile" %></p>
        <p>Cognome Carta: <%= utente != null && utente.getCognomeCarta() != null ? utente.getCognomeCarta() : "informazione non disponibile" %></p>
        <p>Numero Carta: <span id="numeroCarta">informazione non disponibile</span></p>
        <p>CAP: <%= utente != null && utente.getCap() != null ? utente.getCap() : "informazione non disponibile" %></p>
        <p>Via: <%= utente != null && utente.getVia() != null ? utente.getVia() : "informazione non disponibile" %></p>
        <p>Città: <%= utente != null && utente.getCitta() != null ? utente.getCitta() : "informazione non disponibile" %></p>
    </div>
    <a href="<%= request.getContextPath() %>/view/UpdateUser.jsp">Aggiorna i tuoi dati</a>
    <button onclick="window.location.href='<%= dashboardUrl %>'">Torna alla Dashboard</button>
</div>
<script src="<%= request.getContextPath() %>/scripts/modal.js"></script>
</body>
</html>
