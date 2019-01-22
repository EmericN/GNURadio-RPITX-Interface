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
	 private AnchorPane mainContainer;

	@Override
	public void start(Stage primaryStage) {
		mainStage = primaryStage;
		mainStage.setTitle("COMRadio UI");
		
	    initialisationContenu();
	}

    private void initialisationContenu() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/UIView.fxml"));
        try {
			mainContainer = (AnchorPane) loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
        Scene scene = new Scene(mainContainer);
		mainStage.setScene(scene);
		mainStage.show();
		Connection controleur = loader.getController();
		controleur.setMainApp(this);
    }

	public static void main(String[] args) {
		launch(args);
	}
}
