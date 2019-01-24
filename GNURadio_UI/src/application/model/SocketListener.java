
package application.model;

public interface SocketListener {
    public void onMessage(String line);
    //public void voiceIN(float sampleRate, int sampleSizeInBits,int channels,boolean signed, boolean bigEndian);
    //public void voiceOUT(float sampleRate, int sampleSizeInBits,int channels,boolean signed, boolean bigEndian);
    public void serverStatusClient(boolean isClientConnected);
    public void clientStatusClient(boolean isClientConnected);
    public void connectivityStatus(boolean isConnectivity);
}
