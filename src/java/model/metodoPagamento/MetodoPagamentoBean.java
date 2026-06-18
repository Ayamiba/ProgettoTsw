package model.metodoPagamento;

import java.io.Serializable;

public class MetodoPagamentoBean implements Serializable{
	private static final long serializableVersionUID=1L;
	
	private long numeroCarta; //orimary key
	private int cvv;
	private String nome;
	private String cognome;
	
	public MetodoPagamentoBean() {}
	
	public MetodoPagamentoBean(long numeroCarta, int cvv, String nome, String cognome) {
		this.numeroCarta=numeroCarta;
		this.cvv=cvv;
		this.nome=nome;
		this.cognome=cognome;
	}
	
	//getters 
	public long getNumeroCarta() {
		return numeroCarta;
	}
	
	public int getCvv() {
		return cvv;
	}
	
	public String getNome() {
		return nome;
	}
	
	public String getCognome() {
		return cognome
	}
	
	//setters
	public void setNumeroCarta(long numeroCarta) {
		this.numeroCarta=numeroCarta;
	}
	
	public void setCvv(int cvv) {
		this.cvv=cvv;
	}
	
	public void setNome(String nome) {
		this.nome=nome;
	}
	
	public void setCognome(Sting cognome) {
		this.cognome=cognome;
	}
}
    