
package application.model;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.net.*;
import java.util.logging.Logger;

public abstract class GenericSocket implements SocketListener {
    
    public int port;
    protected Socket socketConnection = null;
    private BufferedWriter output = null;
    private BufferedReader input = null;
    private boolean ready = false;
    private Thread socketReaderThread;
    private Thread setupThread;
    public final int DEFAULT_PORT = 2015;
    
    public void connect() {
        try {

            setupThread = new SetupThread();
            setupThread.start();
            socketReaderThread = new SocketReaderThread();
            socketReaderThread.start();
        } catch (Exception e) {
        	 e.printStackTrace();
        }  
    }

    public void shutdown() {
        close();
    }

    private void close() {
        try {
            if (socketConnection != null && !socketConnection.isClosed()) {
                socketConnection.close();
            }
            closeAdditionalSockets();
            //onClosedStatus(true);
        } catch (IOException e) {   
        	 e.printStackTrace();

        }
    }

    protected abstract void initSocketConnection() throws SocketException;
    
    protected abstract void closeAdditionalSockets();
    
    private synchronized void waitForReady() {
        while (!ready) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }
    
    private synchronized void notifyReady() {
        ready = true;
        notifyAll();
    }

    public void sendMessage(String msg) {
        try {
            output.write(msg, 0, msg.length());
            output.newLine();
            output.flush();
        } catch (IOException e) {       
        	 e.printStackTrace();
        }
    }

    class SetupThread extends Thread {

        @Override
        public void run() {
            try {
                initSocketConnection();
                if (socketConnection != null && !socketConnection.isClosed()) {
                    input = new BufferedReader(new InputStreamReader(
                            socketConnection.getInputStream()));
                    output = new BufferedWriter(new OutputStreamWriter(
                            socketConnection.getOutputStream()));
                    output.flush();
                }
                notifyReady();
            } catch (IOException e) {
            	 e.printStackTrace();
            	 notifyReady();
                }
            }
        }


    class SocketReaderThread extends Thread {

        @Override
        public void run() {
        	
        	waitForReady();
        	
            try {
                if (input != null) {
                    String line;
                    while ((line = input.readLine()) != null) { 
                        onMessage(line);
                    }
                }
            } catch (IOException e) {
            	 e.printStackTrace();  
            } finally {
                close();
            }
        }
    }
    
    public GenericSocket() {
        this(Constants.instance().DEFAULT_PORT);
    }

    public GenericSocket(int port) {
        this.port = port;
      
    }
}
