package webserver.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum HttpVersion {
    HTTP_1_1("HTTP/1.1", 1, 1);

    public final String LITERAL;
    public final int MAJOR;
    public final int MINOR;

    HttpVersion(String LITERAL, int MAJOR, int MINOR) {
        this.LITERAL = LITERAL;
        this.MAJOR = MAJOR;
        this.MINOR = MINOR;
    }

    private static final Pattern httpVersionRegextPattern = Pattern.compile("^HTTP/(?<major>\\d+).(?<minor>\\d+)");

    public static HttpVersion getBestCompatibleVersion(String literalVersion) throws BadHttpVersionException {
        Matcher matcher = httpVersionRegextPattern.matcher(literalVersion);

        if (!matcher.find() || matcher.groupCount() != 2) {
            throw new BadHttpVersionException();
        }

        int major = Integer.parseInt(matcher.group("major"));
        int minor = Integer.parseInt(matcher.group("minor"));

        HttpVersion tempBestCompatible = null;

        for (HttpVersion httpVersion : HttpVersion.values()) {
            if (httpVersion.LITERAL.equals(literalVersion)) {
                return httpVersion;
            } else {
                if (httpVersion.MAJOR == major) {
                    if (httpVersion.MINOR < minor) {
                        tempBestCompatible = httpVersion;
                    }
                }
            }
        }

        return tempBestCompatible;
    }
}
