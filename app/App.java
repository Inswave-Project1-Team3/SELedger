package app;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import DTO.CreateTransactionAccountBookDTO;
import DTO.CreateUserDTO;
import DTO.LoginUserDTO;
import DTO.CreateAccountBookDTO;
import contoller.AccountBookController;
import contoller.UserController;
import model.DayAccountBook;
import util.StringCheck;
import view.MainPage;

public class App {
    private static final Map<String, Integer> CurrentMonth = new HashMap<>();
    public static Map<Integer, DayAccountBook> dayAccountBookMap = new HashMap<>();
    public static boolean loginCheck;
    public static String userEmail = "";

    public void run() {
    	Scanner sc = new Scanner(System.in);
    	MainPage mainPage = new MainPage();
    	StringCheck stringcheck = new StringCheck();
    	UserController userController = new UserController();
		AccountBookController accountBookController = new AccountBookController();

    	
        while (true) {

            int number;
            // 로그인이 아닐 경우
            if (loginCheck) {
                mainPage.anonymousMainPage();
                number = stringcheck.numberCheck(sc.next());
                String email = "";
                String password = "";
                switch (number) {
                    case 1:
                        // email 체크해야 함
                        email = sc.next();
                        // pwd 양칙 체크
                        password = sc.next();
                        // 문자열 길이만 체크?
                        String nickname = sc.next();

                        userController.createUser(new CreateUserDTO(email, password, nickname));
                        break;
                    case 2:
                        email = sc.next();
                        password = sc.next();

                        userController.login(new LoginUserDTO(email, password));

                        break;
                    default:
                        System.out.println("화면에 표시된 값만 입력하실 수 있습니다");
                }
                // 로그인 하였을 경우
            } else {
                mainPage.userMainPage();

                number = stringcheck.numberCheck(sc.next());
				switch (number){
					case 1 :
                        int accountBookNumber;
//                        accountBookNumber = stringcheck.numberCheck(sc.next());
//                        accountBookController.getDayAccountBook(accountBookNumber);

                        accountBookController.getDayAccountBook();
                        mainPage.DayAccountBookPage();
                        accountBookNumber = stringcheck.numberCheck(sc.next());
                        	switch (accountBookNumber){
                                case 1 :
                                    System.out.println("아래의 값을 순서대로 입력해주세요");
                                    System.out.println("수익이면 0, 지출이면 1");
                                    System.out.println("가격");
                                    System.out.println("메모내용");
                                    boolean benefitCheck = (sc.next().equals("0"));
                                    long money = sc.nextLong();
                                    String memo = sc.next();
                                    accountBookController.createDayAccountBook(
                                            new CreateAccountBookDTO(memo),
                                            new CreateTransactionAccountBookDTO(benefitCheck, money));
                                    break;
                                case 2 : // 회원정보 수정, 로그아웃, 계정탈퇴

                                    break;
                                case 9 :
                            }

                        break;
                    
                    // 친구 가계부 보기
                    // email 값 받아서 찾아가기
                    case 2 :

				}


            }
        }
    }

}
