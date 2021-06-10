package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

/**
 * 
 */
@Entity
public class Carte extends Entitate<Long>{

    /**
     * Default constructor
     */
    public Carte() {
    }

    /**
     * 
     */
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long ISBN;

    /**
     * 
     */
    public String nume;

    /**
     * 
     */
    public String autor;

    /**
     * 
     */
    public String editura;

    /**
     * 
     */
    public Boolean disponibilitate;

    public Carte(Long ISBN, String nume, String autor, String editura, Boolean disponibilitate) {
        this.ISBN = ISBN;
        this.nume = nume;
        this.autor = autor;
        this.editura = editura;
        this.disponibilitate = disponibilitate;
    }

    public Long getISBN() {
        return ISBN;
    }

    public void setISBN(Long ISBN) {
        this.ISBN = ISBN;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditura() {
        return editura;
    }

    public void setEditura(String editura) {
        this.editura = editura;
    }

    public Boolean getDisponibilitate() {
        return disponibilitate;
    }

    public void setDisponibilitate(Boolean disponibilitate) {
        this.disponibilitate = disponibilitate;
    }

    @Override
    public String toString() {
        return "Carte{" +
                "ISBN='" + ISBN + '\'' +
                ", nume='" + nume + '\'' +
                ", autor='" + autor + '\'' +
                ", editura='" + editura + '\'' +
                ", disponibilitate=" + disponibilitate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carte carte = (Carte) o;
        return Objects.equals(ISBN, carte.ISBN);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ISBN);
    }
}