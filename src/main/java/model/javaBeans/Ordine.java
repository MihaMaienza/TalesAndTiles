package model.javaBeans;

import java.util.Date;

public class Ordine {

    int IDOrdine;
    String username;
    float prezzoTotale;
    Date dataConsegna;
    Date dataOrdine;
    String nomeConsegna;
    String cognomeConsegna;
    int cap;
    String via;
    String citta;
    String numCarta;
    byte[] pdf; // Nuovo campo per memorizzare il PDF

    public Ordine(int IDOrdine, String username, float prezzoTotale, Date dataConsegna, Date dataOrdine,
                  String nomeConsegna, String cognomeConsegna, int cap, String via, String citta, String numCarta) {
        this.IDOrdine = IDOrdine;
        this.username = username;
        this.prezzoTotale = prezzoTotale;
        this.dataConsegna = dataConsegna;
        this.dataOrdine = dataOrdine;
        this.nomeConsegna = nomeConsegna;
        this.cognomeConsegna = cognomeConsegna;
        this.cap = cap;
        this.via = via;
        this.citta = citta;
        this.numCarta = numCarta;
    }

    public int getIDOrdine() {
        return IDOrdine;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public float getPrezzoTotale() {
        return prezzoTotale;
    }

    public void setPrezzoTotale(float prezzoTotale) {
        this.prezzoTotale = prezzoTotale;
    }

    public Date getDataConsegna() {
        return dataConsegna;
    }

    public void setDataConsegna(Date dataConsegna) {
        this.dataConsegna = dataConsegna;
    }

    public Date getDataOrdine() {
        return dataOrdine;
    }

    public void setDataOrdine(Date dataOrdine) {
        this.dataOrdine = dataOrdine;
    }

    public String getNomeConsegna() {
        return nomeConsegna;
    }

    public void setNomeConsegna(String nomeConsegna) {
        this.nomeConsegna = nomeConsegna;
    }

    public String getCognomeConsegna() {
        return cognomeConsegna;
    }

    public void setCognomeConsegna(String cognomeConsegna) {
        this.cognomeConsegna = cognomeConsegna;
    }

    public int getCap() {
        return cap;
    }

    public void setCap(int cap) {
        this.cap = cap;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getNumCarta() {
        return numCarta;
    }

    public void setNumCarta(String numCarta) {
        this.numCarta = numCarta;
    }

    public byte[] getPdf() {
        return pdf;
    }

    public void setPdf(byte[] pdf) {
        this.pdf = pdf;
    }
}
