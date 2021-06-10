package model;

import model.UtilizatorSistem;

import java.util.*;

/**
 * 
 */
public class Bibliotecar extends UtilizatorSistem {

    /**
     * Default constructor
     */
    public Bibliotecar() {
    }

    public Bibliotecar(String CNP, String nume, String adresa, String telefon, long codUnic, String username, String parola) {
        super(CNP, nume, adresa, telefon, codUnic, UserType.bibliotecar, username, parola);
    }
}