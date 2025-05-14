// src/main/java/main/com/syos/util/PasswordUtil.java
package main.com.syos.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordUtil {
    /** Hash a plaintext password with SHA-256 and Base64-encode the result. */
    public static String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[]    dig = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(dig);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not supported on this JVM", e);
        }
    }

    /** Compare plaintext against stored hash. */
    public static boolean verify(String password, String storedHash) {
        return hash(password).equals(storedHash);
    }
}
