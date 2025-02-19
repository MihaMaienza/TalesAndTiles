package control;

import model.DAO.*;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;


@WebServlet("/UpdateProductServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
                 maxFileSize = 1024 * 1024 * 10,      // 10MB
                 maxRequestSize = 1024 * 1024 * 50)   // 50MB
public class UpdateProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String SAVE_DIR = "/uploadTemp";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int productId = Integer.parseInt(request.getParameter("id"));
        String nome = request.getParameter("nome");
        String prezzo = request.getParameter("prezzo");
        String iva = request.getParameter("IVA");
        String tipo = request.getParameter("tipo");
        String disponibilita = request.getParameter("disponibilita");
        String descrizione = request.getParameter("descrizione");
        String condizione = request.getParameter("condizione");
        String annoPubblicazione = request.getParameter("annoPubblicazione");
        String fasciaEta = null;
        String casaProduttrice = null;
        String idGenereGioco = null;
        String editore = null;
        String scrittore = null;
        String isbn = null;
        String idGenereLibro = null;

        if ("gioco".equalsIgnoreCase(tipo)) {
            fasciaEta = request.getParameter("fasciaEta");
            casaProduttrice = request.getParameter("casaProduttrice");
            idGenereGioco = request.getParameter("idGenereGioco");
        } else if ("libro".equalsIgnoreCase(tipo)) {
            editore = request.getParameter("editore");
            scrittore = request.getParameter("scrittore");
            isbn = request.getParameter("isbn");
            idGenereLibro = request.getParameter("idGenereLibro");
        }

        String appPath = request.getServletContext().getRealPath("");
        String savePath = appPath + File.separator + SAVE_DIR;
        File fileSaveDir = new File(savePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdir();
        }

        String filePath = null;
        for (Part part : request.getParts()) {
            if (part.getName().equals("file-upload") && part.getSize() > 0) {
                String fileName = extractFileName(part);
                filePath = savePath + File.separator + fileName;
                part.write(filePath);
            }
        }

        try {
            boolean success = ProdottoDAO2.updateProduct(productId, nome, Float.parseFloat(prezzo),
                    Integer.parseInt(iva), tipo, filePath, Integer.parseInt(disponibilita),
                    descrizione, condizione, Integer.parseInt(annoPubblicazione), fasciaEta,
                    casaProduttrice, idGenereGioco != null ? Integer.parseInt(idGenereGioco) : null,
                    editore, scrittore, isbn, idGenereLibro != null ? Integer.parseInt(idGenereLibro) : null);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/view/DettagliProdotto.jsp?id=" + productId);
            } else {
                request.setAttribute("message", "Errore durante l'aggiornamento del prodotto.");
                request.getRequestDispatcher("/view/EditProduct.jsp?id=" + productId).forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Errore interno durante l'aggiornamento del prodotto.");
            request.getRequestDispatcher("/view/EditProduct.jsp?id=" + productId).forward(request, response);
        }
    }

    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        for (String content : contentDisp.split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf("=") + 2, content.length() - 1);
            }
        }
        return null;
    }
}
