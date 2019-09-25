package edu.kit.iti.formal.keyserver;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexander Weigl
 * @version 1 (25.09.19)
 */
public class Request {
    private final Map<String, String> header = new HashMap<>();
    private String method, path, protocol, body;

    /**
     * <pre>
     * Request = request-line; ((general-header; request-header; entity-header ) CRLF) ;
     * CRLF [ message-body ] ;
     * </pre>
     * <p>
     * Example:
     * <pre>
     * GET /path/file.html HTTP/1.1
     * Host: www.host1.com:80
     * User-Agent: MyBrowser/1.0
     * [blank line here]
     * </pre>
     *
     * @param req
     * @return
     */
    public static Request parse(String req) {
        Request r = new Request();
        String[] lines = req.split("\r\n");
        if (lines.length <= 1 && lines[0].isBlank())
            throw new RuntimeException("Malformed header");

        {
            String[] splitFirstLine = lines[0].split(" ");
            if (splitFirstLine.length != 3)
                throw new RuntimeException("Malformed header");
            r.method = splitFirstLine[0];
            r.path = splitFirstLine[1];
            r.protocol = splitFirstLine[2];
        }
        int i = 1;
        for (; i < lines.length; i++) {
            if (lines[i].isEmpty()) {
                break;
            }
            String[] kv = lines[i].split(": ", 1);
            if (kv.length != 2)
                throw new RuntimeException("Malformed header");
            r.header.put(kv[0], kv[1]);
        }

        for (; i < lines.length; i++) {
            r.body += lines[i] + "\r\n";
        }

        return r;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getBody() {
        return body;
    }
}
