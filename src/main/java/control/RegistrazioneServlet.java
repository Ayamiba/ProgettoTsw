//la gestione dei cookie di registrazione è uguale a quella di login(trasferimento da cookie e database)

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
import java.sql.Date;
import control.Security;

import model.ConnectionPool;
import model.utente.UtenteDAO;
import model.utente.UtenteBean;
import model.carrello.CarrelloDAO;

@WebServlet("/RegistrazioneServlet")
public class RegistrazioneServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UtenteDAO utenteDAO;
    private CarrelloDAO carrelloDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            ConnectionPool.init(5);
        } catch (SQLException e) {
            System.out.println("Errore Connection Pool in RegistrazioneServlet!");
            e.printStackTrace();
        }
        utenteDAO = new UtenteDAO();
        carrelloDAO = new CarrelloDAO();
    }
    
 // Se l'utente clicca sul link "Registrati", gli mostriamo la pagina con il form
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/registrazione.jsp").forward(request, response);
    }
    //dopo che l'utente conferma il form
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	String email = request.getParameter("email");
        String password = request.getParameter("password");
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String dataNascitaStr = request.getParameter("data_nascita");
        Date dataNascita = null;
        //per trasformare la stringa nell'oggetto Date per il DB
        if(dataNascitaStr != null && !dataNascitaStr.isEmpty()) {
        	dataNascita = java.sql.Date.valueOf(dataNascitaStr);
        }
        
        //Creazione dell'utente che verrà mandato al metodo dosave di UtenteBean
        UtenteBean nuovoUtente = new UtenteBean();
        nuovoUtente.setEmail(email);
        nuovoUtente.setPassword(Security.hashPassword(password));
        nuovoUtente.setNome(nome);
        nuovoUtente.setCognome(cognome);
        nuovoUtente.setDataNascita(dataNascita); // Passiamo la data
        nuovoUtente.setTipo("utente registrato");  //di base è utente registrato
        
        try {
        	utenteDAO.doSave(nuovoUtente);
        	//sincronizzazione del carello come fatto in login
            HttpSession session = request.getSession();
            session.setAttribute("user", nuovoUtente);

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
                            carrelloDAO.doSave(nuovoUtente.getEmail(), idProdotto);
                        } catch (SQLException | NumberFormatException e) {
                            e.printStackTrace(); 
                        }
                    }
                }
                Cookie svuotaCookie = new Cookie("carrello_ospite", "");
                svuotaCookie.setMaxAge(0);
                svuotaCookie.setPath(request.getContextPath());
                response.addCookie(svuotaCookie);
            }

            response.sendRedirect("CatalogoServlet");

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errore", "Questa email è già registrata! Scegline un'altra.");
            request.getRequestDispatcher("/registrazione.jsp").forward(request, response);
        }
    }
    }
