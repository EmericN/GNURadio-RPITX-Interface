
package application.model;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.net.*;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public abstract class GenericSocket implements SocketListener {
    
    public int port;
    protected Socket socketConnection = null;
    private BufferedWriter output = null;
    private BufferedReader input = null;
    private InputStream fromServer = null;
    private OutputStream toServer = null;
    private ObjectOutputStream objetToServer =null;
    private ObjectInputStream objetFromServer =null;
    private BufferedOutputStream bos = null;
    private boolean ready = false;
    private Thread socketReaderThread;
    private Thread setupThread;
    private Thread FileReaderThread;
    public final int DEFAULT_PORT = 2015;
    
    public void connect() {
        try {

            setupThread = new SetupThread();
            setupThread.start();
            socketReaderThread = new SocketReaderThread();
            socketReaderThread.start();
          //  FileReaderThread = new FileReaderThread();
            //FileReaderThread.start();
            
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
    
    public void receiveFile() {
    	 try {
    		System.out.println("Receiver FILE !");
    		DataInputStream dis = new DataInputStream(socketConnection.getInputStream());
    		FileOutputStream fos = new FileOutputStream("C:\\testGNU\\VirtualBox-5.2.20-125813-Win.exe");
    		byte[] buffer = new byte[4096];

    		
    		int count;
    		while ((count = dis.read(buffer)) > 0)
    		{
    			fos.write(buffer, 0, count);
    		}
    			
    		fos.close();
    		dis.close();
 
         } catch (IOException e) {
             e.printStackTrace();
         }
    }
    
	public void sendFile(File file) {
		try {
			System.out.println("Sender FILE !");
			DataOutputStream dos = new DataOutputStream(socketConnection.getOutputStream());
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[4096];

			int read;
			while ((read=fis.read(buffer)) > 0) {
				dos.write(buffer,0,read);
			}

			fis.close();
			dos.close(); 
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}


	
		public void voiceOverNetwork(float sampleRate, int sampleSizeInBits,int channels,boolean signed, boolean bigEndian) {
			System.out.println("Lancement de la fonction");
			AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
			
			DataLine.Info targetInfo = new DataLine.Info(TargetDataLine.class, format);
			DataLine.Info sourceInfo = new DataLine.Info(SourceDataLine.class, format);

			try {
				TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
				targetLine.open(format);
				targetLine.start();
				
				SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(sourceInfo);
				sourceLine.open(format);
				sourceLine.start();

				int numBytesRead;
				byte[] targetData = new byte[targetLine.getBufferSize() / 5];
				
				fromServer = new DataInputStream(socketConnection.getInputStream());
		        toServer = new DataOutputStream(socketConnection.getOutputStream());
		        
				while (true) {
			    byte[] readData = new byte[sourceLine.getBufferSize()];
			    
				//System.out.println("On rentre dans le while infie");
					
					numBytesRead = targetLine.read(targetData, 0, targetData.length);
					if (numBytesRead == -1)	break;
					
					toServer.write(targetData);
					toServer.flush();
					
					fromServer.read(readData);
					
					sourceLine.write(readData, 0, numBytesRead);//ecrit dans le hautparleur le son du micro
					//System.out.println("Envoi");
					readData=null;
				}
			}
			catch (Exception e) {
				System.err.println(e);
			}
	}
		
		
		
		
	/*Fin voix*/
	
    class SetupThread extends Thread {

        @Override
        public void run() {
            try {
                initSocketConnection();
                if (socketConnection != null && !socketConnection.isClosed()) {
                    input = new BufferedReader(new InputStreamReader(socketConnection.getInputStream()));
                    output = new BufferedWriter(new OutputStreamWriter(socketConnection.getOutputStream()));
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
    
    class FileReaderThread extends Thread {
    	public void run() {
    		waitForReady();
    		receiveFile();
    	}
    }
    
    
    public GenericSocket() {
        //this(Constants.instance().DEFAULT_PORT);
    }

    public GenericSocket(int port) {
        this.port = port;
      
    }
}
