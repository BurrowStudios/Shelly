package org.burrow_studios.shelly.crypto;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import org.burrow_studios.shelly.Shelly;
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

    public @NotNull String newIdentityToken(long subject) {
        final long id = TurtleUtil.newId();
        final Algorithm algorithm;

        try {
            algorithm = keyManager.newIdentityAlgorithm(id);
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Could not generate new token for subject " + id + " due to an internal encryption exception.", e);
            throw new RuntimeException("Internal encryption exception");
        }

        return JWT.create()
                .withIssuer("Shelly")
                .withSubject(Long.toString(subject))
                .withJWTId(Long.toString(id))
                .sign(algorithm);
    }

    public @NotNull String newSessionToken(long id, long identity, long subject) {
        JWTCreator.Builder builder = JWT.create();

        // TODO

        return "null";
    }
}
