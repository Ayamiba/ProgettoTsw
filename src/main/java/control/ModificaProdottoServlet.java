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

@WebServlet("/ModificaProdottoServlet")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,
    maxFileSize = 1024 * 1024 * 10,
    maxRequestSize = 1024 * 1024 * 50
)

public class ModificaProdottoServlet extends HttpServlet {
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

    // Se l'admin digita l'URL della servlet, lo mandiamo direttamente alla pagina del form
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/ModificaProdotto.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        // Leggiamo le cose che l'admin ha scritto nel form
        int idProdotto = Integer.parseInt(request.getParameter("idProdotto"));
        String nome = request.getParameter("nome");
        String descrizione = request.getParameter("descrizione");
        float prezzo = Float.parseFloat(request.getParameter("prezzo"));
    
        //per il file immagine
        Part filePart = request.getPart("foto");
        String nomeImmagine = filePart.getSubmittedFileName();
        String nomeOriginale = filePart.getSubmittedFileName();
        String nomeImmagineUnivoco = null;

        //Logica di modifica del nome per prevenire duplicati
        if (nomeOriginale != null && !nomeOriginale.isEmpty()) {
            String estensione = "";
            int index = nomeOriginale.lastIndexOf('.');
            if (index > 0) {
                estensione = nomeOriginale.substring(index); // Es: ".jpg"
                // Puliamo il nome da eventuali spazi bianchi per evitare problemi negli URL HTML
                nomeOriginale = nomeOriginale.substring(0, index).replaceAll("\\s+", "_"); 
            }
            // Generiamo il nuovo nome unendo il timestamp corrente in millisecondi
            nomeImmagineUnivoco = System.currentTimeMillis() + "_" + nomeOriginale + estensione;
        }

        try {
            // Controlliamo se il prodotto con quell'ID esiste davvero nel DB
            ProdottoBean prodottoEsistente = prodottoDAO.doRetrieveByKey(idProdotto);
            if (prodottoEsistente == null) {
                response.sendRedirect("ModificaProdotto.jsp?messaggio=Errore: ID prodotto non esistente!");
                return;
            }

            // Se esiste il prodotto, creiamo il bean aggiornato
            ProdottoBean prodottoAggiornato = new ProdottoBean();
            prodottoAggiornato.setIdProdotto(idProdotto);
            prodottoAggiornato.setNome(nome);
            prodottoAggiornato.setDescrizione(descrizione);
            prodottoAggiornato.setPrezzo(prezzo);

         // Se ha caricato una nuova foto la salviamo usando la Tecnica del Doppio Salvataggio
            if (nomeImmagineUnivoco != null) {
                
                // -------------------------------------------------------------
                // PERCORSO 1: Cartella del Server (Per vedere la foto SUBITO)
                // -------------------------------------------------------------
                String serverPath = request.getServletContext().getRealPath("/img/prodotti");
                File serverDir = new File(serverPath);
                if (!serverDir.exists()) serverDir.mkdirs();
                File serverFile = new File(serverDir, nomeImmagineUnivoco);

                // -------------------------------------------------------------
                // PERCORSO 2: Il tuo PC (Per NON PERDERE la foto al riavvio)
                // -------------------------------------------------------------
                String workspacePath = "C:/Users/gvarr/OneDrive/Desktop/Projects/TSW/ProgettoTsw/src/main/webapp/img/prodotti";
                File workspaceDir = new File(workspacePath);
                if (!workspaceDir.exists()) workspaceDir.mkdirs();
                File workspaceFile = new File(workspaceDir, nomeImmagineUnivoco);

                // Eseguiamo il salvataggio
                try (java.io.InputStream input = filePart.getInputStream()) {
                    
                    // 1. Salviamo prima nel server
                    java.nio.file.Files.copy(input, serverFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    
                    // 2. Poi cloniamo il file dal server al tuo Workspace
                    java.nio.file.Files.copy(serverFile.toPath(), workspaceFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    
                    // Stampiamo nella console per avere la certezza assoluta
                    System.out.println("✅ MAGIA RIUSCITA!");
                    System.out.println("-> Salvata per il web in: " + serverFile.getAbsolutePath());
                    System.out.println("-> Salvata per sempre in: " + workspaceFile.getAbsolutePath());
                    
                } catch (IOException e) {
                    System.out.println("❌ Errore critico durante il doppio salvataggio.");
                    e.printStackTrace();
                    throw e;
                }

                // Aggiorniamo il Bean con il nome
                prodottoAggiornato.setImmagine(nomeImmagineUnivoco);

            } else {
                // Altrimenti teniamo quella vecchia
                prodottoAggiornato.setImmagine(prodottoEsistente.getImmagine());
            }

            // Facciamo l'update nel DB usando il metodo che sta in prodottoDAO
            prodottoDAO.doUpdate(prodottoAggiornato);
            response.sendRedirect("ModificaProdotto.jsp?messaggio=Prodotto aggiornato con successo!");

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("errore500.jsp");
        }
    }
}