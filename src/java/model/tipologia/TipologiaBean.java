package model.tipologia;

import java.io.Serializable;

public class TipologiaBean implements Srializable{
	private static long serializableVersionUID=1L;
	
	private int fkProdotto; //primary key
	private String fkCategoria; //primary Key
	
	public TipologiaBean() {}
	
	public TipologiaBean(int fkProdotto, String fkCategoria) {
		this.fkProdotto=fkProdotto;
		this.fkCategoria=fkCategoria;
	}
	
	//getters
	public int getFkProdotto() {
		return fkProdotto;
	}
	
	public String getFkCategoria() {
		return fkCategoria;
	}
	
	//setters
	public void setFkProdotto(int fkProdotto) {
		this.fkProdotto=fkProdotto;
	}
	
	public void setFkCategoria(String fkCategoria) {
		this.fkCategoria=fkCategoria;
	}
}