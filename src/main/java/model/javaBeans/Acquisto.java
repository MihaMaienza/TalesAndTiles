package model.javaBeans;

public class Acquisto {

	int IDAcquisto;
	int IDOrdine;
	int IDProdotto;
	String nome;
	int quantita;
	String immagine;
	float prezzoAq;
	int ivaAq;
	
	public Acquisto(int iDAcquisto, int iDOrdine, int iDProdotto, String nome, int quantita, String immagine, 
			float prezzoAq, int ivaAq) {
		this.IDAcquisto = iDAcquisto;
		this.IDOrdine = iDOrdine;
		this.IDProdotto = iDProdotto;
		this.nome = nome;
		this.quantita = quantita;
		this.immagine = immagine;
		this.prezzoAq = prezzoAq;
		this.ivaAq = ivaAq;
	}
	
	public int getIDAcquisto() {
		return IDAcquisto;
	}
	
	public int getIDOrdine() {
		return IDOrdine;
	}
	public void setIDOrdine(int iDOrdine) {
		IDOrdine = iDOrdine;
	}
	public int getIDProdotto() {
		return IDProdotto;
	}
	public void setIDProdotto(int iDProdotto) {
		IDProdotto = iDProdotto;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public int getQuantita() {
		return quantita;
	}
	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}
	public String getImmagine() {
		return immagine;
	}
	public void setImmagine(String immagine) {
		this.immagine = immagine;
	}
	public float getPrezzoAq() {
		return prezzoAq;
	}
	public void setPrezzoAq(float prezzoAcquistato) {
		this.prezzoAq = prezzoAcquistato;
	}
	public int getivaAq() {
		return ivaAq;
	}
	public void setIVAAcquisto(int ivaAq) {
		this.ivaAq = ivaAq;
	}

	public float getDiCuiIva() {
		float ivaEffettiva;
		float aliquota = (float) ivaAq / 100;
		ivaEffettiva=(prezzoAq*aliquota)/(1+aliquota);
		ivaEffettiva = Math.round(ivaEffettiva * 100) / 100.0f;
		
		return ivaEffettiva;
	}
}
