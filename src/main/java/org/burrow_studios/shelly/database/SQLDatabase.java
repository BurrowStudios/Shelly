package org.burrow_studios.shelly.database;

import java.sql.SQLException;

public abstract class SQLDatabase implements Database {
    @Override
    public final void createSession(long id, long identity, String token) throws DatabaseException {
        try {
            this.createSession0(id, identity, token);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void createSession0(long id, long identity, String token) throws SQLException;
}
