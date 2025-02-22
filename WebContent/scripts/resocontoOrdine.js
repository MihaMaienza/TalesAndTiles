document.addEventListener('DOMContentLoaded', function() {
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

    window.finalizePurchase = function() {
        // Verifica che i campi di indirizzo e carta non siano vuoti
        const addressDisplay = document.getElementById('address-display').textContent;
        const cardDisplay = document.getElementById('card-display').textContent;

        if (addressDisplay.includes('Nessun indirizzo salvato') || cardDisplay.includes('Nessuna carta salvata')) {
            showFlashMessage('Per favore, compila i campi di indirizzo e carta per procedere.', true);
            return;
        }

        const nome = document.getElementById('nome').value;
        const cognome = document.getElementById('cognome').value;
        const via = document.getElementById('via').value;
        const cap = document.getElementById('cap').value;
        const citta = document.getElementById('citta').value;
        const numCarta = document.getElementById('numero-carta').value;

        // Rimuovi l'evento beforeunload prima di finalizzare l'acquisto
        window.removeEventListener('beforeunload', beforeUnloadListener);

        fetch(contextPath + '/FinalizePurchaseServlet', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                orderId: orderId,
                nome: nome,
                cognome: cognome,
                via: via,
                cap: cap,
                citta: citta,
                numCarta: numCarta,
                productIds: productIds
            })
        })
        .then(response => response.json())
        .then(data => {
            if (data.status === 'success') {
                showFlashMessage('Pagamento finalizzato con successo!');
                setTimeout(() => {
                    window.open(data.pdfUrl, '_blank');
                    window.location.href = contextPath + '/index.jsp';
                }, 1500);
            } else {
                showFlashMessage('Errore durante la finalizzazione del pagamento.', true);
            }
        })
        .catch(error => {
            console.error('Errore:', error);
            showFlashMessage('Errore durante la finalizzazione del pagamento.', true);
        });
    };

    window.cancelOrder = function() {
        fetch(contextPath + '/CancelOrderServlet', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ orderId: orderId })
        })
        .then(response => response.json())
        .then(data => {
            if (data.status === 'success') {
                showFlashMessage('Ordine annullato con successo!');
                setTimeout(() => {
                    window.location.href = contextPath + '/index.jsp';
                }, 1500);
            } else {
                showFlashMessage('Errore durante l\'annullamento dell\'ordine.', true);
            }
        })
        .catch(error => {
            console.error('Errore:', error);
            showFlashMessage('Errore durante l\'annullamento dell\'ordine.', true);
        });
    };

    // Aggiungi un listener per l'evento beforeunload
    //permette di eliminare l'ordine se si esce dalla pagina
    function beforeUnloadListener(event) {
        fetch(contextPath + '/cancelOrder', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ orderId: orderId })
        });

        // Aggiungi un messaggio per confermare l'uscita
        event.preventDefault();
        event.returnValue = '';
    }
    window.addEventListener('beforeunload', beforeUnloadListener);

    // Gestisci il pulsante "indietro" del browser
    window.addEventListener('popstate', function(event) {
        window.location.href = contextPath + '/index.jsp';
    });

    // Spingi un nuovo stato nella cronologia
    history.pushState(null, document.title, location.href);

    // Funzione per modificare l'indirizzo
    window.editAddress = function() {
        document.getElementById('address-form').style.display = 'block';
    };

    window.saveAddress = function() {
        const nome = document.getElementById('nome').value;
        const cognome = document.getElementById('cognome').value;
        const via = document.getElementById('via').value;
        const cap = document.getElementById('cap').value;
        const citta = document.getElementById('citta').value;

        // Validazioni
        if (!nome || /\d/.test(nome)) {
            showFlashMessage('Nome non valido', true);
            return;
        }
        if (!cognome || /\d/.test(cognome)) {
            showFlashMessage('Cognome non valido', true);
            return;
        }
        if (!cap || !/^\d{5}$/.test(cap)) {
            showFlashMessage('CAP non valido', true);
            return;
        }
        if (!via) {
            showFlashMessage('Via non valida', true);
            return;
        }
        if (!citta || /\d/.test(citta)) {
            showFlashMessage('Città non valida', true);
            return;
        }

        const addressDisplay = `${nome} ${cognome}, ${via}, ${cap}, ${citta}`;
        document.getElementById('address-display').textContent = addressDisplay;
        document.getElementById('address-form').style.display = 'none';
    };

    // Funzione per modificare la carta
    window.editCard = function() {
        document.getElementById('card-form').style.display = 'block';
    };

    window.saveCard = function() {
        const nomeCarta = document.getElementById('nome-carta').value;
        const cognomeCarta = document.getElementById('cognome-carta').value;
        const numCarta = document.getElementById('numero-carta').value;
        const cvv = document.getElementById('cvv').value;
        const dataScadenza = document.getElementById('data-scadenza').value;

        // Validazioni
        if (!nomeCarta || /\d/.test(nomeCarta)) {
            showFlashMessage('Nome sulla carta non valido', true);
            return;
        }
        if (!cognomeCarta || /\d/.test(cognomeCarta)) {
            showFlashMessage('Cognome sulla carta non valido', true);
            return;
        }
        if (!numCarta || !/^\d{16}$/.test(numCarta)) {
            showFlashMessage('Numero della carta non valido', true);
            return;
        }
        if (!cvv || !/^\d{3}$/.test(cvv)) {
            showFlashMessage('CVV non valido', true);
            return;
        }
        if (!dataScadenza || !/^(0[1-9]|1[0-2])\/?([0-9]{2})$/.test(dataScadenza)) {
            showFlashMessage('Data di scadenza non valida', true);
            return;
        }

        // Verifica che la carta non sia scaduta
        const currentDate = new Date();
        const [month, year] = dataScadenza.split('/');
        const expiryDate = new Date(`20${year}`, month - 1); // Anno in formato 20XX

        if (expiryDate < currentDate) {
            showFlashMessage('La carta è scaduta', true);
            return;
        }

        const cardDisplay = `Mastercard ${numCarta.slice(-4)}`;
        document.getElementById('card-display').textContent = cardDisplay;
        document.getElementById('card-form').style.display = 'none';
    };
});
