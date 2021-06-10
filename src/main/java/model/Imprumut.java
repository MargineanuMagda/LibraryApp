package model;

import javax.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;


@MappedSuperclass
public class Imprumut extends Entitate<Long>{

        /**
         * Default constructor
         */
        public Imprumut() {
        }

        public Long idImprumut;
        /**
         *
         */
        public UtilizatorSistem user;

        /**
         *
         */
        public Carte carte;

        /**
         *
         */
        public LocalDateTime dataImprumut;

    public Integer getIntervalImprumut() {
        return intervalImprumut;
    }

    public void setIntervalImprumut(Integer intervalImprumut) {
        this.intervalImprumut = intervalImprumut;
    }

    public Integer intervalImprumut;
        /**
         *
         */
        public Boolean stareImprumut;

    public Boolean getStareImprumut() {
        return stareImprumut;
    }

    public void setStareImprumut(Boolean stareImprumut) {
        this.stareImprumut = stareImprumut;
    }

    public Imprumut(Long idImprumut,UtilizatorSistem user, Carte carte, LocalDateTime dataImprumut, boolean stareImprimut) {
        this.user = user;
        this.idImprumut=idImprumut;
        this.carte = carte;
        this.dataImprumut = dataImprumut;
        this.stareImprumut = stareImprimut;
        this.intervalImprumut=14;
    }

    public UtilizatorSistem getUser() {
        return user;
    }


    public void setUser(UtilizatorSistem user) {
        this.user = user;
    }

    public Carte getCarte() {
        return carte;
    }

    public void setCarte(Carte carte) {
        this.carte = carte;
    }
    public Long getIsbn(){return carte.getISBN();}
    public String getNumeCarte(){return carte.getNume();}

    public Long getIdImprumut() {
        return idImprumut;
    }

    public void setIdImprumut(Long idImprumut) {
        this.idImprumut = idImprumut;
    }

    public LocalDateTime getDataImprumut() {
        return dataImprumut;
    }

    public void setDataImprumut(LocalDateTime dataImprumut) {
        this.dataImprumut = dataImprumut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Imprumut imprumut = (Imprumut) o;
        return Objects.equals(idImprumut, imprumut.idImprumut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idImprumut);
    }

    @Override
    public String toString() {
        return "Imprumut{" +
                "user=" + user.getUsername() +
                ", carte=" + carte.getNume() +
                ", dataImprumut=" + dataImprumut +
                ", stareImprimut=" + stareImprumut +
                ", intervalImprimut=" + intervalImprumut +
                '}';
    }
}

