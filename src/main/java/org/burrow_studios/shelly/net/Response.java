package org.burrow_studios.shelly.net;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public final class Response {
    public static final Response ERROR_BAD_REQUEST           = new Response(400);
    public static final Response ERROR_NOT_FOUND             = new Response(404);
    public static final Response ERROR_METHOD_NOT_ALLOWED    = new Response(405);
    public static final Response ERROR_INTERNAL_SERVER_ERROR = new Response(500);
    public static final Response ERROR_NOT_IMPLEMENTED       = new Response(501);

    private final int code;
    private final byte[] body;
    private final Map<String, String> headers;

    public Response(int code) {
        this(code, null);
    }

    public Response(int code, byte[] body) {
        this(code, body, null);
    }

    public Response(int code, byte[] body, Map<String, String> headers) {
        this.code = code;
        this.body = Objects.requireNonNullElseGet(body, () -> new byte[0]);
        this.headers = headers == null ? Map.of() : Map.copyOf(headers);
    }

    public int getCode() {
        return code;
    }

    public byte[] getBody() {
        return body;
    }

    public int getBodyLength() {
        return body.length == 0 ? -1 : body.length;
    }

    public @NotNull Map<String, String> getHeaders() {
        return headers;
    }

    /* - BUILDER METHODS - */

    public Response withBody(byte[] body) {
        return new Response(this.code, body);
    }

    public Response withBody(String content) {
        return this.withBody(String.valueOf(content).getBytes());
    }

    public Response withBody(JsonElement json) {
        return this.withBody(Server.serializeJson(json));
    }

    /* - - - */

    public static Response ofJson(int code, JsonElement json) {
        String contentString = Server.serializeJson(json);
        Map<String, String> headers = Map.of("Content-Type", "application/json");
        return new Response(code, contentString.getBytes(), headers);
    }
}
