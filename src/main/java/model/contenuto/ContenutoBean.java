package model.contenuto;

import java.io.Serializable;

public class ContenutoBean implements Serializable{
	private static long serializableVersionUID=1L;
	
	private int fkOrdine; //primary key
	private int fkProdotto; //primary key
	
	public ContenutoBean() {}
	
	public ContenutoBean(int fkOrdine, int fkProdotto) {
		this.fkOrdine=fkOrdine;
		this.fkProdotto=fkProdotto;
	}
	
	//getters
	public int getFkOrdine() {
		return fkOrdine;
	}
	
	public int getFkProdotto() {
		return fkProdotto;
	}
	
	//setters
	public void setFkOrdine(int fkOrdine) {
		this.fkOrdine=fkOrdine;
	}
	
	public void setFkProdotto(int fkProdotto) {
		this.fkProdotto=fkProdotto;
	}

}