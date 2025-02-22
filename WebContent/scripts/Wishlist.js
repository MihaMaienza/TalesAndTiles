document.addEventListener('DOMContentLoaded', function () {
    // Funzioni per la modale dell'immagine
    function openModal(imageSrc) {
        var modal = document.getElementById("imageModal");
        var modalImg = document.getElementById("modalImage");
        modal.style.display = "block";
        modalImg.src = imageSrc; // Correzione per impostare correttamente la sorgente dell'immagine
    }

    function closeModal() {
        var modal = document.getElementById("imageModal");
        modal.style.display = "none";
    }

    // Funzioni per mostrare i messaggi flash
    function showFlashMessage(message, isError = false) {
        const flashMessage = document.getElementById('flashMessage');
        flashMessage.textContent = message;
        flashMessage.className = 'flash-message';
        if (isError) {
            flashMessage.classList.add('error');
        }
        flashMessage.style.display = 'block';
        setTimeout(() => {
            flashMessage.style.display = 'none';
        }, 3000);
    }

    // Funzioni per i prodotti
    function editProduct(productId) {
        window.location.href = contextPath + '/view/DettagliProdotto.jsp?id=' + productId;
    }

    function removeProduct(productId) {
        if (confirm('Sei sicuro di voler rimuovere questo prodotto?')) {
            // Fai una richiesta per rimuovere il prodotto
            fetch(contextPath + '/RemoveFromWishlistServlet', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ productId: productId })
            })
            .then(response => response.json())
            .then(data => {
                if (data.status === 'success') {
                    // Rimuovi l'elemento dalla DOM
                    document.querySelector(`tr[data-id="${productId}"]`).remove();
                    if (document.querySelectorAll('tbody tr').length === 0) {
                        const productTable = document.querySelector('.product-table');
                        const message = document.createElement('p');
                        message.textContent = 'Nessun elemento presente nella tua wishlist.';
                        productTable.appendChild(message);
                        document.querySelector('.btn.empty-cart').style.display = 'none';
                    }
                    showFlashMessage('Prodotto rimosso con successo!');
                } else {
                    showFlashMessage('Errore durante la rimozione del prodotto.', true);
                }
            })
            .catch(error => {
                console.error('Errore:', error);
                showFlashMessage('Errore durante la rimozione del prodotto.', true);
            });
        }
    }

    function confirmEmptyCart() {
        if (confirm('Sei sicuro di voler svuotare la wishlist?')) {
            emptyCart();
        }
    }

    function emptyCart() {
        fetch(contextPath + '/SvuotaWishlistServlet', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ userLoggedIn: isLoggedIn })
        })
        .then(response => response.json())
        .then(data => {
            if (data.status === 'success') {
                // Rimuovi tutti gli elementi dalla tabella
                const tableBody = document.querySelector('.product-table tbody');
                while (tableBody.firstChild) {
                    tableBody.removeChild(tableBody.firstChild);
                }
                // Aggiungi il messaggio di nessun elemento presente
                const productTable = document.querySelector('.product-table');
                const message = document.createElement('p');
                message.textContent = 'Nessun elemento presente nella tua wishlist.';
                productTable.appendChild(message);

                // Nascondi il pulsante "Svuota Wishlist"
                document.querySelector('.btn.empty-cart').style.display = 'none';

                showFlashMessage('Wishlist svuotata con successo!');
            } else {
                showFlashMessage('Errore durante lo svuotamento della wishlist.', true);
            }
        })
        .catch(error => {
            console.error('Errore:', error);
            showFlashMessage('Errore durante lo svuotamento della wishlist.', true);
        });
    }

    // Associa le funzioni agli eventi
    document.querySelectorAll('.product-image').forEach(img => {
        img.addEventListener('click', function () {
            openModal(this.src);
        });
    });

    document.querySelectorAll('.btn.edit').forEach(btn => {
        btn.addEventListener('click', function () {
            editProduct(this.getAttribute('data-id'));
        });
    });

    document.querySelectorAll('.btn.delete').forEach(btn => {
        btn.addEventListener('click', function () {
            removeProduct(this.getAttribute('data-id'));
        });
    });

    if (document.querySelector('.btn.empty-cart')) {
        document.querySelector('.btn.empty-cart').addEventListener('click', confirmEmptyCart);
    }

    // Funzione per chiudere la modale
    document.querySelector('.close').addEventListener('click', closeModal);

    // Funzione per chiudere la modale cliccando fuori
    window.onclick = function(event) {
        var modal = document.getElementById("imageModal");
        if (event.target == modal) {
            closeModal();
        }
    }
});
