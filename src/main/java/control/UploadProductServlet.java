package control;

import model.DAO.*;
import model.javaBeans.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;


@WebServlet("/UploadProductServlet")
@MultipartConfig(fileSizeThreshold=1024*1024*2, // 2MB
                 maxFileSize=1024*1024*10,      // 10MB
                 maxRequestSize=1024*1024*50)   // 50MB
public class UploadProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static String SAVE_DIR = "/uploadTemp";
    

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Utente utente = (Utente) request.getSession().getAttribute("utente");
        if (utente == null || utente.getTipo() != 1) { // Controlla se l'utente non è admin
            response.sendRedirect(request.getContextPath() + "/view/AccessoVietato.jsp");
            return;
        }
        /*System.out.println("doGet method called");  // Log per debug
        try {
            int maxId = PhotoControl.getMaxId();
            System.out.println("Max ID from database: " + maxId);  // Log per debug
            int nextId = maxId + 1;
            request.setAttribute("nextId", nextId);
            System.out.println("Next ID to be used: " + nextId);  // Log per debug
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Database error", e);
        }*/
        RequestDispatcher view = request.getRequestDispatcher("/view/CaricamentoProdotto");
        view.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
        String nome = request.getParameter("nome");
        String prezzo = request.getParameter("prezzo");
        String iva = request.getParameter("IVA");
        String tipo = request.getParameter("tipo");
        String disponibilita = request.getParameter("disponibilita");
        String descrizione = request.getParameter("descrizione");
        String condizione = request.getParameter("condizione");
        String annoPubblicazione = request.getParameter("annoPubblicazione");
        String fasciaEta=null;
        String casaProduttrice=null;
        String idGenereGioco=null;
        String editore=null;;
        String scrittore=null;;
        String isbn=null;;
        String idGenereLibro=null;;
        if ("gioco".equals(tipo)) {
            // Gestione del gioco
             fasciaEta = request.getParameter("fasciaEta");
             casaProduttrice = request.getParameter("casaProduttrice");
             idGenereGioco = request.getParameter("idGenereGioco");

            // Logica per salvare il gioco
        } else if ("libro".equals(tipo)) {
            // Gestione del libro
             editore = request.getParameter("editore");
             scrittore = request.getParameter("scrittore");
             isbn = request.getParameter("isbn");
             idGenereLibro = request.getParameter("idGenereLibro");
            // Logica per salvare il libro
        }


        // Logging per debug
        /*System.out.println("Nome: " + nome);
        System.out.println("Prezzo: " + prezzo);
        System.out.println("IVA: " + iva);
        System.out.println("Tipo: " + tipo);
        System.out.println("Disponibilità: " + disponibilita);
        System.out.println("Descrizione: " + descrizione);
        System.out.println("Condizione: " + condizione);
        System.out.println("Anno Pubblicazione: " + annoPubblicazione);
        System.out.println("ID Piattaforma: " + idPiattaforma);*/

        // Validazione dei parametri
        if (nome == null || nome.isEmpty() || prezzo == null || prezzo.isEmpty() || iva == null || iva.isEmpty() || tipo == null || tipo.isEmpty() || disponibilita == null || disponibilita.isEmpty() || descrizione == null || descrizione.isEmpty() || condizione == null || condizione.isEmpty() || annoPubblicazione == null || annoPubblicazione.isEmpty()) {
            request.setAttribute("message", "All fields are required.");
            RequestDispatcher view = request.getRequestDispatcher("/view/CaricamentoProdotto.jsp");
            view.forward(request, response);
            return;
        }

        String appPath = request.getServletContext().getRealPath("");
        String savePath = appPath + File.separator + SAVE_DIR;
        
        File fileSaveDir = new File(savePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdir();
        }

        String filePath = "";
        for (Part part : request.getParts()) {
            if (part.getName().equals("file-upload")) {
                String fileName = extractFileName(part);
                if (fileName != null && !fileName.equals("")) {
                    filePath = savePath + File.separator + fileName;
                    part.write(filePath);
                }
            }
        }

        try {
        	int productId;
        	if("gioco".equals(tipo)){
                productId = ProdottoDAO2.saveProduct(nome, Float.parseFloat(prezzo), Integer.parseInt(iva), tipo, filePath, Integer.parseInt(disponibilita), descrizione, condizione, Integer.parseInt(annoPubblicazione),fasciaEta, casaProduttrice,Integer.parseInt(idGenereGioco),null,null,null,0);
        	}
        	else {
                productId = ProdottoDAO2.saveProduct(nome, Float.parseFloat(prezzo), Integer.parseInt(iva), tipo, filePath, Integer.parseInt(disponibilita), descrizione, condizione, Integer.parseInt(annoPubblicazione),null,null,0,editore,scrittore,isbn,Integer.parseInt(idGenereLibro));
        		}
            if (productId > 0) {
                response.sendRedirect(request.getContextPath() + "/view/DettagliProdotto.jsp?id=" + productId);
            } else {
                request.setAttribute("message", "Product ID already exists or ID Piattaforma does not exist. Please try again.");
                RequestDispatcher view = request.getRequestDispatcher("/view/CaricamentoProdotto.jsp");
                view.forward(request, response);
            }
        } catch (SQLException sqlException) {
            request.setAttribute("message", "Error occurred while saving the product. Please try again.");
            System.out.println(sqlException);
            RequestDispatcher view = request.getRequestDispatcher("/view/CaricamentoProdotto.jsp");
            view.forward(request, response);
        }
    }

    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return "";
    }
}
