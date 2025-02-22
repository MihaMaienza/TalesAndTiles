document.addEventListener('DOMContentLoaded', function() {
    const wishlistButtons = document.querySelectorAll('.add-to-wishlist');

    wishlistButtons.forEach(button => {
        button.addEventListener('click', function() {
            if (!isLoggedIn) {
                showLoginModal();
                return;
            }

            const productId = this.getAttribute('data-product-id');
            const icon = this.querySelector('i');
            const isRemoving = icon.classList.contains('ri-dislike-fill');
            const url = isRemoving ? `${contextPath}/RemoveFromWishlistServlet` : `${contextPath}/AddToWishlistServlet`;
            const body = isRemoving ? JSON.stringify({ productId: productId }) : `IDProdotto=${productId}`;

            fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': isRemoving ? 'application/json' : 'application/x-www-form-urlencoded'
                },
                body: body
            })
            .then(response => response.json())
            .then(data => {
                if (data.status === 'success') {
                    updateWishlistIcon(this, !isRemoving);
                    updateWishlistCount();
                    showFlashMessage(isRemoving ? 'Prodotto rimosso dalla wishlist' : 'Prodotto aggiunto alla wishlist');

                    // Reload the page if an item is removed from the wishlist
                    if (isRemoving) {
                        setTimeout(() => {
                            location.reload();
                        }, 500);
                    }
                } else {
                    showFlashMessage('Errore durante l\'operazione.', true);
                }
            })
            .catch(error => {
                console.error('Errore:', error);
                showFlashMessage('Errore durante l\'operazione.', true);
            });
        });
    });

    function updateWishlistIcon(button, isInWishlist) {
        const icon = button.querySelector('i');
        if (isInWishlist) {
            icon.classList.remove('ri-heart-add-fill');
            icon.classList.add('ri-dislike-fill');
        } else {
            icon.classList.remove('ri-dislike-fill');
            icon.classList.add('ri-heart-add-fill');
        }
    }

    function updateWishlistCount() {
        fetch(`${contextPath}/GetWishlistCountServlet`, {
            method: 'GET'
        })
        .then(response => response.json())
        .then(data => {
            const wishlistCountElement = document.querySelector('.wishlist-count');
            wishlistCountElement.textContent = data.count;
        })
        .catch(error => {
            console.error('Errore:', error);
        });
    }

    // Aggiorna il conteggio degli articoli nella wishlist al caricamento della pagina
    updateWishlistCount();

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

    function showLoginModal() {
        // Mostra la modale di login
        document.getElementById("myWrapper").classList.add('active-popup');
    }
});
