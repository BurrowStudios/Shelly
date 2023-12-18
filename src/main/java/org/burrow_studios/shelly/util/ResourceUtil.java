package org.burrow_studios.shelly.util;

import org.burrow_studios.shelly.Main;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Properties;

public class ResourceUtil {
    private ResourceUtil() { }

    public static @Nullable String getProperty(@NotNull String filename, @NotNull String key) throws IOException {
        Properties properties = new Properties();
        properties.load(getResource(filename + ".properties"));
        return properties.getProperty(key);
    }

    /**
     * Attempts to read the application version from the {@code meta.properties} resource.
     * @return Application version.
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public static @Nullable String getVersion() {
        try {
            return getProperty("meta", "version");
        } catch (Exception e) {
            System.out.println("\nCould not load version from resources.");
            e.printStackTrace();
            return null;
        }
    }

    private static @Nullable InputStream getResource(@NotNull String name) {
        return Main.class.getClassLoader().getResourceAsStream(name);
    }
}
