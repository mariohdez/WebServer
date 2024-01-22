package webserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpConnectionWorkerThread extends Thread {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);

    private final Socket socket;

    public HttpConnectionWorkerThread(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            LOGGER.info("Accepted connection!" + socket.getInetAddress());

            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            String html = "<html><head><title>Simple Java HTTP Server</title></head><body><h1>This page was served using my Simple Java HTTP Server.</h1></body></html>";
            final String CRLF= "\n\r";

            String response =
                    "HTTP/1.1 200 OK" + CRLF
                            + "Content-Length: " + html.getBytes().length + CRLF +
                            CRLF +
                            html + CRLF;

            outputStream.write(response.getBytes());

            LOGGER.info("Connection processing finished.");
            try {
                sleep(5000);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        } catch (IOException ioe) {
            LOGGER.error("Error with communication.", ioe);
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }

            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }

            if (this.socket != null) {
                try {
                    this.socket.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }
}
