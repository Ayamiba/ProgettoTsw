package model.recensione;

import java.io.Serializable;

public class RecensioneBean implements Serializable{
	private static long serializableVersionUID=1L;
	
	private int fkOrdine; //primary key
	private int voto;
	private String commento;
	private Date dataRecensione;
	
	public RecensioneBean() {}
	
	public RecensioneBean(int fkOrdine, int voto, String commento, Date dataRecensione) {
		this.fkOrdine=fkOrdine;
		this.voto=voto;
		this.commento=commento;
		this.dataRecensione=dataRecensione;
	}
	
	//getters
	public int getFkOrdine() {
		return fkOrdine;
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
	
	//setters
	public void setFkOrdine(int fkOrdine) {
		this.fkOrdine=fkOrdine;
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
}