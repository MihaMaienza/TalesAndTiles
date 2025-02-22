<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, model.javaBeans.Ordine, model.DAO.OrdineDAO, model.javaBeans.Utente" %>
<%@ page import="com.google.gson.Gson" %>

<%
    Utente utente = (Utente) session.getAttribute("utente");
    if (utente == null) {
    	response.sendRedirect(request.getContextPath() + "/view/AccessoVietato.jsp");
        return;
    }

    String username = utente.getUsername();
    OrdineDAO ordineDAO = new OrdineDAO();
    List<Ordine> ordini = ordineDAO.getOrdersByUsername(username);
    float totaleSpeso = 0;
    for (Ordine ordine : ordini) {
        totaleSpeso += ordine.getPrezzoTotale();
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lista Ordini</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        .order-table th, .order-table td {
            text-align: center;
        }
        .order-table img {
            max-width: 50px;
        }
        .order-total {
            font-weight: bold;
        }
        .sortable {
            cursor: pointer;
        }
        .sort-indicator {
            margin-left: 5px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1 class="my-4 text-center">Lista Ordini</h1>

        <table class="table table-bordered table-striped order-table">
            <thead class="thead-dark">
                <tr>
                    <th>ID Ordine</th>
                    <th class="sortable" onclick="sortTable('data-ordine')">Data Ordine <span class="sort-indicator" id="data-ordine-indicator">▼</span></th>
                    <th>Data Consegna</th>
                    <th class="sortable" onclick="sortTable('prezzo-totale')">Prezzo Totale <span class="sort-indicator" id="prezzo-totale-indicator">▼</span></th>
                    <th>Visualizza Fattura</th>
                </tr>
            </thead>
            <tbody id="order-table-body">
                <% for (Ordine ordine : ordini) { %>
                    <tr class="order-row">
                        <td><%= ordine.getIDOrdine() %></td>
                        <td class="data-ordine" data-order="<%= ordine.getDataOrdine().getTime() %>"><%= ordine.getDataOrdine() %></td>
                        <td><%= ordine.getDataConsegna() %></td>
                        <td class="prezzo-totale" data-order="<%= ordine.getPrezzoTotale() %>">€<%= ordine.getPrezzoTotale() %></td>
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

        <div class="order-total text-right">
            <p>Totale Speso: €<%= totaleSpeso %></p>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script>
        var currentSortColumn = 'data-ordine';
        var currentSortOrder = 'asc';

        function sortTable(columnClass) {
            var table = document.getElementById('order-table-body');
            var rows = Array.from(table.getElementsByClassName('order-row'));
            var sortOrder = (currentSortColumn === columnClass && currentSortOrder === 'asc') ? 'desc' : 'asc';
            currentSortColumn = columnClass;
            currentSortOrder = sortOrder;

            rows.sort(function(a, b) {
                var aValue = a.getElementsByClassName(columnClass)[0].getAttribute('data-order');
                var bValue = b.getElementsByClassName(columnClass)[0].getAttribute('data-order');

                if (columnClass === 'prezzo-totale') {
                    aValue = parseFloat(aValue);
                    bValue = parseFloat(bValue);
                }

                if (sortOrder === 'asc') {
                    return aValue - bValue;
                } else {
                    return bValue - aValue;
                }
            });

            // Rimuovi tutte le righe esistenti e aggiungi le righe ordinate
            while (table.firstChild) {
                table.removeChild(table.firstChild);
            }
            rows.forEach(function(row) {
                table.appendChild(row);
            });

            // Aggiorna l'indicatore di ordinamento
            updateSortIndicators();
        }

        function updateSortIndicators() {
            var indicators = document.querySelectorAll('.sort-indicator');
            indicators.forEach(function(indicator) {
                indicator.textContent = '';
            });
            var currentIndicator = document.getElementById(currentSortColumn + '-indicator');
            currentIndicator.textContent = currentSortOrder === 'asc' ? '▲' : '▼';
        }
    </script>
</body>
</html>
