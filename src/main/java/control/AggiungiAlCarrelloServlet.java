//questa Servlet ci servirà per aggiungere i prodotti al carrello in base al fatto che l'utente sia loggato o meno
//in LoginServlet poi ci occuperemo di trasferire che cose che stanno nei cookie dell'utente quando si registrerà
package control;
import model.carrello.*;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/AggiungiAlCarrelloServlet")
public class AggiungiAlCarrelloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        model.utente.UtenteBean utenteLoggato = (model.utente.UtenteBean) session.getAttribute("user"); //se session.getAttribute è uguale a null significa che l'utente non è loggato
        
        String idProdottoStr = request.getParameter("idProdotto"); // Riceve l'id del prodotto cliccato
        
        if (idProdottoStr != null && !idProdottoStr.trim().isEmpty()) {
            if (utenteLoggato != null) {
                // CASO A: L'utente è loggato quindi viene fatta una INSERT nel DB usando il tuo CarrelloDAO
                try {
                	CarrelloDAO carrelloDAO = new CarrelloDAO();
                    int idProdotto = Integer.parseInt(idProdottoStr);
                    carrelloDAO.doSave(utenteLoggato.getEmail(), idProdotto);
                    //stampa di verifica
                    System.out.println("DEBUG: Prodotto " + idProdotto + " aggiunto al DB per l'utente " + utenteLoggato.getEmail());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // CASO B: L'utente è un ospite -> Aggiorniamo o creiamo il COOKIE
                Cookie[] cookies = request.getCookies();
                String vecchioContenuto = "";
                Cookie vecchioCookie = null;
                
                if (cookies != null) {
                    for (Cookie c : cookies) {
                        if (c.getName().equals("carrello_ospite")) {
                            vecchioContenuto = c.getValue();
                            vecchioCookie = c;
                            break;
                        }
                    }
                }
                
                // Uniamo il vecchio contenuto del carrello con il nuovo ID prodotto
                String nuovoContenuto = "";
                if (vecchioContenuto.isEmpty()) {
                    nuovoContenuto = idProdottoStr;
                } else {
                    nuovoContenuto = vecchioContenuto + "-" + idProdottoStr; // crea una stringa del tipo 3-6-8 che corrispondono agli id dei prodotti
                }
                
                // Creiamo o aggiorniamo il cookie sul browser dell'utente
                Cookie cookieCarrello = new Cookie("carrello_ospite", nuovoContenuto);
                cookieCarrello.setMaxAge(60 * 60 * 24 * 7); // Il carrello dura 7 giorni sul browser
                cookieCarrello.setPath(request.getContextPath()); // Rende il cookie leggibile in tutto il sito
                
                response.addCookie(cookieCarrello);
            }
        }
        
        // Una volta aggiunto il prodotto, reindirizziamo l'utente alla schermata del carrello
        response.sendRedirect("CarrelloServlet");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}