package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.*;
import services.IObserver;
import services.IServices;
import services.ServiceException;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.Period;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class BibliotecarController implements IObserver {

    private IServices service;
    private Bibliotecar mainUser;

    public void setService(IServices service) {
        this.service = service;
        List<Carte> carteList= StreamSupport.stream(service.getCarti().spliterator(), false).collect(Collectors.toList());
        updateazaCatalogCarti(carteList);

    }
    ObservableList<Carte> modelCarti= FXCollections.observableArrayList();
    ObservableList<Imprumut> modelImprumuturi = FXCollections.observableArrayList();

    public void setUser(Bibliotecar admin) {
        mainUser=admin;
        List<Imprumut> imprumuturi = StreamSupport.stream(service.getImprumuturi(mainUser).spliterator(), false).collect(Collectors.toList());
        //initListaImprumuturi(imprumuturi);
        initInfoDetails(admin);
    }
    @FXML
    TabPane tabAdmin;
    @FXML
    TableView<Carte> tableTab1;
    @FXML
    TableColumn<Long,Carte> isbnColumnTab1;
    @FXML
    TableColumn<String,Carte> numeColumnTab1;
    @FXML
    TableColumn<String ,Carte> autorColumnTab1;
    @FXML
    TableColumn<String ,Carte> edituraColumnTab1;
    @FXML
    TableColumn<Byte,Carte> statusColumnTab1;



    private void updateazaCatalogCarti(List<Carte> carteList) {

        modelCarti.setAll(carteList);
        isbnColumnTab1.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        numeColumnTab1.setCellValueFactory(new PropertyValueFactory<>("Nume"));
        autorColumnTab1.setCellValueFactory(new PropertyValueFactory<>("Autor"));
        edituraColumnTab1.setCellValueFactory(new PropertyValueFactory<>("Editura"));
        statusColumnTab1.setCellValueFactory(new PropertyValueFactory<>("Disponibilitate"));


        tableTab1.setItems(modelCarti);

    }
    private void refreshCatalog(Carte carte,Integer type) {
        if(type==1){
            modelCarti.forEach(c->{
                if(c.getISBN().equals(carte.getISBN())){
                    System.out.println("AM GASIT CARTEA: "+c);
                    c.setDisponibilitate(carte.getDisponibilitate());
                }
            });
        }else{
            if (type==2){
                modelCarti.add(carte);
            }
            else if(type==3){
                modelCarti.forEach(c->{
                    if(c.getISBN()==carte.getISBN()){
                        System.out.println("AM GASIT CARTEA: "+c);
                        c.setAutor(carte.getAutor());
                        c.setNume(carte.getNume());
                        c.setEditura(carte.getEditura());
                    }
                });}
            else{
                modelCarti.remove(carte);
            }
        }


        //modelCarti.forEach(System.out::println);
        tableTab1.refresh();
        tableTab1.setItems(modelCarti);
    }

    @FXML
    CheckBox numeCheck;
    @FXML
    CheckBox autorCheck;
    @FXML
    CheckBox codCheck;

    @FXML
    TextField numeField;
    @FXML
    TextField autorField;
    @FXML
    TextField codField;

    public void cautaCarti(ActionEvent actionEvent) {

        String nume="";
        String autor="";
        Long codISBN=0L;

        if(numeCheck.isSelected())
            nume=numeField.getText();
        if(autorCheck.isSelected())
            autor=autorField.getText();
        try{
            if(codCheck.isSelected())
                codISBN=Long.getLong(codField.getText());
        }catch (Exception e){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setContentText("Va rog introduceti un cod ISBN format doar din cifre!");
            alert.showAndWait();
        }

        Iterable<Carte> cartiFiltered;
        try {
            cartiFiltered =  service.filtreazaCarti(nume,autor,codISBN);
            List<Carte> carteList= StreamSupport.stream(cartiFiltered.spliterator(), false).collect(Collectors.toList());

            updateazaCatalogCarti(carteList);

        } catch (ServiceException e){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setContentText("Va rog introduceti un cod ISBN format doar din cifre!");
            alert.showAndWait();
        }


    }
    //tab2
    @FXML
    TextField codAbonat;
    @FXML
    TextField codCarte;
    @FXML
    TextArea status;


    @FXML
    TableView<Imprumut> tableTab3;
    @FXML
    TableColumn<Imprumut,Long> idColumnTab3;
    @FXML
    TableColumn<Imprumut,Long> isbnColumnTab3;
    @FXML
    TableColumn<Imprumut,String> numeColumnTab3;
    @FXML
    TableColumn<Imprumut, LocalDate> dataImpColumnTab3;
    @FXML
    TableColumn<Imprumut,Integer> dataResColumnTab3;
    @FXML
    TableColumn<Imprumut,Byte> statusColumnTab3;


    public void cautaImprumut(ActionEvent actionEvent) {
        String cA=codAbonat.getText();
        String cB=codCarte.getText();
        Long codA=Long.parseLong(cA);
        Long codC = Long.parseLong(cB);
        System.out.println("Caut imprumut cu userID: "+codA+" si carteID: "+codC);

        List<Imprumut> imprumutList= StreamSupport.stream(service.cautaImprumuturi(codA,codC).spliterator(), false).collect(Collectors.toList());

        if(imprumutList.size()==0){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setContentText("Nu exista acest imprumut!Date incorecte\n");

            alert.showAndWait();
        }
        else{

            status.setText("");
            initImprumuturi(imprumutList);
        }
    }

    private void initImprumuturi(List<Imprumut> imprumuturi) {
        modelImprumuturi.setAll(imprumuturi);
        idColumnTab3.setCellValueFactory(new PropertyValueFactory<>("idImprumut"));
        //isbnColumnTab3.setCellValueFactory(c->new SimpleStringProperty(c.getValue().getCarte().getISBN().toString()));

        isbnColumnTab3.setCellValueFactory(new PropertyValueFactory<>("Isbn"));
        //numeColumnTab3.setCellValueFactory(new PropertyValueFactory<>("NumeCarte"));
        numeColumnTab3.setCellValueFactory(c->new SimpleStringProperty(service.getCarte(c.getValue().getIsbn()).getNume()));

        dataImpColumnTab3.setCellValueFactory(new PropertyValueFactory<>("DataImprumut"));
        dataResColumnTab3.setCellValueFactory(new PropertyValueFactory<>("IntervalImprumut"));

        statusColumnTab3.setCellValueFactory(new PropertyValueFactory<>("StareImprumut"));


        tableTab3.setItems(modelImprumuturi);

    }
    public void calculeazaPenalizare(MouseEvent mouseEvent) {
        Imprumut imprumut = tableTab3.getSelectionModel().getSelectedItem();

        System.out.println("IMPRUMUT SELECTAT: "+imprumut);
        if(imprumut!=null){
            if(imprumut.getStareImprumut()){
                LocalDateTime dataImprumut = imprumut.getDataImprumut();
                Integer intervalImprumut = imprumut.getIntervalImprumut();
                Integer zileDeLaImprumut = Period.between(dataImprumut.toLocalDate(),LocalDate.now()).getDays();
                Double penalizare = 0d;
                if(zileDeLaImprumut>intervalImprumut){
                    penalizare= (zileDeLaImprumut-intervalImprumut)*0.7;
                }
                status.setText("Nereturnat/Penalizare: "+penalizare);
                System.out.println(Period.between(dataImprumut.toLocalDate(),LocalDate.now()).getDays());

            }else {
                status.setText("Returnat/Penalizare: 0");
            }
            }
    }

    public void returneazaCarte(ActionEvent actionEvent) {
        Imprumut imprumut = tableTab3.getSelectionModel().getSelectedItem();

        if(imprumut!=null){
            if(imprumut.getStareImprumut()==false){
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error ");
                alert.setContentText("Imprumut deja restituit!\n");

                alert.showAndWait();
            }else{
                service.restituieImprumut(imprumut);
                Alert alert=new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Info ");
                alert.setContentText("Imprumut restituit cu succes!\n");

                alert.showAndWait();
                status.setText("");
                codCarte.setText("");

            }

        }else{
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setContentText("Nu ati selectat niciun imprumut\n");

            alert.showAndWait();
        }

    }
    public void returneaza1Carte(ActionEvent actionEvent) {
        returneazaCarte(actionEvent);
        codAbonat.setText("");
    }

    public void returneazaCarteRepetat(ActionEvent actionEvent) {
        returneazaCarte(actionEvent);
    }


    //tab 3 ADAUGA CARTE
    @FXML
    TextField numeFieldTab3;
    @FXML
    TextField autorFieldTab3;
    @FXML
    TextField edituraFieldTab3;

    public void adaugaCarte(ActionEvent actionEvent) {
        String nume = numeFieldTab3.getText();
        String autor = autorFieldTab3.getText();
        String editura = edituraFieldTab3.getText();
        if(nume!="" && autor!="" && editura!=""){
            Carte carteNoua = new Carte(0L,nume,autor,editura,true);
            service.adaugaCarte(carteNoua);
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info ");
            alert.setContentText("Codul isbn generat este: "+carteNoua.getISBN()+"\n");

            alert.showAndWait();
        }else{
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setContentText("Niciun camp nu poate fi lasat vid!\n");

            alert.showAndWait();
        }

    }

    //tab 4 MODIFICA CARTE
    @FXML
    TextField isbnTab4;
    @FXML
    TextField numeVechiTab4;
    @FXML
    TextField autorVechiTab4;
    @FXML
    TextField edituraVecheTab4;

    @FXML
    TextField numeNouTab4;
    @FXML
    TextField autorNouTab4;
    @FXML
    TextField edituraNouaTab4;

    public void cautaCarte(ActionEvent actionEvent) {
        Long isbn = Long.parseLong(isbnTab4.getText());
        Carte carte = service.cautaCarte(isbn);
        if(carte==null){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setContentText("Nu exista nicio carte cu acest ISBN\n");

            alert.showAndWait();
        }else{
            //completex campurile
            numeVechiTab4.setText(carte.getNume());
            autorVechiTab4.setText(carte.getAutor());
            edituraVecheTab4.setText(carte.getEditura());
        }
    }
    public void modificaCarte(ActionEvent actionEvent) {

        Long isbn = Long.parseLong(isbnTab4.getText());
        String numeNou = numeNouTab4.getText();
        String autorNou = autorNouTab4.getText();
        String edituraNoua = edituraNouaTab4.getText();
        if(numeNou==""){
            numeNou = numeVechiTab4.getText();

        }
        if(autorNou==""){
            autorNou=autorVechiTab4.getText();
        }
        if(edituraNoua==""){
            edituraNoua=edituraVecheTab4.getText();
        }
        Carte book = service.cautaCarte(isbn);
        book.setNume(numeNou);
        book.setAutor(autorNou);
        book.setEditura(edituraNoua);

        service.updateCarte(book);
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info ");
        alert.setContentText("Carte modificata cu succes\n");

        alert.showAndWait();

        numeVechiTab4.setText(numeNou);
        autorVechiTab4.setText(autorNou);
        edituraVecheTab4.setText(edituraNoua);

        numeNouTab4.setText("");
        autorNouTab4.setText("");
        edituraNouaTab4.setText("");


    }
    //tab 5 STERGE CARTE
    @FXML
    TextField isbnTab5;
    @FXML
    TextField numeTab5;
    @FXML
    TextField autorTab5;
    @FXML
    TextField edituraTab5;

    public void cautaCarteDeSters(ActionEvent actionEvent) {
        Long isbn = Long.parseLong(isbnTab5.getText());
        Carte carte = service.cautaCarte(isbn);
        if(carte==null){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setContentText("Nu exista nicio carte cu acest ISBN\n");

            alert.showAndWait();
        }else{
            //completex campurile
            numeTab5.setText(carte.getNume());
            autorTab5.setText(carte.getAutor());
            edituraTab5.setText(carte.getEditura());
        }
    }
    public void stergeCarte(ActionEvent actionEvent) {
        Long isbn = Long.parseLong(isbnTab5.getText());
        Carte carte = service.cautaCarte(isbn);
        if(carte!=null){
            Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete ");
            alert.setContentText("Esti sigur ca doresti sa stergi cartea selectata?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent())
            // alert is exited, no button has been pressed.
            {
                if (result.get() == ButtonType.OK) {

                    alert.close();
                    service.stergeCarte(carte);

                } else if (result.get() == ButtonType.CANCEL) {
                    alert.close();
                }
            }
        }
    }


    //tab 6 INFO
    @FXML
    TextField numeTab4;
    @FXML
    TextField telTab4;
    @FXML
    TextArea adrTab4;
    @FXML
    TextField userTab4;
    @FXML
    PasswordField oldPassTab4;
    @FXML
    PasswordField newPassTab4;
    @FXML
    Label codTab4;

    private void initInfoDetails(Bibliotecar admin) {
        codTab4.setText(admin.getCodUnic().toString());
        numeTab4.setText(admin.getNume());
        telTab4.setText(admin.getTelefon());
        adrTab4.setText(admin.getAdresa());
        userTab4.setText(admin.getUsername());

    }

    public void editNume(MouseEvent mouseEvent) {
        if(numeTab4.isDisabled()){
            numeTab4.setDisable(false);
        }else{
            //save the changes
            numeTab4.setDisable(true);
        }
    }

    public void editUsername(MouseEvent mouseEvent) {
        if(userTab4.isDisabled()){
            userTab4.setDisable(false);
        }
        else{
            numeTab4.setDisable(true);
        }
    }

    public void editTel(MouseEvent mouseEvent) {
        if(telTab4.isDisabled()){
            telTab4.setDisable(false);
        }
        else{
            telTab4.setDisable(true);
        }
    }

    public void editAdr(MouseEvent mouseEvent) {
        if(adrTab4.isDisabled()){
            adrTab4.setDisable(false);
        }
        else{
            adrTab4.setDisable(true);
        }
    }

    public void editParola(MouseEvent mouseEvent) {
        if(oldPassTab4.isDisabled()){
            oldPassTab4.setDisable(false);
        }else{
            if(newPassTab4.isDisabled() && oldPassTab4.getText().equals(mainUser.getParola())){
                newPassTab4.setDisable(false);
            }
            else{


                if(newPassTab4.getText()==""){
                    oldPassTab4.setText("");
                    oldPassTab4.setDisable(true);
                    newPassTab4.setText("");
                    newPassTab4.setDisable(true);
                }

            }
        }
    }

    public void modificaInformatii(MouseEvent mouseEvent) {
        String nume = numeTab4.getText();
        String tel = telTab4.getText();
        String adr = adrTab4.getText();
        String user = userTab4.getText();
        String pass = newPassTab4.getText();

        if(nume!=""){
            mainUser.setNume(nume);
        }
        if(tel!=""){
            mainUser.setTelefon(tel);
        }
        if(adr!=""){
            mainUser.setAdresa(adr);
        }
        if(user!=""){
            mainUser.setUsername(user);
        }
        if(pass!=""){
            mainUser.setParola(pass);
        }


        UtilizatorSistem userFromAdmin = new UtilizatorSistem(mainUser);
        service.updateInfo(userFromAdmin);

        oldPassTab4.setText("");
        newPassTab4.setText("");
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info ");
        alert.setContentText("Informatii modificate! "+mainUser);
        alert.showAndWait();

    }
    //tab7

    public void logout() {
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Error ");
        alert.setContentText("Are you sure you want to leave?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent())
        // alert is exited, no button has been pressed.
        {
            if (result.get() == ButtonType.OK) {

                alert.close();
                try {
                    service.logoutBibliotecar(mainUser,this);
                } catch (ServiceException e) {
                    e.printStackTrace();
                    Alert alert2=new Alert(Alert.AlertType.ERROR);
                    alert2.setTitle("Error ");
                    alert2.setContentText(e.getMessage());
                    alert2.showAndWait();
                }

            } else if (result.get() == ButtonType.CANCEL) {
                alert.close();
            }
        }
    }


    @Override
    public void adaugaImprumut(Imprumut imprumut) {
        refreshCatalog(imprumut.getCarte(),1);
    }

    @Override
    public void restituieImprumut(Imprumut imprumut) {
        refreshCatalog(imprumut.getCarte(),1);
        refreshTabelImprumuturi(imprumut);

    }

    @Override
    public void modificaCatalog(Carte carte, Integer type) {
        refreshCatalog(carte,type);
    }

    private void refreshTabelImprumuturi(Imprumut imprumutRestituit) {

            modelImprumuturi.forEach(imprumut -> {
                if(imprumut.getIdImprumut()==imprumutRestituit.getIdImprumut()){
                    imprumut.setStareImprumut(imprumutRestituit.getStareImprumut());
                }
            });
            tableTab3.setItems(modelImprumuturi);

    }
}
