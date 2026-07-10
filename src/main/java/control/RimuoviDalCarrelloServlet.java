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
	    // Al login viene salvato l'utente che c'è
	    model.utente.UtenteBean utenteLoggato = (model.utente.UtenteBean) session.getAttribute("user");
	    String idParam = request.getParameter("idProdotto");
	    try {
	    	int idProdotto = Integer.parseInt(idParam);
	    	CarrelloDAO.doDelete(idProdotto);
	    	response.sendRedirect("CarrelloServlet");
	    } catch (NumberFormatException e) {
                e.printStackTrace();
                response.sendRedirect("paginaAdmin.jsp?messaggio=Errore: ID non valido.");
                return;
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect("errore500.jsp");
                return;
	    }
	}
}
	