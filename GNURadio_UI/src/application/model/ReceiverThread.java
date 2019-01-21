package application.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReceiverThread extends Thread{
	
	BufferedReader bReader;
	
	public ReceiverThread(Socket workerSocket) {
		
	try {
		bReader = new BufferedReader(
		new InputStreamReader(workerSocket.getInputStream()));
	} catch (IOException e) {
	  e.printStackTrace();
	}
	}
         
	public void run() {
		
		while(true){
	 	String messageReceive = null;
	 		try {
	 			messageReceive = bReader.readLine();
	 		} catch (IOException e) {
			  e.printStackTrace();
	 		  }
		System.out.println("Received message : " + messageReceive);
		}
	}
}
