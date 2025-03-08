package DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원가입 요청 정보를 담는 DTO(Data Transfer Object) 클래스
 * 
 * 이 클래스는 회원가입 시 필요한 사용자 정보(이메일, 비밀번호, 닉네임)를 
 * 컨트롤러에서 서비스 계층으로 전달하는 데 사용됩니다.
 * Lombok 어노테이션을 사용하여 getter, setter, 생성자 등을 자동 생성합니다.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDTO {
	/**
	 * 사용자 이메일 (로그인 ID로 사용)
	 * 이메일 형식(example@domain.com)을 준수해야 합니다.
	 */
	private String email;
	
	/**
	 * 사용자 비밀번호
	 * 8자리 이상이며, 최소 하나 이상의 특수문자를 포함해야 합니다.
	 */
	private String password;
	
	/**
	 * 사용자 닉네임
	 * 한글, 영문, 숫자만 허용되며, 2~12자 이내여야 합니다.
	 */
	private String nickname;
}
