
package application.model;

import java.io.*;
import java.net.*;


public class Server{
	
    static final int port = 9999;
    
    public Server() {
    }

    public static void serverStart() throws Exception {
        ServerSocket serverSocket = new ServerSocket(port);     
        Socket workerSocket = serverSocket.accept();            

        (new ReceiverThread(workerSocket)).start();
        (new SenderThread(workerSocket)).start();

       //serverSocket.close();
       //workerSocket.close();
   }
    
}
