package model.javaBeans;

public class libro {

	private int id;
	private String editore;
	private String scrittore;
	private String isbn;
	private int idGenere;
	private int idProdotto;
	
	public libro(int id, String editore, String scrittore, String isbn, int idGenere, int idProdotto) {

		this.id = id;
		this.editore = editore;
		this.scrittore = scrittore;
		this.isbn = isbn;
		this.idGenere = idGenere;
		this.idProdotto = idProdotto;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEditore() {
		return editore;
	}

	public void setEditore(String editore) {
		this.editore = editore;
	}

	public String getScrittore() {
		return scrittore;
	}

	public void setScrittore(String scrittore) {
		this.scrittore = scrittore;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public int getIdGenere() {
		return idGenere;
	}

	public void setIdGenere(int idGenere) {
		this.idGenere = idGenere;
	}

	public int getIdProdotto() {
		return idProdotto;
	}

	public void setIdProdotto(int idProdotto) {
		this.idProdotto = idProdotto;
	}

}
