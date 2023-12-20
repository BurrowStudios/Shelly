package org.burrow_studios.shelly.database.sqlite;

import org.burrow_studios.shelly.database.SQLDatabase;
import org.jetbrains.annotations.NotNull;
import org.sqlite.SQLiteConfig;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLiteDatabase extends SQLDatabase {
    private static final String STMT_CREATE_TABLE_IDENTITIES   = "CREATE TABLE IF NOT EXISTS `identities` (`subject` BIGINT(20) NOT NULL, `token_family` INT NOT NULL, `token_id` BIGINT(20) NOT NULL, PRIMARY KEY (`token_id`));";
    private static final String STMT_CREATE_TABLE_EXP_FAMILIES = "CREATE TABLE IF NOT EXISTS `expired_families` (`subject` BIGINT(20) NOT NULL, `family` INT NOT NULL, PRIMARY KEY (`subject`, `family`), FOREIGN KEY (`subject`, `family`) REFERENCES `identities`(`subject`, `token_family`));";
    private static final String STMT_CREATE_TABLE_SESSIONS     = "CREATE TABLE IF NOT EXISTS `sessions` (`id` BIGINT(20) NOT NULL, `identity` BIGINT(20) NOT NULL, `token` TEXT NOT NULL, `expired` BOOLEAN NOT NULL DEFAULT FALSE, PRIMARY KEY (`id`), FOREIGN KEY (`identity`) REFERENCES `identities`(`token_id`), UNIQUE (`token`));"; // TODO: don't store token?

    private static final String STMT_INDEX_CREATE_IDENTITIES_SUBJECT      = "CREATE INDEX IF NOT EXISTS `subject` ON `identities` (`subject`);";
    private static final String STMT_INDEX_CREATE_IDENTITIES_TOKEN_FAMILY = "CREATE INDEX IF NOT EXISTS `token_family` ON `identities` (`token_family`);";

    private static final String STMT_SELECT_SESSIONS = "SELECT * FROM `sessions` WHERE `identity` = ? AND `expired` = 0;";

    private static final String STMT_INSERT_SESSION = "INSERT INTO `sessions` (`id`, `identity`, `token`) VALUES (?, ?, ?)";

    private static final String STMT_UPDATE_SESSION_EXPIRE     = "UPDATE `sessions` SET `expired` = 1 WHERE `id` = ? AND `identity` = ?;";
    private static final String STMT_UPDATE_SESSION_EXPIRE_ALL = "UPDATE `sessions` SET `expired` = 1 WHERE `identity` = ?;";

    private static final Logger LOG = Logger.getLogger(SQLiteDatabase.class.getSimpleName());

    private final Connection connection;

    public SQLiteDatabase(@NotNull File file) throws SQLException {
        String url = String.format("jdbc:sqlite:%s", file.getAbsolutePath());

        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);

        LOG.log(Level.INFO, "Initiating database connection to " + url);
        this.connection = DriverManager.getConnection(url, config.toProperties());

        this.init();

        LOG.log(Level.INFO, "Database is online");
    }

    private void init() {
        LOG.log(Level.INFO, "Creating tables");
        try {
            final PreparedStatement createIdentities  = connection.prepareStatement(STMT_CREATE_TABLE_IDENTITIES);
            final PreparedStatement createExpFamilies = connection.prepareStatement(STMT_CREATE_TABLE_EXP_FAMILIES);
            final PreparedStatement createSessions    = connection.prepareStatement(STMT_CREATE_TABLE_SESSIONS);

            createIdentities.execute();
            createExpFamilies.execute();
            createSessions.execute();


            final PreparedStatement indexIdentitiesSubject     = connection.prepareStatement(STMT_INDEX_CREATE_IDENTITIES_SUBJECT);
            final PreparedStatement indexIdentitiesTokenFamily = connection.prepareStatement(STMT_INDEX_CREATE_IDENTITIES_TOKEN_FAMILY);

            indexIdentitiesSubject.execute();
            indexIdentitiesTokenFamily.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Could not create tables", e);
        }
    }

    @Override
    protected long[] getActiveSessions0(long identity) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(STMT_SELECT_SESSIONS)) {
            stmt.setLong(1, identity);

            ResultSet result = stmt.executeQuery();

            ArrayList<Long> sessionIds = new ArrayList<>();

            while (result.next())
                sessionIds.add(result.getLong("id"));

            return sessionIds.stream()
                    .mapToLong(Long::longValue)
                    .toArray();
        }
    }

    @Override
    public void createSession0(long id, long identity, String token) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(STMT_INSERT_SESSION)) {
            stmt.setLong(1, id);
            stmt.setLong(2, identity);
            stmt.setString(3, token);

            stmt.execute();
        }
    }

    @Override
    protected void invalidateSession0(long id, long identity) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(STMT_UPDATE_SESSION_EXPIRE)) {
            stmt.setLong(1, id);
            stmt.setLong(2, identity);

            stmt.execute();
        }
    }

    @Override
    protected void invalidateAllSessions0(long identity) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(STMT_UPDATE_SESSION_EXPIRE_ALL)) {
            stmt.setLong(1, identity);

            stmt.execute();
        }
    }
}
