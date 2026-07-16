package control;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.servlet.http.HttpSession;

import model.tracciaAudio.TracciaAudioBean;
import model.tracciaAudio.TracciaAudioDAO;
import model.utente.UtenteBean;

@WebServlet("/CaricaTracciaLiberaServlet")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,
    maxFileSize = 1024 * 1024 * 50,
    maxRequestSize = 1024 * 1024 * 100
)
public class CaricaTracciaLiberaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private String uploadPath;

    @Override
    public void init() throws ServletException {
        super.init();
        Properties prop = new Properties();
        
        // Carichiamo il file properties dal classpath (es. se posizionato in src/main/resources)
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new ServletException("Impossibile trovare il file config.properties nel Classpath!");
            }
            prop.load(input);
            // Recuperiamo la proprietà specifica per l'audio
            this.uploadPath = prop.getProperty("upload.path.audio");
            
            if (this.uploadPath == null || this.uploadPath.isEmpty()) {
                throw new ServletException("La proprietà 'upload.path.audio' non è configurata!");
            }
            
            // Creiamo la cartella fisica se non esiste ancora sul PC dello sviluppatore
            File uploadDir = new File(this.uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
        } catch (Exception e) {
            System.err.println("Errore durante il caricamento della configurazione!");
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UtenteBean utente = (UtenteBean) session.getAttribute("user");

        if (utente == null) {
            response.sendRedirect("LoginServlet");
            return;
        }

        Part filePart = request.getPart("tracciaAudio");
        if (filePart == null || filePart.getSize() == 0) {
            response.sendRedirect("ProfiloServlet?error=upload_empty");
            return;
        }

        String nomeFileOriginale = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String nomeFileUnico = System.currentTimeMillis() + "_" + nomeFileOriginale;

        // Utilizziamo il percorso letto dinamicamente dal file properties locale!
        String percorsoFileFisico = this.uploadPath + File.separator + nomeFileUnico;
        
        // Per il database, puoi salvare il nome univoco del file. 
        // Sarà poi una Servlet di download a servire il file leggendolo dal percorso properties.
        String percorsoDatabase = nomeFileUnico; 

        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, Paths.get(percorsoFileFisico), StandardCopyOption.REPLACE_EXISTING);
            
            TracciaAudioBean nuovaTraccia = new TracciaAudioBean();
            nuovaTraccia.setNomeFile(nomeFileOriginale);
            nuovaTraccia.setPercorsoFile(percorsoDatabase);
            nuovaTraccia.setCheck(false);
            nuovaTraccia.setfKUtente(utente.getEmail());

            TracciaAudioDAO tracciaDAO = new TracciaAudioDAO();
            tracciaDAO.doSave(nuovaTraccia);

            response.sendRedirect("ProfiloServlet?success=upload");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ProfiloServlet?error=upload_failed");
        }
    }
}