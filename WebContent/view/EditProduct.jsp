<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true"%>
<%@ page import="java.util.Map, java.util.List, model.javaBeans.Utente, model.javaBeans.GenereLibro, model.javaBeans.GenereGioco, model.DAO.GenereLibroDAO, model.DAO.GenereGiocoDAO"%>
<%@ page import="model.DAO.ProdottoDAO" %>
<%
    // Verifica della sessione
    Utente utente = (Utente) session.getAttribute("utente");
    if (utente == null || utente.getTipo() != 1) {
        response.sendRedirect(request.getContextPath() + "view/AccessoVietato.jsp");
        return;
    }

    // Ottieni i dettagli del prodotto e le liste dei generi
    int productId = Integer.parseInt(request.getParameter("id"));
    ProdottoDAO prodottoDAO = new ProdottoDAO();
    Map<String, Object> productDetails = prodottoDAO.getProductDetails(productId);
    List<GenereLibro> genereLibri = GenereLibroDAO.getGenereLibro();
    List<GenereGioco> genereGiochi = GenereGiocoDAO.getGenereGioco();
    String tipo = (String) productDetails.get("tipo");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Modifica Prodotto</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/styles/CaricamentoProdotto.css">
    <script>
        function toggleFields(tipo) {
            const gameFields = document.getElementById('game-fields');
            const bookFields = document.getElementById('book-fields');

            if (tipo === 'gioco') {
                gameFields.style.display = 'block';
                bookFields.style.display = 'none';
            } else if (tipo === 'libro') {
                gameFields.style.display = 'none';
                bookFields.style.display = 'block';
            }
        }

        // Mostra i campi corretti al caricamento della pagina
        window.onload = function () {
            const tipo = "<%=tipo%>";
            toggleFields(tipo);
        };
    </script>
</head>
<body>
    <form action="<%=request.getContextPath()%>/UpdateProductServlet" method="post" enctype="multipart/form-data">
        <input type="hidden" name="id" value="<%=productId%>">

        <div class="product-upload">
            <!-- Gestione dell'immagine -->
            <div class="product-image-upload" id="drop-area">
                <input type="file" id="file-upload" name="file-upload" accept="image/*">
                <label for="file-upload">Sostituisci immagine (opzionale), se non inserito niente, verrà mantenuta la foto precedente</label>
                <button type="button" id="removeImageBtn" onclick="removeImage()" style="display: none;">Remove Image</button>
            </div>

            <!-- Dettagli del prodotto -->
            <div class="product-info-upload">
                <h3>Modifica Prodotto</h3>
                <div class="form-group">
                    <label for="nome">Nome:</label>
                    <input type="text" id="nome" name="nome" value="<%=productDetails.get("nome")%>" required>
                </div>
                <div class="form-group">
                    <label for="prezzo">Prezzo:</label>
                    <input type="number" id="prezzo" name="prezzo" step="0.01" value="<%=productDetails.get("prezzo")%>" required>
                </div>
                <div class="form-group">
                    <label for="IVA">IVA:</label>
                    <input type="number" id="IVA" name="IVA" value="<%=productDetails.get("IVA")%>" required>
                </div>
                <div class="form-group">
                    <label for="disponibilita">Disponibilità:</label>
                    <input type="number" id="disponibilita" name="disponibilita" value="<%=productDetails.get("disponibilita")%>" required>
                </div>
                <div class="form-group">
                    <label for="descrizione">Descrizione:</label>
                    <textarea id="descrizione" name="descrizione" required><%=productDetails.get("descrizione")%></textarea>
                </div>
                <div class="form-group">
                    <label for="condizione">Condizione:</label>
                    <select id="condizione" name="condizione" required>
                        <option value="nuovo" <%= "nuovo".equals(productDetails.get("condizione")) ? "selected" : "" %>>Nuovo</option>
                        <option value="usato" <%= "usato".equals(productDetails.get("condizione")) ? "selected" : "" %>>Usato</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="annoPubblicazione">Anno Pubblicazione:</label>
                    <input type="number" id="annoPubblicazione" name="annoPubblicazione" value="<%=productDetails.get("annoPubblicazione")%>" required>
                </div>
                <div class="form-group">
                    <label for="tipo">Tipo:</label>
                    <input type="text" id="tipo" name="tipo" value="<%=tipo%>" readonly>
                </div>

                <!-- Campi specifici per giochi -->
                <% if ("gioco".equalsIgnoreCase(tipo)) { %>
                <div id="game-fields">
                    <div class="form-group">
                        <label for="fasciaEta">Fascia Età:</label>
                        <input type="text" id="fasciaEta" name="fasciaEta" value="<%=productDetails.get("eta")%>">
                    </div>
                    <div class="form-group">
                        <label for="casaProduttrice">Casa Produttrice:</label>
                        <input type="text" id="casaProduttrice" name="casaProduttrice" value="<%=productDetails.get("casaProduttrice")%>">
                    </div>
                    <div class="form-group">
                        <label for="idGenereGioco">Genere:</label>
                        <select id="idGenereGioco" name="idGenereGioco">
                            <% for (GenereGioco genere : genereGiochi) { %>
                                <option value="<%=genere.getId()%>" <%=genere.getId() == (int) productDetails.get("idGenere") ? "selected" : "" %>>
                                    <%=genere.getNome()%>
                                </option>
                            <% } %>
                        </select>
                    </div>
                </div>
                <% } else if ("libro".equalsIgnoreCase(tipo)) { %>
                <!-- Campi specifici per libri -->
                <div id="book-fields">
                    <div class="form-group">
                        <label for="scrittore">Scrittore:</label>
                        <input type="text" id="scrittore" name="scrittore" value="<%=productDetails.get("scrittore")%>">
                    </div>
                    <div class="form-group">
                        <label for="editore">Editore:</label>
                        <input type="text" id="editore" name="editore" value="<%=productDetails.get("editore")%>">
                    </div>
                    <div class="form-group">
                        <label for="isbn">ISBN:</label>
                        <input type="text" id="isbn" name="isbn" value="<%=productDetails.get("isbn")%>">
                    </div>
                    <div class="form-group">
                        <label for="idGenereLibro">Genere:</label>
                        <select id="idGenereLibro" name="idGenereLibro">
                            <% for (GenereLibro genere : genereLibri) { %>
                                <option value="<%=genere.getId()%>" <%=genere.getId() == (int) productDetails.get("idGenere") ? "selected" : "" %>>
                                    <%=genere.getNome()%>
                                </option>
                            <% } %>
                        </select>
                    </div>
                </div>
                <% } %>
                <button type="submit">Salva Modifiche</button>
                <button type="reset">Reset</button>
            </div>
        </div>
    </form>
