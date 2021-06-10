import controller.AbonatController;
import controller.BibliotecarController;
import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import model.Carte;
import model.UserType;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import persistance.*;
import services.IServices;
import services.ServiceBiblioteca;

public class MainFX extends Application {
    public static void main(String[] args) {
        launch(args);
    }


    static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            UtilizatorRepositoryImpl.sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
            CarteRepositoryImpl.sessionFactory=UtilizatorRepositoryImpl.sessionFactory;
            ImprumutRepositoryImpl.sessionFactory = UtilizatorRepositoryImpl.sessionFactory;

        }
        catch (Exception e) {
            StandardServiceRegistryBuilder.destroy( registry );
        }

    }

    static void close(){
        if ( UtilizatorRepositoryImpl.sessionFactory != null ) {
            UtilizatorRepositoryImpl.sessionFactory.close();
        }
        if ( CarteRepositoryImpl.sessionFactory != null ) {
            CarteRepositoryImpl.sessionFactory.close();
        }
        if(ImprumutRepositoryImpl.sessionFactory!=null){
            ImprumutRepositoryImpl.sessionFactory.close();
        }

    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {

            initialize();
            CarteRepository repoCarti = new CarteRepositoryImpl();
            UtilizatorRepository repoUsers = new UtilizatorRepositoryImpl();
            ImprumutRepository repoImprumuturi = new ImprumutRepositoryImpl();

            IServices services = new ServiceBiblioteca(repoCarti,repoUsers, repoImprumuturi);

            System.out.println("Am creat Service si repo");
            Carte carte = new Carte();
            carte.setAutor("Marin Preda");
            carte.setDisponibilitate(true);
            carte.setNume("Cel mai iubit dintre pamanteni");
            carte.setEditura("Nemira");


           /* System.out.println("Am creat cartea: "+carte);
            services.addBook(carte);
            System.out.println("Carte adaugata");

            services.getCarti().forEach(System.out::println);

            services.getUseri().forEach(System.out::println);
*/

            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/login.fxml"));
            Parent root = loader.load();

            LoginController loginController = loader.<LoginController>getController();
            loginController.setServices(services, UserType.abonat);

            FXMLLoader cloader = new FXMLLoader(
                    getClass().getClassLoader().getResource("views/abonat.fxml"));
            Parent croot=cloader.load();

            System.out.println("bbb");


            AbonatController appCtrl =
                    cloader.<AbonatController>getController();
            appCtrl.setService(services);

            loginController.setAbonatCtr(appCtrl);
            loginController.setParent(croot);

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Biblioteca ABONAT");
            primaryStage.show();

            //admin window
            FXMLLoader loaderAdmin = new FXMLLoader(getClass().getResource("views/login.fxml"));
            Parent rootAdmin = loaderAdmin.load();

            LoginController loginControllerAdmin = loaderAdmin.<LoginController>getController();
            loginControllerAdmin.setServices(services, UserType.bibliotecar);

            FXMLLoader cloaderAdmin = new FXMLLoader(
                    getClass().getClassLoader().getResource("views/admin.fxml"));
            Parent crootAdmin=cloaderAdmin.load();

            System.out.println("bbb");


            BibliotecarController appCtrlAdmin =
                    cloaderAdmin.<BibliotecarController>getController();
            appCtrlAdmin.setService(services);

            loginControllerAdmin.setBibliotecarCtrl(appCtrlAdmin);
            loginControllerAdmin.setParent(crootAdmin);

            Scene scene2 = new Scene(rootAdmin);
            Stage stageAdmin = new Stage();
            stageAdmin.setScene(scene2);
            stageAdmin.setTitle("Biblioteca ADMIN");
            stageAdmin.show();




        }catch(Exception e){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setContentText("Error while starting app "+e);
            e.printStackTrace();
            alert.showAndWait();
            close();
        }
    }
}
