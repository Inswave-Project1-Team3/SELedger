package app;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import DTO.CreateUserDTO;
import DTO.LoginUserDTO;
import contoller.AccountBookController;
import contoller.UserController;
import model.ExpenseCategory;
import model.IncomeCategory;
import model.MonthAccountBook;
import DTO.CreateTransactionAccountBookDTO;
import DTO.CreateUserDTO;
import DTO.LoginUserDTO;
import DTO.CreateAccountBookDTO;
import contoller.AccountBookController;
import contoller.UserController;
import model.DayAccountBook;
import util.StringCheck;
import view.MainPage;

/**
 * 애플리케이션 실행을 담당하는 클래스
 */
public class App {
    //로그인 상태 (true: 로그인됨, false: 비로그인)
    public static boolean loginCheck = true;

    //현재 로그인한 사용자의 이메일
    public static String userEmail = "";

    public static Map<Integer, DayAccountBook> dayAccountBookMap = new HashMap<>();
    public static Map<Integer, Map<Integer, DayAccountBook>> monthAccountBook = new HashMap<>();

    //애플리케이션 실행 메서드
    public void run() {
        Scanner sc = new Scanner(System.in);
        MainPage mainPage = new MainPage();
        StringCheck stringCheck = new StringCheck();
        UserController userController = new UserController();
        AccountBookController accountBookController = new AccountBookController();
        StringCheck stringcheck = new StringCheck();


        while (true) {
            if (!loginCheck) {  // 로그인되지 않은 경우
                mainPage.anonymousMainPage();
                int number = stringCheck.numberCheck(sc.next());

                switch (number) {
                    case 1: // 회원가입
                        System.out.print("이메일: ");
                        String email = sc.next();
                        System.out.print("비밀번호: ");
                        String password = sc.next();
                        System.out.print("닉네임: ");
                        String nickname = sc.next();

                        // 회원가입 요청
                        userController.createUser(new CreateUserDTO(email, password, nickname));
                        break;

                    case 2: // 로그인
                        System.out.print("이메일: ");
                        email = sc.next();
                        System.out.print("비밀번호: ");
                        password = sc.next();

                        // 로그인 요청
                        if (loginCheck) {
                            loginCheck = true;
                            userEmail = email;
                            System.out.println("로그인 성공!");
                        } else {
                            System.out.println("로그인 실패! 이메일 또는 비밀번호를 확인하세요.");
                        }
                        break;

                    default:
                        System.out.println("화면에 표시된 값만 입력하실 수 있습니다.");
                }
            } else {  // 로그인된 경우

                mainPage.userMainPage();
                mainPage.mainSelect();
                accountBookController.getMontAccountBook();
                int number = stringcheck.numberCheck(sc.next());
                switch (number) {
                    // 상세 요일 보기
                    case 1:
                        System.out.println("조회하고 싶은 일수를 입력해주세요");
                        int day = stringcheck.numberCheck(sc.next());
                        accountBookController.getDayAccountBook(day);
                        mainPage.DayAccountBookPage();
                        int accountBookNumber = stringcheck.numberCheck(sc.next());
                        switch (accountBookNumber) {
                            case 1:
                                System.out.println("아래의 값을 순서대로 입력해주세요");
                                System.out.println("수익이면 0, 지출이면 1");
                                System.out.println("가격");
                                System.out.println("메모내용");
                                boolean benefitCheck = (sc.next().equals("0"));
                                long money = sc.nextLong();
                                String memo = sc.next();
                                accountBookController.createDayAccountBook(
                                        new CreateAccountBookDTO(memo),
                                        new CreateTransactionAccountBookDTO(benefitCheck, money),
                                        day);
                                break;
                            case 2: // 회원정보 수정, 친구 가계부 보기, 계정탈퇴

                                break;
                            case 9:
                                loginCheck = false;
                                userEmail = "";
                                System.out.println("로그아웃 되었습니다.");
                                break;
                            default:
                                System.out.println("올바른 값을 입력하세요.");
                        }

                        break;

                    // 친구 가계부 보기
                    // email 값 받아서 찾아가기
                    case 2:

                }

            }
        }
    }
}
