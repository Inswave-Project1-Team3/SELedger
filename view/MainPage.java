package view;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

public class MainPage {
	// 셀 폭(12칸), 셀 높이(3줄: Date/Income/Expense)
	private static final int CELL_WIDTH = 12;
	private static final int CELL_HEIGHT = 3;
	// 날짜별 데이터
	static class TransactionData {
		String income;
		String expense;
		TransactionData(String income, String expense) {
			this.income = income;
			this.expense = expense;
		}
	}

	public void anonymousMainPage() {
		System.out.println("===== 가계부 어플리케이션 =====");
		System.out.println("1. 회원가입");
		System.out.println("2. 로그인");
		System.out.println("0. 프로그램 종료");
		System.out.println("==========================");
	}
	
	/**
	 * 회원가입 입력 형식 안내
	 * 사용자에게 이메일, 비밀번호, 닉네임 입력 형식을 안내합니다.
	 */
	public void showSignupGuide() {
		System.out.println("\n----- 회원가입 입력 형식 -----");
		System.out.println("이메일: example@domain.com 형식으로 입력해주세요.");
		System.out.println("비밀번호: 8자리 이상이며, 최소 하나 이상의 특수문자를 포함해야 합니다.");
		System.out.println("닉네임: 한글, 영문, 숫자만 허용되며, 2~12자 이내여야 합니다.");
		System.out.println("주의: 닉네임은 회원가입 후 변경할 수 없습니다.");
		System.out.println("---------------------------\n");
	}
	
	/**
	 * 로그인 입력 형식 안내
	 * 사용자에게 이메일, 비밀번호 입력 형식을 안내합니다.
	 */
	public void showLoginGuide() {
		System.out.println("\n----- 로그인 입력 형식 -----");
		System.out.println("이메일: 가입 시 등록한 이메일을 입력해주세요.");
		System.out.println("비밀번호: 가입 시 등록한 비밀번호를 입력해주세요.");
		System.out.println("-------------------------\n");
	}
	
	public void userMainPage() {
			int year = 2025;
			int month = 3;

			// 예시 데이터 (영문·숫자만 사용)
			Map<Integer, TransactionData> dayData = new HashMap<>();
			dayData.put(1,  new TransactionData("1000", "500"));
			dayData.put(2,  new TransactionData("200000", "150"));
			dayData.put(5,  new TransactionData("3000", "100000"));
			dayData.put(10, new TransactionData("1500Plus", "300Minus"));

			YearMonth ym = YearMonth.of(year, month);
			int daysInMonth = ym.lengthOfMonth();
			LocalDate firstDay = LocalDate.of(year, month, 1);
			int startColumn = firstDay.getDayOfWeek().getValue() % 7; // Sunday=0

			// 요일 헤더
			String[] weekDays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
			for (String dayName : weekDays) {
				System.out.print("|" + fitAsciiWidth(dayName, CELL_WIDTH - 1));
			}
			System.out.println("|");
			printHorizontalBorder();

			int currentDay = 1;
			String[][] weekCells = new String[7][CELL_HEIGHT];

			// 첫 주
			for (int i = 0; i < 7; i++) {
				if (i < startColumn) {
					weekCells[i] = emptyCell();
				} else if (currentDay <= daysInMonth) {
					weekCells[i] = createCell(currentDay, dayData.get(currentDay));
					currentDay++;
				} else {
					weekCells[i] = emptyCell();
				}
			}
			printWeekRow(weekCells);

			// 나머지 주
			while (currentDay <= daysInMonth) {
				weekCells = new String[7][CELL_HEIGHT];
				for (int i = 0; i < 7; i++) {
					if (currentDay <= daysInMonth) {
						weekCells[i] = createCell(currentDay, dayData.get(currentDay));
						currentDay++;
					} else {
						weekCells[i] = emptyCell();
					}
				}
				printWeekRow(weekCells);
			}
		}

		// 빈 칸용 셀
		private static String[] emptyCell() {
			String[] cell = new String[CELL_HEIGHT];
			for (int i = 0; i < CELL_HEIGHT; i++) {
				cell[i] = "";
			}
			return cell;
		}

		// 날짜/Income/Expense 구성
		private static String[] createCell(int day, TransactionData data) {
			String[] cell = new String[CELL_HEIGHT];
			cell[0] = String.valueOf(day);
			if (data != null) {
				cell[1] = "In:" + data.income;
				cell[2] = "Out:" + data.expense;
			} else {
				cell[1] = "";
				cell[2] = "";
			}
			return cell;
		}

		// 한 주(7일) 출력
		private static void printWeekRow(String[][] weekCells) {
			for (int line = 0; line < CELL_HEIGHT; line++) {
				for (int i = 0; i < 7; i++) {
					// ASCII만 다루므로 length()로도 크게 문제 안 됨
					System.out.print("|" + fitAsciiWidth(weekCells[i][line], CELL_WIDTH - 1));
				}
				System.out.println("|");
			}
			printHorizontalBorder();
		}

		// 가로 경계선
		private static void printHorizontalBorder() {
			for (int i = 0; i < 7; i++) {
				System.out.print("+" + "-".repeat(CELL_WIDTH - 1));
			}
			System.out.println("+");
		}

		// ASCII만 다룬다고 가정할 때, 단순 length() 기준으로 폭 맞춤
		private static String fitAsciiWidth(String text, int width) {
			if (text == null) text = "";
			if (text.length() > width) {
				// 길이가 너무 길면 뒤에 "..." 붙이기
				if (width >= 3) {
					text = text.substring(0, width - 3) + "...";
				} else {
					text = text.substring(0, width);
				}
			}
			// 남은 폭만큼 공백
			StringBuilder sb = new StringBuilder(text);
			while (sb.length() < width) {
				sb.append(' ');
			}
			return sb.toString();

		// 1.1 달력 내부에 일일 수익 및 지출내역
		// 2. 달력 및 이번달 총 수익 및 지출내역
		// 3. 가장 많이 사용한 카테고리 + " 에 가장 많은 지출이 발생하였습니다"
		// 4. 선택지 제공(1. 상세요일 보기, 2. 친구 가계부 보기, ....)
	}

	public void mainSelect() {
		System.out.println("1. 상세요일 보기, 2. 친구 가계부 보기, 9. 로그아웃");
	}
	public void DayAccountBookPage() {
		// 일일 거래내역 전체 return
		// 댓글 전체 return

		System.out.println("1. 내역 추가, 2. 댓글달기, 9. 뒤로가기");
			
	}
}
