package application.view;

import java.io.File;
import java.io.IOException;

import application.Main;
import application.controler.FxSocketClient;
import application.controler.FxSocketServer;
import application.model.SocketListener;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class UIApp {

	@FXML
	private TextField ipAddr;
	@FXML
	private TextField port;
	@FXML
	private TextField portCreateServer;
	@FXML
	private TextField nicknameServer;
	@FXML
	private TextField nicknameClient;
	@FXML
	private Button connectServer;
	@FXML
	private Button createServer;
	@FXML
	private Button clientButton;
	@FXML
	private Button serverButton;
	@FXML
	private Button disconnectServer;
	@FXML
	private Button disconnectClient;
	@FXML
	private Button sendMessage;
	@FXML
	private Button sendFile;
	@FXML
	private Button voiceCall;
	@FXML
	private TextField textFieldMessage;
	@FXML
	private TextArea textAreaDisplayMessage;
	@FXML
	private VBox vBoxMessage;
	@FXML
	private Circle serverStatus;
	@FXML
	private Circle serverStatusClient;
	@FXML
	private Circle clientStatus;
	@FXML
	private AnchorPane anchorPaneMain;
	@FXML
	private AnchorPane anchorPaneChoice;
	@FXML
	private AnchorPane anchorPaneClient;
	@FXML
	private AnchorPane anchorPaneServer;
	@FXML
	private AnchorPane anchorPaneServerStatus;
	@FXML
	private AnchorPane anchorPaneClientStatus;

	private Main main;
	private FxSocketServer socketServer;
	private FxSocketClient socketClient;
	private boolean isServer = false;

	public UIApp() {
	}

	@FXML
	private void initialize() {

		//Handler event menu server
		serverButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				anchorPaneChoice.setVisible(false);
				anchorPaneServer.setVisible(true);
			}
		});

		//Handler event menu client
		clientButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				anchorPaneChoice.setVisible(false);
				anchorPaneClient.setVisible(true);
			}
		});

		//Handler event connection server
		connectServer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (anchorPaneClient.isVisible()) {
					clientConnect();
					anchorPaneClient.setVisible(false);
					anchorPaneClientStatus.setVisible(true);
				}
			}
		});

		//Handler event creation server
		createServer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (anchorPaneServer.isVisible()) {
					serverConnect();
					anchorPaneServer.setVisible(false);
					anchorPaneServerStatus.setVisible(true);
					serverStatus.setFill(Color.GREEN);
					serverStatusClient.setFill(Color.RED);
				}
			}
		});

		//Handler event deconnexion server
		disconnectServer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (isServer) {
					socketServer.shutdown();
					anchorPaneChoice.setVisible(true);
					anchorPaneServerStatus.setVisible(false);
				}
			}
		});

		//Handler event deconnexion client
		disconnectClient.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (!isServer) {
					socketClient.shutdown();
					anchorPaneChoice.setVisible(true);
					anchorPaneClientStatus.setVisible(false);
				}
			}
		});

		//Handler event envoyer message
		sendMessage.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String message = "";
				message += textFieldMessage.getText();
				textFieldMessage.clear();

				if (isServer) {
					socketServer.sendMessage(nicknameServer.getText() + " : " + message);
					textAreaDisplayMessage.appendText(nicknameServer.getText() + " : " + message + "\n");
				} else {
					socketClient.sendMessage(nicknameClient.getText() + " : " + message);
					textAreaDisplayMessage.appendText(nicknameClient.getText() + " : " + message + "\n");
				}
			}
		});

		//Handler event envoyer fichier
		sendFile.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (isServer) {
					socketServer.sendFile(new File("E:\\Téléchargements\\VirtualBox-5.2.20-125813-Win.exe"));
				} else {
					socketClient.receiveFile();
				}
			}
		});

		//Handler event conv call
		voiceCall.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (isServer) {
					socketServer.voiceOverNetwork(48000, 16, 2, true, true);
				} else {
					socketClient.voiceOverNetwork(48000, 16, 2, true, true);
				}
			}
		});
	}

	// methode qui crée le serveur avec le port indiqué par l'utilisateur
	private void serverConnect() {
		socketServer = new FxSocketServer(new FxSocketListener(), Integer.valueOf(portCreateServer.getText()));
		socketServer.connect();
		isServer = true;
	}

	// methode qui connecte le client au server
	private void clientConnect() {
		socketClient = new FxSocketClient(new FxSocketListener(), ipAddr.getText(), Integer.valueOf(port.getText()));
		socketClient.connect();
	}

	// classe FxSocketListener qui implémente les méthodes de la classe SocketListener
	class FxSocketListener implements SocketListener {

		// Méthode onMessage() récupère tous les nouveaux message et les affiches dans le conteneur de chat 
		public void onMessage(String messageReceive) {
			if (messageReceive != null && !messageReceive.equals("")) {
				textAreaDisplayMessage.appendText(messageReceive + "\n");
			}
		}

		// Méthode serverStatusClient() qui indique si oui ou non le serveur à accepter la connexion d'un client
		// Si oui alors on change la couleur du rond correspondant
		@Override
		public void serverStatusClient(boolean isClientConnected) {
			if (isClientConnected) {
				serverStatusClient.setFill(Color.GREEN);
			}
		}

		// Méthode clientStatusConnect() qui indique si oui ou non le client à réussi à se sonnecter
		// Si oui alors on change la couleur du rond correspondant
		@Override
		public void clientStatusConnect(boolean isClientConnected) {
			if (isClientConnected) {
				clientStatus.setFill(Color.GREEN);
			}
		}
		
		// Méthode connectivityStatus() qui indique si il y perte de connectivité
		@Override
		public void connectivityStatus(boolean isConnectivity) {
			if (!isConnectivity) {
				clientStatus.setFill(Color.RED);
				serverStatusClient.setFill(Color.RED);
				serverStatus.setFill(Color.RED);

			}
		}
	}

	public void setMainApp(Main mainApp) {
		this.main = mainApp;
	}
}
