package model;

import model.UtilizatorSistem;

import java.util.*;

/**
 * 
 */
public class Abonat extends UtilizatorSistem {

    /**
     * Default constructor
     */
    public Abonat() {
    }

    public Abonat(String CNP, String nume, String adresa, String telefon, long codUnic, String username, String parola) {
        super(CNP, nume, adresa, telefon, codUnic, UserType.abonat, username, parola);
    }
}