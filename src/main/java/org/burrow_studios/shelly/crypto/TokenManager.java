package org.burrow_studios.shelly.crypto;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import org.burrow_studios.shelly.Shelly;
import org.jetbrains.annotations.NotNull;

public class TokenManager {
    private final Shelly shelly;
    private final KeyManager keyManager;

    public TokenManager(@NotNull Shelly shelly) {
        this.shelly = shelly;

        this.keyManager = new KeyManager(this);
    }

    public @NotNull String newIdentityToken() {
        JWTCreator.Builder builder = JWT.create();

        // TODO

        return "null";
    }

    public @NotNull String newSessionToken(long id, long identity, long subject) {
        JWTCreator.Builder builder = JWT.create();

        // TODO

        return "null";
    }
}
