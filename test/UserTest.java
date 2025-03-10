package test;

import DTO.CreateUserDTO;
import DTO.DeleteUserDTO;
import DTO.LoginUserDTO;
import DTO.UpdateUserDTO;
import contoller.UserController;
import model.User;
import util.InputValidator;
import util.StringCheck;

import java.util.Scanner;

/**
 * 사용자 관련 기능을 테스트하기 위한 클래스
 * 회원가입, 로그인, 회원정보 수정, 회원탈퇴 등의 기능을 테스트합니다.
 */
public class UserTest {
	private static final UserController userController = new UserController();
	private static final Scanner scanner = new Scanner(System.in);
	private static String currentUserEmail = null;
	private static final StringCheck stringCheck = new StringCheck();
	
	/**
	 * 메인 메서드
	 * 테스트 메뉴를 표시하고 사용자 입력에 따라 테스트를 실행합니다.
	 * 
	 * @param args 명령행 인수 (사용하지 않음)
	 */
	public static void main(String[] args) {
		System.out.println("===== 사용자 기능 테스트 =====");
		
		boolean running = true;
		while (running) {
			printMenu();
			int choice = getIntInput("메뉴 선택: ");
			
			switch (choice) {
				case 1:
					testRegister();
					break;
				case 2:
					testLogin();
					break;
				case 3:
					testUpdateUser();
					break;
				case 4:
					testDeleteUser();
					break;
				case 5:
					testLogout();
					break;
				case 6:
					testGetCurrentUser();
					break;
				case 0:
					running = false;
					System.out.println("테스트를 종료합니다.");
					break;
				default:
					System.out.println("잘못된 메뉴 선택입니다.");
			}
			
			System.out.println(); // 줄바꿈
		}
		
		scanner.close();
	}
	
	/**
	 * 메뉴 출력
	 * 테스트 가능한 기능 목록을 표시합니다.
	 */
	private static void printMenu() {
		System.out.println("\n===== 메뉴 =====");
		System.out.println("1. 회원가입 테스트");
		System.out.println("2. 로그인 테스트");
		System.out.println("3. 회원정보 수정 테스트");
		System.out.println("4. 회원탈퇴 테스트");
		System.out.println("5. 로그아웃 테스트");
		System.out.println("6. 현재 사용자 정보 조회 테스트");
		System.out.println("0. 종료");
		System.out.println("==============");
	}
	
	/**
	 * 회원가입 테스트
	 * 이메일, 비밀번호, 닉네임을 입력받아 회원가입을 테스트합니다.
	 * 회원가입 성공 시 사용자의 닉네임으로 폴더가 생성됩니다.
	 */
	private static void testRegister() {
		System.out.println("\n===== 회원가입 테스트 =====");
		
		String email = getValidatedInput("이메일: ", InputValidator::isValidEmail, 
				"이메일 형식이 올바르지 않습니다. 다시 입력해주세요.");
		
		String password = getValidatedInput("비밀번호 (8자리 이상, 특수문자 포함): ", InputValidator::isValidPassword, 
				"비밀번호는 8자리 이상이며, 최소 하나 이상의 특수문자를 포함해야 합니다. 다시 입력해주세요.");
		
		System.out.println("주의: 닉네임은 회원가입 후 변경할 수 없습니다.");
		String nickname = getValidatedInput("닉네임 (한글, 영문, 숫자만 허용, 2~12자): ", InputValidator::isValidNickname, 
				"닉네임은 한글, 영문, 숫자만 허용되며, 2~12자 이내여야 합니다. 다시 입력해주세요.");
		
		CreateUserDTO dto = new CreateUserDTO(email, password, nickname);
		boolean result = userController.createUser(dto);
		
		if (result) {
			System.out.println("회원가입 성공!");
			System.out.println("사용자 폴더가 생성되었습니다: data/" + nickname);
		} else
			System.out.println("회원가입 실패!");
	}
	
	/**
	 * 로그인 테스트
	 * 이메일과 비밀번호를 입력받아 로그인을 테스트합니다.
	 */
	private static void testLogin() {
		System.out.println("\n===== 로그인 테스트 =====");
		
		String email = getValidatedInput("이메일: ", InputValidator::isValidEmail, 
				"이메일 형식이 올바르지 않습니다. 다시 입력해주세요.");
		
		System.out.print("비밀번호: ");
		String password = scanner.next();
		
		LoginUserDTO dto = new LoginUserDTO(email, password);
		boolean result = userController.login(dto);
		
		if (result) {
			System.out.println("로그인 성공!");
			currentUserEmail = email;
		} else
			System.out.println("로그인 실패!");
	}
	
