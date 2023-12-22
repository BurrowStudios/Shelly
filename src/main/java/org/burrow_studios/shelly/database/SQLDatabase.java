package org.burrow_studios.shelly.database;

import java.sql.SQLException;

public abstract class SQLDatabase implements Database {
    @Override
    public final long[] getActiveSessions(long identity) throws DatabaseException {
        try {
            return this.getActiveSessions0(identity);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract long[] getActiveSessions0(long identity) throws SQLException;

    @Override
    public final void createSession(long id, long identity, String token) throws DatabaseException {
        try {
            this.createSession0(id, identity, token);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void createSession0(long id, long identity, String token) throws SQLException;

    @Override
    public final void invalidateSession(long id, long identity) throws DatabaseException {
        try {
            this.invalidateSession0(id, identity);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void invalidateSession0(long id, long identity) throws SQLException;

    @Override
    public final void invalidateAllSessions(long identity) throws DatabaseException {
        try {
            this.invalidateAllSessions0(identity);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void invalidateAllSessions0(long identity) throws SQLException;

    @Override
    public final void invalidateIdentityTokenFamily(long subject) throws DatabaseException {
        try {
            this.invalidateIdentityTokenFamily0(subject);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void invalidateIdentityTokenFamily0(long subject) throws SQLException;

    @Override
    public final void createIdentity(long id, long subject) throws DatabaseException {
        try {
            this.createIdentity0(id, subject);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    protected abstract void createIdentity0(long id, long subject) throws SQLException;
}
