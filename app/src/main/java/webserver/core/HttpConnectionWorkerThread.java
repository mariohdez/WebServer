package webserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpParser;
import webserver.http.HttpParsingException;
import webserver.http.HttpRequest;

import java.awt.image.LookupOp;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpConnectionWorkerThread extends Thread {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);
    private Socket socket;

    public HttpConnectionWorkerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            HttpRequest httpRequest = (new HttpParser()).parseHttpRequest(inputStream);

            LOGGER.info("httpRequest {}", httpRequest.toString());

            String html = "<html><head><title>Simple Java HTTP Server</title></head><body><h1>This page was served using my Simple Java HTTP Server</h1></body></html>";

            final String CRLF = "\r\n"; // 13, 10

            String response =
                    "HTTP/1.1 200 OK" + CRLF + // Status Line  :   HTTTP_VERSION RESPONSE_CODE RESPONSE_MESSAGE
                            "Content-Length: " + html.getBytes().length + CRLF + // HEADER
                            CRLF +
                            html +
                            CRLF + CRLF;

            outputStream.write(response.getBytes());
            LOGGER.info(" * Connection Processing Finished.");
        } catch (IOException e) {
            LOGGER.error("Problem with communication", e);
        } catch (HttpParsingException e) {

        } finally {
            try {
                inputStream.close();
                outputStream.close();
                socket.close();
            } catch (IOException e) {
                LOGGER.error("error with closing streams", e);
            }
        }
    }

    public static String readString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream into = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        for (int n; 0 < (n = inputStream.read(buf));) {
            into.write(buf, 0, n);
        }
        into.close();
        return new String(into.toByteArray(), "UTF-8");
    }
}