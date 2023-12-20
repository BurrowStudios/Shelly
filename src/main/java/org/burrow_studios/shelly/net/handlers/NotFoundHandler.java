package org.burrow_studios.shelly.net.handlers;

import org.burrow_studios.shelly.net.Response;
import org.burrow_studios.shelly.net.RouteHandler;
import org.burrow_studios.shelly.net.Server;
import org.jetbrains.annotations.NotNull;

public class NotFoundHandler extends RouteHandler {
    public NotFoundHandler(@NotNull Server server) {
        super(server);
    }

    @Override
    public @NotNull Response handle(String method, String path, String[] segments, String content) {
        return Response.ERROR_NOT_FOUND;
    }
}
