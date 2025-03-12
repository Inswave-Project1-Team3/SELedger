package util;

import java.util.Scanner;
public class StringCheck {

	public int numberCheck(Scanner sc) {
		int num;

		while (true) {
			String input = sc.next();
			try {
				num = Integer.parseInt(input);
				return num; // 정상적인 숫자면 반환
			} catch (NumberFormatException e) {
				System.out.println("올바른 숫자를 입력해주세요");
			}
		}
	}

	public long longCheck(Scanner sc){
		long num;
		while (true) {
			String input = sc.next();
			try {
				num = Long.parseLong(input);
				return num;
			} catch (NumberFormatException e) {
				System.out.println("올바른 숫자를 입력해주세요");
			}
		}
	}

//	public int getValidNumber(Scanner scanner) {
//		int number;
//		do {
//			String input = scanner.next();
//			number = numberCheck(input);
//		} while (number == -1);
//
//		return number;
//	}
}
