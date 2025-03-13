package util;

import java.util.regex.Pattern;

/**
 * 사용자 입력값 검증을 위한 유틸리티 클래스
 */
public class InputValidator {
	/** 이메일 정규 표현식 */
	private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,256}$";
	
	/** 비밀번호 정규 표현식 (8자 이상, 특수문자 포함) */
	private static final String PASSWORD_REGEX = "^(?=.*[!@#$%^&*(),.?\":{}|<>])(?=\\S+$).{8,16}$";
	
	/** 닉네임 정규 표현식 (한글, 영문, 숫자 2~12자) */
	private static final String NICKNAME_REGEX = "^[가-힣a-zA-Z0-9]{2,12}$";
	
	// 정규 표현식 패턴 컴파일
	private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
	private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
	private static final Pattern NICKNAME_PATTERN = Pattern.compile(NICKNAME_REGEX);
	

	// 이메일 형식 검증
	public static boolean isValidEmail(String email) {
		if (email == null || email.isEmpty())
			return false;
		
		return EMAIL_PATTERN.matcher(email).matches();
	}
	
	// 비밀번호 형식 검증
	public static boolean isValidPassword(String password) {
		if (password == null || password.isEmpty())
			return false;
		
		return PASSWORD_PATTERN.matcher(password).matches();
	}

	//  닉네임 형식 검증
	public static boolean isValidNickname(String nickname) {
		if (nickname == null || nickname.isEmpty())
			return false;
		
		return NICKNAME_PATTERN.matcher(nickname).matches();
	}

	//통합 검증
	public static String validateUserInput(String email, String password, String nickname) {
		if (!isValidEmail(email))
			return "이메일 형식이 올바르지 않습니다. (예: example@domain.com)";
		
		if (!isValidPassword(password))
			return "비밀번호는 8자리 이상 16자리 이하 이며, 최소 하나 이상의 특수문자를 포함해야 합니다.";
		
		if (!isValidNickname(nickname))
			return "닉네임은 한글, 영문, 숫자만 허용되며, 2~12자 이내여야 합니다.";
		
		return null; // 모든 검증 통과
	}
} 