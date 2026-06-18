package model.utilizzo;

import java.io.Serializable;

public class UtilizzoBean implements Serializable{
	private static long serializableVarsioneUID=1L;
	
	private String fkUtente; //primary key
	private long fkMetodoPagamento; //primary key
	
	public UtilizzoBean() {}
	
	public UtilizzoBean(String fkUtente, long fkMetodoPagamento) {
		this.fkUtente=fkUtente;
		this.fkMetodoPagamento=fkMetodoPagamento;
	}
	
	//getters
	public String getFkUtente() {
		return fkUtente;
	}
	
	public long getFkMetodoPagamento() {
		return fkMetodoPagamento;
	}
	
	//setters
	
	public void setFkUtente(String fkUtente) {
		this.fkUtente=fkUtente;
	}
	
	public void setFkMetodoPagamento(long fkMetodoPagamento) {
		this.fkMetodoPagamento=fkMetodoPagamento;
	}
}