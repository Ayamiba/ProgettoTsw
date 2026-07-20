package control;

import model.carrello.CarrelloDAO;
import model.contenuto.ContenutoBean;
import model.contenuto.ContenutoDAO;
import model.ordine.OrdineBean;
import model.ordine.OrdineDAO;
import model.prodotto.ProdottoBean;
import model.utente.UtenteBean;
import model.tracciaAudio.TracciaAudioBean;
import model.tracciaAudio.TracciaAudioDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.util.List;
import java.util.Properties;

@WebServlet("/CheckoutServlet")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
    maxFileSize = 1024 * 1024 * 50,       // 50MB
    maxRequestSize = 1024 * 1024 * 100    // 100MB
)
public class CheckoutServlet extends HttpServlet {
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
                File uploadDir = new File(this.uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdirs();
            }
        } catch (Exception e) {
            System.err.println("Impossibile caricare config.properties per l'upload audio in CheckoutServlet.");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            response.sendRedirect("LoginServlet?errore=devi_accedere");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/views/user/checkout.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UtenteBean utente = (UtenteBean) session.getAttribute("user");
        
        @SuppressWarnings("unchecked")
        List<ProdottoBean> carrello = (List<ProdottoBean>) session.getAttribute("carrelloProdotti");

        // 1. Controlli di sicurezza base
        if (utente == null) {
            response.sendRedirect("LoginServlet");
            return;
        }
        if (carrello == null || carrello.isEmpty()) {
            response.sendRedirect("CarrelloServlet?errore=vuoto");
            return;
        }

        try {
            // 2. Lettura dei parametri (Ora funziona grazie a @MultipartConfig)
            String numeroCartaStr = request.getParameter("numeroCarta");
            if (numeroCartaStr == null) {
                response.sendRedirect("CarrelloServlet?errore=dati_mancanti");
                return;
            }
            long numeroCarta = Long.parseLong(numeroCartaStr.replaceAll("\\s+", ""));

            // Lettura Descrizione personalizzata
            String descrizioneOrdine = request.getParameter("descrizioneOrdine");
            if (descrizioneOrdine == null || descrizioneOrdine.trim().isEmpty()) {
                descrizioneOrdine = "Nessuna istruzione particolare.";
            }

            // 3. Gestione Traccia Audio (Cloud vs Nuova)
            int idTracciaFinale = -1;
            String sorgenteTraccia = request.getParameter("sorgenteTraccia");

            if ("cloud".equals(sorgenteTraccia)) {
                String idTracciaCloud = request.getParameter("idTracciaCloud");
                if (idTracciaCloud != null) {
                    idTracciaFinale = Integer.parseInt(idTracciaCloud);
                }
            } else if ("nuova".equals(sorgenteTraccia)) {
                Part filePart = request.getPart("nuovaTracciaFile");
                if (filePart != null && filePart.getSize() > 0) {
                    // Logica di upload presa dalla tua CaricaTracciaLiberaServlet
                    String nomeFileOriginale = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                    String nomeFileUnico = System.currentTimeMillis() + "_" + nomeFileOriginale;
                    String percorsoFileFisico = this.uploadPath + File.separator + nomeFileUnico;

                    try (InputStream input = filePart.getInputStream()) {
                        Files.copy(input, Paths.get(percorsoFileFisico), StandardCopyOption.REPLACE_EXISTING);
                        
                        TracciaAudioBean nuovaTraccia = new TracciaAudioBean();
                        nuovaTraccia.setNomeFile(nomeFileOriginale);
                        nuovaTraccia.setPercorsoFile(nomeFileUnico);
                        nuovaTraccia.setCheck(false);
                        nuovaTraccia.setfKUtente(utente.getEmail());

                        TracciaAudioDAO tracciaDAO = new TracciaAudioDAO();
                        // Otteniamo subito l'ID della traccia appena caricata!
                        idTracciaFinale = tracciaDAO.doSaveGetId(nuovaTraccia); 
                    }
                }
            }

            // Controllo se abbiamo ottenuto un ID traccia valido
            if (idTracciaFinale <= 0) {
                response.sendRedirect("CarrelloServlet?errore=traccia_mancante");
                return;
            }

            // 4. Calcolo Totale
            float totale = 0.0f;
            for (ProdottoBean p : carrello) { totale += p.getPrezzo(); }

            // 5. Creazione Ordine
            OrdineBean nuovoOrdine = new OrdineBean();
            nuovoOrdine.setDataOrdine(new Date(System.currentTimeMillis()));
            nuovoOrdine.setTotale(totale);
            nuovoOrdine.setStato("In Lavorazione");
            nuovoOrdine.setDescrizione(descrizioneOrdine); // Salviamo le istruzioni utente!
            nuovoOrdine.setfKTraccia(idTracciaFinale);
            nuovoOrdine.setfKMetodoPagamento(numeroCarta);

            OrdineDAO ordineDAO = new OrdineDAO();
            int idOrdineGenerato = ordineDAO.doSaveGetId(nuovoOrdine); 

            if (idOrdineGenerato > 0) {
                // 6. Salvataggio della Catena Plugin in Contenuto
                ContenutoDAO contenutoDAO = new ContenutoDAO();

                for (ProdottoBean prodotto : carrello) {
                    ContenutoBean contenuto = new ContenutoBean();
                    contenuto.setFkOrdine(idOrdineGenerato);
                    contenuto.setFkProdotto(prodotto.getIdProdotto());
                    
                    // Recuperiamo la posizione esatta inserita dall'utente nel form!
                    int posizioneCatena = 1;
                    String posParam = request.getParameter("posizione_" + prodotto.getIdProdotto());
                    if (posParam != null && !posParam.trim().isEmpty()) {
                        posizioneCatena = Integer.parseInt(posParam);
                    }
                    contenuto.setPosizioneCatena(posizioneCatena);
                    
                    contenutoDAO.doSave(contenuto);
                }

                // 7. Svuotamento Carrello
                session.removeAttribute("carrelloProdotti");
                CarrelloDAO carrelloDAO = new CarrelloDAO();
                carrelloDAO.doEmptyCarrello(utente.getEmail()); 

             // 8. Reindirizzamento alla VITTORIA tramite la nuova Servlet
                response.sendRedirect("OrdineCompletatoServlet?id=" + idOrdineGenerato);
                
            } else {
                response.sendRedirect("CarrelloServlet?errore=creazione_ordine_fallita");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("CarrelloServlet?errore=eccezione_imprevista");
        }
    }
}