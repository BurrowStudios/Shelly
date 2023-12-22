package org.burrow_studios.shelly.crypto;

import com.auth0.jwt.algorithms.Algorithm;
import org.burrow_studios.shelly.Main;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.security.interfaces.RSAKey;

/** The KeyManager handles encryption, validation of tokens and safe storage of application secrets. */
class KeyManager {
    private final TokenManager tokenManager;

    private final File         keyStore = new File(Main.DIR, "keys");
    private final File identityKeyStore = new File(keyStore, "identity");
    private final File  sessionKeyStore = new File(keyStore, "session");

    /** The current key that is used to sign session tokens. */
    private RSAKey    sessionKey;
    /** The current algorithm that is used to sign session tokens. */
    private Algorithm sessionAlg;

    KeyManager(@NotNull TokenManager tokenManager) throws IOException {
        this.tokenManager = tokenManager;

        this.createDirectories();
    }

    private void createDirectories() throws IOException {
        if (this.keyStore.exists()) {
            if (!keyStore.isDirectory())
                throw new NotDirectoryException("KeyStore is not a directory");
        } else {
            boolean mkdir = this.keyStore.mkdir();
            if (!mkdir)
                throw new IOException("Could not create KeyStore");
        }

        if (this.identityKeyStore.exists()) {
            if (!identityKeyStore.isDirectory())
                throw new NotDirectoryException("Identity KeyStore is not a directory");
        } else {
            boolean mkdir = this.identityKeyStore.mkdir();
            if (!mkdir)
                throw new IOException("Could not create Identity KeyStore");
        }

        if (this.sessionKeyStore.exists()) {
            if (!sessionKeyStore.isDirectory())
                throw new NotDirectoryException("Session KeyStore is not a directory");
        } else {
            boolean mkdir = this.sessionKeyStore.mkdir();
            if (!mkdir)
                throw new IOException("Could not create Session KeyStore");
        }
    }
}
