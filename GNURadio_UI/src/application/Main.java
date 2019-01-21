package application;

import java.io.IOException;

import application.view.Connection;
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
		mainStage.setTitle("COMRadio UI");
		
	    initialisationConteneurPrincipal();
	    initialisationContenu();
	}
	
	private void initialisationConteneurPrincipal() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/MainContainer.fxml"));
        try {
        	mainContainer = (BorderPane) loader.load();
            Scene scene = new Scene(mainContainer);
            mainStage.setScene(scene);
            mainStage.show();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private void initialisationContenu() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/UIView.fxml"));
        try {
            AnchorPane appContainer = (AnchorPane) loader.load();
            mainContainer.setCenter(appContainer);
            
            Connection controleur = loader.getController();
            controleur.setMainApp(this);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public static void main(String[] args) {
		launch(args);
	}
}
