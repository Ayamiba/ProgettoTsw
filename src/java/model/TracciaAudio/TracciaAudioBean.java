package model.traccia;

import java.io.Serializable;

public class TracciaAudioBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idTraccia; //primary key
    private String nomeFile;
    private String percorsoFile;
    private String fKUtente; 
    private boolean check;   

    // Costruttore vuoto 
    public TracciaAudioBean() {}

    // Costruttore 
    public TracciaAudioBean(int idTraccia, String nomeFile, String percorsoFile, String fKUtente, boolean check) {
        this.idTraccia = idTraccia;
        this.nomeFile = nomeFile;
        this.percorsoFile = percorsoFile;
        this.fKUtente = fKUtente;
        this.check = check;
    }

    // Getter e Setter
    public int getIdTraccia() { 
    	return idTraccia; 
    	}
    
    public void setIdTraccia(int idTraccia) { 
    	this.idTraccia = idTraccia; 
    	}

    public String getNomeFile() { 
    	return nomeFile; 
    	}
    
    public void setNomeFile(String nomeFile) { 
    	this.nomeFile = nomeFile;
    	}

    public String getPercorsoFile() { 
    	return percorsoFile; 
    	}
    
    public void setPercorsoFile(String percorsoFile) { 
    	this.percorsoFile = percorsoFile; 
    	}

    public String getfKUtente() {
    	return fKUtente; 
    	}
    
    public void setfKUtente(String fKUtente) { 
    	this.fKUtente = fKUtente; 
    	}

    public boolean isCheck() { 
    	return check; 
    	} 
    
    public void setCheck(boolean check) { 
    	this.check = check; 
    	}

    @Override
    public String toString() {
        return "TracciaAudioBean [idTraccia=" + idTraccia + ", nomeFile=" + nomeFile + ", fKUtente=" + fKUtente
                + ", check=" + check + "]";
    }
}