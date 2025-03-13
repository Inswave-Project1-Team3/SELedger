package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 비밀번호 암호화 기능 제공 클래스
 */
public class PasswordEncryptor {
    /**
     * SHA-256 알고리즘으로 비밀번호 암호화
     * 
     * @param password 원본 비밀번호
     * @return 암호화된 문자열
     */
    public static String encrypt(String password) {
        try {
            // SHA-256 해시 알고리즘 사용
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            
            byte[] byteData = md.digest();
            
            // 바이트 배열을 16진수 문자열로 변환
            StringBuilder sb = new StringBuilder();
            for (byte b : byteData) {
                sb.append(String.format("%02x", b));
            }
            
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // 알고리즘을 찾을 수 없는 경우 (거의 발생하지 않음)
            System.err.println("비밀번호 암호화 중 오류 발생: " + e.getMessage());
            // 암호화 실패 시 원본 비밀번호 반환 (보안상 좋지 않음)
            return password;
        }
    }
} 