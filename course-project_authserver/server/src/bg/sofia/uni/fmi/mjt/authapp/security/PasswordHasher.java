package bg.sofia.uni.fmi.mjt.authapp.security;

import bg.sofia.uni.fmi.mjt.authapp.exceptions.PasswordException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordHasher {
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 512;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";
    private static final String HASH_PASSWORD_EXCEPTION_MESSAGE =
            "Exception encountered in hashPassword()";
    public static final String SALT_LENGTH_EXCEPTION_MESSAGE =
            "Error in generateSalt: length must be > 0";
    private static final SecureRandom RAND = new SecureRandom();

    public static Optional<String> generateSalt(int length) {
        if (length < 1) {
            throw new PasswordException(SALT_LENGTH_EXCEPTION_MESSAGE);
        }

        byte[] salt = new byte[length];
        RAND.nextBytes(salt);
        return Optional.of(Base64.getEncoder().encodeToString(salt));
    }

    public static Optional<String> hashPassword(String password, String salt) {

        char[] chars = password.toCharArray();
        byte[] bytes = salt.getBytes();

        // Create a specification for hashing the password
        PBEKeySpec spec = new PBEKeySpec(chars, bytes, ITERATIONS, KEY_LENGTH);

        Arrays.fill(chars, Character.MIN_VALUE); // Get rid of plain text password

        try {
            SecretKeyFactory fac = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] securePassword = fac.generateSecret(spec).getEncoded();
            return Optional.of(Base64.getEncoder().encodeToString(securePassword));

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            System.err.println(HASH_PASSWORD_EXCEPTION_MESSAGE);
            return Optional.empty();

        } finally {
            spec.clearPassword();
        }
    }

    public static boolean verifyPassword(String password, String key, String salt) {
        Optional<String> optEncrypted = hashPassword(password, salt);
        return optEncrypted.map(s -> s.equals(key)).orElse(false);
    }
}
