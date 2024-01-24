package webserver.http;

public enum HttpMethod {
    GET, HEAD;
    public static final int MAX_LENGTH;

    static {
        int tempMaxLength = 0;
        for (HttpMethod method : values()) {
            tempMaxLength = Math.max(tempMaxLength, method.name().length());
        }

        MAX_LENGTH = tempMaxLength;
    }
}