</body>
    <script>
        // Drag and drop functionality
        var dropArea = document.getElementById('drop-area');
        var fileInput = document.getElementById('file-upload');
        var fileLabel = dropArea.querySelector('label');
        var removeImageButton = document.getElementById('removeImageBtn');

        dropArea.addEventListener('dragover', function(e) {
            e.preventDefault();
            dropArea.classList.add('dragging');
        });

        dropArea.addEventListener('dragleave', function() {
            dropArea.classList.remove('dragging');
        });

        dropArea.addEventListener('drop', function(e) {
            e.preventDefault();
            dropArea.classList.remove('dragging');
            var files = e.dataTransfer.files;
            if (files.length > 0 && files[0].type.startsWith('image/')) {
                fileInput.files = files;
                fileLabel.textContent = files[0].name;
                removeImageButton.style.display = 'block';
            } else {
                alert('Please upload an image file.');
            }
        });

        fileInput.addEventListener('change', function() {
            var file = fileInput.files[0];
            if (file && file.type.startsWith('image/')) {
                fileLabel.textContent = file.name;
                removeImageButton.style.display = 'block';
            } else {
                alert('Please upload an image file.');
                fileInput.value = ''; // Clear the input
                fileLabel.textContent = 'Drag & Drop to upload image';
                removeImageButton.style.display = 'none';
            }
        });

        function removeImage() {
            fileInput.value = '';
            fileLabel.textContent = 'Drag & Drop to upload image';
            removeImageButton.style.display = 'none';
        }
        
		function toggleFields() {
			const tipo = document.getElementById('tipo').value;
			const gameFields = document.getElementById('game-fields');
			const bookFields = document.getElementById('book-fields');

			if (tipo === 'gioco') {
				gameFields.style.display = 'block';
				bookFields.style.display = 'none';
			} else if (tipo === 'libro') {
				gameFields.style.display = 'none';
				bookFields.style.display = 'block';
			} else {
				gameFields.style.display = 'none';
				bookFields.style.display = 'none';
			}
		}

	</script>
</html>
