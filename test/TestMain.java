package test;

/**
 * 테스트 클래스를 실행하기 위한 메인 클래스
 * 
 * 이 클래스는 UserTest 클래스를 실행하여 사용자 관련 기능을 테스트합니다.
 * 독립적인 테스트 환경을 제공하여 다른 팀원들의 코드에 영향을 주지 않고
 * 사용자 관련 기능을 테스트할 수 있습니다.
 */
public class TestMain {
	/**
	 * 메인 메서드
	 * UserTest 클래스의 메인 메서드를 호출하여 테스트를 시작합니다.
	 * 
	 * @param args 명령행 인수 (UserTest 클래스로 전달됨)
	 */
	public static void main(String[] args) {
		System.out.println("===== 사용자 기능 테스트 시작 =====");
		UserTest.main(args);
	}
} 