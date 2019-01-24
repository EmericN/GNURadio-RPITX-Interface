
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
    
    public void receiveFile(String file) {
    	 try {
    		 fromServer = socketConnection.getInputStream();
    		 toServer = new FileOutputStream(file);
             bos = new BufferedOutputStream(toServer);
             byte[] bytes = new byte[8192];
             int count;
             while ((count = fromServer.read(bytes)) >= 0) {
                 bos.write(bytes, 0, count);
             }
             bos.close();
             fromServer.close();
         } catch (IOException e) {
             e.printStackTrace();
         }
    }
    
	public void sendFile(File file) {
		try {
			fromServer = socketConnection.getInputStream();
			toServer = new FileOutputStream(file);
			byte[] bytes = new byte[8192];

	        int count;
	        while ((count = fromServer.read(bytes)) > 0) {
	        	toServer.write(bytes, 0, count);
	        }

	        toServer.close();
	        fromServer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

/*
	public void voiceIN(float sampleRate, int sampleSizeInBits,int channels,boolean signed, boolean bigEndian) {//Voix en entrée
		byte[] data = null;
		try {
		
		AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
		DataLine.Info sourceInfo = new DataLine.Info(SourceDataLine.class, format);//audio
		SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(sourceInfo);
		sourceLine.open(format);
		sourceLine.start();

		
		while (true) {
				
			inputStream = socketConnection.getInputStream();
			inputStream.read(data);
			sourceLine.write(data, 0, data.length);
		}

		}catch(LineUnavailableException | IOException e) {
		}	
	}
	
		public void voiceOUT(float sampleRate, int sampleSizeInBits,int channels,boolean signed, boolean bigEndian) {//Voix en sortie
			try {
				
				AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
				DataLine.Info targetInfo = new DataLine.Info(TargetDataLine.class, format);	
				TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
				targetLine.open(format);
				targetLine.start();
				
				
				
				int numBytesRead;
				byte[] targetData = new byte[targetLine.getBufferSize() / 5];

				while (true) {
					numBytesRead = targetLine.read(targetData, 0, targetData.length);

					if (numBytesRead == -1)	break;
					outputStream.write(targetData);
					outputStream.flush();
					
					//sourceLine.write(targetData, 0, numBytesRead);
					
				}
			}catch (IOException | LineUnavailableException e) {		
			}
		}*/
	
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
				
				//ObjectInputStream objetFromServer = new ObjectInputStream(socketConnection.getInputStream());  //create object streams with the server
				//ObjectOutputStream objetToServer = new ObjectOutputStream(socketConnection.getOutputStream());
				fromServer = new BufferedInputStream(socketConnection.getInputStream());
		        toServer = new BufferedOutputStream(socketConnection.getOutputStream());
				while (true) {
					byte[] readData = new byte[10000];
					System.out.println("On rentre dans le while infie");
					
					numBytesRead = targetLine.read(targetData, 0, targetData.length);

					if (numBytesRead == -1)	break;
					
					
					toServer.write(targetData);
					toServer.flush();
					
					fromServer.read(readData);
					
					sourceLine.write(readData, 0, numBytesRead);//ecrit dans le hautparleur le son du micro
					System.out.println("Envoi");
					readData = null;
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
    
    public GenericSocket() {
        this(Constants.instance().DEFAULT_PORT);
    }

    public GenericSocket(int port) {
        this.port = port;
      
    }
}
