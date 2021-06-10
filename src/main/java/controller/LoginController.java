package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Abonat;
import model.Bibliotecar;
import model.UserType;
import model.UtilizatorSistem;
import services.IServices;
import services.ServiceException;


public class LoginController {

    private UserType tip;
    private IServices services;
    private AbonatController abonatCtrl;
    private BibliotecarController bibliotecarCtrl;
    private Abonat abonatCurent;
    private Bibliotecar bibilotecarCurent;
    Parent mainAppParent;

    public void setServices(IServices services,UserType tip) {
        this.services = services;
        this.tip=tip;
    }

    public void setAbonatCtr(AbonatController abonatController) {
        this.abonatCtrl = abonatController;
    }

    public void setBibliotecarCtrl(BibliotecarController bibliotecarController){
        this.bibliotecarCtrl= bibliotecarController;
    }

    public void setParent(Parent mainAppParent) {
        this.mainAppParent = mainAppParent;
    }

    @FXML
    TabPane tabPane;
    @FXML
    TextField usernameTxt;
    @FXML
    PasswordField parolaTxt;



    public void autentificare(ActionEvent actionEvent) {
        String user=usernameTxt.getText();
        String passwd=parolaTxt.getText();
        UtilizatorSistem userCurent = new UtilizatorSistem();
        userCurent.setUsername(user);
        userCurent.setParola(passwd);

        if(!user.equals("") && !passwd.equals("")){
            try {

                if(tip==UserType.abonat){
                   Abonat abonat = new Abonat();
                   abonat.setUsername(userCurent.getUsername());
                   abonat.setParola(userCurent.getParola());
                   abonat=services.logareAbonat(abonat,abonatCtrl);
                    abonatCtrl.setUser(abonat);
                    abonatCurent =abonat;
                    Stage stage = new Stage();
                    stage.setTitle("Catalog abonat pentru "+abonat.getUsername());
                    stage.setScene(new Scene(mainAppParent));

                    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            abonatCtrl.logout();
                            System.exit(0);
                        }
                    });
                    stage.show();
                    abonatCtrl.setUser(abonatCurent);
                    ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

                }else{
                    Bibliotecar bibliotecar = new Bibliotecar();
                    bibliotecar.setUsername(userCurent.getUsername());
                    bibliotecar.setParola(userCurent.getParola());
                    bibliotecar=services.logareBibliotecar(bibliotecar,bibliotecarCtrl);
                    bibliotecarCtrl.setUser(bibliotecar);
                    bibilotecarCurent =bibliotecar;

                    Stage stage = new Stage();
                    stage.setTitle("Catalog abonat pentru "+ bibilotecarCurent.getUsername());
                    stage.setScene(new Scene(mainAppParent));

                    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            bibliotecarCtrl.logout();
                            System.exit(0);
                        }
                    });
                    stage.show();
                    bibliotecarCtrl.setUser(bibilotecarCurent);
                    ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
                }




            }catch (ServiceException ex){
                ex.printStackTrace();
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error ");
                alert.setContentText("Error while starting app "+ex);
                alert.showAndWait();
            }
        }
    }

    public void changeTab(ActionEvent actionEvent) {
        tabPane.getSelectionModel().select(1);
    }
    @FXML
    TextField numeField;
    @FXML
    TextField cnpField;
    @FXML
    TextArea adrField;
    @FXML
    TextField telField;
    @FXML
    TextField userField;
    @FXML
    PasswordField passField;
    @FXML
    PasswordField pass2Field;
    @FXML
    Label codUnic;


    public void contNou(ActionEvent actionEvent) {
        String nume="";
        String cnp="";
        String adresa="";
        String tel="";
        String username="";
        String password="";

        String error="";

        if(numeField.getText()!=null){
            nume=numeField.getText();
        }else {
            error+="Campul nume e obligatoriu!\n";
        }
        if(cnpField.getText()!=null){
            cnp=cnpField.getText();
        }else{
            error+="Campul CNP este obligatoriu\n";
        }
        if(telField.getText()!=null){
            tel=telField.getText();
        }
        else{
            error+="Campul telefon este obligatoriu!\n";
        }
        adresa=adrField.getText();
        if(passField.getText()!=null && passField.getText().equals(pass2Field.getText())){
            password=passField.getText();

        }else{
            error+="Parolele trebuie sa fie identice!\n";
        }
        if(userField.getText()!=null){
            username=userField.getText();
        }else{
            error+="Username nu poate fi vid!\n";
        }
        if(error==""){
            if(tip==UserType.abonat){
                Abonat newAbonat = new Abonat(cnp,nume,adresa,tel,0L, username,password);
                Abonat newUser = services.adaugaAbonat(newAbonat);
                codUnic.setText(newUser.toString());
                try {

                    services.logareAbonat((Abonat)newUser,abonatCtrl);
                    abonatCtrl.setUser((Abonat)newUser);
                    abonatCurent =(Abonat)newUser;
                    Stage stage = new Stage();
                    stage.setTitle("Catalog abonat pentru "+ abonatCurent.getUsername());
                    stage.setScene(new Scene(mainAppParent));

                    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            abonatCtrl.logout();
                            System.exit(0);
                        }
                    });
                    stage.show();
                    abonatCtrl.setUser(abonatCurent);
                    ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

                }catch (ServiceException ex){
                    ex.printStackTrace();
                    Alert alert=new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error ");
                    alert.setContentText("Error while starting app "+ex);
                    alert.showAndWait();
                }
            }else{
                Bibliotecar newBibliotecar = new Bibliotecar(cnp,nume,adresa,tel,0L, username,password);
                Bibliotecar newUser = services.adaugaBibliotecar(newBibliotecar);

                codUnic.setText(newUser.toString());
                try {

                    services.logareBibliotecar(newUser,bibliotecarCtrl);

                    bibliotecarCtrl.setUser(newUser);
                    bibilotecarCurent =newUser;

                    Stage stage = new Stage();
                    stage.setTitle("Catalog abonat pentru "+ bibilotecarCurent.getUsername());
                    stage.setScene(new Scene(mainAppParent));

                    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            bibliotecarCtrl.logout();
                            System.exit(0);
                        }
                    });
                    stage.show();
                    ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

                }catch (ServiceException ex){
                    ex.printStackTrace();
                    Alert alert=new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error ");
                    alert.setContentText("Error while starting app "+ex);
                    alert.showAndWait();
                }
            }


        }else{
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setContentText(error);
            alert.showAndWait();
        }
    }


}
