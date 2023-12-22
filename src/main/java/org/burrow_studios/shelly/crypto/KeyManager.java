package org.burrow_studios.shelly.crypto;

import com.auth0.jwt.algorithms.Algorithm;
import org.jetbrains.annotations.NotNull;

import java.security.interfaces.RSAKey;

/** The KeyManager handles encryption, validation of tokens and safe storage of application secrets. */
class KeyManager {
    private final TokenManager tokenManager;

    private RSAKey key;
    private Algorithm algorithm;

    KeyManager(@NotNull TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }
}
