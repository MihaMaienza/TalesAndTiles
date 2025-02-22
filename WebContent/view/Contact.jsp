<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Tiles And Tales</title>
<!-- Link al file CSS -->
<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/contact.css">
</head>
<body>
    <jsp:include page="navbar.jsp" />
    <jsp:include page="register.jsp" />

    <div class="main-content">
        <!-- Home -->
        <section id="home">
            <h1>Benvenuti in TalesAndTiles</h1>
            <p>Esplora il mondo di libri e giochi da tavolo con noi!</p>
        </section>

        <!-- About -->
        <section id="about">
            <h2>Chi Siamo</h2>
            <p>TalesAndTiles Srl e' un negozio specializzato nella vendita di libri e giochi da tavolo, </p>
            <p>pensato per gli appassionati di storie avvincenti e momenti di divertimento condiviso.</p>
            <p>La nostra missione e' quella di offrire un'esperienza unica, combinando la passione per la lettura con il fascino del gioco.</p>
            <p>Dalla narrativa piu' emozionante ai giochi strategici piu' avvincenti,</p>
            <p>TalesAndTiles propone un vasto catalogo di prodotti accuratamente selezionati per soddisfare ogni tipo di interesse e preferenza.</p>
			<p>Ogni libro e gioco presente nel nostro negozio e' scelto con cura per garantire qualità e unicità'.</p>
        </section>

        <!-- Services -->
        <section id="services">
            <h2>Cosa ci distingue</h2>
            <p>La nostra forza risiede nella passione per cio' che facciamo.</p>
            <p>TalesAndTiles e' il punto di incontro perfetto per lettori e giocatori, offrendo non solo prodotti eccellenti,</p>
			<p>ma anche un ambiente accogliente e professionale, in cui ogni cliente puo' trovare il consiglio giusto per le proprie esigenze.</p>
        </section>

        <!-- Team -->
        <section id="team">
            <h2>Il nostro impegno</h2>
            <p><strong>Qualità e Varietà:</strong> Offriamo una selezione ampia e diversificata, adatta a tutte le eta' e livelli di esperienza.</p>
            <p><strong>Esperienza Personalizzata:</strong> Il nostro team di esperti e' sempre pronto ad aiutarti a trovare il libro o il gioco perfetto.</p>
            <p><strong>Innovazione e Tradizione:</strong>  Combiniamo il fascino dei giochi classici con le ultime novità del mercato, creando un'esperienza unica per tutti i nostri clienti.</p>
        </section>
        
        <!-- Unisciti a noi -->
        <section id="team">
            <h2>Unisciti a Noi</h2>
            <p>Che tu sia un lettore appassionato, un giocatore esperto o semplicemente curioso di scoprire qualcosa di nuovo,</p>
            <p> TalesAndTiles è il luogo perfetto per te. Vieni a trovarci e lasciati ispirare dal nostro mondo fatto di storie straordinarie e avventure da vivere.</p>
            <p><strong>TalesAndTiles Srl: dove le storie prendono vita e il gioco diventa arte.</strong></p>
        </section>

        <!-- Contact -->
        <section id="contact">
            <h2>Contattaci</h2>
            <p>Se hai domande, richieste o bisogno di supporto, non esitare a contattarci tramite i seguenti canali:</p>
            <div id="email-contact">
                <h3>Email</h3>
                <p>Per qualsiasi informazione o supporto, puoi contattarci via email all'indirizzo: <a href="mailto:contact@talesandtiles.com">contact@talesandtiles.com</a></p>
            </div>
            <div id="phone-contact">
                <h3>Telefono</h3>
                <p>Puoi anche raggiungerci telefonicamente al numero: <a href="tel:+391234567890">+39 3500153486</a></p>
            </div>
        </section>
    </div>

    <jsp:include page="footer.jsp" />
</body>
</html>
