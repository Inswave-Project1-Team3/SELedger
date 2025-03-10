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
 * 사용자 관련 요청을 처리하는 컨트롤러 클래스
 * 
 * 이 클래스는 사용자 인터페이스(View)와 비즈니스 로직(Service) 사이에서 중간 역할을 하며,
 * 사용자의 요청을 받아 적절한 서비스 메서드를 호출하고 결과를 반환합니다.
 * 또한 로그인 상태 관리와 사용자 세션 정보를 유지합니다.
 */
public class UserController {
	// 사용자 관련 비즈니스 로직을 처리하는 서비스
	private final UserService userService;
	
	// 현재 로그인한 사용자의 이메일 (로그아웃 상태면 null)
	private String currentUserEmail = null;
	
	// 로그인 상태 (true: 로그인, false: 로그아웃)
	private boolean isLoggedIn = false;
	
	/**
	 * 생성자
	 * UserService 인스턴스를 생성하여 초기화합니다.
	 */
	public UserController() {
		this.userService = new UserService();
	}
	
	/**
	 * 회원가입
	 * 
	 * 사용자 정보를 받아 회원가입을 처리하고 결과를 반환합니다.
	 * 입력값 검증 실패 시 오류 메시지를 출력합니다.
	 * 회원가입 성공 시 사용자의 닉네임으로 폴더가 생성됩니다.
	 * 
	 * @param dto 회원가입 정보를 담은 DTO
	 * @return 회원가입 결과 (성공: true, 실패: false)
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
	 * 로그인
	 * 
	 * 이메일과 비밀번호를 받아 로그인을 처리하고 결과를 반환합니다.
	 * 로그인 성공 시 세션 정보를 업데이트합니다.
	 * 
	 * @param dto 로그인 정보를 담은 DTO
	 * @return 로그인 결과 (성공: true, 실패: false)
	 */
	public boolean login(LoginUserDTO dto) {
		boolean result = userService.login(dto);
		if (result) {
			// 로그인 성공 시 컨트롤러 내부 상태 업데이트
			this.isLoggedIn = true;
			this.currentUserEmail = dto.getEmail();
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
		} else
			System.out.println("로그인에 실패했습니다. 이메일 또는 비밀번호를 확인해주세요.");
		
		return result;
	}
	
	/**
	 * 로그아웃
	 * 
	 * 현재 로그인된 사용자의 세션 정보를 초기화합니다.
	 */
	public void logout() {
		// 컨트롤러 내부 상태 초기화
		this.isLoggedIn = false;
		this.currentUserEmail = null;
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
	 * 
	 * 현재 로그인된 사용자의 정보를 수정하고 결과를 반환합니다.
	 * 로그인 상태가 아니거나 다른 사용자의 정보를 수정하려는 경우 실패합니다.
	 * 닉네임은 변경할 수 없으므로, 닉네임 변경 요청은 무시됩니다.
	 * 
	 * @param dto 회원정보 수정 정보를 담은 DTO
	 * @return 수정 결과 (성공: true, 실패: false)
	 */
	public boolean updateUser(UpdateUserDTO dto) {
		// 로그인 상태 및 현재 사용자 확인
		if (!isLoggedIn || !currentUserEmail.equals(dto.getEmail())) {
			System.out.println("회원정보 수정 권한이 없습니다.");
			return false;
		}
		
		// 닉네임 변경 요청이 있는 경우 안내 메시지 출력
		if (dto.getNickname() != null && !dto.getNickname().isEmpty())
			System.out.println("닉네임은 변경할 수 없습니다. 닉네임 변경 요청은 무시됩니다.");
		
		try {
			boolean result = userService.updateUser(dto);
			if (result)
				System.out.println("회원정보가 성공적으로 수정되었습니다.");
			else
				System.out.println("회원정보 수정에 실패했습니다. 현재 비밀번호를 확인해주세요.");
			
			return result;
		} catch (IllegalArgumentException e) {
			System.out.println("회원정보 수정 실패: " + e.getMessage());
			return false;
		}
	}
	
	/**
	 * 회원탈퇴
	 * 
	 * 현재 로그인된 사용자의 탈퇴 처리를 하고 결과를 반환합니다.
	 * 로그인 상태가 아니거나 다른 사용자를 탈퇴시키려는 경우 실패합니다.
	 * 탈퇴 성공 시 자동으로 로그아웃 처리됩니다.
	 * 사용자 폴더는 삭제하지 않고 유지됩니다.
	 * 
	 * @param dto 회원탈퇴 정보를 담은 DTO
	 * @return 탈퇴 결과 (성공: true, 실패: false)
	 */
	public boolean deleteUser(DeleteUserDTO dto) {
		// 로그인 상태 및 현재 사용자 확인
		if (!isLoggedIn || !currentUserEmail.equals(dto.getEmail())) {
			System.out.println("회원탈퇴 권한이 없습니다.");
			return false;
		}
		
		boolean result = userService.deleteUser(dto);
		if (result) {
			// 탈퇴 성공 시 로그아웃 처리
			logout();
			System.out.println("회원탈퇴가 성공적으로 처리되었습니다.");
			System.out.println("사용자 폴더는 데이터 관리를 위해 유지됩니다.");
		} else
			System.out.println("회원탈퇴에 실패했습니다. 비밀번호를 확인해주세요.");
		
		return result;
	}
	
	/**
	 * 현재 로그인한 사용자 정보 조회
	 * 
	 * @return 현재 사용자 정보 (로그인 상태가 아니면 null)
	 */
	public User getCurrentUser() {
		if (!isLoggedIn)
			return null;
		
		return userService.getUserByEmail(currentUserEmail);
	}
	
	/**
	 * 현재 로그인 상태 확인
	 * 
	 * @return 로그인 상태 (로그인: true, 로그아웃: false)
	 */
	public boolean isLoggedIn() {
		return isLoggedIn;
	}
	
	/**
	 * 현재 로그인한 사용자의 이메일 조회
	 * 
	 * @return 현재 사용자 이메일 (로그인 상태가 아니면 null)
	 */
	public String getCurrentUserEmail() {
		return isLoggedIn ? currentUserEmail : null;
	}
	
	/**
	 * 현재 로그인한 사용자의 폴더 경로 조회
	 * 
	 * @return 사용자 폴더 경로 (로그인 상태가 아니면 null)
	 */
	public String getCurrentUserDirectoryPath() {
		if (!isLoggedIn)
			return null;
		
		return FileUtil.getUserDirectoryPath(currentUserEmail);
	}
}


