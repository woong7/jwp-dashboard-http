package org.apache.coyote.http11;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class HttpRequestURI {

    private static final String DEFAULT_PATH = "/hello";

    private final String path;
    private final Map<String, String> queryParams;

    public HttpRequestURI(String path, Map<String, String> queryParams) {
        this.path = path;
        this.queryParams = queryParams;
    }

    public static HttpRequestURI from(String uri) {
        String path = uri;
        Map<String, String> queryParams = new HashMap<>();

        if (uri.contains("?")) {
            int index = uri.indexOf("?");
            path = uri.substring(0, index);

            String queryString = uri.substring(index + 1);
            String[] queries = queryString.split("&");
            for (String query : queries) {
                String[] queryEntry = query.split("=");
                queryParams.put(queryEntry[0], queryEntry[1]);
            }
        }

        if (path.equals("/")) {
            path = DEFAULT_PATH;
        }

        if (!path.contains(".")) {
            path = path + ".html";
        }

        return new HttpRequestURI(path, queryParams);
    }

    public boolean startsWith(String text) {
        return path.startsWith(text);
    }

    public String getStaticPath() {
        return "static" + path;
    }

    public String getExtension() {
        return StringUtils.substringAfterLast(path, ".");
    }

    public String getPath() {
        return path;
    }
}
