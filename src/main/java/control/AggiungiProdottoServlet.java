package control;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part; // Serve per gestire il file caricato

import model.prodotto.ProdottoDAO;
import model.ConnectionPool;
import model.prodotto.ProdottoBean;

@WebServlet("/AggiungiProdottoServlet")
@MultipartConfig(
	    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
	    maxFileSize = 1024 * 1024 * 10,      // 10MB massimo per singolo file
	    maxRequestSize = 1024 * 1024 * 50    // 50MB massimo per l'intero form
	)

public class AggiungiProdottoServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
    private ProdottoDAO prodottoDAO;

    public void init() throws ServletException {
		super.init();
		try {
	        ConnectionPool.init(5);
	    } catch (SQLException e) {
	        System.out.println("Errore fatale: Impossibile avviare il Connection Pool!");
	        e.printStackTrace();
	    }
		prodottoDAO=new ProdottoDAO(); 
	} 
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	doPost(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	//recupero dati dal form
    	String nome = request.getParameter("nome");
        String descrizione = request.getParameter("descrizione");
        float prezzo = Float.parseFloat(request.getParameter("prezzo"));
        
        //prendere l'immagine dal form
     // 2. Gestione del File Immagine
        Part filePart = request.getPart("foto"); // Prende il campo con nome="foto"' del form
        String nomeImmagine = filePart.getSubmittedFileName(); // Nome originale del file
        //viene usata la cartella webapp/img/prodotti
        // File.separator mette la barra corretta a seconda se sei su Windows (\) o Mac/Linux (/)
        String uploadPath = request.getServletContext().getRealPath("") + File.separator + "img" + File.separator + "prodotti";
        File uploadDir = new File(uploadPath);
        //se la cartella non esiste la crea
        if (!uploadDir.exists()) {
            uploadDir.mkdirs(); 
        }
        // Scriviamo il file usando il suo nome originale
        String percorsoCompletoFile = uploadPath + File.separator + nomeImmagine;
        filePart.write(percorsoCompletoFile);
        
        ProdottoBean nuovoProdotto = new ProdottoBean();
        nuovoProdotto.setNome(nome);
        nuovoProdotto.setDescrizione(descrizione);
        nuovoProdotto.setPrezzo(prezzo);
        nuovoProdotto.setImmagine(nomeImmagine); 

        try {
            prodottoDAO.doSave(nuovoProdotto);
            response.sendRedirect("paginaAdmin.jsp");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("errore500.jsp");
        }
    }
}