package control;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.ConnectionPool;
import model.tracciaAudio.*; // Cambia il pacchetto in base al tuo progetto

@WebServlet("/CheckServlet")
public class CheckServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TracciaAudioDAO tracciaAudioDAO;

    public void init() throws ServletException {
    	super.init();
		try {
	        ConnectionPool.init(5);
	    } catch (SQLException e) {
	        System.out.println("Errore fatale: Impossibile avviare il Connection Pool!");
	        e.printStackTrace();
	    }
        tracciaAudioDAO = new TracciaAudioDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        // Prendo i parametri inviati dalla JSP
        int idTraccia = Integer.parseInt(request.getParameter("idTraccia"));
        // prendiamo il nuovo valore di check dal bottone/opzione che diamo al professionista per modificarl
        boolean nuovoCheck = Boolean.parseBoolean(request.getParameter("nuovoCheck")); //i bottoni nel jsp di prova hanno value true e false in base a ciò che viene cliccato

        try {
            //modifichiamo il valore di check usando il metodo doUpdateCheck
            tracciaAudioDAO.doUpdateCheck(nuovoCheck, idTraccia);
            // Reindirizziamo il professionista alla pagina con il messaggio di successo
            response.sendRedirect("ModificaCheck.jsp?messaggio=Stato traccia aggiornato con successo!");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore nel database durante la modifica del check.");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}