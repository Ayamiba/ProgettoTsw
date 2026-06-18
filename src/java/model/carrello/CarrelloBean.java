package model.carrello;

import java.io.Serializable;

public class CarrelloBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String fkUtente; //primary key
	private int fkProdotto;
	
	public CarrelloBean() {}
	
	public CarrelloBean(String fkUtente, int fkProdotto) {
		this.fkUtente=fkUtente;
		this.fkProdotto=fkProdotto;
	}
	
	//getters
	public String getfkUtente() {
		return fkUtente;
	}
	
	public int getfkProdotto() {
		return fkProdotto;
	}
	
	//setters
	public void setfkUtente(String fkUtente) {
		this.fkUtente=fkUtente;
	}
	
	public void setfkProdotto(int fkProdotto) {
		this.fkProdotto=fkProdotto;
	}
}