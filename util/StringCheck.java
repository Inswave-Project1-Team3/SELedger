package util;

public class StringCheck {
	public int numberCheck(String number) {
		int num;
		try {
			num = Integer.parseInt(number);
		} catch (NumberFormatException e) {
			System.out.println("올바른 숫자를 입력해주세요");
			return 0;
		}
		return num;
	}

}
