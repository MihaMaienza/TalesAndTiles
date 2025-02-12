package model.javaBeans;

public class Prodotto {
    private int id;
    private String nome;
    private float prezzo;
    private int IVA;
    private String tipo;
    private String immagine;
    private int disponibilita;
    private String descrizione;
    private String condizione;
    private int annoPubblicazione;
    
    // Costruttore
    public Prodotto(int id, String nome, float prezzo, int IVA, String tipo, String immagine, int disponibilita, 
                    String descrizione, String condizione, int annoPubblicazione) {
        this.id = id;
        this.nome = nome;
        this.prezzo = prezzo;
        this.IVA = IVA;
        this.tipo = tipo;
        this.immagine=immagine;
        this.disponibilita = disponibilita;
        this.descrizione = descrizione;
        this.condizione = condizione;
        this.annoPubblicazione = annoPubblicazione;
    }
    
    public int getId() {
        return id;
    }
    
    /*public void setId(int id) {
        this.id = id;
    }*/
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public float getPrezzo() {
        return prezzo;
    }
    
    public void setPrezzo(float prezzo) {
        this.prezzo = prezzo;
    }
    
    public int getIVA() {
        return IVA;
    }
    
    public void setIVA(int IVA) {
        this.IVA = IVA;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public int getDisponibilita() {
        return disponibilita;
    }
    
    public void setDisponibilita(int disponibilita) {
        this.disponibilita = disponibilita;
    }
    
    public String getDescrizione() {
        return descrizione;
    }
    
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    
    public String getCondizione() {
        return condizione;
    }
    
    public void setCondizione(String condizione) {
        this.condizione = condizione;
    }
    
    public int getAnnoPubblicazione() {
        return annoPubblicazione;
    }
    
    public void setAnnoPubblicazione(int annoPubblicazione) {
        this.annoPubblicazione = annoPubblicazione;
    }
    
	public String getImmagine() {
		return immagine;
	}

	public void setImmagine(String immagine) {
		this.immagine = immagine;
	}
}
