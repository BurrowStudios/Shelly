package org.burrow_studios.shelly.database;

public interface Database {
    long[] getActiveSessions(long identity) throws DatabaseException;

    void createSession(long id, long identity, String token) throws DatabaseException;

    void invalidateSession(long id, long identity) throws DatabaseException;

    void invalidateAllSessions(long identity) throws DatabaseException;

    void invalidateIdentityTokenFamily(long subject) throws DatabaseException;

    void createIdentity(long id, long subject) throws DatabaseException;
}
