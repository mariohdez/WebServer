package webserver.http;

public class HttpRequest extends HttpMessage {
    private HttpMethod method;
    private String requestTarget;
    private String originalHttpVersion;
    private HttpVersion bestCompatibleVersion;

    HttpRequest() {
    }

    void setMethod(String methodName) throws HttpParsingException {
        for (HttpMethod method : HttpMethod.values()) {
            if (methodName.equals(method.name())) {
                this.method = HttpMethod.valueOf(methodName);

                return;
            }
        }

        throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
    }

    public HttpMethod getMethod() {
        return method;
    }

    void setRequestTarget(String requestTarget) throws HttpParsingException {
        if (requestTarget == null || requestTarget.length() == 0) {
            throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR);
        }
        this.requestTarget = requestTarget;
    }

    public String getRequestTarget() {
        return this.requestTarget;
    }

    void setHttpVersion(String originalHttpVersion) throws BadHttpVersionException, HttpParsingException {
        this.originalHttpVersion = originalHttpVersion;
        this.bestCompatibleVersion = HttpVersion.getBestCompatibleVersion(originalHttpVersion);

        if (this.bestCompatibleVersion == null) {
            throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
        }
    }

    public HttpVersion getBestCompatibleVersion() {
        return this.bestCompatibleVersion;
    }

    public String getOriginalHttpVersion() {
        return this.originalHttpVersion;
    }

    @Override
    public String toString() {
        String response = "method:" + getMethod() + "\n" +
                "request target: " + getRequestTarget() + "\n" +
                "request version: " + getOriginalHttpVersion() + "\n";
        return response;
    }
}
