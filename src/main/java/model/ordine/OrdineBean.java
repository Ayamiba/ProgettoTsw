package model.ordine;

import java.io.Serializable;
import java.sql.Date;

public class OrdineBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idOrdine; //primary key
    private Date dataOrdine; 
    private float totale;
    private String stato;
    private String descrizione;
    private int fKTraccia; 

    public OrdineBean() {}

    public OrdineBean(int idOrdine, Date dataOrdine, float totale, String stato, String descrizione, int fKTraccia) {
        this.idOrdine = idOrdine;
        this.dataOrdine = dataOrdine;
        this.totale = totale;
        this.stato = stato;
        this.descrizione = descrizione;
        this.fKTraccia = fKTraccia;
    }

    public int getIdOrdine() { 
    	return idOrdine; 
    	}
    
    public void setIdOrdine(int idOrdine) { 
    	this.idOrdine = idOrdine; 
    	}

    public Date getDataOrdine() {
    	return dataOrdine; 
    	}
    
    public void setDataOrdine(Date dataOrdine) { 
    	this.dataOrdine = dataOrdine; 
    	}

    public float getTotale() { 
    	return totale; 
    	}
    
    public void setTotale(float totale) { 
    	this.totale = totale; 
    	}

    public String getStato() { 
    	return stato; 
    	}
    
    public void setStato(String stato) { 
    	this.stato = stato; 
    	}

    public String getDescrizione() { 
    	return descrizione; 
    	}
    
    public void setDescrizione(String descrizione) { 
    	this.descrizione = descrizione; 
    	}

    public int getfKTraccia() { 
    	return fKTraccia; 
    	}
    
    public void setfKTraccia(int fKTraccia) { 
    	this.fKTraccia = fKTraccia; 
    	}
}