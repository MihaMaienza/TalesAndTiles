package model.javaBeans;

public class Wishlist {
		int IDWishlist;
		String username;
		
		public Wishlist(int IDWishlist, String username) {
			this.IDWishlist=IDWishlist;
			this.username=username;
		}
		
		public int getIDWishlist() {
			return IDWishlist;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}
}