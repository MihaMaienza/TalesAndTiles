<%@ page session="true" %>
<%@ page import="model.javaBeans.Utente"%>
<%
	Utente utente = (Utente) session.getAttribute("utente");
	if (utente == null) {
	response.sendRedirect(request.getContextPath() + "/view/AccessoVietato.jsp");
	return;
	}
%>
<!DOCTYPE html>
<html>
<head>
    <title>Aggiorna Dati Utente</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/UpdateUser.css">
    <script src="<%= request.getContextPath() %>/scripts/scriptUpdateUser.js"></script>
    <meta name="viewport" content="width=device-width, initial-scale=1" charset="UTF-8">    
</head>
<body>
<div class="account-container">
    <h1>Aggiorna i tuoi dati</h1>
    <form action="<%= request.getContextPath() %>/UpdateUserServlet" method="post" onsubmit="return validateForm()">
    <div class="account-info">
        <label>Username: <input type="text" name="username" value="<%= utente.getUsername() %>" readonly></label>
        <label>Nome: <input type="text" name="nome" value="<%= utente.getNome() != null ? utente.getNome() : "" %>"></label>
        <label>Cognome: <input type="text" name="cognome" value="<%= utente.getCognome() != null ? utente.getCognome() : "" %>"></label>
        <label>Email: <input type="email" name="email" value="<%= utente.getEmail() != null ? utente.getEmail() : "" %>"></label>
        <small id="emailError" class="full-width" style="color: red; display: <%= request.getAttribute("emailError") != null ? "block" : "none" %>;">
            <%= request.getAttribute("emailError") != null ? request.getAttribute("emailError") : "" %>
        </small>
        <label>Data di Nascita: <input type="date" name="dataNascita" value="<%= utente.getDataNascita() != null ? utente.getDataNascita().toString() : "" %>"></label>
        <label>Nome Carta: <input type="text" name="nomeCarta" value="<%= utente.getNomeCarta() != null ? utente.getNomeCarta() : "" %>"></label>
        <label>Cognome Carta: <input type="text" name="cognomeCarta" value="<%= utente.getCognomeCarta() != null ? utente.getCognomeCarta() : "" %>"></label>
        <label>Numero Carta: <input type="text" name="numCarta" value="<%= utente.getNumCarta() != null ? utente.getNumCarta() : "" %>"></label>
        <label>Data Scadenza Carta: <input type="date" name="dataScadenza" value="<%= utente.getDataScadenza() != null ? utente.getDataScadenza().toString() : "" %>"></label>
        <label>CVV: <input type="text" name="CVV" value="<%= utente.getCVV() != null ? utente.getCVV() : "" %>"></label>
        <label>CAP: <input type="text" name="cap" value="<%= utente.getCap() != null ? utente.getCap() : "" %>"></label>
        <label>Via: <input type="text" name="via" value="<%= utente.getVia() != null ? utente.getVia() : "" %>"></label>
        <label>Città: <input type="text" name="citta" value="<%= utente.getCitta() != null ? utente.getCitta() : "" %>"></label>
        
        <h3 class="full-width">Cambia Password</h3>
        <label>Vecchia Password: <input type="password" name="oldPassword"></label>
        <label>Nuova Password: <input type="password" name="newPassword"></label>
        <small id="oldPasswordError" class="full-width" style="color: red; display: <%= request.getAttribute("oldPasswordError") != null ? "block" : "none" %>;">
            <%= request.getAttribute("oldPasswordError") != null ? request.getAttribute("oldPasswordError") : "" %>
        </small>
        <small id="passwordFormatError" class="full-width" style="color: red; display: <%= request.getAttribute("passwordFormatError") != null ? "block" : "none" %>;">
            <%= request.getAttribute("passwordFormatError") != null ? request.getAttribute("passwordFormatError") : "" %>
        </small>
    </div>
    <input type="submit" value="Aggiorna">
</form>
</div>
</body>
</html>
