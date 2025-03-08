package DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원정보 수정 요청 정보를 담는 DTO(Data Transfer Object) 클래스
 * 
 * 이 클래스는 회원정보 수정 시 필요한 정보(이메일, 현재 비밀번호, 새 비밀번호, 새 닉네임)를
 * 컨트롤러에서 서비스 계층으로 전달하는 데 사용됩니다.
 * Lombok 어노테이션을 사용하여 getter, setter, 생성자 등을 자동 생성합니다.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDTO {
	/**
	 * 사용자 이메일 (사용자 식별용)
	 * 수정 대상 사용자를 식별하는 데 사용됩니다.
	 */
	private String email;
	
	/**
	 * 현재 비밀번호 (인증용)
	 * 사용자 본인 확인을 위해 사용됩니다.
	 */
	private String currentPassword;
	
	/**
	 * 새 비밀번호 (변경 시)
	 * null이거나 빈 문자열이면 비밀번호를 변경하지 않습니다.
	 * 값이 있으면 8자리 이상이며, 최소 하나 이상의 특수문자를 포함해야 합니다.
	 */
	private String newPassword;
	
	/**
	 * 새 닉네임 (변경 시)
	 * null이거나 빈 문자열이면 닉네임을 변경하지 않습니다.
	 * 값이 있으면 한글, 영문, 숫자만 허용되며, 2~12자 이내여야 합니다.
	 */
	private String nickname;
} 