package model.javaBeans;

public class gioco {
	private int id;
	private int eta;
	private String casaProduttrice;
	private int genereGioco;
	private int idProdotto;
	
	public gioco(int id, int eta, String casaProduttrice, int genereGioco, int idProdotto) {
		this.id = id;
		this.eta = eta;
		this.casaProduttrice = casaProduttrice;
		this.genereGioco = genereGioco;
		this.idProdotto = idProdotto;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getEta() {
		return eta;
	}
	public void setEta(int eta) {
		this.eta = eta;
	}
	public String getCasaProduttrice() {
		return casaProduttrice;
	}
	public void setCasaProduttrice(String casaProduttrice) {
		this.casaProduttrice = casaProduttrice;
	}
	public int getGenereGioco() {
		return genereGioco;
	}
	public void setGenereGioco(int genereGioco) {
		this.genereGioco = genereGioco;
	}
	public int getIdProdotto() {
		return idProdotto;
	}
	public void setIdProdotto(int idProdotto) {
		this.idProdotto = idProdotto;
	}

	
}
