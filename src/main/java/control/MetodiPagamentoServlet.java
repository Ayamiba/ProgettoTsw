package control;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.utente.UtenteBean;

@WebServlet("/MetodiPagamentoServlet")
public class MetodiPagamentoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public MetodiPagamentoServlet() {
        super();
    }
    
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // Inizializza il pool di connessioni con 5 connessioni
            model.ConnectionPool.init(5);
        } catch (SQLException e) {
            System.out.println("Errore nell'inizializzazione del Connection Pool in MetodiPagamentoServlet!");
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UtenteBean utente = (UtenteBean) session.getAttribute("user");

        // Controllo di sicurezza: se l'utente non è loggato, va al login
        if (utente == null) {
            response.sendRedirect("LoginServlet");
            return;
        }

        String azione = request.getParameter("azione");
        if (azione != null && azione.equals("aggiungi")) {
            request.getRequestDispatcher("/WEB-INF/views/user/aggiungiMetodoPagamento.jsp").forward(request, response);
        } else {
            // Di default, se non viene specificata l'azione, rimandiamo al profilo
            response.sendRedirect("ProfiloServlet");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        model.utente.UtenteBean utente = (model.utente.UtenteBean) session.getAttribute("user");

        if (utente == null) {
            response.sendRedirect("LoginServlet");
            return;
        }

        try {
            // 1. Recupero e pulizia dei dati
            String intestatario = request.getParameter("intestatario");
            String numeroCartaStr = request.getParameter("numeroCarta").replace("-", "");
            String scadenza = request.getParameter("scadenza");
            int cvv = Integer.parseInt(request.getParameter("cvv"));
            long numeroCarta = Long.parseLong(numeroCartaStr);
            
            // Separa nome e cognome
            String[] partiNome = intestatario.trim().split("\\s+", 2);
            String nome = partiNome[0];
            String cognome = (partiNome.length > 1) ? partiNome[1] : "";

         // 2. Creazione del Bean per la Carta
            model.metodoPagamento.MetodoPagamentoBean nuovaCarta = new model.metodoPagamento.MetodoPagamentoBean();
            nuovaCarta.setNumeroCarta(numeroCarta);
            nuovaCarta.setCvv(cvv);
            nuovaCarta.setNome(nome);
            nuovaCarta.setCognome(cognome);
            nuovaCarta.setScadenza(scadenza);

            model.metodoPagamento.MetodoPagamentoDAO pagDAO = new model.metodoPagamento.MetodoPagamentoDAO();
            
            // Salvataggio pulito: se c'è un duplicato, l'IGNORE in SQL lo gestisce in silenzio!
            pagDAO.doSave(nuovaCarta);

            // 3. Creazione del collegamento nella tabella Utilizzo
            model.utilizzo.UtilizzoBean utilizzo = new model.utilizzo.UtilizzoBean();
            utilizzo.setFkUtente(utente.getEmail());
            utilizzo.setFkMetodoPagamento(numeroCarta);
            
            model.utilizzo.UtilizzoDAO utilizzoDAO = new model.utilizzo.UtilizzoDAO();
            utilizzoDAO.doSave(utilizzo);
            
            // Successo! Ritorno al profilo
            response.sendRedirect("ProfiloServlet?success=pagamento");
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errore", "Errore nel salvataggio. Controlla i dati inseriti.");
            request.getRequestDispatcher("/WEB-INF/views/user/aggiungiMetodoPagamento.jsp").forward(request, response);
        }
    
    }
}