package control;

import model.DAO.*;
import model.javaBeans.*;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import at.favre.lib.crypto.bcrypt.BCrypt;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    /*private UtenteDAO utenteDAO;

    public RegisterServlet() {
        this.utenteDAO = new UtenteDAO();
    }*/

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	UtenteDAO utenteDAO = new UtenteDAO();
        String username = request.getParameter("username");
        String pwd = request.getParameter("pwd");
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String email = request.getParameter("email");
        String dataNascitaStr = request.getParameter("dataNascita");
        String nomeCarta = request.getParameter("nomeCarta");
        String cognomeCarta = request.getParameter("cognomeCarta");
        String numCarta = request.getParameter("numCarta");
        String dataScadenzaStr = request.getParameter("dataScadenza");
        String CVV = request.getParameter("CVV");
        String cap = request.getParameter("cap");
        String via = request.getParameter("via");
        String citta = request.getParameter("citta");

        Date dataNascita = null;
        Date dataScadenza = null;
        if(dataNascitaStr!=null) {
            try {
                dataNascita = new SimpleDateFormat("yyyy-MM-dd").parse(dataNascitaStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if(dataScadenzaStr!=null) {
            try {
                dataScadenza = new SimpleDateFormat("yyyy-MM-dd").parse(dataScadenzaStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Cripta la password
        String hashedPwd = BCrypt.withDefaults().hashToString(12, pwd.toCharArray());

        Utente utente = new Utente(username, hashedPwd, nome, cognome, email, dataNascita, nomeCarta, cognomeCarta, numCarta, dataScadenza, CVV, cap, via, citta, 0);

        try {
            boolean usernameExists = utenteDAO.isUsernameInUse(username);
            boolean emailExists = utenteDAO.isEmailInUse(email);

            if (usernameExists || emailExists) {
                if (usernameExists) {
                    request.setAttribute("usernameError", "Username already in use");
                }
                if (emailExists) {
                    request.setAttribute("emailError", "Email already in use");
                }
                request.setAttribute("popup", "true");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            } else {
                utenteDAO.save(utente);
                CarrelloDAO.CreaCarrello(username);//Quando creiamo un nuovo utente gli creiamo anche un carrello
                WishlistDAO.CreaWishlist(username);
                request.getRequestDispatcher("/view/registrationSuccess.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
