package util;

import model.User;
import repository.UserRepository;

import java.util.Optional;

/**
 * 사용자 정보 검증을 위한 유틸리티 클래스
 */
public class UserCheck {
    /**
     * 이메일 존재 여부 확인
     * 
     * @param email 확인할 이메일
     * @param repository 사용자 저장소
     * @return 이메일 존재 여부
     */
    public static boolean isEmailExists(String email, UserRepository repository) {
        return repository.findByEmail(email).isPresent();
    }
}
