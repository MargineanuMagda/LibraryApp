package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.*;
import services.IObserver;
import services.IServices;
import services.ServiceException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class AbonatController implements IObserver {
    public void setService(IServices service) {
        this.service = service;
        List<Carte> carteList= StreamSupport.stream(service.getCarti().spliterator(), false).collect(Collectors.toList());
        updateazaCatalogCarti(carteList);

    }



    private IServices service;
    private Abonat mainUser;

    ObservableList<Carte> modelCarti= FXCollections.observableArrayList();
    ObservableList<Imprumut> modelImprumuturi = FXCollections.observableArrayList();

    public void setUser(Abonat abonat) {
        mainUser=abonat;
        List<Imprumut> imprumuturi = StreamSupport.stream(service.getImprumuturi(mainUser).spliterator(), false).collect(Collectors.toList());
        imprumuturi=imprumuturi.stream().filter(x->x.getStareImprumut()==true).collect(Collectors.toList());
        initListaImprumuturi(imprumuturi);
        initInfoDetails(abonat);
    }





    @FXML
    TabPane tabAbonat;
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

    @FXML
    Label isbnLbl;
    @FXML
    Label  numeLbl;
    @FXML
    Label autorLbl;
    @FXML
    Label edituraLbl;
    @FXML
    Label dataLbl;


    public void selectareCarteImprumut(ActionEvent actionEvent) {
        Carte carteDeImprumutat = tableTab1.getSelectionModel().getSelectedItem();
        if(carteDeImprumutat!=null && carteDeImprumutat.getDisponibilitate()==true){
            isbnLbl.setText(carteDeImprumutat.getISBN().toString());
            numeLbl.setText(carteDeImprumutat.getNume());
            autorLbl.setText(carteDeImprumutat.getAutor());
            edituraLbl.setText(carteDeImprumutat.getEditura());
            dataLbl.setText(LocalDateTime.now().toString());
            tabAbonat.getSelectionModel().select(1);
        }else {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setContentText("Selectati mai intai o carte VALIDA din catalog!");
            alert.showAndWait();
        }
    }

    public void inapoiCatalog(ActionEvent actionEvent) {
        isbnLbl.setText("");
        numeLbl.setText("");
        autorLbl.setText("");
        edituraLbl.setText("");
        dataLbl.setText("");
        tabAbonat.getSelectionModel().select(0);
    }

    public void imprumutaCarte(ActionEvent actionEvent) {
        Long isbn = Long.parseLong(isbnLbl.getText());
        String nume = numeLbl.getText();
        String autor = autorLbl.getText();
        String editura = edituraLbl.getText();
        LocalDateTime date = LocalDateTime.parse(dataLbl.getText());
        Carte carte = new Carte(isbn,nume,autor,editura, true);
        carte.setId(isbn);
        UtilizatorSistem user =mainUser;
        System.out.println("MainUser id "+mainUser);
        System.out.println(mainUser);
        Imprumut newImprumut = new Imprumut(0L,user,carte,date,true);
        try {
            service.imprumutaCarte(newImprumut);
            
            //refresh la tabela de imprumuturi
            //refreshTabelImprumuturi(newImprumut);
            //refreshCatalog(newImprumut.getCarte());
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("INFO");
            alert.setContentText("Imprumut facut cu succes!");
            alert.showAndWait();
        }catch (Exception e){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setContentText(e.getMessage());
            System.out.println(e.getMessage());
            alert.showAndWait();
        }
        
        
    }

    private void refreshCatalog(Carte carte,Integer type) {
        if(type==1){
            modelCarti.forEach(c->{
                if(c.getISBN()==carte.getISBN()){
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
    TableView<Imprumut> tableTab3;
    @FXML
    TableColumn<Imprumut,Long> idColumnTab3;
    @FXML
    TableColumn<Imprumut,Long> isbnColumnTab3;
    @FXML
    TableColumn<Imprumut,String> numeColumnTab3;
    @FXML
    TableColumn<Imprumut,LocalDate> dataImpColumnTab3;
    @FXML
    TableColumn<Imprumut,Integer> dataResColumnTab3;


    private void refreshTabelImprumuturi(Imprumut newImprumut) {

        boolean ok =modelImprumuturi.remove(newImprumut);
        if(ok==false){
            System.out.println("nu am gasit niciun imprumut cu acel id");
            if(newImprumut.getUser().getCodUnic()== mainUser.getCodUnic()){
                modelImprumuturi.add(newImprumut);
                tableTab3.setItems(modelImprumuturi);
            }
        }

        else{
            System.out.println("inprumut adaugat");
        }


    }
    private void initListaImprumuturi(List<Imprumut> imprumuturi) {
        modelImprumuturi.setAll(imprumuturi);
        idColumnTab3.setCellValueFactory(new PropertyValueFactory<>("idImprumut"));
        //isbnColumnTab3.setCellValueFactory(c->new SimpleStringProperty(c.getValue().getCarte().getISBN().toString()));

        isbnColumnTab3.setCellValueFactory(new PropertyValueFactory<>("Isbn"));
        //numeColumnTab3.setCellValueFactory(new PropertyValueFactory<>("NumeCarte"));
        numeColumnTab3.setCellValueFactory(c->new SimpleStringProperty(service.getCarte(c.getValue().getIsbn()).getNume()));

        dataImpColumnTab3.setCellValueFactory(new PropertyValueFactory<>("DataImprumut"));
        dataResColumnTab3.setCellValueFactory(new PropertyValueFactory<>("IntervalImprumut"));



        tableTab3.setItems(modelImprumuturi);
    }

    //tab 4 INFO
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

    private void initInfoDetails(Abonat abonat) {
        codTab4.setText(abonat.getCodUnic().toString());
        numeTab4.setText(abonat.getNume());
        telTab4.setText(abonat.getTelefon());
        adrTab4.setText(abonat.getAdresa());
        userTab4.setText(abonat.getUsername());

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


        UtilizatorSistem userFromAbonat = new UtilizatorSistem(mainUser);
        service.updateInfo(userFromAbonat);

        oldPassTab4.setText("");
        newPassTab4.setText("");
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info ");
        alert.setContentText("Informatii modificate! "+mainUser);
        alert.showAndWait();

    }

    //tab 5
    public void logout(){
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Error ");
        alert.setContentText("Are you sure you want to leave?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent())
        // alert is exited, no button has been pressed.
        {
            if (result.get() == ButtonType.OK) {

                try {
                    service.logoutAbonat(mainUser,this);
                } catch (ServiceException e) {
                    e.printStackTrace();
                    Alert alert2=new Alert(Alert.AlertType.ERROR);
                    alert2.setTitle("Error ");
                    alert2.setContentText(e.getMessage());
                    System.out.println(e.getMessage());
                    alert2.showAndWait();
                }
                alert.close();

            } else if (result.get() == ButtonType.CANCEL) {
                alert.close();
            }
        }
    }
    public void handleLogout(ActionEvent actionEvent) {

        logout();
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    @Override
    public void adaugaImprumut(Imprumut imprumut) {
        refreshCatalog(imprumut.getCarte(),1);
        refreshTabelImprumuturi(imprumut);
    }

    @Override
    public void restituieImprumut(Imprumut imprumut) {
        refreshCatalog(imprumut.getCarte(),1);
        refreshTabelImprumuturi(imprumut);

    }

    @Override
    public void modificaCatalog(Carte carte,Integer type) {
        refreshCatalog(carte,type);
    }
}
