package contoller;

import DTO.CreateUserDTO;
import DTO.DeleteUserDTO;
import DTO.LoginUserDTO;
import DTO.UpdateUserDTO;
import app.App;
import model.User;
import service.UserService;
import util.FileUtil;

/**
 * 사용자 관련 요청 처리 컨트롤러
 * 사용자 인터페이스와 서비스 계층 연결 및 세션 관리
 */
public class UserController {
	// 사용자 관련 비즈니스 로직 서비스
	private final UserService userService;
	
	// 현재 로그인한 사용자 닉네임
	private String currentUserNickName = null;
	
	// 로그인 상태
	private boolean isLoggedIn = false;
	
	/**
	 * 생성자 - UserService 초기화
	 */
	public UserController() {
		this.userService = new UserService();
	}
	
	/**
	 * 회원가입 처리
	 * 사용자 정보 검증 및 계정 생성
	 */
	public boolean createUser(CreateUserDTO dto) {
		try {
			boolean result = userService.createUser(dto);
			if (result) {
				System.out.println("회원가입이 성공적으로 완료되었습니다.");
				System.out.println("사용자 폴더가 생성되었습니다: data/" + dto.getNickname());
				
				// App 클래스의 정적 변수도 업데이트 (기존 코드와의 호환성 유지)
				if (App.class != null) {
					try {
						App.loginCheck = false;
						App.userEmail = "";
					} catch (Exception e) {
						// App 클래스가 없거나 접근할 수 없는 경우 무시
					}
				}
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
	 * 인증 후 세션 정보 업데이트
	 */
	public boolean login(LoginUserDTO dto) {
		boolean result = userService.login(dto);
		if (result) {
			// 로그인 성공 시 컨트롤러 내부 상태 업데이트
			this.isLoggedIn = true;
			
			// 사용자 정보를 조회하여 닉네임 설정
			User user = userService.getUserByEmail(dto.getEmail());
			if (user != null) {
				this.currentUserNickName = user.getNickName();
			}
			
			System.out.println("로그인에 성공했습니다.");
			
			// App 클래스의 정적 변수도 업데이트 (기존 코드와의 호환성 유지)
			if (App.class != null) {
				try {
					App.loginCheck = true;
					App.userEmail = dto.getEmail();
				} catch (Exception e) {
					// App 클래스가 없거나 접근할 수 없는 경우 무시
				}
			}
		} else {
			System.out.println("로그인에 실패했습니다. 이메일과 비밀번호를 확인해주세요.");
			
			// App 클래스의 정적 변수도 업데이트 (기존 코드와의 호환성 유지)
			if (App.class != null) {
				try {
					App.loginCheck = false;
					App.userEmail = "";
				} catch (Exception e) {
					// App 클래스가 없거나 접근할 수 없는 경우 무시
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 로그아웃 처리
	 * 세션 정보 초기화
	 */
	public void logout() {
		// 로그아웃 처리
		this.isLoggedIn = false;
		this.currentUserNickName = null;
		
		System.out.println("로그아웃 되었습니다.");
		
		// App 클래스의 정적 변수도 업데이트 (기존 코드와의 호환성 유지)
		if (App.class != null) {
			try {
				App.loginCheck = false;
				App.userEmail = "";
			} catch (Exception e) {
				// App 클래스가 없거나 접근할 수 없는 경우 무시
			}
		}
	}
	
	/**
	 * 회원정보 수정
	 * 현재 비밀번호 확인 후 정보 업데이트
	 */
	public boolean updateUser(UpdateUserDTO dto) {
		try {
			// 로그인 상태 확인
			if (!isLoggedIn) {
				System.out.println("로그인 후 이용 가능합니다.");
				return false;
			}
			
			// 현재 로그인한 사용자와 수정 요청한 사용자가 일치하는지 확인
			User currentUser = userService.getUserByNickName(currentUserNickName);
			if (currentUser == null || !currentUser.getEmail().equals(dto.getEmail())) {
				System.out.println("본인 계정만 수정할 수 있습니다.");
				return false;
			}
			
			boolean result = userService.updateUser(dto);
			
			if (result) {
				System.out.println("회원정보가 성공적으로 수정되었습니다.");
				
				// 이메일이 변경된 경우 App 클래스의 정적 변수도 업데이트
				if (dto.getNewEmail() != null && !dto.getNewEmail().isEmpty() && App.class != null) {
					try {
						App.userEmail = dto.getNewEmail();
					} catch (Exception e) {
						// App 클래스가 없거나 접근할 수 없는 경우 무시
					}
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
	 * 회원 탈퇴
	 * 비밀번호 확인 후 계정 비활성화
	 */
	public boolean deleteUser(DeleteUserDTO dto) {
		// 로그인 상태 확인
		if (!isLoggedIn) {
			System.out.println("로그인 후 이용 가능합니다.");
			return false;
		}
		
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
	 */
	public User getCurrentUser() {
		if (!isLoggedIn || currentUserNickName == null)
			return null;
		
		return userService.getUserByNickName(currentUserNickName);
	}
	
	/**
	 * 로그인 상태 확인
	 */
	public boolean isLoggedIn() {
		return isLoggedIn;
	}
	
	/**
	 * 현재 로그인한 사용자 닉네임 조회
	 */
	public String getCurrentUserNickName() {
		return currentUserNickName;
	}
	
	/**
	 * 현재 사용자의 데이터 디렉토리 경로 조회
	 */
	public String getCurrentUserDirectoryPath() {
		if (!isLoggedIn || currentUserNickName == null)
			return null;
		
		return "data/" + currentUserNickName;
	}
}