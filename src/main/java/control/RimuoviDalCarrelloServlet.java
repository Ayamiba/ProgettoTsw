package control;

import model.carrello.*;
import model.ConnectionPool;

import model.prodotto.*;
import model.categoria.*;
import model.tipologia.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;

@WebServlet ("/RimuoviDalCarrelloServlet")

public class RimuoviDalCarrelloServlet extends HttpServlet {
	private static final long serialVersionUID= 1L;
	
	private CarrelloDAO carrelloDAO;
	private static final int itemPerPage=6; //Elementi caricati in una pagina (caricati != mostrati)
	
	@Override
	public void init() throws ServletException {
		super.init();
		try {
	        ConnectionPool.init(5);
	    } catch (SQLException e) {
	        System.out.println("Errore fatale: Impossibile avviare il Connection Pool!");
	        e.printStackTrace();
	    }
		carrelloDAO=new CarrelloDAO();
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession session = request.getSession();
	    model.utente.UtenteBean utenteLoggato = (model.utente.UtenteBean) session.getAttribute("user");
	    String idParam = request.getParameter("idProdotto");

	    if (idParam == null || idParam.trim().isEmpty()) {
	        response.sendRedirect("CarrelloServlet");
	        return;
	    }

	    try {
	        int idProdotto = Integer.parseInt(idParam);

	        if (utenteLoggato != null) {
	            // 1. UTENTE LOGGATO: Cancelliamo dal Database
	            carrelloDAO.doDeleteByUtenteAndProdotto(utenteLoggato.getEmail(), idProdotto);
	        } else {
	            // 2. UTENTE OSPITE: Rimuoviamo il prodotto dal Cookie
	            Cookie[] cookies = request.getCookies();
	            if (cookies != null) {
	                for (Cookie c : cookies) {
	                    if (c.getName().equals("carrello_ospite")) {
	                        String[] idArray = c.getValue().split("-");
	                        StringBuilder nuovoContenuto = new StringBuilder();

	                        // Ricostruiamo la stringa escludendo l'ID da eliminare
	                        for (String idStr : idArray) {
	                            if (!idStr.isEmpty() && Integer.parseInt(idStr) != idProdotto) {
	                                nuovoContenuto.append(idStr).append("-");
	                            }
	                        }

	                        Cookie nuovoCookie = new Cookie("carrello_ospite", nuovoContenuto.toString());
	                        
	                        // FIX: Se il carrello è diventato vuoto, uccidiamo il cookie. Altrimenti durata 7 giorni.
	                        if (nuovoContenuto.length() == 0) {
	                            nuovoCookie.setMaxAge(0); 
	                        } else {
	                            nuovoCookie.setMaxAge(60 * 60 * 24 * 7); 
	                        }
	                        
	                        // FIX PERCORSO: Evita la creazione di cookie fantasma duplicati
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
	