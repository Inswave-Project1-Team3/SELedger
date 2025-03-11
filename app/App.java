package app;

import java.time.LocalDateTime;
import java.util.Scanner;
import DTO.CreateUserDTO;
import DTO.DeleteUserDTO;
import DTO.LoginUserDTO;
import DTO.UpdateUserDTO;
import contoller.AccountBookController;
import contoller.UserController;
import model.*;
import DTO.CreateTransactionAccountBookDTO;
import DTO.CreateAccountBookDTO;
import util.InputValidator;
import util.StringCheck;
import view.MainPage;
import view.AccountBookPage;

/**
 * 애플리케이션 실행을 담당하는 클래스
 */
public class App {
    //로그인 상태 (true: 로그인됨, false: 비로그인)
    public static boolean loginCheck = false;

    //현재 로그인한 사용자의 이메일
    public static String userEmail = "";
    
    //현재 로그인한 사용자의 닉네임
    public static String userNickName = "";

    public static int year = LocalDateTime.now().getYear();
    public static int month = LocalDateTime.now().getMonthValue();

    //애플리케이션 실행 메서드
    public void run() {
        Scanner sc = new Scanner(System.in);
        MainPage mainPage = new MainPage();
        AccountBookPage accountBookPage = new AccountBookPage();
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
                        mainPage.showSignupGuide(); // 회원가입 입력 형식 안내
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
                        mainPage.showLoginGuide(); // 로그인 입력 형식 안내
                        System.out.print("이메일: ");
                        email = sc.next();
                        System.out.print("비밀번호: ");
                        password = sc.next();

                        // 로그인 요청
                        boolean loginResult = userController.login(new LoginUserDTO(email, password));
                        if (loginResult) {
                            // 로그인 성공 시 닉네임 정보 업데이트
                            userNickName = userController.getCurrentUserNickName();
                        }
                        break;

                    case 0: // 프로그램 종료
                        System.out.println("프로그램을 종료합니다.");
                        sc.close();
                        return;

                    default:
                        System.out.println("화면에 표시된 값만 입력하실 수 있습니다.");
                }
            } else {  // 로그인된 경우

                accountBookPage.accountMainPage(accountBookController.getMonthMoney(userNickName));

                mainPage.mainSelect();
                int number = stringcheck.numberCheck(sc.next());
                switch (number) {
                    // 상세 요일 보기
                    case 1:
                        System.out.println("조회하고 싶은 월수와 일수를 입력해주세요");
                        month = stringcheck.numberCheck(sc.next());
                        int day = stringcheck.numberCheck(sc.next());

                        DayAccountBook dayAccountBook = accountBookController.getDayAccountBook(month, day, userNickName);
                        accountBookPage.DayAccountBookPage(dayAccountBook, month, day);
                        int accountBookNumber = stringcheck.numberCheck(sc.next());
                        switch (accountBookNumber) {
                            case 1:
                                System.out.println("수익이면 0, 지출이면 1");
                                boolean benefitCheck = (sc.next().equals("0"));

                                accountBookPage.categoryView(benefitCheck);

                                String input = sc.next().toUpperCase();

                                AccountCategory accountCategory = (benefitCheck) ?
                                        IncomeCategory.valueOf(input) :
                                        ExpenseCategory.valueOf(input);

                                accountBookPage.addAccount();

                                long money = sc.nextLong();
                                String memo = sc.next();

                                accountBookController.createDayAccountBook(
                                        new CreateAccountBookDTO(memo),
                                        new CreateTransactionAccountBookDTO(benefitCheck, money, accountCategory),
                                        month, day, userNickName);
                                break;
                            case 9: // 뒤로가기
                                // 아무 작업 없이 상세 요일 보기 메뉴를 빠져나감
                                System.out.println("메인 메뉴로 돌아갑니다.");
                                break;
                            default:
                                System.out.println("올바른 값을 입력하세요.");
                        }
                        break;

                    // 친구 가계부 보기
                    // email 값 받아서 찾아가기
                    case 2:
                        // 기존 코드 유지
                        break;
                        
                    // 회원정보 조회
                    case 3:
                        // 현재 로그인한 사용자 정보 조회
                        User currentUser = userController.getCurrentUser();
                        if (currentUser != null) {
                            System.out.println("\n----- 회원정보 -----");
                            System.out.println("이메일: " + currentUser.getEmail());
                            System.out.println("닉네임: " + currentUser.getNickName());
                            System.out.println("회원상태: " + (currentUser.isDeleted() ? "탈퇴" : "활성"));
                            System.out.println("-------------------\n");
                        } else {
                            System.out.println("회원정보를 조회할 수 없습니다.");
                        }
                        break;
                        
                    // 회원정보 수정
                    case 4:
                        System.out.println("\n----- 회원정보 수정 -----");
                        System.out.println("현재 비밀번호를 입력하세요:");
                        String currentPassword = sc.next();
                        
                        System.out.println("새 이메일을 입력하세요 (변경하지 않으려면 'skip' 입력):");
                        String newEmail = sc.next();
                        if (newEmail.equalsIgnoreCase("skip"))
                            newEmail = null;
                        else if (!InputValidator.isValidEmail(newEmail)) {
                            System.out.println("이메일 형식이 올바르지 않습니다.");
                            break;
                        }
                        
                        System.out.println("새 비밀번호를 입력하세요 (8자리 이상, 특수문자 포함, 변경하지 않으려면 'skip' 입력):");
                        String newPassword = sc.next();
                        if (newPassword.equalsIgnoreCase("skip"))
                            newPassword = null;
                        else if (!InputValidator.isValidPassword(newPassword)) {
                            System.out.println("새 비밀번호는 8자리 이상이며, 최소 하나 이상의 특수문자를 포함해야 합니다.");
                            break;
                        }
                        
                        // 닉네임은 변경할 수 없으므로 null로 설정
                        String newNickname = null;
                        System.out.println("닉네임은 변경할 수 없습니다. 회원가입 시 설정한 닉네임이 유지됩니다.");
                        
                        userController.updateUser(new UpdateUserDTO(userEmail, currentPassword, newEmail, newPassword, newNickname));
                        break;
                        
                    // 회원탈퇴
                    case 8:
                        System.out.println("\n----- 회원탈퇴 -----");
                        System.out.println("정말 탈퇴하시겠습니까? (Y/N)");
                        String confirm = sc.next();
                        
                        if (confirm.equalsIgnoreCase("Y")) {
                            System.out.println("비밀번호를 입력하세요:");
                            String password = sc.next();
                            
                            userController.deleteUser(new DeleteUserDTO(userEmail, password));
                        } else {
                            System.out.println("회원탈퇴가 취소되었습니다.");
                        }
                        break;
                        
                    // 로그아웃
                    case 9:
                        userController.logout();
                        // 로그아웃 시 닉네임 정보 초기화
                        userNickName = "";
                        break;
                        
                    case 0: // 프로그램 종료
                        System.out.println("프로그램을 종료합니다.");
                        sc.close();
                        return;
                        
                    default:
                        System.out.println("화면에 표시된 값만 입력하실 수 있습니다.");
                }
            }
        }
    }
}
