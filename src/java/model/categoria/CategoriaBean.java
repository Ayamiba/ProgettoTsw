package model.categoria;

import java.io.Serializable;

public class CategoriaBean implements Serializable{
	private static long serializableVersionUID=1L;
	
	private String nome; //primary key
	private boolean studioTool;
	private boolean effetto;
	private String tipo;
	
	public CategoriaBean() {}
	
	public CategoriaBean(String nome, boolean studioTool, boolean effetto, String tipo) {
		this.nome=nome;
		this.studioTool=studioTool;
		this.effetto=effetto;
		this.tipo=tipo;
	}
	
	//getters
	public String getNome() {
		return nome;
	}
	
	public boolean getStudioTool() {
		return studioTool;
	}
	
	public boolean getEffetto() {
		return effetto;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	//ssetters
	public void setNome(String nome) {
		this.nome=nome;
	}
	
	public void setStudio_tool(boolean studioTool) {
		this.studioTool=studioTool;
	}
	
	public void setEffetto(boolean effetto) {
		this.effetto=effetto;
	}
	
	public void setTipo(String tipo) {
		this.tipo=tipo;
	}
}