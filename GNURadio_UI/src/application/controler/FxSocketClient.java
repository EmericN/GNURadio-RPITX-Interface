
package application.controler;

import java.io.IOException;
import java.net.*;

import application.model.GenericSocket;
import application.model.SocketListener;

public class FxSocketClient extends GenericSocket
        implements SocketListener {

    public String host;
    private SocketListener fxListener;

    @Override
    public void onMessage(final String line) {
        javafx.application.Platform.runLater(new Runnable() {
            @Override
            public void run() {
                fxListener.onMessage(line);
            }
        });
    }
    
    public void voiceOverNetwork(float sampleRate, int sampleSizeInBits, int channels, boolean signed,
    		boolean bigEndian) {
    	javafx.application.Platform.runLater(new Runnable() {
    		public void run() {
             fxListener.voiceOverNetwork(sampleRate,sampleSizeInBits,channels,signed,bigEndian);
         }
    	});
    }

   /* @Override
    public void onClosedStatus(final boolean isClosed) {
        javafx.application.Platform.runLater(new Runnable() {
            @Override
            public void run() {
                fxListener.onClosedStatus(isClosed);
            }
        });
    }*/

    @Override
    protected void initSocketConnection() throws SocketException {
        try {
            socketConnection = new Socket();
            socketConnection.setReuseAddress(true);
            socketConnection.connect(new InetSocketAddress(host, port));
            clientStatusClient(true);
            System.out.println("Client connected");
        } catch (IOException e) {
                e.printStackTrace();
        }
    }

    @Override
    protected void closeAdditionalSockets() {
    	connectivityStatus(false);
    	}
    
    public FxSocketClient(SocketListener fxListener,String host, int port) {
        super(port);
        this.host = host;
        this.fxListener = fxListener;
    }

	@Override
	public void serverStatusClient(boolean isClientConnected) {
	}

	@Override
	public void clientStatusClient(boolean isClientConnected) {
		javafx.application.Platform.runLater(new Runnable() {
            @Override
            public void run() {
                fxListener.clientStatusClient(isClientConnected);
            }
        });
	}

	@Override
	public void connectivityStatus(boolean isConnectivity) {
		javafx.application.Platform.runLater(new Runnable() {
            @Override
            public void run() {
                fxListener.connectivityStatus(isConnectivity);
            }
        });
	}
}
