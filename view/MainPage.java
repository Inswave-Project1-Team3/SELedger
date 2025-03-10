package view;

import app.App;
import model.User;
import service.UserService;

/**
 * 사용자 인터페이스를 담당하는 뷰 클래스
 * 
 * 이 클래스는 사용자에게 메뉴를 표시하고 상호작용하는 인터페이스를 제공합니다.
 * 로그인 상태에 따라 다른 메뉴를 표시하며, 가계부 정보를 조회하는 기능도 포함합니다.
 */
public class MainPage {
	/**
	 * 비로그인 상태 메인 페이지
	 * 회원가입, 로그인 등의 메뉴를 표시합니다.
	 */
	public void anonymousMainPage() {
		System.out.println("===== 가계부 어플리케이션 =====");
		System.out.println("1. 회원가입");
		System.out.println("2. 로그인");
		System.out.println("0. 프로그램 종료");
		System.out.println("==========================");
		// 메뉴 선택 프롬프트는 App 클래스에서 직접 출력
	}
	
	/**
	 * 로그인 상태 메인 페이지
	 * 가계부 관리, 회원정보 수정, 회원탈퇴, 로그아웃 등의 메뉴를 표시합니다.
	 * App 클래스의 로그인 정보를 사용하여 사용자 정보를 조회합니다.
	 */
	public void userMainPage() {
		// App 클래스의 로그인 정보를 사용하여 사용자 정보 조회
		UserService userService = new UserService();
		User currentUser = userService.getUserByEmail(App.userEmail);
		
		if (currentUser == null) {
			System.out.println("오류: 사용자 정보를 찾을 수 없습니다.");
			return;
		}
		
		System.out.println("===== 가계부 어플리케이션 =====");
		System.out.println("환영합니다, " + currentUser.getNickName() + "님!");
		System.out.println("1. 가계부 관리");
		System.out.println("2. 회원정보 수정");
		System.out.println("3. 회원탈퇴");
		System.out.println("4. 로그아웃");
		System.out.println("0. 프로그램 종료");
		System.out.println("==========================");
		// 메뉴 선택 프롬프트는 App 클래스에서 직접 출력
	}
	
	/**
	 * 특정 일자의 가계부 정보 표시
	 * 본인 또는 다른 사용자의 가계부 정보를 표시합니다.
	 * 다른 팀원이 구현할 부분입니다.
	 * 
	 * @param email 조회할 사용자의 이메일
	 * @param number 조회할 일자
	 */
	public void dayInfo(String email, int number) {
		if (App.userEmail.equals(email)) {
			// 본인 가계부 조회
			System.out.println("===== " + number + "일 가계부 =====");
			// TODO: 가계부 정보 표시 (다른 팀원이 구현)
		} else {
			// 다른 사용자의 가계부 조회 (공개된 내역만)
			System.out.println("===== " + email + "님의 " + number + "일 가계부 =====");
			// TODO: 공개된 가계부 정보 표시 (다른 팀원이 구현)
		}
	}
}