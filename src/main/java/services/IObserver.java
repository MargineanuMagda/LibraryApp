package services;

import model.Carte;
import model.Imprumut;

public interface IObserver {
    void adaugaImprumut(Imprumut imprumut);
    void restituieImprumut(Imprumut imprumut);
    void modificaCatalog(Carte carte,Integer type);
}
