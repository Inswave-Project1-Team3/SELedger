package util;

import java.util.Scanner;

/**
 * 문자열 검증 및 변환을 위한 유틸리티 클래스
 */
public class StringCheck {
	/**
	 * 문자열을 정수로 변환하고 검증합니다.
	 * 잘못된 입력이 들어오면 올바른 숫자를 입력할 때까지 다시 입력받습니다.
	 * 
	 * @param input 변환할 문자열
	 * @return 변환된 정수
	 */
	public int numberCheck(String input) {
		int num;
		try {
			num = Integer.parseInt(input);
			return num;
		} catch (NumberFormatException e) {
			System.out.println("올바른 숫자를 입력해주세요");
			return -1; // 잘못된 입력임을 나타내는 값 반환
		}
	}
	
	/**
	 * 사용자로부터 올바른 숫자를 입력받을 때까지 반복합니다.
	 * 
	 * @param scanner 입력을 받을 Scanner 객체
	 * @return 사용자가 입력한 올바른 숫자
	 */
	public int getValidNumber(Scanner scanner) {
		int number;
		do {
			String input = scanner.next();
			number = numberCheck(input);
		} while (number == -1);
		
		return number;
	}
}
