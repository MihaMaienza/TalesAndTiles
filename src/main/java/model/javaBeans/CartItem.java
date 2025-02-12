package model.javaBeans;

public class CartItem {
    private int IDCartItem;
    private int IDProdotto;
    private int IDCarrello;
    private int quantita;

    // Costruttore
    public CartItem(int IDCartItem, int IDProdotto, int IDCarrello,int quantita) {
        this.IDCartItem = IDCartItem;
        this.IDProdotto = IDProdotto;
        this.IDCarrello = IDCarrello;
        this.quantita = quantita;
    }

    public int getQuantita() {
		return quantita;
	}

	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}

	// Getter e Setter
    public int getIDCartItem() {
        return IDCartItem;
    }

    public int getIDProdotto() {
        return IDProdotto;
    }

    public void setIDProdotto(int IDProdotto) {
        this.IDProdotto = IDProdotto;
    }

    public int getIDCarrello() {
        return IDCarrello;
    }

    public void setIDCarrello(int IDCarrello) {
        this.IDCarrello = IDCarrello;
    }
}
