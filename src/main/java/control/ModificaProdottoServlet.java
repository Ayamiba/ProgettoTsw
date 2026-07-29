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

@WebServlet("/ModificaProdottoServlet")
@MultipartConfig(
	    fileSizeThreshold = 1024 * 1024 * 5,  // 5MB di buffer in memoria
	    maxFileSize = 1024 * 1024 * 50,       // 50MB massimo per singolo file (WAV)
	    maxRequestSize = 1024 * 1024 * 120    // 120MB massimo per l'intera richiesta (Copertina + Dry + Wet)
	)
public class ModificaProdottoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProdottoDAO prodottoDAO;
    private String workspacePath;
    private String workspaceDemoPath; // Aggiunta variabile per i path audio

    public void init() throws ServletException {
        super.init();
        try {
            ConnectionPool.init(5);
        } catch (SQLException e) {
            System.out.println("Errore fatale: Impossibile avviare il Connection Pool!");
            e.printStackTrace();
        }
        prodottoDAO = new ProdottoDAO(); 
        
        // Caricamento del config.properties
        try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            if (input != null) {
                prop.load(input);
                workspacePath = prop.getProperty("upload.path.prodotti");
                workspaceDemoPath = prop.getProperty("upload.path.demo.prodotti"); // Caricamento del nuovo path
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    } 

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UtenteBean utente = (UtenteBean) session.getAttribute("user");
        
        // Sicurezza: Solo Admin
        if (utente == null || !utente.getTipo().equalsIgnoreCase("admin")) {
            response.sendRedirect("LoginServlet");
            return;
        }
        
        // Dobbiamo sapere quale prodotto modificare!
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect("ProfiloAdminServlet?errore=Nessun_ID_fornito");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            ProdottoBean prodottoDaModificare = prodottoDAO.doRetrieveByKey(id);
            
            if (prodottoDaModificare == null) {
                response.sendRedirect("ProfiloAdminServlet?errore=Prodotto_Non_Trovato");
                return;
            }
            
            // Passiamo il prodotto alla JSP per pre-compilare i campi
            request.setAttribute("prodotto", prodottoDaModificare);
            request.getRequestDispatcher("/WEB-INF/views/admin/modificaProdotto.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ProfiloAdminServlet?errore=Errore_Server");
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UtenteBean utente = (UtenteBean) session.getAttribute("user");
        if (utente == null || !utente.getTipo().equalsIgnoreCase("admin")) {
            response.sendRedirect("LoginServlet");
            return;
        }

        request.setCharacterEncoding("UTF-8");

        int idProdotto = Integer.parseInt(request.getParameter("idProdotto"));
        String nome = request.getParameter("nome");
        String descrizione = request.getParameter("descrizione");
        float prezzo = Float.parseFloat(request.getParameter("prezzo"));
    
        // 1. Gestione Immagine
        Part filePart = request.getPart("foto");
        String nomeOriginale = (filePart != null) ? filePart.getSubmittedFileName() : null;
        String nomeImmagineUnivoco = null;

        if (nomeOriginale != null && !nomeOriginale.isEmpty()) {
            String estensione = "";
            int index = nomeOriginale.lastIndexOf('.');
            if (index > 0) {
                estensione = nomeOriginale.substring(index);
                nomeOriginale = nomeOriginale.substring(0, index).replaceAll("\\s+", "_"); 
            }
            nomeImmagineUnivoco = System.currentTimeMillis() + "_" + nomeOriginale + estensione;
        }

        // 2. Gestione Audio Dry (Senza effetto)
        Part filePartDry = request.getPart("demoDry");
        String nomeOriginaleDry = (filePartDry != null) ? filePartDry.getSubmittedFileName() : null;
        String nomeDryUnivoco = null;

        if (nomeOriginaleDry != null && !nomeOriginaleDry.isEmpty()) {
            String estensioneDry = "";
            int indexDry = nomeOriginaleDry.lastIndexOf('.');
            if (indexDry > 0) {
                estensioneDry = nomeOriginaleDry.substring(indexDry);
                nomeOriginaleDry = nomeOriginaleDry.substring(0, indexDry).replaceAll("\\s+", "_");
            }
            nomeDryUnivoco = "dry_" + System.currentTimeMillis() + "_" + nomeOriginaleDry + estensioneDry;
        }

        // 3. Gestione Audio Wet (Con effetto)
        Part filePartWet = request.getPart("demoWet");
        String nomeOriginaleWet = (filePartWet != null) ? filePartWet.getSubmittedFileName() : null;
        String nomeWetUnivoco = null;

        if (nomeOriginaleWet != null && !nomeOriginaleWet.isEmpty()) {
            String estensioneWet = "";
            int indexWet = nomeOriginaleWet.lastIndexOf('.');
            if (indexWet > 0) {
                estensioneWet = nomeOriginaleWet.substring(indexWet);
                nomeOriginaleWet = nomeOriginaleWet.substring(0, indexWet).replaceAll("\\s+", "_");
            }
            nomeWetUnivoco = "wet_" + System.currentTimeMillis() + "_" + nomeOriginaleWet + estensioneWet;
        }

        try {
            ProdottoBean prodottoEsistente = prodottoDAO.doRetrieveByKey(idProdotto);
            if (prodottoEsistente == null) {
                response.sendRedirect("ProfiloAdminServlet?messaggio=Errore: ID prodotto non esistente!");
                return;
            }

            ProdottoBean prodottoAggiornato = new ProdottoBean();
            prodottoAggiornato.setIdProdotto(idProdotto);
            prodottoAggiornato.setNome(nome);
            prodottoAggiornato.setDescrizione(descrizione);
            prodottoAggiornato.setPrezzo(prezzo);

            // GESTIONE SALVATAGGIO IMMAGINE
            if (nomeImmagineUnivoco != null) {
                String serverPath = request.getServletContext().getRealPath("/img/prodotti");
                File serverDir = new File(serverPath);
                if (!serverDir.exists()) serverDir.mkdirs();
                File serverFile = new File(serverDir, nomeImmagineUnivoco);

                try (InputStream input = filePart.getInputStream()) {
                    java.nio.file.Files.copy(input, serverFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    
                    if (workspacePath != null && !workspacePath.trim().isEmpty()) {
                        File workspaceDir = new File(workspacePath);
                        if (!workspaceDir.exists()) workspaceDir.mkdirs();
                        File workspaceFile = new File(workspaceDir, nomeImmagineUnivoco);
                        java.nio.file.Files.copy(serverFile.toPath(), workspaceFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    }
                }
                prodottoAggiornato.setImmagine("img/prodotti/" + nomeImmagineUnivoco);
            } else {
                prodottoAggiornato.setImmagine(prodottoEsistente.getImmagine());
            }

            // GESTIONE SALVATAGGIO AUDIO DRY
            if (nomeDryUnivoco != null) {
                String serverPathUploads = request.getServletContext().getRealPath("/uploads/demoProdotti");
                File uploadDir = new File(serverPathUploads);
                if (!uploadDir.exists()) uploadDir.mkdirs();
                File serverFileDry = new File(uploadDir, nomeDryUnivoco);

                try (InputStream input = filePartDry.getInputStream()) {
                    java.nio.file.Files.copy(input, serverFileDry.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    
                    if (workspaceDemoPath != null && !workspaceDemoPath.trim().isEmpty()) {
                        File workspaceDir = new File(workspaceDemoPath);
                        if (!workspaceDir.exists()) workspaceDir.mkdirs();
                        File workspaceFile = new File(workspaceDir, nomeDryUnivoco);
                        java.nio.file.Files.copy(serverFileDry.toPath(), workspaceFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    }
                }
                prodottoAggiornato.setDemoDry("uploads/demoProdotti/" + nomeDryUnivoco);
            } else {
                prodottoAggiornato.setDemoDry(prodottoEsistente.getDemoDry());
            }

            // GESTIONE SALVATAGGIO AUDIO WET
            if (nomeWetUnivoco != null) {
                String serverPathUploads = request.getServletContext().getRealPath("/uploads/demoProdotti");
                File uploadDir = new File(serverPathUploads);
                if (!uploadDir.exists()) uploadDir.mkdirs();
                File serverFileWet = new File(uploadDir, nomeWetUnivoco);

                try (InputStream input = filePartWet.getInputStream()) {
                    java.nio.file.Files.copy(input, serverFileWet.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    
                    if (workspaceDemoPath != null && !workspaceDemoPath.trim().isEmpty()) {
                        File workspaceDir = new File(workspaceDemoPath);
                        if (!workspaceDir.exists()) workspaceDir.mkdirs();
                        File workspaceFile = new File(workspaceDir, nomeWetUnivoco);
                        java.nio.file.Files.copy(serverFileWet.toPath(), workspaceFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    }
                }
                prodottoAggiornato.setDemoWet("uploads/demoProdotti/" + nomeWetUnivoco);
            } else {
                prodottoAggiornato.setDemoWet(prodottoEsistente.getDemoWet());
            }

            prodottoDAO.doUpdate(prodottoAggiornato);
            response.sendRedirect("ModificaProdottoServlet?id=" + idProdotto + "&messaggio=Prodotto aggiornato con successo!");

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("errore500.jsp");
        }
    }
}