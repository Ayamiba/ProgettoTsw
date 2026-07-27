package model.recensione;

import java.io.Serializable;
import java.sql.Date;

public class RecensioneBean implements Serializable{
	private static long serializableVersionUID=1L;
	
	private int idRecensione;  // Primary Key (Auto-Increment)
    private Integer fkOrdine;   // Foreign Key (può essere null)
    private Integer fkProdotto; // Foreign Key (può essere null)
    private String fkUtente;
	private int voto;
	private String commento;
	private Date dataRecensione;
	private String tipo;
	
	public RecensioneBean() {}
	
	public RecensioneBean(int idRecensione, String fkUtente, Integer fkOrdine, Integer fkProdotto, int voto, String commento, Date dataRecensione, String tipo) {
        this.idRecensione = idRecensione;
        this.fkOrdine = fkOrdine;
        this.fkProdotto = fkProdotto;
        this.voto = voto;
        this.commento = commento;
        this.dataRecensione = dataRecensione;
        this.tipo = tipo;
        this.fkUtente= fkUtente;
    }
	
	//getters
	
	public int getIdRecensione() { 
		return idRecensione; 
		}
	
    public Integer getFkOrdine() { 
    	return fkOrdine; 
    	}
	
    public Integer getFkProdotto() { 
    	return fkProdotto; 
    	}
    
    public String getFkUtente() {
    	return fkUtente;
    }
    
	public int getVoto() {
		return voto;
	}
	
	public String getCommento() {
		return commento;
	}
	
	public Date getDataRecensione() {
		return dataRecensione;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	//setters
	
	public void setIdRecensione(int idRecensione) { 
		this.idRecensione = idRecensione; 
		}
	
	public void setFkOrdine(Integer fkOrdine) {
		this.fkOrdine=fkOrdine;
	}
	
	public void setFkProdotto(Integer fkProdotto) { 
		this.fkProdotto = fkProdotto; 
		}
	
	public void setFkUtente(String fkUtente) {
		this.fkUtente= fkUtente;
	}
	
	public void setVoto(int voto) {
		this.voto=voto;
	}
	
	public void setCommento(String commento) {
		this.commento=commento;
	}
	
	public void setDataRecensione(Date dataRecensione) {
		this.dataRecensione=dataRecensione;
	}
	
	public void setTipo(String tipo) {
		this.tipo=tipo;
	}
}