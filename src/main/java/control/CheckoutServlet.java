package control;

import model.carrello.CarrelloDAO;
import model.contenuto.ContenutoBean;
import model.contenuto.ContenutoDAO;
import model.ordine.OrdineBean;
import model.ordine.OrdineDAO;
import model.prodotto.ProdottoBean;
import model.utente.UtenteBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        // Se l'utente non è loggato, lo mandiamo al login
        if (session.getAttribute("user") == null) {
            response.sendRedirect("LoginServlet?errore=devi_accedere");
            return;
        }
        
        // Altrimenti, mostriamo la pagina di Checkout (il file JSP)
        request.getRequestDispatcher("/WEB-INF/views/user/checkout.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UtenteBean utente = (UtenteBean) session.getAttribute("user");
        
        // 1. Recuperiamo i plugin dal carrello in sessione
        @SuppressWarnings("unchecked")
        List<ProdottoBean> carrello = (List<ProdottoBean>) session.getAttribute("carrelloProdotti");

        // Controlli di sicurezza
        if (utente == null) {
            response.sendRedirect("LoginServlet");
            return;
        }
        if (carrello == null || carrello.isEmpty()) {
            response.sendRedirect("CarrelloServlet?errore=vuoto");
            return;
        }

        // 2. Recuperiamo le scelte dell'utente dal form di Checkout (Carta e Traccia)
        String idTracciaStr = request.getParameter("idTraccia");
        String numeroCartaStr = request.getParameter("numeroCarta");

        if (idTracciaStr == null || numeroCartaStr == null) {
            response.sendRedirect("CarrelloServlet?errore=dati_mancanti");
            return;
        }

        try {
            int idTraccia = Integer.parseInt(idTracciaStr);
            long numeroCarta = Long.parseLong(numeroCartaStr.replaceAll("\\s+", "")); // Rimuove eventuali spazi

            // 3. Calcolo del totale SICURO lato server (non fidarsi mai dell'HTML!)
            float totale = 0.0f;
            for (ProdottoBean p : carrello) {
                totale += p.getPrezzo();
            }

            // 4. Inizializziamo il nuovo Ordine
            OrdineBean nuovoOrdine = new OrdineBean();
            nuovoOrdine.setDataOrdine(new Date(System.currentTimeMillis())); // Data odierna esatta
            nuovoOrdine.setTotale(totale);
            nuovoOrdine.setStato("In Lavorazione"); // Va dritto ai professionisti!
            nuovoOrdine.setDescrizione("Acquisto catena plugin tramite carrello");
            nuovoOrdine.setfKTraccia(idTraccia);
            nuovoOrdine.setfKMetodoPagamento(numeroCarta);

            OrdineDAO ordineDAO = new OrdineDAO();
            
            // IL FAMOSO METODO! Salviamo e ci facciamo restituire il numero
            int idOrdineGenerato = ordineDAO.doSaveGetId(nuovoOrdine); 

            if (idOrdineGenerato > 0) {
                // 5. Ora che abbiamo l'ID, salviamo i plugin nella tabella Contenuto!
                ContenutoDAO contenutoDAO = new ContenutoDAO();
                int posizione = 1; // Serve per l'attributo posizione_catena

                for (ProdottoBean prodotto : carrello) {
                    ContenutoBean contenuto = new ContenutoBean();
                    contenuto.setFkOrdine(idOrdineGenerato);
                    contenuto.setFkProdotto(prodotto.getIdProdotto());
                    contenuto.setPosizioneCatena(posizione);
                    
                    contenutoDAO.doSave(contenuto);
                    posizione++;
                }

                // 6. Tutto è salvato! Possiamo svuotare il carrello.
                session.removeAttribute("carrelloProdotti"); // Rimuoviamo dalla sessione
                CarrelloDAO carrelloDAO = new CarrelloDAO();
                carrelloDAO.doEmptyCarrello(utente.getEmail()); // Svuotiamo il database 

                response.sendRedirect("ordineCompletato.jsp?id=" + idOrdineGenerato);
                
            } else {
                response.sendRedirect("CarrelloServlet?errore=creazione_fallita");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("CarrelloServlet?errore=eccezione");
        }
    }
}