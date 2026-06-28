package control;

import model.ConnectionPool;
import model.carrello.CarrelloDAO;
import model.ordine.*;

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

@WebServlet ("/VisualizzaOrdiniAdminServlet")

public class VisualizzaOrdiniAdminServlet extends HttpServlet{
	private static final long serialVersionUID= 1L;
	
	private OrdineDAO ordineDAO;
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
		ordineDAO=new OrdineDAO();
	}
	
		protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			request.getRequestDispatcher("/VisualizzaOrdiniAdmin.jsp").forward(request, response);
		}
		

	    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        request.setCharacterEncoding("UTF-8");
	        //nella jsp ci sarà un input type=hidden di name=azione e in base al value chiamaremo un doRetrieve diverso
	        String azione = request.getParameter("azione"); //azione corrisponderà al value del bottone
	        List<OrdineBean> listaOrdini=null;
	        try {
	            if (azione == null || azione.equals("tutti")) { //se l'azione è tutti facciamo la doRetrieveAll
	                listaOrdini = ordineDAO.doRetrieveAll();
	            } 
	            else if (azione.equals("filtraDate")) { //se l'azione è filtraDate prendiamo i due parametri e li passiamo a doRetrieveByIntervalloData
	                String inizioStr = request.getParameter("dataInizio");
	                String fineStr = request.getParameter("dataFine");
	                
	                if (inizioStr != null && !inizioStr.isEmpty() && fineStr != null && !fineStr.isEmpty()) {
	                    java.sql.Date dataInizio = java.sql.Date.valueOf(inizioStr); // Converte "YYYY-MM-DD" in java.sql.Date
	                    java.sql.Date dataFine = java.sql.Date.valueOf(fineStr);
	                    listaOrdini = ordineDAO.doRetrieveByIntervalloData(dataInizio, dataFine);
	                } else {
	                    listaOrdini = ordineDAO.doRetrieveAll(); // Se vuoto, mostra tutti
	                }
	            } 
	            else if (azione.equals("filtraCliente")) { //se value è filtra cliente prendermo dal form la mail che l admin ha inserito e la passiamo a doRetrieveByCliente
	                String idCliente = request.getParameter("idCliente");
	                if (idCliente != null && !idCliente.trim().isEmpty()) {
	                    listaOrdini = ordineDAO.doRetrieveByCliente(idCliente.trim());
	                } else {
	                    listaOrdini = ordineDAO.doRetrieveAll();
	                }
	            }

	            request.setAttribute("listaOrdini", listaOrdini);
	            request.getRequestDispatcher("/VisualizzaOrdiniAdmin.jsp").forward(request, response);
        } catch (SQLException e) {
        	e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore durante il caricamento del catalogo.");
        }
	}
}
	

