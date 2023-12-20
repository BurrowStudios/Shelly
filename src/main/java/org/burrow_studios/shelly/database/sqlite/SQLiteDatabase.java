package org.burrow_studios.shelly.database.sqlite;

import org.burrow_studios.shelly.database.SQLDatabase;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLiteDatabase extends SQLDatabase {
    private static final String STMT_CREATE_TABLE_IDENTITIES   = "CREATE TABLE IF NOT EXISTS `identities` (`subject` BIGINT(20) NOT NULL, `token_family` INT NOT NULL, `token_id` BIGINT(20) NOT NULL, PRIMARY KEY (`token_id`), INDEX (`subject`, `token_family`));";
    private static final String STMT_CREATE_TABLE_EXP_FAMILIES = "CREATE TABLE IF NOT EXISTS `expired_families` (`subject` BIGINT(20) NOT NULL, `family` INT NOT NULL, PRIMARY KEY (`subject`, `family`));";
    private static final String STMT_CREATE_TABLE_SESSIONS     = "CREATE TABLE IF NOT EXISTS `sessions` (`id` BIGINT(20) NOT NULL, `identity` BIGINT(20) NOT NULL, `token` TEXT NOT NULL, PRIMARY KEY (`id`), UNIQUE (`token`));"; // TODO: don't store token?

    private static final String STMT_ALTER_TABLE_EXP_FAMILIES = "ALTER TABLE `expired_families` ADD FOREIGN KEY (`subject`, `family`) REFERENCES `identities`(`subject`, `token_family`) ON DELETE NO ACTION ON UPDATE RESTRICT;";
    private static final String STMT_ALTER_TABLE_SESSIONS     = "ALTER TABLE `sessions` ADD FOREIGN KEY (`identity`) REFERENCES `identities`(`token_id`) ON DELETE NO ACTION ON UPDATE RESTRICT;";

    private static final Logger LOG = Logger.getLogger(SQLiteDatabase.class.getSimpleName());

    private final Connection connection;

    public SQLiteDatabase(@NotNull String path) throws SQLException {
        String url = String.format("jdbc:sqlite://%s", path);

        LOG.log(Level.INFO, "Initiating database connection to " + url);
        this.connection = DriverManager.getConnection(url);

        LOG.log(Level.INFO, "Database is online");
    }

    public void init() {
        LOG.log(Level.INFO, "Creating tables");
        try {
            final PreparedStatement createIdentities  = connection.prepareStatement(STMT_CREATE_TABLE_IDENTITIES);
            final PreparedStatement createExpFamilies = connection.prepareStatement(STMT_CREATE_TABLE_EXP_FAMILIES);
            final PreparedStatement createSessions    = connection.prepareStatement(STMT_CREATE_TABLE_SESSIONS);

            final PreparedStatement alterExpFamilies = connection.prepareStatement(STMT_ALTER_TABLE_EXP_FAMILIES);
            final PreparedStatement alterSessions    = connection.prepareStatement(STMT_ALTER_TABLE_SESSIONS);

            createIdentities.execute();
            createExpFamilies.execute();
            createSessions.execute();

            alterExpFamilies.execute();
            alterSessions.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Could not create tables", e);
        }
    }
}
