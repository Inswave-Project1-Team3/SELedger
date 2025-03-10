package DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원탈퇴 요청 정보를 담는 DTO(Data Transfer Object) 클래스
 * 
 * 이 클래스는 회원탈퇴 시 필요한 정보(이메일, 비밀번호)를
 * 컨트롤러에서 서비스 계층으로 전달하는 데 사용됩니다.
 * Lombok 어노테이션을 사용하여 getter, setter, 생성자 등을 자동 생성합니다.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteUserDTO {
	/**
	 * 사용자 이메일 (사용자 식별용)
	 * 탈퇴 대상 사용자를 식별하는 데 사용됩니다.
	 */
	private String email;
	
	/**
	 * 사용자 비밀번호 (인증용)
	 * 사용자 본인 확인을 위해 사용됩니다.
	 */
	private String password;
} 