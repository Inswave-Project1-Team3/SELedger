package util;

import java.util.regex.Pattern;

/**
 * 사용자 입력값 검증을 위한 유틸리티 클래스
 * 
 * 이 클래스는 이메일, 비밀번호, 닉네임 등 사용자 입력값의 유효성을 검증하는 정적 메서드를 제공합니다.
 * 정규 표현식(Regex)을 사용하여 각 입력값이 지정된 형식을 준수하는지 확인합니다.
 */
public class InputValidator {
	/**
	 * 이메일 정규 표현식
	 * example@domain.com 형식을 검증합니다.
	 * 로컬 파트(@앞 부분)에는 영문자, 숫자, 일부 특수문자(._%+-)를 허용합니다.
	 * 도메인 파트(@뒤 부분)에는 영문자, 숫자, 하이픈(-)을 허용하고, 최소 하나의 점(.)과 2자 이상의 최상위 도메인을 요구합니다.
	 */
	private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
	
	/**
	 * 비밀번호 정규 표현식
	 * 8자리 이상이며, 최소 하나 이상의 특수문자를 포함해야 합니다.
	 * 공백 문자는 허용하지 않습니다.
	 */
	private static final String PASSWORD_REGEX = "^(?=.*[!@#$%^&*(),.?\":{}|<>])(?=\\S+$).{8,}$";
	
	/**
	 * 닉네임 정규 표현식
	 * 한글, 영문, 숫자만 허용하며, 2~12자 이내여야 합니다.
	 */
	private static final String NICKNAME_REGEX = "^[가-힣a-zA-Z0-9]{2,12}$";
	
	// 정규 표현식 패턴 컴파일 (성능 향상을 위해 미리 컴파일)
	private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
	private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
	private static final Pattern NICKNAME_PATTERN = Pattern.compile(NICKNAME_REGEX);
	
	/**
	 * 이메일 형식 검증
	 * 
	 * @param email 검증할 이메일
	 * @return 유효한 이메일이면 true, 아니면 false
	 */
	public static boolean isValidEmail(String email) {
		if (email == null || email.isEmpty())
			return false;
		
		return EMAIL_PATTERN.matcher(email).matches();
	}
	
	/**
	 * 비밀번호 형식 검증
	 * 8자리 이상이며, 최소 하나 이상의 특수문자를 포함해야 합니다.
	 * 
	 * @param password 검증할 비밀번호
	 * @return 유효한 비밀번호면 true, 아니면 false
	 */
	public static boolean isValidPassword(String password) {
		if (password == null || password.isEmpty())
			return false;
		
		return PASSWORD_PATTERN.matcher(password).matches();
	}
	
	/**
	 * 닉네임 형식 검증
	 * 한글, 영문, 숫자만 허용하며, 2~12자 이내여야 합니다.
	 * 
	 * @param nickname 검증할 닉네임
	 * @return 유효한 닉네임이면 true, 아니면 false
	 */
	public static boolean isValidNickname(String nickname) {
		if (nickname == null || nickname.isEmpty())
			return false;
		
		return NICKNAME_PATTERN.matcher(nickname).matches();
	}
	
	/**
	 * 입력값 통합 검증
	 * 이메일, 비밀번호, 닉네임을 한 번에 검증하고 결과 메시지를 반환합니다.
	 * 
	 * @param email 이메일
	 * @param password 비밀번호
	 * @param nickname 닉네임
	 * @return 검증 결과 메시지 (모두 유효하면 null 반환)
	 */
	public static String validateUserInput(String email, String password, String nickname) {
		if (!isValidEmail(email))
			return "이메일 형식이 올바르지 않습니다. (예: example@domain.com)";
		
		if (!isValidPassword(password))
			return "비밀번호는 8자리 이상이며, 최소 하나 이상의 특수문자를 포함해야 합니다.";
		
		if (!isValidNickname(nickname))
			return "닉네임은 한글, 영문, 숫자만 허용되며, 2~12자 이내여야 합니다.";
		
		return null; // 모든 검증 통과
	}
} 