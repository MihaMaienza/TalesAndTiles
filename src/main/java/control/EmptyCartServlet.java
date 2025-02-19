package control;

import model.DAO.*;

import java.io.IOException;
import java.sql.SQLException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/EmptyCartServlet")
public class EmptyCartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        JsonObject jsonObject = JsonParser.parseReader(request.getReader()).getAsJsonObject();
        boolean userLoggedIn = jsonObject.get("userLoggedIn").getAsBoolean();

        try {
            if (userLoggedIn) {
                int IDCarrello = (int) session.getAttribute("IDCarrello");
                CartItemDAO cartItemDAO = new CartItemDAO();
                cartItemDAO.SvuotaCarrello(IDCarrello);
            } else {
                session.removeAttribute("cartNoLog");
            }
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":\"success\"}");
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\":\"error\"}");
        }
    }
}
