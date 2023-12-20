package org.burrow_studios.shelly.net.handlers;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import org.burrow_studios.shelly.net.Response;
import org.burrow_studios.shelly.net.RouteHandler;
import org.burrow_studios.shelly.net.Server;
import org.jetbrains.annotations.NotNull;

public class SessionHandler extends RouteHandler {
    public SessionHandler(@NotNull Server server) {
        super(server);
    }

    @Override
    public @NotNull Response handle(String method, String path, String[] segments, String content) throws Exception {
        assert segments.length > 1;

        assert segments[0].equals("sessions");

        String subjectIdStr = segments[1];
        long   subjectId;

        try {
            subjectId = Long.parseLong(subjectIdStr);
        } catch (NumberFormatException e) {
            return Response.ERROR_BAD_REQUEST.withBody("Invalid subject id format");
        }

        if (segments.length == 3) {
            String sessionIdStr = segments[2];
            long   sessionId;

            try {
                sessionId = Long.parseLong(sessionIdStr);
            } catch (NumberFormatException e) {
                return Response.ERROR_BAD_REQUEST.withBody("Invalid session id format");
            }

            if (!method.equals("DELETE"))
                return Response.ERROR_METHOD_NOT_ALLOWED;

            // TODO: invalidate session
        }

        if (method.equals("DELETE")) {
            // TODO: invalidate all sessions
        }

        if (method.equals("POST")) {
            JsonObject json;
            try {
                json = Server.deserializeJson(content).getAsJsonObject();
            } catch (JsonSyntaxException | IllegalStateException e) {
                return Response.ERROR_BAD_REQUEST;
            }

            final String identityToken = json.get("identity").getAsString();

            // TODO: create new session
        }

        return Response.ERROR_METHOD_NOT_ALLOWED;
    }
}
