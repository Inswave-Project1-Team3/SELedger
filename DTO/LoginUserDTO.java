package DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 로그인 요청 정보 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserDTO {
	/** 사용자 이메일 */
	private String email;
	
	/** 사용자 비밀번호 */
	private String password;
}
