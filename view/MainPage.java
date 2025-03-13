package view;

import model.DayAccountBook;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

public class MainPage {

	public void anonymousMainPage() {
		System.out.println("===== 가계부 어플리케이션 =====");
		System.out.println("1. 회원가입");
		System.out.println("2. 로그인");
		System.out.println("0. 프로그램 종료");
		System.out.println("==========================");
	}
	
	/**
	 * 회원가입 입력 형식 안내
	 * 사용자에게 이메일, 비밀번호, 닉네임 입력 형식을 안내합니다.
	 */
	public void showSignupGuide() {
		System.out.println("\n----- 회원가입 입력 형식 -----");
		System.out.println("이메일: example@domain.com 형식으로 입력해주세요.");
		System.out.println("비밀번호: 8자리 이상 16자리 이하 이며, 최소 하나 이상의 특수문자를 포함해야 합니다.");
		System.out.println("닉네임: 한글, 영문, 숫자만 허용되며, 2~12자 이내여야 합니다.");
		System.out.println("주의: 닉네임은 회원가입 후 변경할 수 없습니다.");
		System.out.println("---------------------------\n");
	}
	
	/**
	 * 로그인 입력 형식 안내
	 * 사용자에게 이메일, 비밀번호 입력 형식을 안내합니다.
	 */
	public void showLoginGuide() {
		System.out.println("\n----- 로그인 입력 형식 -----");
		System.out.println("이메일: 가입 시 등록한 이메일을 입력해주세요.");
		System.out.println("비밀번호: 가입 시 등록한 비밀번호를 입력해주세요.");
		System.out.println("-------------------------\n");
	}
}
