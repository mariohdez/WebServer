package webserver.http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class HttpVersionTest {
    @Test
    void getBestCompatibleVersionExactMatch() throws BadHttpVersionException {
        HttpVersion version = HttpVersion.getBestCompatibleVersion("HTTP/1.1");
        Assertions.assertNotNull(version);
        Assertions.assertEquals(HttpVersion.HTTP_1_1, version);
    }

    @Test
    void getBestCompatibleVersionBadVersion() {
        try {
            HttpVersion version = HttpVersion.getBestCompatibleVersion("http/1.1");
            fail();
        } catch (BadHttpVersionException bhve) {
            Assertions.assertNotNull(bhve);
        }
    }

    @Test
    void getBestCompatibleVersionHigherVersion() {
        try {
            HttpVersion version = HttpVersion.getBestCompatibleVersion("HTTP/1.3");
            Assertions.assertNotNull(version);
            Assertions.assertEquals(HttpVersion.HTTP_1_1, version);
        } catch (BadHttpVersionException bhve) {
            fail();
        }
    }
}
