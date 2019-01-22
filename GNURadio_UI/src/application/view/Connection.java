package application.view;

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

public class Connection {
	
	@FXML private TextField ipAddr;
	@FXML private TextField port;
	@FXML private TextField portCreateServer;
	@FXML private Button connectServer;
	@FXML private Button createServer;
	@FXML private Button clientButton;
	@FXML private Button serverButton;
	@FXML private Button disconnectServer;
	@FXML private Button disconnectClient;
	@FXML private TextField textFieldMessage;
	@FXML private TextArea textAreaDisplayMessage;
	@FXML private VBox vBoxMessage;
	@FXML private Label label1;
	@FXML private Circle serverStatus;
	@FXML private Circle serverStatusClient;
	@FXML private Circle clientStatus;
	@FXML private AnchorPane anchorPaneMain;
	@FXML private AnchorPane anchorPaneChoice;
	@FXML private AnchorPane anchorPaneClient;
	@FXML private AnchorPane anchorPaneServer;
	@FXML private AnchorPane anchorPaneServerStatus;
	@FXML private AnchorPane anchorPaneClientStatus;
	private Main main;
	Pane newLoadedPane;
	private FxSocketServer socketServer;
	private FxSocketClient socketClient;
	private boolean isServer = false;
	private boolean connected;

	public Connection() { }
	
	 @FXML private void initialize() {
		 
	     serverButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				anchorPaneChoice.setVisible(false);
				anchorPaneServer.setVisible(true); 
			}
		 });
		 
		 clientButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					anchorPaneChoice.setVisible(false);
					anchorPaneClient.setVisible(true); 
				}
			 });
		 
		 connectServer.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					 if(anchorPaneClient.isVisible()) {
						 clientConnect();
						 anchorPaneClient.setVisible(false);
						 anchorPaneClientStatus.setVisible(true);
					 }
				}
		 });
		 
		 createServer.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if(anchorPaneServer.isVisible()) {
						serverConnect();
						anchorPaneServer.setVisible(false);
						anchorPaneServerStatus.setVisible(true);
						serverStatus.setFill(Color.GREEN);
						serverStatusClient.setFill(Color.RED);
					}
				}
		 });
		 
		 disconnectServer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(isServer) {
					socketServer.shutdown();
				}
			}
		});
		 
		 disconnectClient.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(!isServer) {
					socketClient.shutdown();					
				}
			}
		});

		 
		 textFieldMessage.setOnAction(event -> {
			 String message = "";
			 message += textFieldMessage.getText();
			 textFieldMessage.clear();
			 
			 if(isServer) {
				 socketServer.sendMessage(message);
			 }else {
				 socketClient.sendMessage(message);
			 }
			 
			 textAreaDisplayMessage.appendText(message + "\n");
		
		 });
	 }
	 
	 private void serverConnect() {
		 socketServer = new FxSocketServer(new FxSocketListener());
		 socketServer.connect();
		 isServer = true;
	    }
	 
	 private void clientConnect() {
		 socketClient = new FxSocketClient(new FxSocketListener(),
				 ipAddr.getText(),Integer.valueOf(port.getText()));
		 socketClient.connect();
	 }
	 
	 class FxSocketListener implements SocketListener {

	        public void onMessage(String messageReceive) {
	            if (messageReceive != null && !messageReceive.equals("")) {
	                //rcvdMsgsData.add(line);
	                textAreaDisplayMessage.appendText(messageReceive + "\n");
	            }
	        }

			@Override
			public void serverStatusClient(boolean isClientConnected) {
				if(isClientConnected) {
					serverStatusClient.setFill(Color.GREEN);
				}
				
			}

			@Override
			public void clientStatusClient(boolean isClientConnected) {
				if(isClientConnected) {
					clientStatus.setFill(Color.GREEN);
				}
			}

			@Override
			public void connectivityStatus(boolean isConnectivity) {
				if(!isConnectivity) {
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
