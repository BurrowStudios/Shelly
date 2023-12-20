package org.burrow_studios.shelly;

import org.burrow_studios.shelly.net.Server;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Shelly {
    private static final Logger LOG = Logger.getLogger("MAIN");

    private final Server server;

    Shelly() throws Exception {
        LOG.log(Level.INFO, "Starting API server");
        this.server = new Server(this);
    }

    public Server getServer() {
        return server;
    }

    void stop() throws Exception {
        LOG.log(Level.WARNING, "Shutting down");
        server.stop();
        LOG.log(Level.INFO, "OK bye");
    }
}
