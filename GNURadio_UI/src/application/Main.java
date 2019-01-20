package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	 private Stage mainStage;
	 private BorderPane mainContainer;

	@Override
	public void start(Stage primaryStage) {
		mainStage = primaryStage;
	     //Ca ne vous rappelle pas une JFrame ?
		mainStage.setTitle("COMRadio UI");
	     //Nous allons utiliser nos fichier FXML dans ces deux méthodes
	     initialisationConteneurPrincipal();
	     initialisationContenu();
	}
	
	private void initialisationConteneurPrincipal() {
        //On créé un chargeur de FXML
        FXMLLoader loader = new FXMLLoader();
        //On lui spécifie le chemin relatif à notre classe
        //du fichier FXML a charger : dans le sous-dossier vie
        loader.setLocation(Main.class.getResource("view/MainContainer.fxml"));
        try {
            //Le chargement nous donne notre conteneur
        	mainContainer = (BorderPane) loader.load();
            //On définit une scène principale avec notre conteneur
            Scene scene = new Scene(mainContainer);
            //Que nous affectons à notre Stage
            mainStage.setScene(scene);
            //Pour l'afficher
            mainStage.show();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private void initialisationContenu() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/UIView.fxml"));
        try {
            //Nous récupérons notre conteneur qui contiendra les données
            //Pour rappel, c'est un AnchorPane...
            AnchorPane appContainer = (AnchorPane) loader.load();
            //Qui nous ajoutons à notre conteneur principal
            //Au centre, puisque'il s'agit d'un BorderPane
            mainContainer.setCenter(appContainer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public static void main(String[] args) {
		launch(args);
	}
}
