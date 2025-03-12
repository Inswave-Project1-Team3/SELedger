package app;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

import DTO.*;
import DTO.VO.GetMonthDataVO;
import contoller.AccountBookController;
import contoller.UserController;
import model.*;
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

    //방문할 사용자의 닉네임. 존재하지 않으면 자신의 가계부 호출
    public static String visitUserNickname = "";

    public static Scanner sc = new Scanner(System.in);
    public static int year = LocalDateTime.now().getYear();
    public static int month = LocalDateTime.now().getMonthValue();
    public static int day = LocalDateTime.now().getDayOfYear();

    MainPage mainPage = new MainPage();
    AccountBookPage accountBookPage = new AccountBookPage();
    StringCheck stringCheck = new StringCheck();
    UserController userController = new UserController();
    AccountBookController accountBookController = new AccountBookController();
    StringCheck stringcheck = new StringCheck();

    //애플리케이션 실행 메서드
    public void run() {

        while (true) {
            if (!loginCheck) {
                mainPage.anonymousMainPage();
                int number = stringCheck.numberCheck(sc);

                switch (number) {
                    case 1:
                        signUp();
                        break;
                    case 2:
                        login();
                        break;
                    case 0:
                        exitProgram();
                        return;
                    default:
                        System.out.println("화면에 표시된 값만 입력하실 수 있습니다.");
                }
            } else {
                handleLoggedInUser();
            }
        }
    }

    private void signUp() {
        mainPage.showSignupGuide(); // 회원가입 입력 형식 안내
        System.out.print("이메일: ");
        String email = sc.next();
        System.out.print("비밀번호: ");
        String password = sc.next();
        System.out.print("닉네임: ");
        String nickname = sc.next();

        userController.createUser(new CreateUserDTO(email, password, nickname));
    }

    private void login() {
        mainPage.showLoginGuide(); // 로그인 입력 형식 안내
        System.out.print("이메일: ");
        String email = sc.next();
        System.out.print("비밀번호: ");
        String password = sc.next();

        if (userController.login(new LoginUserDTO(email, password))) {
            userNickName = userController.getCurrentUserNickName();
        }
    }

    private void exitProgram() {
        System.out.println("프로그램을 종료합니다.");
        sc.close();
    }

    private void handleLoggedInUser() {
        GetMonthDataVO vo = (visitUserNickname.isEmpty()) ?
                accountBookController.getMonthMoney(userNickName) :
                accountBookController.getMonthMoney(visitUserNickname);

        accountBookPage.accountMainPage(vo);

        System.out.println("1. 상세요일 보기/2. 친구 가계부 보기/3. 회원정보 조회/4. 회원정보 수정 /7. 뒤로가기/8. 회원탈퇴/9. 로그아웃/0. 프로그램 종료");
        int number = stringcheck.numberCheck(sc);

        switch (number) {
            case 1:
                viewDetailedDay();
                break;
            case 2:
                getToUser();
                break;
            case 3:
                checkUser();
                break;
            case 4:
                updateUser();
                break;
            case 7:
                userNickName = "";
                visitUserNickname = "";
                break;
            case 8:
                deleteID();

                break;
            // 로그아웃
            case 9:
                userController.logout();
                // 로그아웃 시 닉네임 정보 초기화
                userNickName = "";
                visitUserNickname = "";
                break;

            case 0: // 프로그램 종료
                System.out.println("프로그램을 종료합니다.");
                exitProgram();
                return;

            default:
                System.out.println("화면에 표시된 값만 입력하실 수 있습니다.");
        }
    }


    private void viewDetailedDay() {
        System.out.println("조회하고 싶은 월수와 일수를 입력해주세요");
        month = stringcheck.numberCheck(sc);
        day = stringcheck.numberCheck(sc);

        DayAccountBook dayAccountBook = (visitUserNickname.isEmpty()) ?
                accountBookController.getDayAccountBook(day, userNickName) :
                accountBookController.getDayAccountBook(day, visitUserNickname);

        accountBookPage.DayAccountBookPage(dayAccountBook, month, day);

        System.out.println("1. 내역 추가 / 2. 내역 수정 / 3. 내역 삭제 / 4. 댓글달기 / 9. 뒤로가기");
        int accountBookNumber = stringcheck.numberCheck(sc);

        switch (accountBookNumber) {
            case 1:
                addTransaction(day);
                break;
            case 2:
                updateTransaction(day);
                break;
            case 3:
                deleteTransaction();
                break;
            case 9: // 뒤로가기
                visitUserNickname = "";
                System.out.println("메인 메뉴로 돌아갑니다.");
                break;
            default:
                System.out.println("올바른 값을 입력하세요.");
        }
    }

    private void addTransaction(int day) {
        if (!isUserAuthorized()) return;

        System.out.println("수익이면 0, 지출이면 1");
        boolean benefitCheck = (sc.next().equals("0"));

        System.out.println("카테고리");
        accountBookPage.categoryView(benefitCheck);
        String input = sc.next().toUpperCase();
        AccountCategory accountCategory = null;
        try {
            accountCategory = (benefitCheck) ?
                    IncomeCategory.valueOf(input) :
                    ExpenseCategory.valueOf(input);
        } catch (IllegalArgumentException e) {
            System.out.println("화면에 표시된 값만 입력 가능합니다. 다시 입력해주세요");
            return;
        }

        System.out.println("아래의 값을 순서대로 입력해주세요");
        System.out.println("1. 가격");
        System.out.println("2. 메모내용");
        long money = stringcheck.longCheck(sc);
        String memo = sc.next();

        accountBookController.createDayAccountBook(
                new CreateAccountBookDTO(memo),
                new CreateTransactionAccountBookDTO(benefitCheck, money, accountCategory),
                day);
    }

    private void updateTransaction(int day) {
        if (!isUserAuthorized()) return;

        System.out.println("몇번째 값을 수정하시겠습니까?");
        int transactionNumber = stringcheck.numberCheck(sc);
        System.out.println("수익이면 0, 지출이면 1");
        boolean benefitCheck = (sc.next().equals("0"));

        System.out.println("카테고리");
        accountBookPage.categoryView(benefitCheck);
        String input = sc.next().toUpperCase();

        AccountCategory accountCategory = (benefitCheck) ?
                IncomeCategory.valueOf(input) :
                ExpenseCategory.valueOf(input);

        System.out.println("가격");
        long money = sc.nextLong();

        accountBookController.updateDayAccountBook(
                new UpdateTransactionAccountBookDTO(benefitCheck, money, accountCategory),
                transactionNumber, day);
    }

    private void deleteTransaction() {
        if (!isUserAuthorized()) return;
        System.out.println("몇번째 값을 삭제하시겠습니까?");
        accountBookController.deleteDayAccountBook(stringCheck.numberCheck(sc), day);

    }

    // 다른 유저의 보기
    private void getToUser() {
        System.out.println("방문할 유저의 nickName 을 입력해주세요. 존재하지 않는 user 라면 방문하지 않습니다");
        String inputUserName = sc.next();
        if (userController.checkNicknameExists(inputUserName)) {
            visitUserNickname = inputUserName;
        } else {
            System.out.println("존재하지 않는 유저이거나, 본인의 nickname 을 입력하셨습니다. 다시 입력해주세요");
            visitUserNickname = "";
            System.out.println();
        }
    }

    //현재 로그인한 사용자 정보 조회
    private void checkUser() {
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
    }

    private void updateUser() {
        // 회원정보 수정
        System.out.println("\n----- 회원정보 수정 -----");
        System.out.println("현재 비밀번호를 입력하세요:");
        String currentPassword = sc.next();

        System.out.println("새 이메일을 입력하세요 (변경하지 않으려면 'skip' 입력):");
        String newEmail = sc.next();
        if (newEmail.equalsIgnoreCase("skip"))
            newEmail = null;
        else if (!InputValidator.isValidEmail(newEmail)) {
            System.out.println("이메일 형식이 올바르지 않습니다.");
            return;
        }
        System.out.println("새 비밀번호를 입력하세요 (8자리 이상, 특수문자 포함, 변경하지 않으려면 'skip' 입력):");
        String newPassword = sc.next();
        if (newPassword.equalsIgnoreCase("skip"))
            newPassword = null;
        else if (!InputValidator.isValidPassword(newPassword)) {
            System.out.println("새 비밀번호는 8자리 이상이며, 최소 하나 이상의 특수문자를 포함해야 합니다.");
            return;
        }
        // 닉네임은 변경할 수 없으므로 null로 설정
        String newNickname = null;
        System.out.println("닉네임은 변경할 수 없습니다. 회원가입 시 설정한 닉네임이 유지됩니다.");

        userController.updateUser(new UpdateUserDTO(userEmail, currentPassword, newEmail, newPassword, newNickname));
    }

    private void deleteID() {
        System.out.println("\n----- 회원탈퇴 -----");
        System.out.println("정말 탈퇴하시겠습니까? (Y/N)");
        String confirm = sc.next();

        if (confirm.equalsIgnoreCase("Y")) {
            System.out.println("비밀번호를 입력하세요:");
            String password = sc.next();

            userController.deleteUser(new DeleteUserDTO(userEmail, password));
            userNickName = "";
            visitUserNickname = "";
        } else {
            System.out.println("회원탈퇴가 취소되었습니다.");
        }
    }

    private boolean isUserAuthorized() {
        if (!visitUserNickname.isEmpty()) {
            System.out.println("자신의 게시글에만 접근할 수 있습니다");
            return false;
        }
        return true;
    }


}