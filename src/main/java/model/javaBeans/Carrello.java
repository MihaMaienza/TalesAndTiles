package model.javaBeans;

public class Carrello {
		int IDCarrello;
		String username;
		
		public Carrello(int IDCarrello, String username) {
			this.IDCarrello=IDCarrello;
			this.username=username;
		}
		
		public int getIDCarrello() {
			return IDCarrello;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}
}
