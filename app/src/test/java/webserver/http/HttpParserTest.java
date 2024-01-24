package webserver.http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpParserTest {
    private HttpParser httpParser;

    @BeforeAll
    public void beforeClass() {
        this.httpParser = new HttpParser();
    }

    @Test
    void parseHttpRequest() throws IOException, HttpParsingException {
        HttpRequest request = this.httpParser.parseHttpRequest(generateValidTestCase());
        Assertions.assertEquals(HttpMethod.GET, request.getMethod());
        Assertions.assertEquals("/", request.getRequestTarget());
    }

    @Test
    void parseHttpRequestWithBadMethodName() throws IOException {
        try {
            HttpRequest request = this.httpParser.parseHttpRequest(generateInvalidMethodNameTestCase());
            fail();
        } catch (HttpParsingException hpe) {
            Assertions.assertEquals(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED, hpe.getErrorCode());
        }
    }

    @Test
    void parseHttpRequestWithMethodNameTooLong() throws IOException {
        try {
            HttpRequest request = this.httpParser.parseHttpRequest(generateInvalidMethodNameTooLongTestCase());
            fail();
        } catch (HttpParsingException hpe) {
            Assertions.assertEquals(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED, hpe.getErrorCode());
        }
    }

    @Test
    void parseHttpRequestWithMethodTooManyItems() throws IOException {
        try {
            HttpRequest request = this.httpParser.parseHttpRequest(generateInvalidRequestLine());
            fail();
        } catch (HttpParsingException hpe) {
            Assertions.assertEquals(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST, hpe.getErrorCode());
        }
    }

    @Test
    void parseHttpRequestWithEmptyRequestLine() throws IOException {
        try {
            HttpRequest request = this.httpParser.parseHttpRequest(generateInvalidEmptyRequestLine());
            fail();
        } catch (HttpParsingException hpe) {
            Assertions.assertEquals(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST, hpe.getErrorCode());
        }
    }

    @Test
    void parseHttpRequestWithRequestLineNoNewLine() throws IOException {
        try {
            HttpRequest request = this.httpParser.parseHttpRequest(generateInvalidRequestLineNoReturn());
            fail();
        } catch (HttpParsingException hpe) {
            Assertions.assertEquals(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST, hpe.getErrorCode());
        }
    }

    @Test
    void parseHttpRequestWithInvalidHttpVersion() throws IOException {
        try {
            HttpRequest request = this.httpParser.parseHttpRequest(generateInvalidHttpVersionTestCase());
            fail();
        } catch (HttpParsingException hpe) {
            Assertions.assertEquals(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST, hpe.getErrorCode());
        }
    }

    @Test
    void parseHttpRequestWithUnsupportedHttpVersion() throws IOException {
        try {
            HttpRequest request = this.httpParser.parseHttpRequest(generateUnsupportedHttpVersionTestCase());
            fail();
        } catch (HttpParsingException hpe) {
            Assertions.assertEquals(HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED, hpe.getErrorCode());
        }
    }

    @Test
    void parseHttpRequestWithSupportedHttpVersion() throws IOException {
        try {
            HttpRequest request = this.httpParser.parseHttpRequest(generateSupportedHttpVersionTestCase());
            Assertions.assertEquals(HttpVersion.HTTP_1_1, request.getBestCompatibleVersion());
            Assertions.assertEquals("HTTP/1.4", request.getOriginalHttpVersion());
        } catch (HttpParsingException hpe) {
            fail();
        }
    }

    private InputStream generateValidTestCase() {
        String rawData = "GET / HTTP/1.1\r\n" +
                "Host: localhost:2004\r\n" +
                "Connection: keep-alive\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Accept:\r\n" +
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Accept-Language: en-US,en;q=0.9\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );

        return inputStream;
    }

    private InputStream generateInvalidMethodNameTestCase() {
        String rawData = "GeT / HTTP/1.1\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );

        return inputStream;
    }

    private InputStream generateInvalidMethodNameTooLongTestCase() {
        String rawData = "GETTTTTTTDDFSDFSDF / HTTP/1.1\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );

        return inputStream;
    }

    private InputStream generateInvalidRequestLine() {
        String rawData = "GET / HTTP/1.1 JUNK_VALUE\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );

        return inputStream;
    }

    private InputStream generateInvalidEmptyRequestLine() {
        String rawData = "\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );

        return inputStream;
    }

    private InputStream generateInvalidRequestLineNoReturn() {
        String rawData = "GET / HTTP/1.1\r" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );

        return inputStream;
    }

    private InputStream generateInvalidHttpVersionTestCase() {
        String rawData = "GET / HT/1.1\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );

        return inputStream;
    }

    private InputStream generateUnsupportedHttpVersionTestCase() {
        String rawData = "GET / HTTP/2.1\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );

        return inputStream;
    }

    private InputStream generateSupportedHttpVersionTestCase() {
        String rawData = "GET / HTTP/1.4\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );

        return inputStream;
    }
}