package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordEncryptor {
    public static String encrypt(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes(StandardCharsets.UTF_8));   // 솔트 추가
            md.update(password.getBytes(StandardCharsets.UTF_8));

            byte[] byteData = md.digest();

            StringBuilder sb = new StringBuilder();
            for (byte b : byteData) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 알고리즘을 찾을 수 없습니다.", e);
        }
    }

    public static String generateSalt() {
        SecureRandom random = new SecureRandom();       // 보안 강한 랜덤 생성기
        byte[] salt = new byte[16];                     // 16바이트(128비트) 솔트 생성
        random.nextBytes(salt);                         // 랜덤 바이트 채우기
        return Base64.getEncoder().encodeToString(salt);// Base64 인코딩 후 반환
    }
}
