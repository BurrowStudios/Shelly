package org.burrow_studios.shelly.net;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final Logger LOG = Logger.getLogger(Server.class.getSimpleName());

    private final HttpServer httpServer;

    public Server() throws IOException {
        LOG.log(Level.INFO, "Starting HTTP server on port 8080");
        this.httpServer = HttpServer.create(new InetSocketAddress(8080),  0);

        LOG.log(Level.INFO, "Binding server...");
        this.httpServer.start();
        LOG.log(Level.INFO, "OK!");
    }

    public void stop() {
        LOG.log(Level.INFO, "Stopping HTTP server");
        this.httpServer.stop(4);
    }
}
