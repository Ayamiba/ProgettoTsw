package control;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import control.Security;

import model.utente.UtenteDAO;
import model.utente.UtenteBean;
import model.carrello.CarrelloDAO;
import model.ConnectionPool;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UtenteDAO utenteDAO;
    private CarrelloDAO carrelloDAO;
    
    public void init() throws ServletException {
        super.init();
        try {
            // Inizializza il pool di connessioni (se non è già stato fatto da un'altra servlet)
            ConnectionPool.init(5);
        } catch (SQLException e) {
            System.out.println("Errore nell'inizializzazione del Connection Pool in LoginServlet!");
            e.printStackTrace();
        }
        // Istanziamo i DAO una volta sola qui dentro
        utenteDAO = new UtenteDAO();
        carrelloDAO = new CarrelloDAO();
    }
    
    // Se l'utente arriva qui tramite un link, gli mostriamo la pagina di login
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
    
    //quando l'utente preme sul pulsante "Login" i dati vengono inviati qui
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        password=Security.hashPassword(password);
        UtenteBean utenteLoggato = null;
        
        try {
            utenteLoggato = utenteDAO.doRetrieveByLogin(email, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        if(utenteLoggato != null) {
        	//salviamo l'utente nella sessione
        	HttpSession session = request.getSession();
        	session.setAttribute("user", utenteLoggato); //user non corrisponderà più a null
        	//trasferiamo i cookie al database, così da salvare gli elementi che l'utente aveva nel carrello prima di loggarsi
            Cookie[] cookies = request.getCookies();
            String contenutoCookie = null;
            
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if (c.getName().equals("carrello_ospite")) {
                        contenutoCookie = c.getValue(); 
                        break;
                    }
                }
            }
            
            if (contenutoCookie != null && !contenutoCookie.trim().isEmpty()) {
                String[] idProdottiArray = contenutoCookie.split("-");
       
                for (String idStr : idProdottiArray) {
                    if (!idStr.isEmpty()) {
                        try {
                            int idProdotto = Integer.parseInt(idStr);
                            //chiamiamo doSave per far eseguire la insert nel database
                            carrelloDAO.doSave(utenteLoggato.getEmail(), idProdotto);
                        } catch (SQLException | NumberFormatException e) {
                            e.printStackTrace(); // Ignoriamo se c'è un duplicato e andiamo avanti
                        }
                    }
                }
                //visto che abbiamo messo le cose nel database svuotiamo i cookie
                Cookie svuotaCookie = new Cookie("carrello_ospite", "");
                svuotaCookie.setMaxAge(0); 
                svuotaCookie.setPath(request.getContextPath());
                response.addCookie(svuotaCookie);
            }
            //dopo il login rimandiamo l'utente al catalogo
            response.sendRedirect("CatalogoServlet"); 
            
        } else {
           //login fallito
            request.setAttribute("errore", "Email o password non validi. Riprova.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
} 