package control;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.utente.UtenteBean;

@WebServlet("/DownloadLavoroServlet")
public class DownloadLavoroServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. Controllo Sicurezza
        HttpSession session = request.getSession();
        UtenteBean utente = (UtenteBean) session.getAttribute("user");
        
        if (utente == null) {
            response.sendRedirect("LoginServlet");
            return;
        }

        String idOrdineStr = request.getParameter("idOrdine");
        
        if (idOrdineStr == null || idOrdineStr.trim().isEmpty()) {
            response.sendRedirect("HomeServlet");
            return;
        }

        try {
            // 2. Leggiamo il file properties tramite il ClassLoader (perché è in src/main/java)
            Properties prop = new Properties();
            InputStream propInput = DownloadLavoroServlet.class.getClassLoader().getResourceAsStream("config.properties");
            
            if (propInput == null) {
                System.out.println("ERRORE GRAVE: File config.properties non trovato nel Classpath!");
                response.sendRedirect("ProfiloServlet?messaggio=Errore interno del server (config mancante).");
                return;
            }
            
            prop.load(propInput);
            String cartellaConsegne = prop.getProperty("upload.path.consegne");
            propInput.close();

            System.out.println("🔄 Cerco file dell'ordine " + idOrdineStr + " in: " + cartellaConsegne);

            File directory = new File(cartellaConsegne);
            File fileDaScaricare = null;

            // 3. Cerchiamo il file che inizia con l'ID corretto
            if (directory.exists() && directory.isDirectory()) {
                File[] files = directory.listFiles();
                if (files != null) {
                    for (File f : files) {
                        if (f.getName().startsWith("master_ordine_" + idOrdineStr + ".")) {
                            fileDaScaricare = f;
                            break;
                        }
                    }
                }
            }

            // 4. Se il file esiste, forziamo il download
            if (fileDaScaricare != null && fileDaScaricare.exists()) {
                
                System.out.println("✅ File trovato! Inizio il download di: " + fileDaScaricare.getName());
                
                // Impostiamo gli header HTTP per forzare la finestra "Salva con nome..."
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileDaScaricare.getName() + "\"");
                response.setContentLength((int) fileDaScaricare.length());

                // Trasferiamo i byte dal file fisico al browser dell'utente
                try (FileInputStream inStream = new FileInputStream(fileDaScaricare);
                     OutputStream outStream = response.getOutputStream()) {
                    
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    
                    while ((bytesRead = inStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, bytesRead);
                    }
                }
                
            } else {
                System.out.println("❌ File NON trovato nella cartella!");
                // Rimandiamo indietro l'utente (N.B. per vedere questo messaggio servirà aggiungere il Toast anche in profiloUtente.jsp)
                response.sendRedirect("ProfiloServlet?messaggio=Errore: File non ancora disponibile per il download.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ProfiloServlet?messaggio=Errore critico durante il download.");
        }
    }
}