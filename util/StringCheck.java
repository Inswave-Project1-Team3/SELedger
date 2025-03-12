package util;

import java.util.Scanner;

/**
 * 문자열 검증 및 변환을 위한 유틸리티 클래스
 */
public class StringCheck {
	/**
	 * 문자열을 정수로 변환
	 * 
	 * @param input 변환할 문자열
	 * @return 변환된 정수 (실패 시 -1)
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
	 * 올바른 숫자 입력 받기
	 * 
	 * @param scanner 입력 Scanner
	 * @return 유효한 정수
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
