package app;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import DTO.CreateUserDTO;
import DTO.DeleteUserDTO;
import DTO.LoginUserDTO;
import DTO.UpdateUserDTO;
import contoller.UserController;
import util.InputValidator;
import util.StringCheck;
import view.MainPage;

/**
 * 애플리케이션의 메인 클래스
 * 
 * 이 클래스는 프로그램의 진입점이며, 사용자 인터페이스와 컨트롤러를 연결합니다.
 * 로그인 상태를 관리하고, 사용자 입력을 처리합니다.
 */
public class App {
    private static final Map<String, Integer> CurrentMonth = new HashMap<>();
    public static boolean loginCheck = false; // 초기값은 false (로그인 안 된 상태)
    public static String userEmail = "";

    /**
     * 애플리케이션 실행 메서드
     * 사용자 입력을 받아 적절한 기능을 실행합니다.
     */
    public void run() {
        Scanner sc = new Scanner(System.in);
        MainPage mainPage = new MainPage();
        StringCheck stringcheck = new StringCheck();
        UserController userController = new UserController();
        
        while (true) {
            // 로그인 상태가 아닐 때
            if (!loginCheck) {
                mainPage.anonymousMainPage();
                // 올바른 숫자를 입력할 때까지 반복
                System.out.print("메뉴 선택: ");
                int number = stringcheck.getValidNumber(sc);
                String email = "";
                String password = "";
                String nickname = "";
                
                switch(number) {
                case 1: // 회원가입
                    boolean isValidInput = false;
                    
                    while (!isValidInput) {
                        System.out.println("이메일을 입력하세요 (example@domain.com):");
                        email = sc.next();
                        
                        if (!InputValidator.isValidEmail(email)) {
                            System.out.println("이메일 형식이 올바르지 않습니다. 다시 입력해주세요.");
                            continue;
                        }
                        
                        System.out.println("비밀번호를 입력하세요 (8자리 이상, 특수문자 포함):");
                        password = sc.next();
                        
                        if (!InputValidator.isValidPassword(password)) {
                            System.out.println("비밀번호는 8자리 이상이며, 최소 하나 이상의 특수문자를 포함해야 합니다. 다시 입력해주세요.");
                            continue;
                        }
                        
                        System.out.println("닉네임을 입력하세요 (한글, 영문, 숫자만 허용, 2~12자):");
                        System.out.println("주의: 닉네임은 회원가입 후 변경할 수 없습니다.");
                        nickname = sc.next();
                        
                        if (!InputValidator.isValidNickname(nickname)) {
                            System.out.println("닉네임은 한글, 영문, 숫자만 허용되며, 2~12자 이내여야 합니다. 다시 입력해주세요.");
                            continue;
                        }
                        
                        isValidInput = true;
                    }
                    
                    userController.createUser(new CreateUserDTO(email, password, nickname));
                    break;
                    
                case 2: // 로그인
                    System.out.println("이메일을 입력하세요:");
                    email = sc.next();
                    
                    if (!InputValidator.isValidEmail(email)) {
                        System.out.println("이메일 형식이 올바르지 않습니다.");
                        break;
                    }
                    
                    System.out.println("비밀번호를 입력하세요:");
                    password = sc.next();

                    userController.login(new LoginUserDTO(email, password));
                    break;
                    
                case 0: // 프로그램 종료
                    System.out.println("프로그램을 종료합니다.");
                    sc.close();
                    return;
                    
                default:
                    System.out.println("화면에 표시된 값만 입력하실 수 있습니다");
                }
            } 
            // 로그인 상태일 때
            else {
                mainPage.userMainPage();
                // 올바른 숫자를 입력할 때까지 반복
                System.out.print("메뉴 선택: ");
                int number = stringcheck.getValidNumber(sc);
                
                switch(number) {
                case 1: // 가계부 관련 기능 (다른 팀원이 구현)
                    // TODO: 가계부 관련 기능 구현
                    break;
                    
                case 2: // 회원정보 수정
                    System.out.println("현재 비밀번호를 입력하세요:");
                    String currentPassword = sc.next();
                    
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
                    
                    userController.updateUser(new UpdateUserDTO(userEmail, currentPassword, newPassword, newNickname));
                    break;
                    
                case 3: // 회원탈퇴
                    System.out.println("정말 탈퇴하시겠습니까? (Y/N)");
                    String confirm = sc.next();
                    
                    if (confirm.equalsIgnoreCase("Y")) {
                        System.out.println("비밀번호를 입력하세요:");
                        String password = sc.next();
                        
                        userController.deleteUser(new DeleteUserDTO(userEmail, password));
                    }
                    break;
                    
                case 4: // 로그아웃
                    userController.logout();
                    break;
                    
                case 0: // 프로그램 종료
                    System.out.println("프로그램을 종료합니다.");
                    sc.close();
                    return;
                    
                default:
                    System.out.println("화면에 표시된 값만 입력하실 수 있습니다");
                }
            }
        }
    }
}