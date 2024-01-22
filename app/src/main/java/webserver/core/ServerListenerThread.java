package webserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListenerThread extends Thread {
    private final static Logger LOGGER = LoggerFactory.getLogger(ServerListenerThread.class);
    private final String webroot;

    private final ServerSocket serverSocket;

    public ServerListenerThread(int port, String webroot) throws IOException {
        this.webroot = webroot;
        this.serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        try {
            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                LOGGER.info("Kicking off a new HttpConnectionWorkerThread thread.");
                HttpConnectionWorkerThread httpConnectionWorkerThread = new HttpConnectionWorkerThread(socket);
                httpConnectionWorkerThread.start();
            }
        } catch (IOException ioe) {
            LOGGER.error("Error with setting socket.", ioe);
        }
        finally {
            if (serverSocket != null) {
                try {
                    this.serverSocket.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }

    public String getWebroot() {
        return this.webroot;
    }
}
