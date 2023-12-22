package org.burrow_studios.shelly.crypto;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.burrow_studios.shelly.Shelly;
import org.burrow_studios.shelly.database.Database;
import org.burrow_studios.shelly.util.TurtleUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TokenManager {
    private static final Logger LOG = Logger.getLogger(TokenManager.class.getSimpleName());

    private final Shelly shelly;
    private final KeyManager keyManager;

    public TokenManager(@NotNull Shelly shelly) throws IOException {
        this.shelly = shelly;

        this.keyManager = new KeyManager(this);
    }

    public @NotNull String newIdentityToken(long subject, boolean newFamily) {
        final long id = TurtleUtil.newId();
        final Algorithm algorithm;

        try {
            algorithm = keyManager.newIdentityAlgorithm(id);
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Could not generate new token for subject " + id + " due to an internal encryption exception.", e);
            throw new RuntimeException("Internal encryption exception");
        }

        final String token = JWT.create()
                .withIssuer("Shelly")
                .withSubject(Long.toString(subject))
                .withJWTId(Long.toString(id))
                .sign(algorithm);

        Database database = shelly.getDatabase();
        if (newFamily)
            database.invalidateIdentityTokenFamily(subject);
        database.createIdentity(id, subject);

        return token;
    }

    public @NotNull String newSessionToken(long identity, long subject) {
        final long id = TurtleUtil.newId();
        final Algorithm algorithm = keyManager.getCurrentSessionAlgorithm();

        final String token = JWT.create()
                .withKeyId(Long.toString(identity))
                .withIssuer("Shelly")
                .withSubject(Long.toString(subject))
                .withJWTId(Long.toString(id))
                .sign(algorithm);

        Database database = shelly.getDatabase();
        database.invalidateAllSessions(identity);
        database.createSession(id, identity, token);

        return token;
    }
}
