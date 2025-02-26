USE talesandtiles;

CREATE TABLE Ordine (
	ID INT AUTO_INCREMENT,
    username VARCHAR(30) NOT NULL,
    prezzoTotale FLOAT DEFAULT 0 NOT NULL,
    dataConsegna DATE NOT NULL,
    dataOrdine DATE NOT NULL,
    nomeConsegna VARCHAR(30),
    cognomeConsegna VARCHAR(30),
    cap VARCHAR(5),
    via VARCHAR(70),
    citta VARCHAR (30),
    numCarta VARCHAR(4),
	pdf longblob,
    PRIMARY KEY(ID),

    FOREIGN KEY(username) REFERENCES Utente(username)
        ON UPDATE CASCADE ON DELETE NO ACTION
);

DROP TABLE IF EXISTS Acquisto;

CREATE TABLE Acquisto (
    IDAcquisto INT AUTO_INCREMENT,
    IDOrdine INT NOT NULL,
    IDProdotto INT NULL,
    nome VARCHAR(50) NOT NULL,
    quantita INT DEFAULT 1 NOT NULL,
    immagine MEDIUMBLOB,
    prezzoAq FLOAT DEFAULT 0 NOT NULL,
    ivaAq INT DEFAULT 0 NOT NULL,
    PRIMARY KEY(IDAcquisto),

    FOREIGN KEY(IDOrdine) REFERENCES Ordine(ID)
        ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY(IDProdotto) REFERENCES Prodotto(ID)
        ON UPDATE CASCADE ON DELETE SET NULL
);

DROP TABLE IF EXISTS Wishlist;

CREATE TABLE Wishlist (
IDWishlist INT AUTO_INCREMENT,
`Username`VARCHAR(30),
PRIMARY KEY(IDWishlist),

FOREIGN KEY(Username) REFERENCES Utente(username)
);

DROP TABLE IF EXISTS WishlistItem;

CREATE TABLE WishlistItem (
IDWishlistItem INT AUTO_INCREMENT,
IDWishlist INT,
IDProdotto INT,
PRIMARY KEY(IDWishlistItem),

FOREIGN KEY(IDWishlist) REFERENCES Wishlist(IDWishlist)
);