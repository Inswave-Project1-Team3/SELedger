package app;

import java.time.YearMonth;
import java.time.LocalDate;
import java.util.Scanner;

import DTO.AccountBookDTO;
import DTO.CreateUserDTO;
import DTO.LoginUserDTO;
import contoller.AccountBookController;
import contoller.UserController;
import model.ExpenseCategory;
import model.IncomeCategory;
import model.MonthAccountBook;
import util.StringCheck;
import view.MainPage;

/**
 * 애플리케이션 실행을 담당하는 클래스
 */
public class App {
    /** 로그인 상태 (true: 로그인됨, false: 비로그인) */
    public static boolean loginCheck = false;

    /** 현재 로그인한 사용자의 이메일 */
    public static String userEmail = "";

    /**
     * 애플리케이션 실행 메서드
     */
    public void run() {
        Scanner sc = new Scanner(System.in);
        MainPage mainPage = new MainPage();
        StringCheck stringCheck = new StringCheck();
        UserController userController = new UserController();
        AccountBookController accountBookController = new AccountBookController(null);

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
                        boolean success = false; //= userController.loginUser(new LoginUserDTO(email, password));
                        if (success) {
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
                System.out.println("로그인한 사용자: " + userEmail);
                System.out.println("1. 거래 내역 추가");
                System.out.println("2. 월별 가계부 조회");
                System.out.println("3. 로그아웃");
                System.out.print("선택: ");

                int option = stringCheck.numberCheck(sc.next());

                switch (option) {
                    case 1: // 거래 내역 추가
                        System.out.print("거래 날짜 (YYYY-MM-DD): ");
                        LocalDate date = LocalDate.parse(sc.next());
                        System.out.print("거래 금액: ");
                        double amount = sc.nextDouble();
                        System.out.print("카테고리 (예: FOOD, TRANSPORT, SALARY 등): ");
                        String categoryStr = sc.next().toUpperCase();
                        IncomeCategory incomecategory;
                        ExpenseCategory expensecategory;

                        try {
                            incomecategory = IncomeCategory.valueOf(categoryStr);
                            expensecategory = ExpenseCategory.valueOf(categoryStr);
                        } catch (IllegalArgumentException e) {
                            System.out.println("잘못된 카테고리입니다. 다시 입력하세요.");
                            continue;
                        }

                        System.out.print("설명: ");
                        String description = sc.next();

                        // 거래 내역 추가
                        //accountBookController.addTransaction(new AccountBookDTO(date, amount, incomecategory, description));
                        System.out.println("거래 내역이 추가되었습니다.");
                        break;

                    case 2: // 월별 가계부 조회
                        System.out.print("조회할 월 (YYYY-MM): ");
                        YearMonth month = YearMonth.parse(sc.next());

                        // 가계부 정보 가져오기
                        MonthAccountBook monthAccountBook = accountBookController.getMonthAccountBook(month);
                        if (monthAccountBook == null) {
                            System.out.println("해당 월의 가계부 내역이 없습니다.");
                        } else {
                            System.out.println("==== " + month + " 가계부 ====");
                            System.out.println("총 수입: " + monthAccountBook.getTotalIncomeByDate(LocalDate.now()));
                            System.out.println("총 지출: " + monthAccountBook.getTotalExpenseByDate(LocalDate.now()));
                            System.out.println("총 잔액: " + monthAccountBook.getTotalMoney());
                            System.out.println("가장 많이 사용한 수입 카테고리: " + monthAccountBook.getMaxIncomeCategory());
                            System.out.println("가장 많이 사용한 지출 카테고리: " + monthAccountBook.getMaxExpenseCategory());
                        }
                        break;

                    case 3: // 로그아웃
                        loginCheck = false;
                        userEmail = "";
                        System.out.println("로그아웃 되었습니다.");
                        break;

                    default:
                        System.out.println("올바른 값을 입력하세요.");
                }
            }
        }
    }
}
