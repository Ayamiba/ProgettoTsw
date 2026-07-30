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

import model.ConnectionPool;
import model.ordine.OrdineBean;
import model.ordine.OrdineDAO;
import model.utente.UtenteBean;

@WebServlet("/DettaglioOrdineAdminServlet")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 5,  // 5MB
    maxFileSize = 1024 * 1024 * 50,       // 50MB (per mix/master finali)
    maxRequestSize = 1024 * 1024 * 100    // 100MB
)
public class DettaglioOrdineAdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrdineDAO ordineDAO;
    private String workspaceConsegnePath;

    public void init() throws ServletException {
        super.init();
        try {
            ConnectionPool.init(5);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ordineDAO = new OrdineDAO(); 
        
        // Carichiamo il path per le consegne finali dal config.properties
        try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            if (input != null) {
                prop.load(input);
                workspaceConsegnePath = prop.getProperty("upload.path.consegne");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    } 

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UtenteBean utente = (UtenteBean) session.getAttribute("user");
        
        // Sicurezza: Solo admin
        if (utente == null || !utente.getTipo().equalsIgnoreCase("admin")) {
            response.sendRedirect("LoginServlet");
            return;
        }
        
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
        	response.sendRedirect("GestioneOrdiniServlet?errore=Nessun_ID_Ordine");
            return;
        }
        
        try {
            int idOrdine = Integer.parseInt(idStr);
            // Uso il tuo metodo doRetrieveByKey
            OrdineBean ordine = ordineDAO.doRetrieveByKey(idOrdine);
            
            if (ordine == null) {
            	response.sendRedirect("GestioneOrdiniServlet?errore=Ordine_Non_Trovato");
                return;
            }
            
            String emailCliente = ordineDAO.doRetrieveEmailClienteByOrdine(idOrdine);
            request.setAttribute("emailCliente", emailCliente);

            request.setAttribute("ordine", ordine);
            request.getRequestDispatcher("/WEB-INF/views/admin/dettaglioOrdineAdmin.jsp").forward(request, response);
            
        } catch (Exception e) {
        
            System.out.println("⬇️⬇️ ERRORE FATALE IN DETTAGLIO ORDINE ⬇️⬇️");
            e.printStackTrace();
            System.out.println("⬆️⬆️ FINE ERRORE ⬆️⬆️");
            response.sendRedirect("GestioneOrdiniServlet?errore=Errore_Server");
        
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
        
        String action = request.getParameter("action");
        int idOrdine = Integer.parseInt(request.getParameter("idOrdine"));
        
        try {
        	// AZIONE 1: AGGIORNAMENTO DELLO STATO (Senza file)
            if ("aggiornaStato".equals(action)) {
                String nuovoStato = request.getParameter("statoOrdine");
                
                // 1. Recupero l'ordine dal DAO
                OrdineBean ordineDaAggiornare = ordineDAO.doRetrieveByKey(idOrdine);
                if(ordineDaAggiornare != null) {
                    
                    // 2. Modifico lo stato
                    ordineDaAggiornare.setStato(nuovoStato);
                    
                    // 3. LOGICA CHIAVE: Se lo rimetto "In attesa", cancello il professionista
                    if ("In Attesa".equals(nuovoStato) || "In attesa".equals(nuovoStato)) {
                        ordineDaAggiornare.setfkEmailProfessionista(null);
                    }
                    
                    // 4. Salvo nel database
                    ordineDAO.doUpdate(ordineDaAggiornare);
                }
                
                response.sendRedirect("DettaglioOrdineAdminServlet?id=" + idOrdine + "&messaggio=Stato ordine aggiornato!");
            }
            // AZIONE 2: CONSEGNA FILE FINALE (Upload)
            else if ("consegnaFile".equals(action)) {
                Part filePart = request.getPart("fileConsegna");
                String nomeOriginale = (filePart != null) ? filePart.getSubmittedFileName() : null;
                
                if (nomeOriginale != null && !nomeOriginale.isEmpty()) {
                    String estensione = "";
                    int index = nomeOriginale.lastIndexOf('.');
                    if (index > 0) {
                        estensione = nomeOriginale.substring(index);
                        nomeOriginale = nomeOriginale.substring(0, index).replaceAll("\\s+", "_"); 
                    }
                    String nomeFileUnivoco = "consegna_" + idOrdine + "_" + System.currentTimeMillis() + estensione;
                    
                    // Salvataggio Server Tomcat
                    String serverPath = request.getServletContext().getRealPath("/uploads/consegne");
                    File serverDir = new File(serverPath);
                    if (!serverDir.exists()) serverDir.mkdirs();
                    File serverFile = new File(serverDir, nomeFileUnivoco);

                    try (InputStream input = filePart.getInputStream()) {
                        java.nio.file.Files.copy(input, serverFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                        
                        // Salvataggio Workspace Permanente (dal config.properties)
                        if (workspaceConsegnePath != null && !workspaceConsegnePath.trim().isEmpty()) {
                            File workspaceDir = new File(workspaceConsegnePath);
                            if (!workspaceDir.exists()) workspaceDir.mkdirs();
                            File workspaceFile = new File(workspaceDir, nomeFileUnivoco);
                            java.nio.file.Files.copy(serverFile.toPath(), workspaceFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                        }
                    }
                    
                    String pathConsegna = "uploads/consegne/" + nomeFileUnivoco;
                    
                    // Uso il TUO metodo completaOrdine: aggiorna lo stato a "Completato" e salva il path!
                    ordineDAO.completaOrdine(idOrdine, pathConsegna);
                    
                    response.sendRedirect("DettaglioOrdineAdminServlet?id=" + idOrdine + "&messaggio=File consegnato e ordine completato!");
                } else {
                    response.sendRedirect("DettaglioOrdineAdminServlet?id=" + idOrdine + "&errore=Nessun file selezionato.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("DettaglioOrdineAdminServlet?id=" + idOrdine + "&errore=Errore di sistema durante il salvataggio.");
        }
    }
}