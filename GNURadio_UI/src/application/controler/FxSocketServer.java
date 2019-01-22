
package application.controler;

import java.net.*;

import application.model.GenericSocket;
import application.model.SocketListener;


public class FxSocketServer extends GenericSocket
        implements SocketListener {

    private SocketListener fxListener;
    private ServerSocket serverSocket;

    @Override
    public void onMessage(final String line) {
        javafx.application.Platform.runLater(new Runnable() {
            @Override
            public void run() {
                fxListener.onMessage(line);
            }
        });
    }

    /*@Override
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
            serverSocket = new ServerSocket(9999);
            System.out.println("Server started");
            serverSocket.setReuseAddress(true);
            socketConnection = serverSocket.accept();
            System.out.println("Client accepted");
            serverStatusClient(true);
        } catch (Exception e) {
        	serverStatusClient(false);
                e.printStackTrace();
        }
    }

    @Override
    protected void closeAdditionalSockets() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                connectivityStatus(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public FxSocketServer(SocketListener fxListener) {
        this.fxListener = fxListener;
    }

	@Override
	public void serverStatusClient(boolean isClientConnected) {
		javafx.application.Platform.runLater(new Runnable() {
            @Override
            public void run() {
                fxListener.serverStatusClient(isClientConnected);
            }
        });
	}

	@Override
	public void clientStatusClient(boolean isClientConnected) {
		
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
