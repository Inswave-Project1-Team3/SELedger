package controller;

import DTO.CreateUserDTO;
import DTO.DeleteUserDTO;
import DTO.LoginUserDTO;
import DTO.UpdateUserDTO;
import app.App;
import model.User;
import service.UserService;

/**
 * 사용자 관련 요청 처리 컨트롤러
 */
public class UserController {
	// 사용자 관련 비즈니스 로직 서비스
	private final UserService userService;
	
	// 현재 로그인한 사용자 닉네임
	private String currentUserNickName = null;

	// 현재 로그인한 사용자 이메일
	private String currentUserEmail = null;
	
	/**
	 * 생성자 - 서비스 초기화
	 */
	public UserController() {
		this.userService = new UserService();
	}
	
	/**
	 * 회원가입 처리
	 * 
	 * @param dto 회원가입 정보
	 * @return 가입 성공 여부
	 */
	public boolean createUser(CreateUserDTO dto) {
		try {
			boolean result = userService.createUser(dto);
			if (result) {
				System.out.println("회원가입이 성공적으로 완료되었습니다.");
				System.out.println("사용자 폴더가 생성되었습니다: data/" + dto.getNickname());
				
				// 세션 초기화
				resetSession();
			} else
				System.out.println("회원가입에 실패했습니다.");
			
			return result;
		} catch (IllegalArgumentException e) {
			System.out.println("회원가입 실패: " + e.getMessage());
			return false;
		}
	}
	
	/**
	 * 로그인 처리
	 * 
	 * @param dto 로그인 정보
	 * @return 로그인 성공 여부
	 */
	public boolean login(LoginUserDTO dto) {
		boolean result = userService.login(dto);
		if (result) {
			// 로그인 성공 시 컨트롤러 내부 상태 업데이트
			this.currentUserEmail = dto.getEmail();

			// 사용자 정보를 조회하여 닉네임 설정
			User user = userService.getUserByEmail(dto.getEmail());
			if (user != null) {
				this.currentUserNickName = user.getNickName();
			}
			
			System.out.println("로그인에 성공했습니다.");
			
			// App 클래스 정적 변수 업데이트 (호환성 유지)
			updateAppSession(true, dto.getEmail());
		} else {
			System.out.println("로그인에 실패했습니다. 이메일과 비밀번호를 확인해주세요.");
			resetSession();
		}
		
		return result;
	}
	
	/**
	 * 로그아웃 처리
	 */
	public void logout() {
		// 로그아웃 처리
		resetSession();
		
		System.out.println("로그아웃 되었습니다.");
		
		// App 클래스 정적 변수 업데이트 (호환성 유지)
		updateAppSession(false, "");
	}
	
	/**
	 * 회원정보 수정
	 * 
	 * @param dto 수정할 정보
	 * @return 수정 성공 여부
	 */
	public boolean updateUser(UpdateUserDTO dto) {
		try {
			
			// 현재 로그인한 사용자와 수정 요청한 사용자가 일치하는지 확인
			User currentUser = userService.getUserByNickName(currentUserNickName);
			if (currentUser == null || !currentUser.getEmail().equals(dto.getEmail())) {
				System.out.println("본인 계정만 수정할 수 있습니다.");
				return false;
			}
			
			boolean result = userService.updateUser(dto);
			
			if (result) {
				System.out.println("회원정보가 성공적으로 수정되었습니다.");
				
				// 이메일이 변경된 경우 세션 정보 업데이트
				if (dto.getNewEmail() != null && !dto.getNewEmail().isEmpty()) {
					this.currentUserEmail = dto.getNewEmail();
					
					// App 클래스 정적 변수 업데이트 (호환성 유지)
					updateAppSession(true, dto.getNewEmail());
				}
			} else {
				System.out.println("회원정보 수정에 실패했습니다.");
			}
			
			return result;
		} catch (IllegalArgumentException e) {
			System.out.println("회원정보 수정 실패: " + e.getMessage());
			return false;
		}
	}
	
	/**
	 * 회원 탈퇴 처리
	 * 
	 * @param dto 탈퇴 정보
	 * @return 탈퇴 성공 여부
	 */
	public boolean deleteUser(DeleteUserDTO dto) {
		
		// 현재 로그인한 사용자와 탈퇴 요청한 사용자가 일치하는지 확인
		User currentUser = userService.getUserByNickName(currentUserNickName);
		if (currentUser == null || !currentUser.getEmail().equals(dto.getEmail())) {
			System.out.println("본인 계정만 탈퇴할 수 있습니다.");
			return false;
		}
		
		boolean result = userService.deleteUser(dto);
		
		if (result) {
			System.out.println("회원탈퇴가 완료되었습니다. 이용해주셔서 감사합니다.");
			logout(); // 탈퇴 후 로그아웃 처리
		} else {
			System.out.println("회원탈퇴에 실패했습니다. 비밀번호를 확인해주세요.");
		}
		
		return result;
	}
	
	/**
	 * 현재 로그인한 사용자 정보 조회
	 * 
	 * @return 현재 사용자 객체 (없으면 null)
	 */
	public User getCurrentUser() {
		if (currentUserNickName == null)
			return null;
		
		return userService.getUserByNickName(currentUserNickName);
	}
	
	/**
	 * 현재 로그인한 사용자 닉네임 조회
	 * 
	 * @return 현재 사용자 닉네임
	 */
	public String getCurrentUserNickName() {
		return currentUserNickName;
	}

	/**
	 * 닉네임 존재 여부 확인
	 * 
	 * @param visitUserNickName 확인할 닉네임
	 * @return 존재 여부
	 */
	public boolean checkNicknameExists(String visitUserNickName) {
		return userService.checkNicknameExists(visitUserNickName);
	}
	
	/**
	 * 세션 정보 초기화
	 */
	private void resetSession() {
		this.currentUserNickName = null;
		this.currentUserEmail = null;
	}
	
	/**
	 * App 클래스 정적 변수 업데이트 (호환성 유지)
	 * 
	 * @param loginStatus 로그인 상태
	 * @param email 사용자 이메일
	 */
	private void updateAppSession(boolean loginStatus, String email) {
		try {
			// 리플렉션을 사용하지 않고 직접 접근 (호환성 유지)
			App.loginCheck = loginStatus;
			App.userEmail = email;
		} catch (Exception e) {
			// App 클래스가 없거나 접근할 수 없는 경우 무시
		}
	}
}