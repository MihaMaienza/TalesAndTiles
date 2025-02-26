<%@ page session="true" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.text.SimpleDateFormat, model.javaBeans.Ordine, model.DAO.OrdineDAO, model.javaBeans.Acquisto, model.DAO.AcquistoDAO" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="model.javaBeans.Utente, model.DAO.UtenteDAO"%>
<%

	Utente utente = (Utente) session.getAttribute("utente");
	if (utente == null || utente.getTipo() != 1) {
    response.sendRedirect(request.getContextPath() + "/view/AccessoVietato.jsp");
    return;
	}
	
    OrdineDAO ordineDAO = new OrdineDAO();
    List<Ordine> tuttiOrdini = ordineDAO.getAllOrders();

    // Ottieni parametri di filtro dalla request
    String startDateStr = request.getParameter("startDate");
    String endDateStr = request.getParameter("endDate");
    String usernameFilter = request.getParameter("username");

    Date startDate = null;
    Date endDate = null;
    List<Ordine> ordiniFiltrati = new ArrayList<>();

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    // Converte le date solo se presenti
    try {
        if (startDateStr != null && !startDateStr.isEmpty()) {
            startDate = sdf.parse(startDateStr);
        }
        if (endDateStr != null && !endDateStr.isEmpty()) {
            endDate = sdf.parse(endDateStr);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    // Applica il filtro agli ordini
    float totaleSpeso = 0;
    if (tuttiOrdini != null) {
        for (Ordine ordine : tuttiOrdini) {
            boolean include = true;

            // Filtra per data
            if (startDate != null && ordine.getDataOrdine().before(startDate)) {
                include = false;
            }
            if (endDate != null && ordine.getDataOrdine().after(endDate)) {
                include = false;
            }

            // Filtra per username
            if (usernameFilter != null && !usernameFilter.isEmpty() &&
                !ordine.getUsername().equalsIgnoreCase(usernameFilter)) {
                include = false;
            }

            if (include) {
                ordiniFiltrati.add(ordine);
                totaleSpeso += ordine.getPrezzoTotale();
            }
        }
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lista Ordini</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
    <div class="container">
        <h1 class="my-4 text-center">Lista Ordini</h1>

        <!-- Form per filtrare gli ordini -->
        <h2 class="mt-4">Filtra Ordini</h2>
        <form method="GET" class="form-inline">
            <label class="mr-2" for="startDate">Data inizio:</label>
            <input type="date" id="startDate" name="startDate" class="form-control mr-2" value="<%= startDateStr != null ? startDateStr : "" %>">

            <label class="mr-2" for="endDate">Data fine:</label>
            <input type="date" id="endDate" name="endDate" class="form-control mr-2" value="<%= endDateStr != null ? endDateStr : "" %>">

            <label class="mr-2" for="username">Username Cliente:</label>
            <input type="text" id="username" name="username" class="form-control mr-2" value="<%= usernameFilter != null ? usernameFilter : "" %>">

            <button type="submit" class="btn btn-primary">Filtra</button>
        </form>

        <div class="order-total text-right">
            <p>Totale Guadagnato: €<%= totaleSpeso %></p>
        </div>

        <table class="table table-bordered table-striped order-table">
            <thead class="thead-dark">
                <tr>
                    <th>ID Ordine</th>
                    <th>Utente</th>
                    <th>Data Ordine</th>
                    <th>Data Consegna</th>
                    <th>Prezzo Totale</th>
                    <th>Visualizza Fattura</th>
                </tr>
            </thead>
            <tbody>
                <% for (Ordine ordine : ordiniFiltrati) { %>
                <tr>
                    <td><%= ordine.getIDOrdine() %></td>
                    <td><%= ordine.getUsername() %></td>
                    <td><%= ordine.getDataOrdine() %></td>
                    <td><%= ordine.getDataConsegna() %></td>
                    <td>€<%= ordine.getPrezzoTotale() %></td>
                    <td>
                        <form action="<%= request.getContextPath() %>/ViewOrderPDFServlet" method="get" target="_blank">
                            <input type="hidden" name="orderId" value="<%= ordine.getIDOrdine() %>">
                            <button type="submit">Visualizza Fattura</button>
                        </form>
                    </td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>
</body>
</html>
