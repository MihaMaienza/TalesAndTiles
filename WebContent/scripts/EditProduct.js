function validateProductForm() {
    var nome = document.querySelector('input[name="nome"]').value;
    var prezzo = document.querySelector('input[name="prezzo"]').value;
    var iva = document.querySelector('input[name="IVA"]').value;
    var tipo = document.querySelector('select[name="tipo"]').value;
    var disponibilita = document.querySelector('input[name="disponibilita"]').value;
    var descrizione = document.querySelector('input[name="descrizione"]').value;
    var condizione = document.querySelector('select[name="condizione"]').value;
    var annoPubblicazione = document.querySelector('input[name="annoPubblicazione"]').value;
    var idPiattaforma = document.querySelector('select[name="IDPiattaforma"]').value;

    // Check if name meets the criteria if provided
    var namePattern = /^[a-zA-Z0-9\s]{1,40}$/;
    if (nome && !namePattern.test(nome)) {
        alert("Il nome deve contenere solo lettere, numeri e spazi, per un massimo di 40 caratteri.");
        return false;
    }

    // Check if price is a valid float number
    var prezzoPattern = /^\d+(\.\d{1,2})?$/;
    if (prezzo && !prezzoPattern.test(prezzo)) {
        alert("Il prezzo deve essere un numero valido con massimo due decimali.");
        return false;
    }

    // Check if IVA is a valid integer
    if (iva && !/^\d+$/.test(iva)) {
        alert("L'IVA deve essere un numero intero.");
        return false;
    }

    // Check if availability is a valid integer
    if (disponibilita && !/^\d+$/.test(disponibilita)) {
        alert("La disponibilità deve essere un numero intero.");
        return false;
    }

    // Check if description meets pattern requirements if provided
    var descrizionePattern = /^[a-zA-Z0-9\s.,;!?()'\"-]{0,150}$/;
    if (descrizione && !descrizionePattern.test(descrizione)) {
        alert("La descrizione può contenere solo lettere, numeri, spazi e caratteri di punteggiatura. Lunghezza massima 150 caratteri.");
        return false;
    }

    // Check if year of publication is a valid 4 digit number
    var annoPattern = /^\d{4}$/;
    if (annoPubblicazione && !annoPattern.test(annoPubblicazione)) {
        alert("L'anno di pubblicazione deve essere un numero di esattamente 4 cifre.");
        return false;
    }

    // Validate select inputs
    if (!tipo || tipo === "") {
        alert("Seleziona un tipo valido.");
        return false;
    }
    if (!condizione || condizione === "") {
        alert("Seleziona una condizione valida.");
        return false;
    }
    if (!idPiattaforma || idPiattaforma === "") {
        alert("Seleziona una piattaforma valida.");
        return false;
    }

    return true;
}
