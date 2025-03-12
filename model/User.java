package model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import util.PasswordEncryptor;

import java.io.Serializable;

/**
 * 사용자 권한 정의
 */
enum Role {
	ADMIN, USER
}

/**
 * 사용자 정보 및 계정 관리 모델
 */
@Getter
@Setter
@NoArgsConstructor
public class User implements Serializable {
	// 직렬화 버전 ID
	private static final long serialVersionUID = 1L;
	
	// 사용자 권한
	private Role role;
	
	// 사용자 이메일 (로그인 ID)
	private String email;
	
	// 탈퇴 여부
	private boolean deleteCheck;
	
	// 암호화된 비밀번호
	private String password;
	
	// 사용자 닉네임
	private String nickName;

	/**
	 * 회원가입 처리
	 */
	public void registerUser(String email, String password, String nickName) {
		this.email = email;
		this.password = PasswordEncryptor.encrypt(password); // 비밀번호 암호화
		this.nickName = nickName;
		this.role = Role.USER; // 기본 권한은 일반 사용자
		this.deleteCheck = false; // 초기 상태는 활성
	}

	/**
	 * 사용자 정보 업데이트
	 */
	public void updateUserInfo(String email, String password, String nickName) {
		// 이메일이 제공된 경우에만 업데이트
		if (email != null && !email.isEmpty())
			this.email = email;
		
		// 비밀번호가 제공된 경우에만 업데이트
		if (password != null && !password.isEmpty())
			this.password = PasswordEncryptor.encrypt(password);
		
		// 닉네임이 제공된 경우에만 업데이트
		if (nickName != null && !nickName.isEmpty())
			this.nickName = nickName;
	}

	/**
	 * 회원 탈퇴 처리
	 * 
	 * @param password 비밀번호 확인
	 * @return 탈퇴 성공 여부
	 */
	public boolean deleteUser(String password) {
		// 비밀번호 확인
		if (this.password.equals(PasswordEncryptor.encrypt(password))) {
			this.deleteCheck = true; // 탈퇴 상태로 변경
			return true;
		}
		return false;
	}

	/**
	 * 로그인 인증
	 * 
	 * @param email 이메일
	 * @param password 비밀번호
	 * @return 인증 성공 여부
	 */
	public boolean loginUser(String email, String password) {
		// 이메일과 비밀번호 확인
		return this.email.equals(email) && this.password.equals(PasswordEncryptor.encrypt(password));
	}

	/**
	 * 관리자 권한 확인
	 * 
	 * @return 관리자 여부
	 */
	public boolean isAdmin() {
		return this.role == Role.ADMIN;
	}

	/**
	 * 탈퇴 상태 확인
	 * 
	 * @return 탈퇴 여부
	 */
	public boolean isDeleted() {
		return this.deleteCheck;
	}

	@Override
	public String toString() {
		return "User{" +
				"email='" + email + '\'' +
				", nickName='" + nickName + '\'' +
				", role=" + role +
				", deleted=" + deleteCheck +
				'}';
	}
}
