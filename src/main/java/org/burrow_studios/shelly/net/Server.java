package org.burrow_studios.shelly.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpServer;
import org.burrow_studios.shelly.Shelly;
import org.burrow_studios.shelly.net.handlers.NotFoundHandler;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final Logger LOG = Logger.getLogger(Server.class.getSimpleName());

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    private final Shelly shelly;
    private final HttpServer httpServer;

    public Server(@NotNull Shelly shelly) throws IOException {
        this.shelly = shelly;

        LOG.log(Level.INFO, "Starting HTTP server on port 8080");
        this.httpServer = HttpServer.create(new InetSocketAddress(8080),  0);

        this.httpServer.createContext("/", new NotFoundHandler(this));

        LOG.log(Level.INFO, "Binding server...");
        this.httpServer.start();
        LOG.log(Level.INFO, "OK!");
    }

    public void stop() {
        LOG.log(Level.INFO, "Stopping HTTP server");
        this.httpServer.stop(4);
    }

    public @NotNull Shelly getShelly() {
        return shelly;
    }

    public static JsonElement deserializeJson(String rawJson) {
        return GSON.fromJson(rawJson, JsonElement.class);
    }

    public static String serializeJson(JsonElement json) {
        return GSON.toJson(json);
    }
}
