package webserver.http;

public class HttpParsingException extends Exception {
    private final HttpStatusCode httpStatusCode;

    public HttpParsingException(HttpStatusCode httpStatusCode) {
        super(httpStatusCode.MESSAGE);
        this.httpStatusCode = httpStatusCode;
    }

    public HttpStatusCode getErrorCode() {
        return this.httpStatusCode;
    }
}
