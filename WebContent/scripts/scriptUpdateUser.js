function validateForm() {
    var numCarta = document.querySelector('input[name="numCarta"]').value;
    var cvv = document.querySelector('input[name="CVV"]').value;
    var oldPassword = document.querySelector('input[name="oldPassword"]').value;
    var newPassword = document.querySelector('input[name="newPassword"]').value;
    var nome = document.querySelector('input[name="nome"]').value;
    var cognome = document.querySelector('input[name="cognome"]').value;
    var nomeCarta = document.querySelector('input[name="nomeCarta"]').value;
    var cognomeCarta = document.querySelector('input[name="cognomeCarta"]').value;
    var email = document.querySelector('input[name="email"]').value;
    var cap = document.querySelector('input[name="cap"]').value;
    var via = document.querySelector('input[name="via"]').value;
    var citta = document.querySelector('input[name="citta"]').value;

    // Check if card number is 16 digits if provided
    if (numCarta && !/^\d{16}$/.test(numCarta)) {
        alert("Il numero della carta deve essere di 16 cifre.");
        return false;
    }

    // Check if CVV is 3 digits if provided
    if (cvv && !/^\d{3}$/.test(cvv)) {
        alert("Il CVV deve essere di 3 cifre.");
        return false;
    }

    // Check if both old and new passwords are filled or not filled
    if ((oldPassword && !newPassword) || (!oldPassword && newPassword)) {
        alert("Per cambiare la password, riempi sia il campo della vecchia password sia il campo della nuova password.");
        return false;
    }

    // Check if the new password meets the criteria if provided
    var passwordPattern = /^(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&.])[A-Za-z\d@$!%*?&]{8,}(?!.*\s)$/;
    if (newPassword && !passwordPattern.test(newPassword)) {
        alert("La nuova password deve essere di almeno 8 caratteri, contenere una lettera maiuscola, un numero e un carattere speciale.");
        return false;
    }

    // Check if name, surname, nomeCarta, and cognomeCarta meet the criteria if provided
    var namePattern = /^[A-Z][a-z]{0,29}$/;
    if (nome && !namePattern.test(nome)) {
        alert("Il nome deve iniziare con una lettera maiuscola e contenere solo lettere minuscole, per un massimo di 30 caratteri.");
        return false;
    }
    if (cognome && !namePattern.test(cognome)) {
        alert("Il cognome deve iniziare con una lettera maiuscola e contenere solo lettere minuscole, per un massimo di 30 caratteri.");
        return false;
    }
    if (nomeCarta && !namePattern.test(nomeCarta)) {
        alert("Il nome sulla carta deve iniziare con una lettera maiuscola e contenere solo lettere minuscole, per un massimo di 30 caratteri.");
        return false;
    }
    if (cognomeCarta && !namePattern.test(cognomeCarta)) {
        alert("Il cognome sulla carta deve iniziare con una lettera maiuscola e contenere solo lettere minuscole, per un massimo di 30 caratteri.");
        return false;
    }

    // Check if email meets pattern requirements if provided
    var emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,40}$/;
    if (email && !emailPattern.test(email)) {
        alert("Il formato dell'email non è valido.");
        return false;
    }

    // Check if CAP is 5 digits if provided
    if (cap && !/^\d{5}$/.test(cap)) {
        alert("Il CAP deve essere di 5 cifre.");
        return false;
    }

    // Check if via meets pattern requirements if provided
    var viaPattern = /^[a-z ]+[a-z ]{0,68}(\d{0,2})?$/;
    if (via && !viaPattern.test(via)) {
        alert("La via deve essere composta solo da lettere minuscole, spazi e può terminare con un massimo di 2 cifre. Lunghezza massima 70 caratteri.");
        return false;
    }

    // Check if città meets pattern requirements if provided
    var cittaPattern = /^([A-Z][a-z]*)(\s[A-Z][a-z]*)*$/;
    if (citta && !cittaPattern.test(citta)) {
        alert("La città deve essere composta solo da lettere e spazi, e tutte le parole devono iniziare con una lettera maiuscola.");
        return false;
    }

    return true;
}