	/**
	 * 회원정보 수정 테스트
	 * 현재 비밀번호와 새 비밀번호를 입력받아 회원정보 수정을 테스트합니다.
	 * 닉네임은 변경할 수 없으므로, 닉네임 변경 요청은 무시됩니다.
	 */
	private static void testUpdateUser() {
		System.out.println("\n===== 회원정보 수정 테스트 =====");
		
		if (currentUserEmail == null) {
			System.out.println("로그인이 필요합니다. 먼저 로그인해주세요.");
			return;
		}
		
		System.out.print("현재 비밀번호: ");
		String currentPassword = scanner.next();
		
		System.out.println("새 비밀번호 (8자리 이상, 특수문자 포함, 변경하지 않으려면 'skip' 입력): ");
		String newPassword = scanner.next();
		if (newPassword.equalsIgnoreCase("skip"))
			newPassword = null;
		else if (!InputValidator.isValidPassword(newPassword)) {
			System.out.println("새 비밀번호는 8자리 이상이며, 최소 하나 이상의 특수문자를 포함해야 합니다.");
			return;
		}
		
		// 닉네임은 변경할 수 없음을 안내
		System.out.println("닉네임은 변경할 수 없습니다. 회원가입 시 설정한 닉네임이 유지됩니다.");
		String newNickname = null;
		
		UpdateUserDTO dto = new UpdateUserDTO(currentUserEmail, currentPassword, newPassword, newNickname);
		boolean result = userController.updateUser(dto);
		
		if (result)
			System.out.println("회원정보 수정 성공!");
		else
			System.out.println("회원정보 수정 실패!");
	}
	
	/**
	 * 회원탈퇴 테스트
	 * 비밀번호를 입력받아 회원탈퇴를 테스트합니다.
	 * 사용자 폴더는 삭제하지 않고 유지됩니다.
	 */
	private static void testDeleteUser() {
		System.out.println("\n===== 회원탈퇴 테스트 =====");
		
		if (currentUserEmail == null) {
			System.out.println("로그인이 필요합니다. 먼저 로그인해주세요.");
			return;
		}
		
		System.out.println("정말 탈퇴하시겠습니까? (Y/N)");
		String confirm = scanner.next();
		
		if (!confirm.equalsIgnoreCase("Y")) {
			System.out.println("회원탈퇴가 취소되었습니다.");
			return;
		}
		
		System.out.print("비밀번호: ");
		String password = scanner.next();
		
		DeleteUserDTO dto = new DeleteUserDTO(currentUserEmail, password);
		boolean result = userController.deleteUser(dto);
		
		if (result) {
			System.out.println("회원탈퇴 성공!");
			System.out.println("사용자 폴더는 데이터 관리를 위해 유지됩니다.");
			currentUserEmail = null;
		} else
			System.out.println("회원탈퇴 실패!");
	}
	
	/**
	 * 로그아웃 테스트
	 * 로그아웃 기능을 테스트합니다.
	 */
	private static void testLogout() {
		System.out.println("\n===== 로그아웃 테스트 =====");
		
		if (currentUserEmail == null) {
			System.out.println("이미 로그아웃 상태입니다.");
			return;
		}
		
		userController.logout();
		currentUserEmail = null;
		System.out.println("로그아웃 되었습니다.");
	}
	
	/**
	 * 현재 사용자 정보 조회 테스트
	 * 현재 로그인한 사용자의 정보를 조회합니다.
	 */
	private static void testGetCurrentUser() {
		System.out.println("\n===== 현재 사용자 정보 조회 테스트 =====");
		
		if (currentUserEmail == null) {
			System.out.println("로그인이 필요합니다. 먼저 로그인해주세요.");
			return;
		}
		
		User user = userController.getCurrentUser();
		
		if (user != null) {
			System.out.println("현재 사용자 정보:");
			System.out.println("이메일: " + user.getEmail());
			System.out.println("닉네임: " + user.getNickName());
			System.out.println("관리자 여부: " + (user.isAdmin() ? "예" : "아니오"));
			System.out.println("탈퇴 여부: " + (user.isDeleted() ? "예" : "아니오"));
		} else
			System.out.println("사용자 정보를 조회할 수 없습니다.");
	}
	
	/**
	 * 정수 입력 받기
	 * 잘못된 입력이 들어오면 올바른 숫자를 입력할 때까지 다시 입력받습니다.
	 * 
	 * @param prompt 입력 안내 메시지
	 * @return 입력받은 정수
	 */
	private static int getIntInput(String prompt) {
		System.out.print(prompt);
		return stringCheck.getValidNumber(scanner);
	}
	
	/**
	 * 유효성 검증을 포함한 문자열 입력 받기
	 * 
	 * @param prompt 입력 안내 메시지
	 * @param validator 입력값 검증 함수
	 * @param errorMessage 오류 메시지
	 * @return 검증된 입력값
	 */
	private static String getValidatedInput(String prompt, java.util.function.Predicate<String> validator, String errorMessage) {
		String input;
		do {
			System.out.print(prompt);
			input = scanner.next();
			
			if (!validator.test(input))
				System.out.println(errorMessage);
		} while (!validator.test(input));
		
		return input;
	}
} 