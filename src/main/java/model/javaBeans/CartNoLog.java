package model.javaBeans;

public class CartNoLog {
	int IDProdotto;
	int quantita;
	
	public CartNoLog(int iDProdotto, int quantita) {
		IDProdotto = iDProdotto;
		this.quantita = quantita;
	}
	
	public int getIDProdotto() {
		return IDProdotto;
	}

	public void setIDProdotto(int iDProdotto) {
		IDProdotto = iDProdotto;
	}

	public int getQuantita() {
		return quantita;
	}

	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}
	
}
