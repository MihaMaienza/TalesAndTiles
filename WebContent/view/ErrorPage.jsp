<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Errore</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/styles/ErrorPage.css">
    <script>
        var countdown = 5; // Conto alla rovescia di 5 secondi
        function startCountdown() {
            var countdownElement = document.getElementById("countdown");
            var interval = setInterval(function() {
                countdown--;
                countdownElement.textContent = countdown;
                if (countdown <= 0) {
                    clearInterval(interval);
                    window.location.href = "<%= request.getContextPath() %>/index.jsp";
                }
            }, 1000);
        }
    </script>
</head>
<body onload="startCountdown()">
    <div class="container">
        <h1>
            <% 
                Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
                if (statusCode != null) {
                    if (statusCode == 400) {
                        out.print("RICHIESTA NON VALIDA");
                    } else if (statusCode == 500) {
                        out.print("ERRORE INTERNO DEL SERVER");
                    } else {
                        out.print("ERRORE: " + statusCode);
                    }
                } else {
                    out.print("ERRORE");
                }
            %>
        </h1>
        <p>Verrai reindirizzato alla homepage in <span id="countdown">5</span> secondi.</p>
    </div>
</body>
</html>
