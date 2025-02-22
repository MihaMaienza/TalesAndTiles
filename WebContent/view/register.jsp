<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/register.css">
<style>
    .btnPopUp {
        width: 100%;
        height: 45px;
        background: #162938;
        border: none;
        outline: none;
        border-radius: 6px;
        cursor: pointer;
        font-size: 1em;
        color: #fff;
        font-weight: 500;
        transition: background-color 0.3s ease;
    }
</style>
<div class="overlay <%= request.getAttribute("popup") != null ? "active" : "" %>" id="overlay"></div>
<div class="wrapper <%= request.getAttribute("popup") != null ? "active-popup active" : "" %>" id="myWrapper">
    <span class="icon-close"><ion-icon name="close"></ion-icon></span>
    <div class="form-box login <%= "login".equals(request.getAttribute("popup")) ? "active" : "" %>">
        <h2>Login</h2>
        <form action="${pageContext.request.contextPath}/LoginServlet" method="post">
            <input type="hidden" name="redirect" value="<%= request.getRequestURL().toString() + (request.getQueryString() != null ? "?" + request.getQueryString() : "") %>">
            <div class="input-box">
                <span class="icon"><ion-icon name="person"></ion-icon></span>
                <input type="text" name="username" required>
                <label>Username</label>
            </div>
            <div class="input-box">
                <span class="icon"><ion-icon name="lock-closed"></ion-icon></span>
                <input type="password" name="password" required>
                <label>Password</label>
            </div>
            <% if (request.getAttribute("loginError") != null) { %>
                <div class="error-message">
                    <%= request.getAttribute("loginError") %>
                </div>
            <% } %>
            <!--  
            <div class="remember-forgot">
                <label><input type="checkbox">Remember Me</label>
                <a href="#">Forgot Password?</a>
            </div>
            -->
            <button type="submit" class="btnPopUp">Login</button>
            <div class="login-register">
                <p>Don't have an account? <a href="#" class="register-link">Register</a></p>
            </div>
        </form>
    </div>

    <div class="form-box register <%= "register".equals(request.getAttribute("popup")) ? "active" : "" %>">
        <h2>Registration</h2>
        <form id="registerForm" action="${pageContext.request.contextPath}/RegisterServlet" method="post">
            <div class="input-box">
                <span class="icon"><ion-icon name="person"></ion-icon></span>
                <input type="text" name="username" id="username" value="<%= request.getParameter("username") != null ? request.getParameter("username") : "" %>" required>
                <label>Username</label>
                <small id="usernameError" style="color: red; display: <%= request.getAttribute("usernameError") != null ? "block" : "none" %>;">
                    <%= request.getAttribute("usernameError") != null ? request.getAttribute("usernameError") : "" %>
                </small>
            </div>
            <div class="input-box">
                <span class="icon"><ion-icon name="mail"></ion-icon></span>
                <input type="email" name="email" id="email" value="<%= request.getParameter("email") != null ? request.getParameter("email") : "" %>" required>
                <label>Email</label>
                <small id="emailError" style="color: red; display: <%= request.getAttribute("emailError") != null ? "block" : "none" %>;">
                    <%= request.getAttribute("emailError") != null ? request.getAttribute("emailError") : "" %>
                </small>
            </div>
            <div class="input-box">
                <span class="icon"><ion-icon name="lock-closed"></ion-icon></span>
                <input type="password" name="pwd" id="password" required>
                <label>Password</label>
                <small id="passwordError" style="color: red; display: <%= request.getAttribute("passwordError") != null ? "block" : "none" %>;">
                    <%= request.getAttribute("passwordError") != null ? request.getAttribute("passwordError") : "" %>
                </small>
            </div>
            <!--  
            <div class="remember-forgot">
                <label><input type="checkbox">I agree to the terms & conditions</label>
            </div>
            -->
            <button type="submit" class="btnPopUp">Register</button>
            <div class="login-register">
                <p>Already have an account? <a href="#" class="login-link">Login</a></p>
            </div>
        </form>
    </div>
</div>

<script src="${pageContext.request.contextPath}/scripts/registrazione.js"></script>
<script type="module" src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js"></script>
<script nomodule src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.js"></script>
