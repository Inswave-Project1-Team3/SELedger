package DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원탈퇴 요청 정보 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteUserDTO {
	/** 사용자 이메일 */
	private String email;
	
	/** 인증용 비밀번호 */
	private String password;
} 