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

@WebServlet("/AscoltaTracciaServlet")
public class AscoltaTracciaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private String uploadPath;

    @Override
    public void init() throws ServletException {
        super.init();
        Properties prop = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                prop.load(input);
                this.uploadPath = prop.getProperty("upload.path.audio");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nomeFileUnico = request.getParameter("file");
        if (nomeFileUnico == null || nomeFileUnico.isEmpty() || this.uploadPath == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        File fileAudio = new File(this.uploadPath + File.separator + nomeFileUnico);
        if (!fileAudio.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Impostiamo il corretto Content-Type in base all'estensione del file
        String nomeMinuscolo = nomeFileUnico.toLowerCase();
        if (nomeMinuscolo.endsWith(".mp3")) {
            response.setContentType("audio/mpeg");
        } else if (nomeMinuscolo.endsWith(".wav")) {
            response.setContentType("audio/wav");
        } else if (nomeMinuscolo.endsWith(".aif") || nomeMinuscolo.endsWith(".aiff")) {
            response.setContentType("audio/x-aiff");
        } else {
            response.setContentType("application/octet-stream");
        }

        response.setContentLength((int) fileAudio.length());

        // Inviamo il file stream al browser
        try (FileInputStream inStream = new FileInputStream(fileAudio);
             OutputStream outStream = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
        }
    }
}