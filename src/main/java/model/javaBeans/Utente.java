package model.javaBeans;

import java.util.Date;

public class Utente {
    private String username;
    private String pwd;
    private String nome;
    private String cognome;
    private String email;
    private Date dataNascita;
    private String nomeCarta;
    private String cognomeCarta;
    private String numCarta;
    private Date dataScadenza; // Cambiato da String a Date
    private String CVV;
    private String via;
    private String cap;
    private String citta;
    private int tipo;

    public Utente(String username, String pwd, String nome, String cognome, String email, Date dataNascita, 
                  String nomeCarta, String cognomeCarta, String numCarta, Date dataScadenza, String CVV, 
                  String cap, String via, String citta,int tipo) {
        this.username = username;
        this.pwd = pwd;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.dataNascita = dataNascita;
        this.nomeCarta = nomeCarta;
        this.cognomeCarta = cognomeCarta;
        this.numCarta = numCarta;
        this.dataScadenza = dataScadenza;
        this.CVV = CVV;
        this.via = via;
        this.cap = cap;
        this.citta = citta;
        this.tipo= tipo;
    }


    public int getTipo() {
		return tipo;
	}

	public Date getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(Date dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    // Getter e setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNomeCarta() {
        return nomeCarta;
    }

    public void setNomeCarta(String nomeCarta) {
        this.nomeCarta = nomeCarta;
    }

    public String getCognomeCarta() {
        return cognomeCarta;
    }

    public void setCognomeCarta(String cognomeCarta) {
        this.cognomeCarta = cognomeCarta;
    }

    public String getNumCarta() {
        return numCarta;
    }

    public void setNumCarta(String numCarta) {
        this.numCarta = numCarta;
    }

    public String getCVV() {
        return CVV;
    }

    public void setCVV(String CVV) {
        this.CVV = CVV;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }
}
