
package application.model;

public class Constants {   
    private Constants() {}
    
    private static class LazyHolder {
        private static final Constants INSTANCE = new Constants();
    }
    
    public static Constants instance() {
        return LazyHolder.INSTANCE;
    }

    public final int DEFAULT_PORT = 2015;
    public final String DEFAULT_HOST = "localhost";

    public final int DEBUG_NONE = 0x0;
    public final int DEBUG_SEND = 0x1;
    public final int DEBUG_RECV = 0x2;
    public final int DEBUG_IO = DEBUG_SEND | DEBUG_RECV;
    public final int DEBUG_EXCEPTIONS = 0x4;
    public final int DEBUG_STATUS = 0x8;
    public final int DEBUG_XMLOUTPUT = 0x10;
    public final int DEBUG_ALL =
            DEBUG_IO | DEBUG_EXCEPTIONS | DEBUG_STATUS | DEBUG_XMLOUTPUT;
}
