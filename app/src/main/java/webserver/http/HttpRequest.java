package webserver.http;

public class HttpRequest extends HttpMessage {
    private HttpMessage method;
    private String requestTarget;
    private String httpVersion;

    HttpRequest() {
    }

    void setMethod(HttpMessage method) {
        this.method = method;
    }

    public HttpMessage getMethod() {
        return method;
    }
}
