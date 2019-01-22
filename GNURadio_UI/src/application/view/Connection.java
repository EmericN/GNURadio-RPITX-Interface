package application.view;

import application.Main;
import application.controler.FxSocketClient;
import application.controler.FxSocketServer;
import application.model.SocketListener;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class Connection {
	
	@FXML private TextField ipAddr;
	@FXML private TextField port;
	@FXML private Button connectButton;
	@FXML private Button serverButton;
	@FXML private TextField textFieldMessage;
	@FXML private TextArea textAreaDisplayMessage;
	@FXML private VBox vBoxMessage;
	@FXML private Label label1;
	private Main main;
	private FxSocketServer socketServer;
	private FxSocketClient socketClient;
	private boolean isServer = false;

	public Connection() { }
	
	 @FXML private void initialize() {
		 
		 connectButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				clientConnect();
			}
		});
		 
		 serverButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				serverConnect();
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
	    }

	 public void setMainApp(Main mainApp) {
	     this.main = mainApp;
	 }
}
