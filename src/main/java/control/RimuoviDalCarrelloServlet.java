package control;

import model.carrello.*;
import model.ConnectionPool;
import model.prodotto.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.Cookie;

@WebServlet("/RimuoviDalCarrelloServlet")
public class RimuoviDalCarrelloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private CarrelloDAO carrelloDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            ConnectionPool.init(5);
        } catch (SQLException e) {
            System.out.println("Errore fatale: Impossibile avviare il Connection Pool!");
            e.printStackTrace();
        }
        carrelloDAO = new CarrelloDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        model.utente.UtenteBean utenteLoggato = (model.utente.UtenteBean) session.getAttribute("user");
        
        // LEGGE CORRETTAMENTE L'ID RIGA PASSATO DALLA JSP
        String idRigaParam = request.getParameter("idRiga");

        if (idRigaParam == null || idRigaParam.trim().isEmpty()) {
            response.sendRedirect("CarrelloServlet");
            return;
        }

        try {
            int idRiga = Integer.parseInt(idRigaParam);

            if (utenteLoggato != null) {
                // 1. UTENTE LOGGATO: Cancella chirurgicamente la singola riga dal DB
                carrelloDAO.doDeleteByRiga(idRiga);
            } else {
                // 2. UTENTE OSPITE: Rimuove l'elemento dal cookie in base all'indice
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (Cookie c : cookies) {
                        if (c.getName().equals("carrello_ospite")) {
                            String[] idArray = c.getValue().split("-");
                            List<String> listaId = new ArrayList<>();
                            for (String s : idArray) {
                                if (!s.trim().isEmpty()) {
                                    listaId.add(s);
                                }
                            }
                            
                            // Rimuove l'elemento all'indice corrispondente (idRiga parte da 1)
                            int indexToRemove = idRiga - 1;
                            if (indexToRemove >= 0 && indexToRemove < listaId.size()) {
                                listaId.remove(indexToRemove);
                            }

                            StringBuilder nuovoContenuto = new StringBuilder();
                            for (String s : listaId) {
                                nuovoContenuto.append(s).append("-");
                            }

                            Cookie nuovoCookie = new Cookie("carrello_ospite", nuovoContenuto.toString());
                            if (nuovoContenuto.length() == 0) {
                                nuovoCookie.setMaxAge(0); 
                            } else {
                                nuovoCookie.setMaxAge(60 * 60 * 24 * 7); 
                            }
                            nuovoCookie.setPath(request.getContextPath());
                            response.addCookie(nuovoCookie);
                            break;
                        }
                    }
                }
            }

            response.sendRedirect("CarrelloServlet");

        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("CarrelloServlet"); 
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("errore500.jsp");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}