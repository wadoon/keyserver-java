package edu.kit.iti.formal.keyserver;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexander Weigl
 * @version 1 (25.09.19)
 */
public class Request {
    final Map<String, String> header = new HashMap<>();
    String method, path, protocol, body = "";

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
