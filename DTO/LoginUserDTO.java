package DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 로그인 요청 정보를 담는 DTO(Data Transfer Object) 클래스
 * 
 * 이 클래스는 로그인 시 필요한 사용자 정보(이메일, 비밀번호)를
 * 컨트롤러에서 서비스 계층으로 전달하는 데 사용됩니다.
 * Lombok 어노테이션을 사용하여 getter, setter, 생성자 등을 자동 생성합니다.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserDTO {
	/**
	 * 사용자 이메일 (로그인 ID로 사용)
	 */
	private String email;
	
	/**
	 * 사용자 비밀번호
	 */
	private String password;
}
