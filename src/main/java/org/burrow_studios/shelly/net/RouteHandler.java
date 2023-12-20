package org.burrow_studios.shelly.net;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.burrow_studios.shelly.database.Database;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class RouteHandler implements HttpHandler {
    protected static final Logger LOG = Logger.getLogger(RouteHandler.class.getSimpleName());

    protected final Server server;

    protected RouteHandler(@NotNull Server server) {
        this.server = server;
    }

    @Override
    public final void handle(HttpExchange exchange) throws IOException {
        final String   requestMethod;
        final String   requestPath;
        final String[] requestPathSegments;
        final String   requestBody;

        try {
            requestMethod       = exchange.getRequestMethod();
            requestPath         = exchange.getRequestURI().getPath();
            requestPathSegments = requestPath.substring(1).split("/");
            requestBody         = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Encountered an exception when attempting to prepare a request", e);
            exchange.sendResponseHeaders(Response.ERROR_INTERNAL_SERVER_ERROR.getCode(), -1);
            return;
        }

        LOG.log(Level.FINER, "Processing request " + requestMethod + " " + requestPath);

        Response response = handle0(requestMethod, requestPath, requestPathSegments, requestBody);

        exchange.sendResponseHeaders(response.getCode(), response.getBodyLength());
        exchange.getResponseBody().write(response.getBody());

        exchange.close();
    }

    private @NotNull Response handle0(String method, String path, String[] pathSegments, String content) {
        try {
            return this.handle(method, path, pathSegments, content);
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Encountered an exception when attempting to prepare a request", e);
            return Response.ERROR_INTERNAL_SERVER_ERROR;
        }
    }

    public abstract @NotNull Response handle(String method, String path, String[] segments, String content) throws Exception;

    protected @NotNull Database getDatabase() {
        return this.server.getShelly().getDatabase();
    }
}
