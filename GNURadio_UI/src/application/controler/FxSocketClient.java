
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

        } catch (IOException e) {
                e.printStackTrace();
        }
    }

    @Override
    protected void closeAdditionalSockets() {}
    
    public FxSocketClient(SocketListener fxListener,
            String host, int port) {
        super(port);
        this.host = host;
        this.fxListener = fxListener;
    }
}
