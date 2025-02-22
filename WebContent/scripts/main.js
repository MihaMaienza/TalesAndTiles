/*=============== SHOW MENU ===============*/
const showMenu = (toggleId, navId) => {
    const toggle = document.getElementById(toggleId),
        nav = document.getElementById(navId);

    toggle.addEventListener('click', () => {
        // Add show-menu class to nav menu
        nav.classList.toggle('show-menu');

        // Add show-icon to show and hide the menu icon
        toggle.classList.toggle('show-icon');
    });
};

showMenu('nav-toggle', 'nav-menu');

/*=============== SEARCH PRODUCTS ===============*/
function searchProducts() {
    const searchQuery = document.getElementById('search').value.trim();
    const searchResults = document.getElementById('search-results');

    // Se la query è vuota, nascondi i risultati
    if (searchQuery.length === 0) {
        searchResults.innerHTML = '';
        searchResults.style.display = 'none';
        return;
    }

    // Fetch dei risultati della ricerca
    fetch(contextPath + '/SearchServlet?query=' + encodeURIComponent(searchQuery))
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Errore durante la ricerca');
            }
        })
        .then(prodotti => {
            searchResults.innerHTML = ''; // Svuota i risultati precedenti
            searchResults.style.display = 'block'; // Mostra i risultati

            if (prodotti.length > 0) {
                prodotti.forEach(prodotto => {
                    const resultItem = document.createElement('div');
                    resultItem.classList.add('result-item');
                    resultItem.innerHTML = `
                        <img src="data:image/png;base64,${prodotto.immagine}" alt="${prodotto.nome}">
                        <span>${prodotto.nome} - &euro; ${prodotto.prezzo.toFixed(2)}</span>
                    `;
                    resultItem.addEventListener('click', () => {
                        window.location.href = `${contextPath}/view/DettagliProdotto.jsp?id=${prodotto.id}`;
                    });
                    searchResults.appendChild(resultItem);
                });
            } else {
                searchResults.innerHTML = '<p>Nessun prodotto trovato.</p>';
            }
        })
        .catch(error => {
            console.error('Errore:', error);
            searchResults.innerHTML = '<p>Errore durante la ricerca. Riprova più tardi.</p>';
        });
}


/*=============== SHOW SEARCH POPUP ===============*/
document.getElementById('search-icon').addEventListener('click', () => {
    document.getElementById('search-popup').style.display = 'flex';
    document.getElementById('search').focus(); // Focus on the search input
});

document.getElementById('close-search').addEventListener('click', () => {
    document.getElementById('search-popup').style.display = 'none';
});
