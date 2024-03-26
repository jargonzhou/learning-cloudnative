package com.spike.vertx.commons;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JwtKeys {
    public static String publicKey(String folder) throws IOException {
        return readContent(folder + "/public_key.pem");
    }

    public static String privateKey(String folder) throws IOException {
        return readContent(folder + "/private_key.pem");
    }

    private static String readContent(String file) throws IOException {
        Path path = Paths.get(file);
        if (!path.toFile().exists()) {
            throw new IOException("File " + file + " not exist!");
        }
        return String.join("\n", Files.readAllLines(path, StandardCharsets.UTF_8));
    }
}
