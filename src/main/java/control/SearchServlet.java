package control;

import model.DAO.*;
import model.javaBeans.*;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        ProdottoDAO prodottoDAO = new ProdottoDAO();
        List<Prodotto> prodotti = null;

        try {
            prodotti = prodottoDAO.searchProdotti(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String json = new Gson().toJson(prodotti);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }
}


