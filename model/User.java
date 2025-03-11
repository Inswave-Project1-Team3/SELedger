package model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 사용자 권한을 정의하는 열거형
 * ADMIN: 관리자 권한
 * USER: 일반 사용자 권한
 */
enum Role {
	ADMIN, USER
}

/**
 * 사용자 정보를 저장하고 관리하는 클래스
 * 
 * 이 클래스는 사용자의 기본 정보(이메일, 비밀번호, 닉네임 등)를 저장하고,
 * 회원가입, 로그인, 회원정보 수정, 회원탈퇴 등의 기능을 제공합니다.
 * Serializable을 구현하여 객체 직렬화를 통해 파일에 저장할 수 있습니다.
 */
@Getter
@Setter
@NoArgsConstructor
public class User implements Serializable {
	// 직렬화 버전 UID (객체 직렬화 시 버전 관리에 사용)
	private static final long serialVersionUID = 1L;
	
	// 사용자 권한 (관리자 또는 일반 사용자)
	private Role role;
	
	// 사용자 이메일 (로그인 ID로 사용)
	private String email;
	
	// 회원탈퇴 여부 (true: 탈퇴, false: 활성)
	private boolean deleteCheck;
	
	// 암호화된 비밀번호
	private String password;
	
	// 사용자 닉네임
	private String nickName;

	/**
	 * 사용자 등록 (회원가입)
	 * 
	 * @param email 사용자 이메일 (로그인 ID)
	 * @param password 비밀번호 (SHA-256으로 암호화됨)
	 * @param nickName 닉네임
	 */
	public void registerUser(String email, String password, String nickName) {
		this.email = email;
		this.password = encryptPassword(password); // 비밀번호 암호화
		this.nickName = nickName;
		this.role = Role.USER; // 기본 권한은 일반 사용자
		this.deleteCheck = false; // 초기 상태는 활성 (탈퇴 아님)
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
	 * 
	 * @param email 새 이메일 (null이거나 빈 문자열이면 변경하지 않음)
	 * @param password 새 비밀번호 (null이거나 빈 문자열이면 변경하지 않음)
	 * @param nickName 새 닉네임 (null이거나 빈 문자열이면 변경하지 않음)
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
	 * 사용자 탈퇴 처리
	 * 실제로 데이터를 삭제하지 않고 탈퇴 상태(deleteCheck)만 변경합니다.
	 * 
	 * @param password 비밀번호 (확인용)
	 * @return 탈퇴 성공 여부 (true: 성공, false: 실패)
	 */
	public boolean deleteUser(String password) {
		// 비밀번호 확인
		if (this.password.equals(encryptPassword(password))) {
			this.deleteCheck = true; // 탈퇴 상태로 변경
			return true;
		}
		return false; // 비밀번호 불일치
	}

	/**
	 * 사용자 로그인
	 * 
	 * @param email 이메일 (로그인 ID)
	 * @param password 비밀번호
	 * @return 로그인 성공 여부 (true: 성공, false: 실패)
	 */
	public boolean loginUser(String email, String password) {
		// 이메일과 비밀번호 확인 (탈퇴 여부는 UserService에서 확인)
		return this.email.equals(email) && this.password.equals(encryptPassword(password));
	}

	/**
	 * 로그아웃 처리
	 * 실제 로그아웃 처리는 App 클래스에서 세션 정보를 초기화하는 방식으로 구현됩니다.
	 * 이 메서드는 향후 확장을 위해 유지됩니다.
	 */
	public void logoutUser() {
		// 로그아웃 처리는 App 클래스에서 세션 정보를 초기화하는 방식으로 구현
	}

	/**
	 * 관리자 권한 확인
	 * 
	 * @return 관리자 여부 (true: 관리자, false: 일반 사용자)
	 */
	public boolean isAdmin() {
		return this.role == Role.ADMIN;
	}

	/**
	 * 탈퇴 상태 확인
	 * 
	 * @return 탈퇴 여부 (true: 탈퇴, false: 활성)
	 */
	public boolean isDeleted() {
		return this.deleteCheck;
	}

	/**
	 * 비밀번호 암호화 (SHA-256)
	 * 
	 * @param password 평문 비밀번호
	 * @return 암호화된 비밀번호 (16진수 문자열)
	 */
	private String encryptPassword(String password) {
		try {
			// SHA-256 해시 알고리즘 사용
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hash = md.digest(password.getBytes());

			// byte 배열을 16진수 문자열로 변환
			StringBuilder hexString = new StringBuilder();
			for (byte b : hash) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1)
					hexString.append('0'); // 한 자리 수인 경우 앞에 0 추가
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("암호화 알고리즘 오류", e);
		}
	}
	
	/**
	 * 사용자 정보를 문자열로 반환
	 * 
	 * @return 사용자 정보 문자열
	 */
	@Override
	public String toString() {
		return "User{" +
				"email='" + email + '\'' +
				", nickName='" + nickName + '\'' +
				", role=" + role +
				", deleteCheck=" + deleteCheck +
				'}';
	}
}
