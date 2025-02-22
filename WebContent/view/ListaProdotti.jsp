<%@ page session="true" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.*, model.javaBeans.Prodotto,model.DAO.ProdottoDAO"%>
<%@ page import="java.util.*,model.javaBeans.Utente, model.DAO.UtenteDAO"%>

<%
    Utente utente = (Utente) session.getAttribute("utente");
    if (utente == null || utente.getTipo() != 1) {
        response.sendRedirect(request.getContextPath() + "/view/AccessoVietato.jsp");
        return;
    }
    ProdottoDAO prodottoDAO = new ProdottoDAO();
    List<Prodotto> prodotti = prodottoDAO.getAllProdotti(); // Ottieni tutti i prodotti

    String message = request.getParameter("message");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lista Prodotti</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/styles/ListaProdotti.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <style>
        .flash-message {
            position: fixed;
            top: 20px;
            right: 20px;
            padding: 10px 20px;
            background-color: #4CAF50; /* Verde per il successo */
            color: white;
            border-radius: 5px;
            display: none;
            z-index: 1000;
        }
        .flash-message.error {
            background-color: #f44336; /* Rosso per l'errore */
        }
    </style>
</head>
<body>
    <h1>Lista Prodotti</h1>

    <!-- Messaggio flash -->
    <div id="flashMessage" class="flash-message <%= "success".equals(message) ? "" : "error" %>" 
         style="<%= message != null ? "display: block;" : "" %>">
        <% if ("success".equals(message)) { %>
            Prodotto eliminato con successo!
        <% } else if ("error".equals(message)) { %>
            Impossibile eliminare il prodotto. Si è verificato un errore.
        <% } %>
    </div>

    <div class="actions">
        <button class="btn back" onclick="window.location.href='<%= request.getContextPath() %>/view/AdminDashboard.jsp'">Torna alla Dashboard</button>
    </div>
    <div class="product-table">
        <table>
            <thead>
                <tr>
                    <th>Immagine</th>
                    <th>Nome</th>
                    <th>Tipo</th>
                    <th>Prezzo</th>
                    <th>Disponibilità</th>
                    <th>Condizione</th>
                    <th>Azioni</th>
                </tr>
            </thead>
            <tbody>
                <% for (Prodotto prodotto : prodotti) { %>
                    <tr>
                        <td>
                            <img src="data:image/png;base64,<%= prodotto.getImmagine() %>" alt="Immagine Prodotto" class="product-image" onclick="openModal('<%= prodotto.getImmagine() %>')"/>
                        </td>
                        <td><%= prodotto.getNome() %></td>
                        <td><%= prodotto.getTipo() %></td>
                        <td>€ <%= prodotto.getPrezzo() %></td>
                        <td><%= prodotto.getDisponibilita() %></td>
                        <td><%= prodotto.getCondizione() %></td>
                        <td>
                            <button class="btn edit" onclick="editProduct('<%= prodotto.getId() %>')">Modifica</button>
                            <button class="btn delete" onclick="removeProduct('<%= prodotto.getId() %>')">Rimuovi</button>
                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    </div>

    <!-- Form nascosto per rimuovere il prodotto -->
    <form id="removeProductForm" method="POST" action="<%= request.getContextPath() %>/RemoveProductServlet">
        <input type="hidden" name="id" id="productIdInput">
    </form>

    <!-- Modal per visualizzare l'immagine ingrandita -->
    <div id="imageModal" class="modal">
        <span class="close" onclick="closeModal()">&times;</span>
        <img class="modal-content" id="modalImage">
    </div>

    <script>
        // Funzione per mostrare il messaggio flash e farlo sparire dopo 2 secondi
        document.addEventListener("DOMContentLoaded", function () {
            var flashMessage = document.getElementById("flashMessage");
            if (flashMessage.style.display === "block") {
                setTimeout(function () {
                    flashMessage.style.display = "none";
                }, 2000);
            }
        });

        function openModal(imageSrc) {
            var modal = document.getElementById("imageModal");
            var modalImg = document.getElementById("modalImage");
            modal.style.display = "block";
            modalImg.src = 'data:image/png;base64,' + imageSrc;
        }

        function closeModal() {
            var modal = document.getElementById("imageModal");
            modal.style.display = "none";
        }

        function editProduct(productId) {
            window.location.href = '<%= request.getContextPath() %>/view/EditProduct.jsp?id=' + productId;
        }

        function removeProduct(productId) {
            if (confirm('Sei sicuro di voler rimuovere questo prodotto?')) {
                document.getElementById('productIdInput').value = productId;
                document.getElementById('removeProductForm').submit();
            }
        }
    </script>
</body>
</html>
