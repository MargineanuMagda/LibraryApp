package services;

import model.*;
import persistance.CarteRepository;
import persistance.ImprumutRepository;
import persistance.UtilizatorRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceBiblioteca implements IServices{
    private CarteRepository carteRepository;
    private UtilizatorRepository utilizatorRepository;

    private ImprumutRepository imprumutRepository;

    private Map<Long, IObserver> loggedUsers;

    public ServiceBiblioteca(CarteRepository carteRepository, UtilizatorRepository utilizatorRepository, ImprumutRepository imprumutRepository) {
        this.carteRepository = carteRepository;
        this.utilizatorRepository = utilizatorRepository;
        this.imprumutRepository = imprumutRepository;
        loggedUsers=new ConcurrentHashMap<>();
    }
    @Override
    public Abonat logareAbonat(Abonat user,IObserver abonat) throws ServiceException {
        UtilizatorSistem userLogat =  utilizatorRepository.findByUsernameAndPassword(user.getUsername(),user.getParola());
        if(userLogat!=null && userLogat.tip==UserType.abonat){
            user.setCodUnic(userLogat.getCodUnic());
            user.setNume(userLogat.getNume());
            user.setCNP(userLogat.getCNP());
            user.setTelefon(userLogat.getTelefon());
            user.setTip(UserType.abonat);
            user.setAdresa(userLogat.getAdresa());
            user.setId(userLogat.getId());

            if(loggedUsers.get(userLogat.getUsername())!=null)
                throw new ServiceException("User already logged in.");
            loggedUsers.put(userLogat.getCodUnic(), abonat);


            return user;
        }else{
            throw new ServiceException("Utilizator inexistent!");
        }

    }
    @Override
    public Iterable<Carte> getCarti(){
        return carteRepository.findAll();
    }
    @Override
    public Iterable<Carte> filtreazaCarti(String nume, String autor, Long cod) throws ServiceException {
        List<Carte> carti = carteRepository.findFiltered(nume,autor,cod);
        if( carti.size()==0){
            throw new ServiceException("Nu exista carti care sa respecte criteriile cerute!!");
        }
        return carti;
    }

    @Override
    public void adaugaCarte(Carte carte) {
        carteRepository.save(carte);
        System.out.println("Updatam urmatorii useri: ");
        for( IObserver client: loggedUsers.values()){
            client.modificaCatalog(carte,2);
        }
    }



    @Override
    public void imprumutaCarte(Imprumut imprumut) {
        imprumutRepository.save(imprumut);
        //modific disponibilitatea cartii

        Carte carte = imprumut.getCarte();
        carte.setDisponibilitate(false);
        carteRepository.update(carte);

        for( IObserver client: loggedUsers.values()){
            client.adaugaImprumut(imprumut);
        }
    }

    @Override
    public Iterable<Imprumut> getImprumuturi(UtilizatorSistem mainUser) {
        return imprumutRepository.findByUser(mainUser);
    }

    @Override
    public Abonat adaugaAbonat(Abonat newAbonat) {
        UtilizatorSistem user = new UtilizatorSistem(newAbonat);
        newAbonat.setCodUnic(utilizatorRepository.save(user).getCodUnic());
        return newAbonat;

    }

    @Override
    public void updateInfo(UtilizatorSistem mainUser) {
        utilizatorRepository.update(mainUser);
    }

    @Override
    public Carte getCarte(Long isbn) {
        return carteRepository.findOne(isbn);
    }

    @Override
    public Bibliotecar logareBibliotecar(Bibliotecar bibliotecar,IObserver client) throws ServiceException {
        UtilizatorSistem userLogat =  utilizatorRepository.findByUsernameAndPassword(bibliotecar.getUsername(),bibliotecar.getParola());
        if(userLogat!=null&&userLogat.tip==UserType.bibliotecar){
            bibliotecar.setCodUnic(userLogat.getCodUnic());
            bibliotecar.setNume(userLogat.getNume());
            bibliotecar.setCNP(userLogat.getCNP());
            bibliotecar.setTelefon(userLogat.getTelefon());
            bibliotecar.setTip(UserType.bibliotecar);
            bibliotecar.setAdresa(userLogat.getAdresa());

            if(loggedUsers.get(userLogat.getUsername())!=null)
                throw new ServiceException("User already logged in.");
            loggedUsers.put(userLogat.getCodUnic(), client);
            return bibliotecar;
        }else{
            throw new ServiceException("Utilizator inexistent!");
        }
    }

    @Override
    public Bibliotecar adaugaBibliotecar(Bibliotecar newBibliotecar) {
        UtilizatorSistem user = new UtilizatorSistem(newBibliotecar);
        UtilizatorSistem cod = utilizatorRepository.save(user);
        newBibliotecar.setCodUnic(cod.getCodUnic());
        System.out.println("Bibliotecar nou: "+ newBibliotecar);
        return newBibliotecar;
    }

    @Override
    public Iterable<Imprumut> cautaImprumuturi(Long codA, Long codC) {
        return imprumutRepository.findByUserAndBook(codA,codC);

    }

    @Override
    public void restituieImprumut(Imprumut imprumut) {
        imprumut.setStareImprumut(false);
        System.out.println(imprumut);
        System.out.println(imprumut.getCarte());
        Carte c = imprumut.getCarte();
        c.setDisponibilitate(true);
        carteRepository.update(c);
        imprumutRepository.update(imprumut);

        System.out.println("Updatam urmatorii useri: ");
        for( IObserver client: loggedUsers.values()){
            client.restituieImprumut(imprumut);
        }
    }

    @Override
    public Carte cautaCarte(Long isbn) {

        return carteRepository.findOne(isbn);
    }

    @Override
    public void updateCarte(Carte book) {

        carteRepository.update(book);
        System.out.println("Updatam urmatorii useri: ");
        for( IObserver client: loggedUsers.values()){
            client.modificaCatalog(book,3);
        }
    }
    @Override
    public void stergeCarte(Carte book) {
        carteRepository.delete(book.getISBN());
        System.out.println("Updatam urmatorii useri: ");
        for( IObserver client: loggedUsers.values()){
            client.modificaCatalog(book,4);
        }
    }

    @Override
    public void logoutAbonat(Abonat mainUser, IObserver abonatCtr) throws ServiceException {
        IObserver localClient=loggedUsers.remove(mainUser.getCodUnic());
        if (localClient==null)
            throw new ServiceException("User "+mainUser.getId()+" is not logged in.");


    }

    @Override
    public void logoutBibliotecar(Bibliotecar mainUser, IObserver bibliotecarCtr) throws ServiceException {
        IObserver localClient=loggedUsers.remove(mainUser.getCodUnic());
        if (localClient==null)
            throw new ServiceException("User "+mainUser.getId()+" is not logged in.");

    }
}
