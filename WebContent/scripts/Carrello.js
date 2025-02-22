document.addEventListener('DOMContentLoaded', function () {
	updateTotalPrice();

    // Funzioni per la modale dell'immagine
    function openModal(imageSrc) {
        var modal = document.getElementById("imageModal");
        var modalImg = document.getElementById("modalImage");
        modal.style.display = "block";
        modalImg.src = imageSrc;
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
            fetch(contextPath + '/RemoveFromCartServlet', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ productId: productId, userLoggedIn: isLoggedIn })
            })
            .then(response => response.json())
            .then(data => {
                if (data.status === 'success') {
                    document.querySelector(`tr[data-id="${productId}"]`).remove();
                    updateTotalPrice();
                    if (document.querySelectorAll('tbody tr').length === 0) {
                        const productTable = document.querySelector('.product-table');
                        const message = document.createElement('p');
                        message.textContent = 'Nessun elemento presente nel tuo carrello.';
                        productTable.appendChild(message);
                        document.querySelector('.btn.empty-cart').style.display = 'none';
                        document.querySelector('.btn.purchase').style.display = 'none';
                        document.querySelector('.total-price').style.display = 'none';
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
        if (confirm('Sei sicuro di voler svuotare il carrello?')) {
            emptyCart();
        }
    }

    function emptyCart() {
        fetch(contextPath + '/EmptyCartServlet', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ userLoggedIn: isLoggedIn })
        })
        .then(response => response.json())
        .then(data => {
            if (data.status === 'success') {
                const tableBody = document.querySelector('.product-table tbody');
                while (tableBody.firstChild) {
                    tableBody.removeChild(tableBody.firstChild);
                }
                const productTable = document.querySelector('.product-table');
                const message = document.createElement('p');
                message.textContent = 'Nessun elemento presente nel tuo carrello.';
                productTable.appendChild(message);
                document.querySelector('.btn.empty-cart').style.display = 'none';
                document.querySelector('.btn.purchase').style.display = 'none';
                document.querySelector('.total-price').style.display = 'none';

                showFlashMessage('Carrello svuotato con successo!');
                updateTotalPrice();
            } else {
                showFlashMessage('Errore durante lo svuotamento del carrello.', true);
            }
        })
        .catch(error => {
            console.error('Errore:', error);
            showFlashMessage('Errore durante lo svuotamento del carrello.', true);
        });
    }

    // Funzioni per aggiornare la quantità
    function updateQuantity(productId, increment) {
        const quantityInput = document.querySelector(`.quantity[data-id="${productId}"]`);
        const row = document.querySelector(`tr[data-id="${productId}"]`);
        const availability = parseInt(row.getAttribute('data-availability'));
        let quantity = parseInt(quantityInput.value);
        if (increment) {
            if (quantity < availability) {
                quantity++;
            } else {
                showFlashMessage('Quantità massima disponibile raggiunta.', true);
                return;
            }
        } else {
            if (quantity > 1) {
                quantity--;
            } else {
                removeProduct(productId);
                return;
            }
        }
        quantityInput.value = quantity;

        fetch(contextPath + '/UpdateCartQuantityServlet', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ productId: productId, quantity: quantity, userLoggedIn: isLoggedIn })
        })
        .then(response => response.json())
        .then(data => {
            if (data.status === 'success') {
                showFlashMessage('Quantità aggiornata con successo!');
                updateTotalPrice();
            } else {
                showFlashMessage('Errore durante l\'aggiornamento della quantità.', true);
            }
        })
        .catch(error => {
            console.error('Errore:', error);
            showFlashMessage('Errore durante l\'aggiornamento della quantità.', true);
        });
    }

    // Funzione per aggiornare il totale dei prezzi
    function updateTotalPrice() {
        let totalPrice = 0;
        document.querySelectorAll('.product-checkbox:checked').forEach(checkbox => {
            const row = checkbox.closest('tr');
            const price = parseFloat(row.getAttribute('data-price'));
            const quantity = parseInt(row.querySelector('.quantity').value);
            totalPrice += price * quantity;
        });
        document.getElementById('totalPrice').textContent = totalPrice.toFixed(2);
        const totalPriceElement = document.querySelector('.total-price');
        if (totalPrice === 0) {
            totalPriceElement.style.display = 'none';
        } else {
            totalPriceElement.style.display = 'block';
        }
    }
	
	// Funzione per procedere all'acquisto
	    function proceedToPurchase() {
	        const selectedProducts = document.querySelectorAll('.product-checkbox:checked');
	        if (selectedProducts.length === 0) {
	            showFlashMessage('Seleziona almeno un prodotto per procedere all\'acquisto.', true);
	            return;
	        }
	        const productIds = Array.from(selectedProducts).map(checkbox => checkbox.getAttribute('data-id'));
	        fetch(contextPath + '/ProceedToPurchaseServlet', {
	            method: 'POST',
	            headers: {
	                'Content-Type': 'application/json'
	            },
	            body: JSON.stringify({ productIds: productIds, userLoggedIn: isLoggedIn })
	        })
	        .then(response => response.json())
	        .then(data => {
	            if (data.status === 'success') {
	                window.location.href = contextPath + '/view/resocontoOrdine.jsp?orderId=' + data.orderId;
	            } else {
	                showFlashMessage('Errore durante la procedura di acquisto.', true);
	            }
	        })
	        .catch(error => {
	            console.error('Errore:', error);
	            showFlashMessage('Errore durante la procedura di acquisto.', true);
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

    document.querySelectorAll('.btn.increment').forEach(btn => {
        btn.addEventListener('click', function () {
            updateQuantity(this.getAttribute('data-id'), true);
        });
    });

    document.querySelectorAll('.btn.decrement').forEach(btn => {
        btn.addEventListener('click', function () {
            updateQuantity(this.getAttribute('data-id'), false);
        });
    });

    document.querySelectorAll('.product-checkbox').forEach(checkbox => {
        checkbox.addEventListener('change', updateTotalPrice);
    });

    if (document.querySelector('.btn.empty-cart')) {
        document.querySelector('.btn.empty-cart').addEventListener('click', confirmEmptyCart);
    }

    if (document.querySelector('.btn.purchase')) {
        document.querySelector('.btn.purchase').addEventListener('click', proceedToPurchase);
    }

    document.querySelector('.close').addEventListener('click', closeModal);
    window.onclick = function(event) {
        var modal = document.getElementById("imageModal");
        if (event.target == modal) {
            closeModal();
        }
    }

    document.querySelectorAll('.product-checkbox').forEach(checkbox => {
        checkbox.checked = true;
    });

    updateTotalPrice();
});
