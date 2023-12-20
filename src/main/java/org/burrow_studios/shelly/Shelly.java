package org.burrow_studios.shelly;

import org.burrow_studios.shelly.database.Database;
import org.burrow_studios.shelly.database.sqlite.SQLiteDatabase;
import org.burrow_studios.shelly.net.Server;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Shelly {
    private static final Logger LOG = Logger.getLogger("MAIN");

    private final Database database;
    private final Server server;

    Shelly() throws Exception {
        LOG.log(Level.INFO, "Starting Database");
        this.database = new SQLiteDatabase(new File(Main.DIR, "shelly.db"));

        LOG.log(Level.INFO, "Starting API server");
        this.server = new Server(this);
    }

    public Database getDatabase() {
        return database;
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
