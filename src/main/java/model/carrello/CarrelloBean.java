package model.carrello;

import java.io.Serializable;

public class CarrelloBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int idRigaCarrello; //primary Key
	private String fkUtente; 
	private int fkProdotto;
	
	public CarrelloBean() {}
	
	public CarrelloBean(int idRigaCarrello, String fkUtente, int fkProdotto) {
		this.idRigaCarrello=idRigaCarrello;
		this.fkUtente=fkUtente;
		this.fkProdotto=fkProdotto;
	}
	
	//getters
	public int getIdRigaCarrello() {
		return idRigaCarrello;
	}
	
	public String getfkUtente() {
		return fkUtente;
	}
	
	public int getfkProdotto() {
		return fkProdotto;
	}
	
	//setters
	public void setIdRigaCarrello(int idRigaCarrello) {
		this.idRigaCarrello=idRigaCarrello;
	}
	
	public void setfkUtente(String fkUtente) {
		this.fkUtente=fkUtente;
	}
	
	public void setfkProdotto(int fkProdotto) {
		this.fkProdotto=fkProdotto;
	}
}