package model.contenuto;

import java.io.Serializable;

public class ContenutoBean implements Serializable{
	private static long serializableVersionUID=1L;
	
	private int idRigaContenuto; //primary key
	private int fkOrdine;
	private int fkProdotto;
	private int posizioneCatena;
	
	public ContenutoBean() {}
	
	public ContenutoBean(int idRigaContenuto, int fkOrdine, int fkProdotto, int posizioneCatena) {
		this.idRigaContenuto=idRigaContenuto;
		this.fkOrdine=fkOrdine;
		this.fkProdotto=fkProdotto;
		this.posizioneCatena=posizioneCatena;
	}
	
	//getters
	public int getIdRigaContenuto() {
		return idRigaContenuto;
	}
	
	public int getFkOrdine() {
		return fkOrdine;
	}
	
	public int getFkProdotto() {
		return fkProdotto;
	}
	
	public int getPosizioneCatena() {
		return posizioneCatena;
	}
	//setters
	public void setIdRigaContenuto(int idRigaContenuto) {
		this.idRigaContenuto=idRigaContenuto;
	}
	
	public void setFkOrdine(int fkOrdine) {
		this.fkOrdine=fkOrdine;
	}
	
	public void setFkProdotto(int fkProdotto) {
		this.fkProdotto=fkProdotto;
	}
	
	public void setPosizioneCatena(int posizioneCatena) {
		this.posizioneCatena=posizioneCatena;
	}

}