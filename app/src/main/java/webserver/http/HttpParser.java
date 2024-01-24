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

    public HttpRequest parseHttpRequest(InputStream inputStream) throws IOException, HttpParsingException {
        HttpRequest httpRequest = new HttpRequest();

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);

        parseRequestLine(inputStreamReader, httpRequest);
        parseHeaders(inputStreamReader, httpRequest);
        parseBody(inputStreamReader, httpRequest);

        return  httpRequest;
    }

    private void parseRequestLine(InputStreamReader inputStreamReader, HttpRequest httpRequest) throws IOException, HttpParsingException {
        int _byte;
        StringBuilder processingDataBuffer = new StringBuilder();
        boolean methodParsed = false;
        boolean requestTargetParsed = false;

        while ((_byte = inputStreamReader.read()) >= 0) {
            if (_byte == CR) {
                _byte = inputStreamReader.read();

                if (_byte == LF) {
                    try {
                        httpRequest.setHttpVersion(processingDataBuffer.toString());
                    } catch (BadHttpVersionException bhve) {
                        throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                    }

                    processingDataBuffer.delete(0, processingDataBuffer.length());
                    if (!methodParsed || !requestTargetParsed) {
                        throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                    }
                    return;
                } else {
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
            }

            if (_byte == SP) {
                if (!methodParsed) {
                    methodParsed = true;
                    httpRequest.setMethod(processingDataBuffer.toString());
                } else if (!requestTargetParsed) {
                    requestTargetParsed = true;
                    httpRequest.setRequestTarget(processingDataBuffer.toString());
                } else {
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }

                processingDataBuffer.delete(0, processingDataBuffer.length());
            } else {
                processingDataBuffer.append((char)_byte);
                if (!methodParsed) {
                    if (processingDataBuffer.length() > HttpMethod.MAX_LENGTH) {
                        throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
                    }
                }
            }
        }
    }

    private void parseHeaders(InputStreamReader inputStreamReader, HttpRequest httpRequest) {
    }

    private void parseBody(InputStreamReader inputStreamReader, HttpRequest httpRequest) {
    }
}
