package control;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.Properties; // IMPORT FONDAMENTALE

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import model.ordine.OrdineDAO;
import model.utente.UtenteBean;

@WebServlet("/ConsegnaLavoroServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
                 maxFileSize = 1024 * 1024 * 50,      // 50MB
                 maxRequestSize = 1024 * 1024 * 60)   // 60MB totali
public class ConsegnaLavoroServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. Controllo sicurezza sessione
        HttpSession session = request.getSession();
        UtenteBean utente = (UtenteBean) session.getAttribute("user");
        
        if (utente == null || !utente.getTipo().equalsIgnoreCase("professionista")) {
            response.sendRedirect("LoginServlet");
            return;
        }

        String idOrdineStr = request.getParameter("idOrdine");
        
        try {
            int idOrdine = Integer.parseInt(idOrdineStr);
            
            // 2. Estrazione del file audio caricato
            Part filePart = request.getPart("tracciaFinita");
            if (filePart != null && filePart.getSize() > 0) {
                
                // --- INIZIO MODIFICA: LETTURA DAL CONFIG.PROPERTIES ---
                Properties prop = new Properties();
                InputStream propInput = ConsegnaLavoroServlet.class.getClassLoader().getResourceAsStream("config.properties");
                
                if (propInput == null) {
                    System.out.println("ERRORE GRAVE: File config.properties non trovato in WEB-INF!");
                    response.sendRedirect("ProfiloProfessionistaServlet?messaggio=Errore critico: configurazione server mancante.");
                    return;
                }
                
                prop.load(propInput);
                // Leggiamo la nuova chiave che hai appena aggiunto al file properties
                String baseUploadPath = prop.getProperty("upload.path.consegne");
                propInput.close();
                // --- FINE MODIFICA ---

                // Se la cartella indicata nel properties non esiste, Java la crea da solo!
                File fileSaveDir = new File(baseUploadPath);
                if (!fileSaveDir.exists()) {
                    fileSaveDir.mkdirs();
                }

                // Generiamo un nome univoco per il file
                String estensione = getEstensione(filePart);
                String nomeFileFinale = "master_ordine_" + idOrdine + estensione;
                
                // Salvataggio fisico del file nel percorso SICURO
                File fileSalvato = new File(baseUploadPath + File.separator + nomeFileFinale);
                try (InputStream input = filePart.getInputStream()) {
                    Files.copy(input, fileSalvato.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }

                System.out.println("✅ SUCCESSO: Traccia salvata in -> " + fileSalvato.getAbsolutePath());

                // 3. Aggiornamento dello stato nel Database
                OrdineDAO ordineDAO = new OrdineDAO();
                boolean successo = ordineDAO.completaOrdine(idOrdine);
                
                if(successo) {
                    response.sendRedirect("ProfiloProfessionistaServlet?messaggio=Lavoro per l'ordine " + idOrdine + " consegnato con successo!");
                } else {
                    response.sendRedirect("ProfiloProfessionistaServlet?messaggio=Errore: File caricato ma impossibile aggiornare lo stato dell'ordine.");
                }
                
            } else {
                response.sendRedirect("ProfiloProfessionistaServlet?messaggio=Errore: Nessun file audio selezionato per l'upload.");
            }

        } catch (NumberFormatException | SQLException | IOException e) {
            e.printStackTrace();
            response.sendRedirect("ProfiloProfessionistaServlet?messaggio=Errore di sistema durante la consegna.");
        }
    }

    // Metodo di supporto per estrarre l'estensione originale del file caricato
    private String getEstensione(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                String filename = token.substring(token.indexOf("=") + 2, token.length() - 1);
                if(filename.contains(".")) {
                    return filename.substring(filename.lastIndexOf("."));
                }
            }
        }
        return ".wav"; // Fallback di sicurezza
    }
}