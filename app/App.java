package app;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import DTO.CreateUserDTO;
import DTO.LoginUserDTO;
import contoller.UserController;
import util.StringCheck;
import view.MainPage;

public class App {
    private static final Map<String, Integer> CurrentMonth = new HashMap<>();
    public static boolean loginCheck; 
    public static String userEmail = "";

    public void run() {
    	Scanner sc = new Scanner(System.in);
    	MainPage mainPage = new MainPage();
    	StringCheck stringcheck = new StringCheck();
    	UserController userColltroller = new UserController();
    	
        while (true) {
        
        	// 로그인 아닐 시
        	if(loginCheck) {
	        	mainPage.anonymousMainPage();
	        	int number = stringcheck.numberCheck(sc.next());
	        	String email = "";
	        	String password = "";
	        	switch(number) {
	        	case 1 : 
	        		// email 체크해야 함
	        		email = sc.next();
	        		// pwd 양칙 체크
	        		password = sc.next();
	        		// 문자열 길이만 체크?
	        		String nickname = sc.next();
	        		
	        		userColltroller.createUser(new CreateUserDTO(email, password, nickname));
	        		break;
	        	case 2 : 
	        		email = sc.next();
	        		password = sc.next();

	        		userColltroller.login(new LoginUserDTO(email, password));
	        		
	        		break;
	        	default : System.out.println("화면에 표시된 값만 입력하실 수 있습니다");
	        	}
	        // 로그인 하였을 경우
        	} else {
        		mainPage.userMainPage();
        		
        		
        	}
        }
    }

}
