package view;

import app.App;

public class MainPage {
	public void anonymousMainPage() {
		System.out.println("가계부 어플");
    	System.out.println("1. 회원가입");
    	System.out.println("2. 로그인");
	}
	
	public void userMainPage() {
		// 1. 달력 보여주기
		// 1.1 달력 내부에 일일 수익 및 지출내역
		// 2. 달력 및 이번달 총 수익 및 지출내역
		// 3. 가장 많이 사용한 카테고리 + " 에 가장 많은 지출이 발생하였습니다"
		// 4. 선택지 제공(1. 상세요일 보기, 2. 친구 가계부 보기, ....)
		
		
	}
	
	public void dayInfo(String email, int number) {
		
		if(App.userEmail.equals(email)) {
			
		}
	}
	

}
