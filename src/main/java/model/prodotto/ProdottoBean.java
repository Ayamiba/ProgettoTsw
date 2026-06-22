package model.prodotto;

import java.io.Serializable;

public class ProdottoBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idProdotto; // INT AUTO_INCREMENT primary key
    private String nome;
    private float prezzo;
    private String descrizione;

    public ProdottoBean() {}

    public ProdottoBean(int idProdotto, String nome, float prezzo, String descrizione) {
        this.idProdotto = idProdotto;
        this.nome = nome;
        this.prezzo = prezzo;
        this.descrizione = descrizione;
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

    @Override
    public String toString() {
        return "ProdottoBean [idProdotto=" + idProdotto + ", nome=" + nome + ", prezzo=" + prezzo + "]";
    }
}
