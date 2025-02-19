package control;

import model.DAO.*;
import model.javaBeans.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import at.favre.lib.crypto.bcrypt.BCrypt;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/view/register.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String redirectURL = request.getParameter("redirect");

        UtenteDAO utenteDAO = new UtenteDAO();
        try {
            Utente utente = utenteDAO.findByUsername(username);
            if (utente != null) {
                // Verifica la password hashata
                BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), utente.getPwd());
                if (result.verified) {
                    // Login avvenuto con successo, crea una sessione
                    HttpSession session = request.getSession();
                    session.setAttribute("utente", utente);
                    session.setAttribute("isLoggedIn", true);
                    CarrelloDAO carrelloDAO = new CarrelloDAO();
                    Carrello carrello = carrelloDAO.getCarrelloByUsername(username);
                    
                    // Ricava e setta IDCarrello
                    int IDCarrello = carrello.getIDCarrello();
                    session.setAttribute("IDCarrello", IDCarrello);

                    @SuppressWarnings("unchecked")
                    List<CartNoLog> cartNoLogItems = (List<CartNoLog>) session.getAttribute("cartNoLog");
                    if (cartNoLogItems != null && !cartNoLogItems.isEmpty()) {
                        CartItemDAO cartItemDAO = new CartItemDAO();
                        cartItemDAO.addCartNoLogItems(IDCarrello, cartNoLogItems);
                        session.removeAttribute("cartNoLog"); // Rimuovi la lista temporanea
                    }

                    // Reindirizza l'utente alla pagina corrente o alla home se il redirectURL è nullo o vuoto
                    if (redirectURL != null && !redirectURL.isEmpty()) {
                        response.sendRedirect(redirectURL);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/index.jsp");
                    }
                } else {
                    // Password errata
                    request.setAttribute("loginError", "Username o password non validi");
                    request.setAttribute("popup", "login"); // Indica di mostrare il popup di login
                    request.getRequestDispatcher("/index.jsp").forward(request, response);
                }
            } else {
                // Utente non trovato
                request.setAttribute("loginError", "Username o password non validi");
                request.setAttribute("popup", "login"); // Indica di mostrare il popup di login
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("loginError", "Errore interno al server");
            request.setAttribute("popup", "login"); // Indica di mostrare il popup di login
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }
}


