package services;

import model.*;

public interface IServices {
    Abonat logareAbonat(Abonat user,IObserver anobat) throws ServiceException;
    Iterable<Carte> getCarti();

    /**
     * @param nume-numele cartii cautate
     * @param autor-numele autorului cautat
     * @param cod-cautare dupa cod ISBN
     * @return
     */
    Iterable<Carte> filtreazaCarti(String nume, String autor, Long cod) throws ServiceException;

    void adaugaCarte(Carte carte);


    void imprumutaCarte(Imprumut imprumut);

    Iterable<Imprumut> getImprumuturi(UtilizatorSistem mainUser);

    Abonat adaugaAbonat(Abonat newAbonat);

    void updateInfo(UtilizatorSistem mainUser);

    Carte getCarte(Long isbn);

    Bibliotecar logareBibliotecar(Bibliotecar bibliotecar,IObserver client) throws ServiceException;

    Bibliotecar adaugaBibliotecar(Bibliotecar newBibliotecar);


    public Iterable<Imprumut> cautaImprumuturi(Long codA, Long codC);

    void restituieImprumut(Imprumut imprumut);

    Carte cautaCarte(Long isbn);

    void updateCarte(Carte book);

    void stergeCarte(Carte carte);

    void logoutAbonat(Abonat mainUser, IObserver abonatCtr) throws ServiceException;

    void logoutBibliotecar(Bibliotecar mainUser, IObserver bibliotecarCtr) throws ServiceException;
}
