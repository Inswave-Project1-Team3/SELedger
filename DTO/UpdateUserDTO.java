package DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원정보 수정 요청 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDTO {
	/** 현재 사용자 이메일 */
	private String email;
	
	/** 현재 비밀번호 */
	private String currentPassword;
	
	/** 새 이메일 (변경 시) */
	private String newEmail;
	
	/** 새 비밀번호 (변경 시) */
	private String newPassword;
	
	/** 새 닉네임 (변경 시) */
	private String nickname;
} 