package model;

import model.UserType;

import javax.persistence.*;
import java.util.*;

/**
 * 
 */
@Entity
public class UtilizatorSistem extends Entitate<Long>{

    /**
     * Default constructor

     */
    public UtilizatorSistem() {
    }

    public UtilizatorSistem(String CNP, String nume, String adresa, String telefon, long codUnic, UserType tip, String username, String parola) {
        this.CNP = CNP;
        this.nume = nume;
        this.adresa = adresa;
        this.telefon = telefon;
        this.codUnic = codUnic;
        this.tip = tip;
        this.username = username;
        this.parola = parola;
    }

    /**
     * 
     */
    @Id
    public String CNP;

    /**
     * 
     */
    public String nume;

    /**
     * 
     */
    public String adresa;

    /**
     * 
     */
    public String telefon;
    /**
     * 
     */
    public long codUnic;

    /**
     * 
     */
    @Enumerated(EnumType.STRING)
    public UserType tip;

    /**
     * 
     */
    public String username;

    /**
     * 
     */
    public String parola;

    public UtilizatorSistem(Abonat newAbonat) {
        this.CNP = newAbonat.getCNP();
        this.nume = newAbonat.getNume();
        this.adresa = newAbonat.getAdresa();
        this.telefon = newAbonat.getTelefon();
        this.codUnic = newAbonat.getCodUnic();
        this.tip = newAbonat.getTip();
        this.username = newAbonat.getUsername();
        this.parola = newAbonat.getParola();
    }

    public UtilizatorSistem(Bibliotecar newBibliotecar){
        this.CNP = newBibliotecar.getCNP();
        this.nume = newBibliotecar.getNume();
        this.adresa = newBibliotecar.getAdresa();
        this.telefon = newBibliotecar.getTelefon();
        this.codUnic = newBibliotecar.getCodUnic();
        this.tip = newBibliotecar.getTip();
        this.username = newBibliotecar.getUsername();
        this.parola = newBibliotecar.getParola();
    }

    public String getCNP() {
        return CNP;
    }

    public void setCNP(String CNP) {
        this.CNP = CNP;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public Long getCodUnic() {
        return codUnic;
    }

    public void setCodUnic(long codUnic) {
        this.codUnic = codUnic;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "tip")
    public UserType getTip() {
        return tip;
    }

    @Override
    public String toString() {
        return "UtilizatorSistem{" +
                "CNP='" + CNP + '\'' +
                ", nume='" + nume + '\'' +
                ", adresa='" + adresa + '\'' +
                ", telefon='" + telefon + '\'' +
                ", codUnic=" + codUnic +
                ", tip=" + tip +
                ", username='" + username + '\'' +
                ", parola='" + parola + '\'' +
                '}';
    }

    public void setTip(UserType tip) {
        this.tip = tip;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }
}