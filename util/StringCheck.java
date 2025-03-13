package util;

import java.time.YearMonth;
import java.util.Scanner;
import static app.App.month;
import static app.App.year;
public class StringCheck {

	public int numberCheck(String input) {
		int num;

		while (true) {
			try {
				num = Integer.parseInt(input);
				return num;
			} catch (NumberFormatException e) {
				System.out.println("올바른 숫자를 입력해주세요");
			}
		}
	}

	public long longCheck(String input){
		long num;
		while (true) {
			try {
				num = Long.parseLong(input);
				return num;
			} catch (NumberFormatException e) {
				System.out.println("올바른 숫자를 입력해주세요");
			}
		}
	}

	public int monthCheck(int input) {
		do {
			if (input < 1 || input > 12) {
				System.out.println("1 ~ 12 사이의 값만 입력 가능합니다");
			}
		} while (input < 1 || input > 12);

		return input;
	}

	public int dayCheck(int input) {
		int maxDays = YearMonth.of(year, month).lengthOfMonth();
		do {
			if (input < 1 || input > maxDays) System.out.println(month + "월에는 " + input + "일이 존재하지 않습니다. 다시 입력해주세요.");
		} while (input < 1 || input > maxDays);

		return input;
	}

	public boolean BooleanInputCheck(String input){
		do {
			if (!input.equals("0") && !input.equals("1")) System.out.println("0 또는 1만 입력 가능합니다");
		} while (!input.equals("0") && !input.equals("1"));

		return input.equals("0");
	}
}