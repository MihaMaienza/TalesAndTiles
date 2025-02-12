package model.javaBeans;

public class WishlistItem {
    private int IDWishlistItem;
    private int IDProdotto;
    private int IDWishlist;
    
    // Costruttore
    public WishlistItem(int IDWishlistItem,int IDWishlist,int IDProdotto) {
        this.IDWishlistItem=IDWishlistItem;
        this.IDProdotto = IDProdotto;
        this.IDWishlist= IDWishlist;
    }

	// Getter e Setter
    public int getIDWishlistItem() {
        return IDWishlistItem;
    }

    public int getIDProdotto() {
        return IDProdotto;
    }

    public void setIDProdotto(int IDProdotto) {
        this.IDProdotto = IDProdotto;
    }

    public int getIDWishlist() {
        return IDWishlist;
    }

    public void setIDWishlist(int IDWishlist) {
        this.IDWishlist = IDWishlist;
    }
}