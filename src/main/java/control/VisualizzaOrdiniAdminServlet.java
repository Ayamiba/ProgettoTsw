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
	        
	        List<OrdineBean> listaOrdini=null;
	        try {
	        listaOrdini= ordineDAO.doRetrieveAll();
	        request.setAttribute("listaOrdini", listaOrdini); //per passare alla pagina jsp
	        RequestDispatcher dispatcher = request.getRequestDispatcher("/VisualizzaOrdiniAdmin.jsp");     
        	dispatcher.forward(request, response);
        } catch (SQLException e) {
        	e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore durante il caricamento del catalogo.");
        }
	}
}
	

