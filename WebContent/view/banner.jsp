<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/banner.css">
</head>
<body>
    <div class="banner">
        <div class="slides">
            <div class="slide active" style="background-image: url('${pageContext.request.contextPath}/images/banner_3.png');">
                <div class="content">
                    <h2>Libri</h2>
                    <p>Scopri tutti i nostri libri</p>
                    <a href="${pageContext.request.contextPath}/view/PaginaLibri.jsp" class="btn">Sfoglia</a>
                </div>
            </div>
            <div class="slide" style="background-image: url('${pageContext.request.contextPath}/images/banner_2.png');">
                <div class="content">
                    <h2>Giochi</h2>
                    <p>Scopri tutti i nostri giochi</p>
                    <a href="${pageContext.request.contextPath}/view/PaginaGiochi.jsp" class="btn">Sfoglia</a>
                </div>
            </div>
            <div class="slide" style="background-image: url('${pageContext.request.contextPath}/images/banner_1.png');">
                <div class="content">
                    <h2>Libri e giochi usati</h2>
                    <p>Scopri tutti i prodotti usati</p>
                    <a href="${pageContext.request.contextPath}/view/PaginaUsatoNuovo.jsp?condizione=usato" class="btn">Sfoglia</a>
                </div>
            </div>
            <!-- Aggiungi altre slide qui -->
        </div>
        <div class="controls">
            <div class="prev">&laquo;</div>
            <div class="next">&raquo;</div>
        </div>
        <div class="indicators">
            <span class="indicator active"></span>
            <span class="indicator"></span>
            <span class="indicator"></span>
            <!-- Aggiungi altri indicatori qui -->
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/scripts/banner.js"></script>
</body>
</html>
