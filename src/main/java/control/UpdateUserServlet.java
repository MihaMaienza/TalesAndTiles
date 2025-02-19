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


@WebServlet("/UpdateUserServlet")
public class UpdateUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/view/UpdateUser.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
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
        if (dataNascitaStr != null) {
            try {
                dataNascita = new SimpleDateFormat("yyyy-MM-dd").parse(dataNascitaStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (dataScadenzaStr != null) {
            try {
                dataScadenza = new SimpleDateFormat("yyyy-MM-dd").parse(dataScadenzaStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        UtenteDAO utenteDAO = new UtenteDAO();
        try {
            Utente utente = utenteDAO.findByUsername(username);
            if (utente == null) {
                response.sendRedirect("/view/ErrorPage.jsp");
                return;
            }

            boolean emailExists = utenteDAO.isEmailInUse(email) && !email.equals(utente.getEmail());

            if (emailExists) {
                request.setAttribute("emailError", "Email already in use");
                request.getRequestDispatcher("/view/UpdateUser.jsp").forward(request, response);
                return;
            }

            boolean passwordUpdated = false;

            if ((oldPassword != null && !oldPassword.isEmpty()) || (newPassword != null && !newPassword.isEmpty())) {
                if (oldPassword != null && !oldPassword.isEmpty() && newPassword != null && !newPassword.isEmpty()) {
                    BCrypt.Result result = BCrypt.verifyer().verify(oldPassword.toCharArray(), utente.getPwd());
                    if (result.verified) {
                        String hashedPwd = BCrypt.withDefaults().hashToString(12, newPassword.toCharArray());
                        utente.setPwd(hashedPwd);
                        passwordUpdated = true;
                    } else {
                        request.setAttribute("oldPasswordError", "La vecchia password non è corretta.");
                        request.getRequestDispatcher("/view/UpdateUser.jsp").forward(request, response);
                        return;
                    }
                } else {
                    request.setAttribute("passwordFormatError", "Per cambiare la password, riempi sia il campo della vecchia password sia il campo della nuova password.");
                    request.getRequestDispatcher("/view/UpdateUser.jsp").forward(request, response);
                    return;
                }
            }

            utente.setNome(nome);
            utente.setCognome(cognome);
            utente.setEmail(email);
            utente.setDataNascita(dataNascita);
            utente.setNomeCarta(nomeCarta);
            utente.setCognomeCarta(cognomeCarta);
            utente.setNumCarta(numCarta);
            utente.setDataScadenza(dataScadenza);
            utente.setCVV(CVV);
            utente.setCap(cap);
            utente.setVia(via);
            utente.setCitta(citta);

            utenteDAO.update(utente);

            request.setAttribute("successMessage", "Dati aggiornati con successo.");
            request.getSession().setAttribute("utente", utente); // Aggiorna l'utente nella sessione
            request.getRequestDispatcher("/view/AccountUser.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("/view/ErrorPage.jsp");
        }
    }
}
