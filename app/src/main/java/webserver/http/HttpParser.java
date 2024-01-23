package webserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HttpParser {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);

    private static final int SP = 0x20; // 32
    private static final int CR = 0x0D; // 13
    private static final int LF = 0x0A; // 10

    public HttpRequest parseHttpRequest(InputStream inputStream) {
        HttpRequest httpRequest = new HttpRequest();

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);

        parseRequestLine(inputStreamReader, httpRequest);
        parseHeaders(inputStreamReader, httpRequest);
        parseBody(inputStreamReader, httpRequest);

        return  httpRequest;
    }

    private void parseRequestLine(InputStreamReader inputStreamReader, HttpRequest httpRequest) throws IOException {
        int _byte;

        while ((_byte = inputStreamReader.read()) >= 0) {
            if (_byte == CR) {
                _byte = inputStreamReader.read();

                if (_byte == LF) {
                    return;
                }
            }
        }
    }

    private void parseHeaders(InputStreamReader inputStreamReader, HttpRequest httpRequest) {
    }

    private void parseBody(InputStreamReader inputStreamReader, HttpRequest httpRequest) {
    }
}
