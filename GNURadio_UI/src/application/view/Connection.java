package application.view;

import java.net.ServerSocket;

import javax.naming.InitialContext;

import org.omg.CORBA.PUBLIC_MEMBER;

import application.Main;
import application.model.Client;
import application.model.Server;
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
	private Thread server, client;

	public Connection() { }
	
	 @FXML private void initialize() {
		 
		 connectButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				client = new Thread(){
					public void run(){
						Client client = new Client();
						try {
							client.clientStart(ipAddr.getText(),Integer.parseInt(port.getText()));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};
				client.start();
			}
		});
		 
		 serverButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("Server");
				server = new Thread(){
					public void run(){
						Server server = new Server();
						try {
							server.serverStart();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};
				server.start();	
			}
		});
		 
		 textFieldMessage.setOnAction(event -> {
			 String message = "";
			 message += textFieldMessage.getText();
			 System.out.println("le super message est : "+ message);
			 //textFieldMessage.clear();
			 
			 textAreaDisplayMessage.appendText(message = "\n");
			 System.out.println("le super message2 est : "+ message);
			 try {
				//connection.send(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		 });
	 }

	 public void setMainApp(Main mainApp) {
	     this.main = mainApp;
	 }
}
