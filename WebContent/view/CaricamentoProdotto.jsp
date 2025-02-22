<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page session="true"%>
<%@ page import="model.javaBeans.Utente, java.util.List, model.javaBeans.GenereLibro, model.javaBeans.GenereGioco, model.DAO.GenereLibroDAO, model.DAO.GenereGiocoDAO"%>

<%
// Verifica della sessione
Utente utente = (Utente) session.getAttribute("utente");
if (utente == null) {
	response.sendRedirect(request.getContextPath() + "/view/AccessoVietato.jsp");
	return;
}
if (utente.getTipo() == 0) {
	response.sendRedirect(request.getContextPath() + "/view/AccessoVietato.jsp");
}

// Ottieni la lista di generi esistenti
List<GenereLibro> genereLibri = GenereLibroDAO.getGenereLibro();
List<GenereGioco> genereGiochi = GenereGiocoDAO.getGenereGioco();
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Product Upload</title>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/styles/CaricamentoProdotto.css">
</head>
<body>
	<form action="<%=request.getContextPath()%>/UploadProductServlet"
		enctype="multipart/form-data" method="post">
		<div class="product-upload">
			<div class="product-image-upload" id="drop-area">
				<input type="file" id="file-upload" name="file-upload"
					accept="image/*" required> <label for="file-upload">Drag
					& Drop to upload image</label>
				<button type="button" id="removeImageBtn" onclick="removeImage()"
					style="display: none;">Remove Image</button>
			</div>
			<div class="product-info-upload">
				<h3>Product Upload</h3>
				<hr>
				<%
				String message = (String) request.getAttribute("message");
				if (message != null && !message.isEmpty()) {
				%>
				<div class="message">
					<p><%=message%></p>
				</div>
				<%
				}
				%>

				<div class="form-group">
					<label for="nome">Nome:</label> <input type="text" id="nome"
						name="nome" required>
				</div>
				<div class="form-group">
					<label for="prezzo">Prezzo:</label> <input type="number"
						step="0.01" id="prezzo" name="prezzo" required>
				</div>
				<div class="form-group">
					<label for="IVA">IVA:</label> <input type="number" id="IVA"
						name="IVA" required>
				</div>
				<div class="form-group">
                <label for="disponibilita">Disponibilità:</label>
                <input type="number" id="disponibilita" name="disponibilita" required>
            </div>
            <div class="form-group">
                <label for="descrizione">Descrizione:</label>
                <input type="text" id="descrizione" name="descrizione" required>
            </div>
            <div class="form-group">
                <label for="condizione">Condizione:</label>
                <select id="condizione" name="condizione" required>
                    <option value="" disabled selected>Scegli un'opzione</option>
                    <option value="nuovo">Nuovo</option>
                    <option value="usato">Usato</option>
                </select>
            </div>
            <div class="form-group">
                <label for="annoPubblicazione">Anno Pubblicazione:</label>
                <input type="number" id="annoPubblicazione" name="annoPubblicazione" required>
            </div>
				<div class="form-group">
					<label for="tipo">Tipo:</label> <select id="tipo" name="tipo"
						required onchange="toggleFields()">
						<option value="" disabled selected>Scegli un'opzione</option>
						<option value="gioco">Gioco</option>
						<option value="libro">Libro</option>
					</select>
				</div>

<!-- Campi per i giochi -->
<div id="game-fields" style="display: none;">
    <div class="form-group">
        <label for="fasciaEta">Fascia Età:</label>
        <input type="text" id="fasciaEta" name="fasciaEta">
    </div>
    <div class="form-group">
        <label for="casaProduttrice">Casa Produttrice:</label>
        <input type="text" id="casaProduttrice" name="casaProduttrice">
    </div>
    <div class="form-group">
        <label for="idGenereGioco">Genere:</label>
        <select id="idGenereGioco" name="idGenereGioco">
            <option value="" disabled selected>Scegli un'opzione</option>
            <% for (GenereGioco genere : genereGiochi) { %>
                <option value="<%=genere.getId()%>"><%=genere.getNome()%></option>
            <% } %>
        </select>
    </div>
</div>

<!-- Campi per i libri -->
<div id="book-fields" style="display: none;">
    <div class="form-group">
        <label for="editore">Editore:</label>
        <input type="text" id="editore" name="editore">
    </div>
    <div class="form-group">
        <label for="scrittore">Scrittore:</label>
        <input type="text" id="scrittore" name="scrittore">
    </div>
    <div class="form-group">
        <label for="isbn">ISBN:</label>
        <input type="text" id="isbn" name="isbn">
    </div>
    <div class="form-group">
        <label for="idGenereLibro">Genere:</label>
        <select id="idGenereLibro" name="idGenereLibro">
            <option value="" disabled selected>Scegli un'opzione</option>
            <% for (GenereLibro genere : genereLibri) { %>
                <option value="<%=genere.getId()%>"><%=genere.getNome()%></option>
            <% } %>
        </select>
    </div>
</div>


						<button type="submit">Upload</button>
						<button type="reset">Reset</button>
					</div>
				</div>
	</form>
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
</body>
</html>
