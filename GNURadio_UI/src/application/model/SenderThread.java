package application.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;

public class SenderThread extends Thread{
	
	PrintWriter pWriter;
	
	public SenderThread(Socket workerSocket) {

	try {
		pWriter = new PrintWriter(new BufferedWriter(
		new OutputStreamWriter(workerSocket.getOutputStream())),true);
	} catch (IOException e) {
		e.printStackTrace();
	}
	}
	
	public void run() {
		
		while(true){
			Reader reader = new InputStreamReader(System.in);
	        BufferedReader input = new BufferedReader(reader);
	        String message = null;
	        
			try {
				message = input.readLine();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
				System.out.println("Message send : " + message);
				pWriter.println(message);
		}
	}
}
