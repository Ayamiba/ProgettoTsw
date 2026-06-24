package control;

import model.utente.UtenteBean;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter; 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

//tutte le servlet e le JSP da proteggere
@WebFilter(urlPatterns = {"/AdminUpdateProdotto", "/AdminDeleteProdotto", "/AdminAddProdotto", "/paginaAdmin.jsp"})
public class AdminFilter implements Filter{
	//Filterchain serve per dare conferma che il controllo sia andato a buon fine
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)throws IOException, ServletException {
		//casting di request e response perchè doFIlter essendo della classe Filter ha ServletRequest e ServletResponse come parametri
		HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        //recuperiamo la sessione
        HttpSession sessione= httpRequest.getSession(false);
        //creiamo un booleano isAdmin
        boolean isAdmin=false;
        
        //se la sessione è diversa da null creiamo un oggetto UtenteBean dal quale recuperiamo il tipo
        if(sessione != null) {
        	UtenteBean utenteCorrente= (UtenteBean) sessione.getAttribute("user");
        	if(utenteCorrente != null && "admin".equals(utenteCorrente.getTipo())) {
        		isAdmin= true;
        	}		  			
        }
        //Se l'utente non è admin non ha i permessi 
        if(isAdmin==false) {
        	System.out.println("L'utente non è autorizzato");
        	httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
        }
        else {
        	//conferma che utente è admin
        	chain.doFilter(request, response);
        }
	}
}