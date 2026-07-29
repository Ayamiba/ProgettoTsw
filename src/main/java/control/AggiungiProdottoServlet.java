package control;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import model.prodotto.ProdottoDAO;
import model.ConnectionPool;
import model.prodotto.ProdottoBean;
import model.utente.UtenteBean;

@WebServlet("/AggiungiProdottoServlet")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class AggiungiProdottoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProdottoDAO prodottoDAO;
    private String workspacePath;

    public void init() throws ServletException {
        super.init();
        try {
            ConnectionPool.init(5);
        } catch (SQLException e) {
            System.out.println("Errore fatale: Impossibile avviare il Connection Pool!");
            e.printStackTrace();
        }
        prodottoDAO = new ProdottoDAO(); 
        
        // Caricamento del config.properties all'avvio della Servlet
        try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                System.out.println("Attenzione: Impossibile trovare config.properties");
            } else {
                prop.load(input);
                workspacePath = prop.getProperty("upload.path.prodotti");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    } 

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Controllo di Sicurezza Admin
        HttpSession session = request.getSession();
        UtenteBean utente = (UtenteBean) session.getAttribute("user");
        
        if (utente == null || !utente.getTipo().equalsIgnoreCase("admin")) {
            response.sendRedirect("LoginServlet");
            return;
        }
        
        // Indirizziamo alla vista protetta dentro WEB-INF
        request.getRequestDispatcher("/WEB-INF/views/admin/aggiungiProdotto.jsp").forward(request, response);
    } 

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Ripetiamo il controllo di sicurezza per evitare POST diretti tramite Postman/Curl
        HttpSession session = request.getSession();
        UtenteBean utente = (UtenteBean) session.getAttribute("user");
        if (utente == null || !utente.getTipo().equalsIgnoreCase("admin")) {
            response.sendRedirect("LoginServlet");
            return;
        }

        request.setCharacterEncoding("UTF-8");

        // 1. Leggiamo i dati dal form
        String nome = request.getParameter("nome");
        String descrizione = request.getParameter("descrizione");
        float prezzo = Float.parseFloat(request.getParameter("prezzo"));

        // 2. Gestiamo l'immagine di copertina
        Part filePart = request.getPart("foto");
        String nomeOriginale = filePart.getSubmittedFileName();
        String nomeImmagineUnivoco = "default.jpg";

        if (nomeOriginale != null && !nomeOriginale.isEmpty()) {
            String estensione = "";
            int index = nomeOriginale.lastIndexOf('.');
            if (index > 0) {
                estensione = nomeOriginale.substring(index);
                nomeOriginale = nomeOriginale.substring(0, index).replaceAll("\\s+", "_"); 
            }
            nomeImmagineUnivoco = System.currentTimeMillis() + "_" + nomeOriginale + estensione;

            // A. Nel server Tomcat (Deployment)
            String serverPath = request.getServletContext().getRealPath("/img/prodotti");
            File serverDir = new File(serverPath);
            if (!serverDir.exists()) serverDir.mkdirs();
            File serverFile = new File(serverDir, nomeImmagineUnivoco);

            try (InputStream input = filePart.getInputStream()) {
                java.nio.file.Files.copy(input, serverFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                
                // B. Nel Workspace (Se il path è stato caricato correttamente dal file properties)
                if (workspacePath != null && !workspacePath.trim().isEmpty()) {
                    File workspaceDir = new File(workspacePath);
                    if (!workspaceDir.exists()) workspaceDir.mkdirs();
                    File workspaceFile = new File(workspaceDir, nomeImmagineUnivoco);
                    java.nio.file.Files.copy(serverFile.toPath(), workspaceFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }

        // 2b. Gestiamo la traccia audio Dry (Senza effetto)
        Part filePartDry = request.getPart("demoDry");
        String nomeOriginaleDry = (filePartDry != null) ? filePartDry.getSubmittedFileName() : null;
        String pathDemoDry = null;

        if (nomeOriginaleDry != null && !nomeOriginaleDry.isEmpty()) {
            String estensioneDry = "";
            int indexDry = nomeOriginaleDry.lastIndexOf('.');
            if (indexDry > 0) {
                estensioneDry = nomeOriginaleDry.substring(indexDry);
                nomeOriginaleDry = nomeOriginaleDry.substring(0, indexDry).replaceAll("\\s+", "_");
            }
            String nomeDryUnivoco = "dry_" + System.currentTimeMillis() + "_" + nomeOriginaleDry + estensioneDry;

            String serverPathUploads = request.getServletContext().getRealPath("/uploads");
            File uploadDir = new File(serverPathUploads);
            if (!uploadDir.exists()) uploadDir.mkdirs();
            File serverFileDry = new File(uploadDir, nomeDryUnivoco);

            try (InputStream input = filePartDry.getInputStream()) {
                java.nio.file.Files.copy(input, serverFileDry.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                pathDemoDry = "uploads/" + nomeDryUnivoco;
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }

        // 2c. Gestiamo la traccia audio Wet (Con effetto)
        Part filePartWet = request.getPart("demoWet");
        String nomeOriginaleWet = (filePartWet != null) ? filePartWet.getSubmittedFileName() : null;
        String pathDemoWet = null;

        if (nomeOriginaleWet != null && !nomeOriginaleWet.isEmpty()) {
            String estensioneWet = "";
            int indexWet = nomeOriginaleWet.lastIndexOf('.');
            if (indexWet > 0) {
                estensioneWet = nomeOriginaleWet.substring(indexWet);
                nomeOriginaleWet = nomeOriginaleWet.substring(0, indexWet).replaceAll("\\s+", "_");
            }
            String nomeWetUnivoco = "wet_" + System.currentTimeMillis() + "_" + nomeOriginaleWet + estensioneWet;

            String serverPathUploads = request.getServletContext().getRealPath("/uploads");
            File uploadDir = new File(serverPathUploads);
            if (!uploadDir.exists()) uploadDir.mkdirs();
            File serverFileWet = new File(uploadDir, nomeWetUnivoco);

            try (InputStream input = filePartWet.getInputStream()) {
                java.nio.file.Files.copy(input, serverFileWet.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                pathDemoWet = "uploads/" + nomeWetUnivoco;
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }

        // 3. Creiamo il Bean e lo passiamo al DAO
        ProdottoBean nuovoProdotto = new ProdottoBean();
        nuovoProdotto.setNome(nome);
        nuovoProdotto.setDescrizione(descrizione);
        nuovoProdotto.setPrezzo(prezzo);
        nuovoProdotto.setImmagine("img/prodotti/" + nomeImmagineUnivoco); 
        nuovoProdotto.setDemoDry(pathDemoDry);
        nuovoProdotto.setDemoWet(pathDemoWet);

        try {
            prodottoDAO.doSave(nuovoProdotto);
            System.out.println("✅ Prodotto aggiunto con successo!");
            response.sendRedirect("AggiungiProdottoServlet?messaggio=Prodotto aggiunto al catalogo con successo!");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("errore500.jsp");
        }
    }
}