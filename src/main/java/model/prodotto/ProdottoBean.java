package model.prodotto;

import java.io.Serializable;

public class ProdottoBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idProdotto; // INT AUTO_INCREMENT primary key
    private String nome;
    private float prezzo;
    private String descrizione;
    private String immagine;
    private String demoDry;
    private String demoWet;

    public ProdottoBean() {}

    public ProdottoBean(int idProdotto, String nome, float prezzo, String descrizione, String immagine, String demoDry, String demoWet) {
        this.idProdotto = idProdotto;
        this.nome = nome;
        this.prezzo = prezzo;
        this.descrizione = descrizione;
        this.immagine=immagine;
        this.demoDry=demoDry;
        this.demoWet=demoWet;
    }

    public int getIdProdotto() { 
    	return idProdotto; 
    	}
    
    public void setIdProdotto(int idProdotto) { 
    	this.idProdotto = idProdotto; 
    	}

    public String getNome() {
    	return nome; 
    	}
    
    public void setNome(String nome) { 
    	this.nome = nome; 
    	}

    public float getPrezzo() { 
    	return prezzo; 
    	}
    
    public void setPrezzo(float prezzo) { 
    	this.prezzo = prezzo; 
    	}

    public String getDescrizione() { 
    	return descrizione; 
    	}
    
    public void setDescrizione(String descrizione) { 
    	this.descrizione = descrizione; 
    	}
    
    public String getImmagine() {
        return immagine;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }
    
    public String getDemoDry() {
        return demoDry;
    }

    public void setDemoDry(String demoDry) {
        this.demoDry = demoDry;
    }

    public String getDemoWet() {
        return demoWet;
    }

    public void setDemoWet(String demoWet) {
        this.demoWet = demoWet;
    }

    @Override
    public String toString() {
        return "ProdottoBean [idProdotto=" + idProdotto + ", nome=" + nome + ", prezzo=" + prezzo + "]";
    }
}
