package org.burrow_studios.shelly.database;

public interface Database {
    void createSession(long id, long identity, String token) throws DatabaseException;
}
