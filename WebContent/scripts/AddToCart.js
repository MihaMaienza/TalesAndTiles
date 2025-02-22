document.addEventListener('DOMContentLoaded', function() {
    const buttons = document.querySelectorAll('.add-to-cart-btn');

    buttons.forEach(button => {
        button.addEventListener('click', function() {
            const productId = this.getAttribute('data-product-id');

            fetch(`${contextPath}/AddToCartServlet`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: `IDProdotto=${productId}`
            })
            .then(response => response.text())
            .then(data => {
                if (data === 'success') {
                    // Aggiorna il conteggio degli elementi nel carrello
                    updateCartCount();
                    showFlashMessage('Prodotto aggiunto al carrello!');
                } else if (data === 'not_available') {
                    showFlashMessage('Quantità non disponibile.', true);
                } else {
                    showFlashMessage('Errore durante l\'aggiunta del prodotto al carrello.', true);
                }
            })
            .catch(error => {
                console.error('Errore:', error);
                showFlashMessage('Errore durante l\'aggiunta del prodotto al carrello.', true);
            });
        });
    });

    function updateCartCount() {
        fetch(`${contextPath}/GetCartCountServlet`, {
            method: 'GET'
        })
        .then(response => response.json())
        .then(data => {
            const cartCountElement = document.querySelector('.cart-count');
            cartCountElement.textContent = data.count;
        })
        .catch(error => {
            console.error('Errore:', error);
        });
    }

    // Aggiorna il conteggio degli articoli nel carrello al caricamento della pagina
    updateCartCount();

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
});
