package org.burrow_studios.shelly.crypto;

import com.auth0.jwt.algorithms.Algorithm;
import org.burrow_studios.shelly.Main;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.security.*;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/** The KeyManager handles encryption, validation of tokens and safe storage of application secrets. */
class KeyManager {
    private static final String ALGORITHM = "RSA";
    private static final int    KEY_SIZE  = 2048;

    private static final String BEGIN_PUBLIC_KEY  = "-----BEGIN PUBLIC KEY-----";
    private static final String END_PUBLIC_KEY    = "-----END PUBLIC KEY-----";
    private static final String BEGIN_PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----";
    private static final String END_PRIVATE_KEY   = "-----END PRIVATE KEY-----";

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

    public @NotNull Algorithm newIdentityAlgorithm(long id) throws NoSuchAlgorithmException, IOException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
        generator.initialize(KEY_SIZE);

        final KeyPair keyPair = generator.generateKeyPair();

        final  PublicKey  publicKey = keyPair.getPublic();
        final PrivateKey privateKey = keyPair.getPrivate();

        this.saveIdentityAlgorithm(id, publicKey);
        this.saveIdentityAlgorithm(id, privateKey);

        return Algorithm.RSA256(
                (RSAPublicKey)  publicKey,
                (RSAPrivateKey) privateKey
        );
    }

    public @NotNull Algorithm getPublicIdentityVerificationAlgorithm(long id) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        return Algorithm.RSA256((RSAPublicKey) getPublicIdentityKey(id), null);
    }

    public @NotNull PublicKey getPublicIdentityKey(long id) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        final File file = new File(identityKeyStore, id + ".pem");

        final byte[] bytes = readKey(file);
        final X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
        final KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePublic(spec);
    }

    private void saveIdentityAlgorithm(long id, PublicKey key) throws IOException {
        final File file = new File(identityKeyStore, id + ".pem");

        boolean newFile = file.createNewFile();

        byte[] keyBytes = key.getEncoded();

        try (FileOutputStream stream = new FileOutputStream(file, false)) {
            stream.write(BEGIN_PUBLIC_KEY.getBytes());
            stream.write("\n".getBytes());
            stream.write(Base64.getEncoder().encode(keyBytes));
            stream.write("\n".getBytes());
            stream.write(END_PUBLIC_KEY.getBytes());
        }
    }

    private void saveIdentityAlgorithm(long id, PrivateKey key) throws IOException {
        final File file = new File(identityKeyStore, id + ".key");

        boolean newFile = file.createNewFile();

        byte[] keyBytes = key.getEncoded();

        try (FileOutputStream stream = new FileOutputStream(file, false)) {
            stream.write(BEGIN_PRIVATE_KEY.getBytes());
            stream.write("\n".getBytes());
            stream.write(Base64.getEncoder().encode(keyBytes));
            stream.write("\n".getBytes());
            stream.write(END_PRIVATE_KEY.getBytes());
        }
    }

    private static byte[] readKey(File file) throws IOException {
        return Base64.getDecoder().decode(
                Files.readString(file.toPath())
                        .replace(BEGIN_PUBLIC_KEY , "")
                        .replace(END_PUBLIC_KEY   , "")
                        .replace(BEGIN_PRIVATE_KEY, "")
                        .replace(END_PRIVATE_KEY  , "")
                        .replaceAll("\\s", "")
        );
    }
}
