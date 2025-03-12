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
    
    /**
     * 사용자 계정 활성 상태 확인
     * 
     * @param email 확인할 이메일
     * @param repository 사용자 저장소
     * @return 활성 상태 여부
     */
    public static boolean isUserActive(String email, UserRepository repository) {
        Optional<User> userOpt = repository.findByEmail(email);
        return userOpt.isPresent() && !userOpt.get().isDeleted();
    }
    
    /**
     * 관리자 권한 확인
     * 
     * @param email 확인할 이메일
     * @param repository 사용자 저장소
     * @return 관리자 여부
     */
    public static boolean isAdmin(String email, UserRepository repository) {
        Optional<User> userOpt = repository.findByEmail(email);
        return userOpt.isPresent() && userOpt.get().isAdmin();
    }
}
