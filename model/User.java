package model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 사용자 권한 정의
 */
enum Role {
	ADMIN, USER
}

/**
 * 사용자 정보 관리 클래스
 * 회원가입, 로그인, 정보 수정, 탈퇴 기능 제공
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
		this.password = encryptPassword(password); // 비밀번호 암호화
		this.nickName = nickName;
		this.role = Role.USER; // 기본 권한은 일반 사용자
		this.deleteCheck = false; // 초기 상태는 활성
	}

	//가계부 조회
//	public void viewAccountBook(String email) {
//		if (this.email.equals(email)) {
//			System.out.println("가계부 조회 성공!");
//		} else {
//			System.out.println("가계부 조회 실패. 이메일을 확인하세요.");
//		}
//	}

	/**
	 * 사용자 정보 업데이트
	 */
	public void updateUserInfo(String email, String password, String nickName) {
		// 이메일이 제공된 경우에만 업데이트
		if (email != null && !email.isEmpty())
			this.email = email;
		
		// 비밀번호가 제공된 경우에만 업데이트
		if (password != null && !password.isEmpty())
			this.password = encryptPassword(password);
		
		// 닉네임이 제공된 경우에만 업데이트
		if (nickName != null && !nickName.isEmpty())
			this.nickName = nickName;
	}

	/**
	 * 회원 탈퇴 처리
	 * 실제 삭제 대신 상태만 변경
	 */
	public boolean deleteUser(String password) {
		// 비밀번호 확인
		if (this.password.equals(encryptPassword(password))) {
			this.deleteCheck = true; // 탈퇴 상태로 변경
			return true;
		}
		return false;
	}

	/**
	 * 로그인 처리
	 */
	public boolean loginUser(String email, String password) {
		// 이메일과 비밀번호 확인
		return this.email.equals(email) && this.password.equals(encryptPassword(password));
	}

	/**
	 * 로그아웃 처리
	 */
	public void logoutUser() {
		// 로그아웃 시 특별한 처리 없음
		// 세션 관리는 컨트롤러에서 담당
	}

	/**
	 * 관리자 권한 확인
	 */
	public boolean isAdmin() {
		return this.role == Role.ADMIN;
	}

	/**
	 * 탈퇴 상태 확인
	 */
	public boolean isDeleted() {
		return this.deleteCheck;
	}

	/**
	 * 비밀번호 암호화 (SHA-256)
	 */
	private String encryptPassword(String password) {
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
