
package application.model;

import java.io.*;
import java.net.*;


public class Client {

    public Client() {
    }
    
    public static void clientStart(String ip, int port) throws Exception {
    	System.out.println("ip : "+ip + "port : "+port);
        Socket socket = new Socket(ip, port);
       
        (new ReceiverThread(socket)).start();
        (new SenderThread(socket)).start();

        //socket.close();
   }

    
}
