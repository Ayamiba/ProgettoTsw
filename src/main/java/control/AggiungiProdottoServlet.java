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
import javax.servlet.http.Part;

import model.prodotto.ProdottoDAO;
import model.ConnectionPool;
import model.prodotto.ProdottoBean;

@WebServlet("/AggiungiProdottoServlet")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class AggiungiProdottoServlet extends HttpServlet {
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
        prodottoDAO = new ProdottoDAO(); 
    } 

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/AggiungiProdotto.jsp").forward(request, response);
    } 

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        // 1. Leggiamo i dati dal form
        String nome = request.getParameter("nome");
        String descrizione = request.getParameter("descrizione");
        float prezzo = Float.parseFloat(request.getParameter("prezzo"));

        // 2. Gestiamo l'immagine
        Part filePart = request.getPart("foto");
        String nomeOriginale = filePart.getSubmittedFileName();
        String nomeImmagineUnivoco = "default.jpg"; // Nome di sicurezza

        if (nomeOriginale != null && !nomeOriginale.isEmpty()) {
            String estensione = "";
            int index = nomeOriginale.lastIndexOf('.');
            if (index > 0) {
                estensione = nomeOriginale.substring(index);
                // Puliamo gli spazi dal nome originale
                nomeOriginale = nomeOriginale.substring(0, index).replaceAll("\\s+", "_"); 
            }
            // Creiamo il nome con i millisecondi
            nomeImmagineUnivoco = System.currentTimeMillis() + "_" + nomeOriginale + estensione;

            // -------------------------------------------------------------
            // TECNICA DEL DOPPIO SALVATAGGIO
            // -------------------------------------------------------------
            // A. Nel server Tomcat (per vederla subito)
            String serverPath = request.getServletContext().getRealPath("/img/prodotti");
            File serverDir = new File(serverPath);
            if (!serverDir.exists()) serverDir.mkdirs();
            File serverFile = new File(serverDir, nomeImmagineUnivoco);

            // B. Nel tuo Workspace Eclipse (per non perderla mai)
            String workspacePath = "C:/Users/gvarr/OneDrive/Desktop/Projects/TSW/ProgettoTsw/src/main/webapp/img/prodotti";
            File workspaceDir = new File(workspacePath);
            if (!workspaceDir.exists()) workspaceDir.mkdirs();
            File workspaceFile = new File(workspaceDir, nomeImmagineUnivoco);

            try (java.io.InputStream input = filePart.getInputStream()) {
                // Copiamo in entrambe le cartelle
                java.nio.file.Files.copy(input, serverFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                java.nio.file.Files.copy(serverFile.toPath(), workspaceFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                
                System.out.println("✅ NUOVO PRODOTTO - MAGIA RIUSCITA!");
                System.out.println("-> Workspace: " + workspaceFile.getAbsolutePath());
            } catch (IOException e) {
                System.out.println("❌ Errore durante il salvataggio dell'immagine.");
                e.printStackTrace();
                throw e;
            }
        }

        // 3. Creiamo il Bean e lo passiamo al DAO
        ProdottoBean nuovoProdotto = new ProdottoBean();
        nuovoProdotto.setNome(nome);
        nuovoProdotto.setDescrizione(descrizione);
        nuovoProdotto.setPrezzo(prezzo);
        nuovoProdotto.setImmagine(nomeImmagineUnivoco); 

        try {
            prodottoDAO.doSave(nuovoProdotto);
            // Rimando alla pagina di aggiunta con il messaggio di successo!
            response.sendRedirect("AggiungiProdotto.jsp?messaggio=Prodotto aggiunto con successo!");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("errore500.jsp");
        }
    }
}