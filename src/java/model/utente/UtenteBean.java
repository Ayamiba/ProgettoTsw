package model.utente;

import java.io.Serializable; //serve a dire che gli oggetti di questa classe possono essere convertiti in un flusso di byte
import java.sql.Date;

public class UtenteBean implements Serializable {
    private static final long serialVersionUID = 1L; //serve per dare una versione all'oggetto che viene creato ed evitare che i dati vengano corrotti

    private String nome;
    private String cognome;
    private String email; // Chiave Primaria
    private String password;
    private String tipo;
    private Date dataNascita;

    // Costruttore vuoto obbligatorio
    public UtenteBean() {}

    // Costruttore 
    public UtenteBean(String nome, String cognome, String email, String password, String tipo) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.tipo = tipo; 
        this.dataNascita = dataNascita;
    }

    // Getter e Setter
    public String getNome() {
    	return nome; 
    	}
    
    public void setNome(String nome) { 
    	this.nome = nome; 
    	}

    public String getCognome() { 
    	return cognome; 
    	
    }
    public void setCognome(String cognome) { 
    	this.cognome = cognome; 
    	}

    public String getEmail() { 
    	return email; 
    	}
    
    public void setEmail(String email) { 
    	this.email = email; 
    	}

    public String getPassword() { 
    	return password; 
    	}
    
    public void setPassword(String password) { 
    	this.password = password; 
    	}

    public String getTipo() { 
    	return tipo; 
    	}
    
    public void setTipo(String tipo) { 
    	this.tipo = tipo; 
    	}

    public Date getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }
    
    @Override
    public String toString() {
        return "UtenteBean [nome=" + nome + ", cognome=" + cognome + ", email=" + email + ", tipo=" + tipo + "]";
    }
}