package nextstep.jwp.ui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

import org.apache.coyote.http11.HttpRequest;
import org.apache.coyote.http11.HttpResponse;
import org.apache.coyote.http11.HttpStatusCode;

public abstract class AbstractController implements Controller {

    @Override
    public HttpResponse service(HttpRequest request) throws Exception {
        if (request.isGetRequest()) {
            return doGet(request);
        }
        if (request.isPostRequest()) {
            return doPost(request);
        }
        return redirectTo("/404", HttpStatusCode.HTTP_STATUS_NOT_FOUND);
    }

    protected abstract HttpResponse doGet(HttpRequest request) throws Exception;

    protected abstract HttpResponse doPost(HttpRequest request) throws Exception;

    protected HttpResponse redirectTo(String location, HttpStatusCode httpStatusCode) throws IOException {
        return createHttpResponseFrom("static" + location + ".html", httpStatusCode, "text/html", location);
    }

    protected HttpResponse createGetResponseFrom(HttpRequest request) throws IOException {
        return createHttpResponseFrom(request.getResourcePath(), HttpStatusCode.HTTP_STATUS_OK, request.getContentType(),
            request.getPath());
    }

    protected HttpResponse createHttpResponseFrom(String resourcePath, HttpStatusCode httpStatusCode, String ContentType,
        String location) throws
        IOException {
        final URL resource = getClass().getClassLoader().getResource(resourcePath);
        String responseBody = new String(Files.readAllBytes(new File(resource.getFile()).toPath()));

        return HttpResponse.of(httpStatusCode, responseBody)
            .setContentType(ContentType + ";charset=utf-8")
            .setLocation(location);
    }
}
