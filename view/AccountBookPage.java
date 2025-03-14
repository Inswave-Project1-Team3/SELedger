package view;


import DTO.VO.GetMonthDataVO;
import model.DayAccountBook;
import model.DayMoney;
import model.TransactionAccountBook;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;
import static app.App.month;
import static app.App.year;


public class AccountBookPage {
    private static final int CELL_WIDTH = 12;
    private static final int CELL_HEIGHT = 3;

    public void accountMainPage(GetMonthDataVO vo) {
        Map<Integer, DayMoney> dayData = vo.getDaysMoney();
        YearMonth ym = YearMonth.of(year, month);
        int daysInMonth = ym.lengthOfMonth();
        LocalDate firstDay = LocalDate.of(year, month, 1);
        int startColumn = firstDay.getDayOfWeek().getValue() % 7; // Sunday=0

        // 요일 헤더 출력
        String[] weekDays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String dayName : weekDays) {
            System.out.print("|" + fitAsciiWidth(dayName, CELL_WIDTH - 1));
        }
        System.out.println("|");
        printHorizontalBorder();

        int currentDay = 1;

        // 첫 주 출력
        String[][] weekCells = new String[7][CELL_HEIGHT];
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

        // 나머지 주 출력
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

        if(vo.getAccountCategory() != null) {
            System.out.println(month + "월은 " + vo.getAccountCategory() + " 에 " + vo.getMaxCategoryMoney() + "원으로 가장 많은 지출이 발생했습니다");
        } else {
            System.out.println(month + "월은 지출이 없습니다.");
        }

        System.out.println(month + "월 총 수입 및 지출량 : " + vo.getMonthTotalMoney() + "원");
    }

    private static String[] emptyCell() {
        String[] cell = new String[CELL_HEIGHT];
        for (int i = 0; i < CELL_HEIGHT; i++) {
            cell[i] = "";
        }
        return cell;
    }

    private static String[] createCell(int day, DayMoney data) {
        String[] cell = new String[CELL_HEIGHT];
        cell[0] = String.valueOf(day);
        if (data != null) {
            cell[1] = (data.getIncome() != 0) ? " + " + data.getIncome() : "";
            cell[2] = (data.getExpense() != 0) ? " - " + data.getExpense() : "";
        } else {
            cell[1] = "";
            cell[2] = "";
        }
        return cell;
    }

    private static void printWeekRow(String[][] weekCells) {
        for (int line = 0; line < CELL_HEIGHT; line++) {
            for (int i = 0; i < 7; i++) {
                System.out.print("|" + fitAsciiWidth(weekCells[i][line], CELL_WIDTH - 1));
            }
            System.out.println("|");
        }
        printHorizontalBorder();
    }

    private static void printHorizontalBorder() {
        for (int i = 0; i < 7; i++) {
            System.out.print("+" + "-".repeat(CELL_WIDTH - 1));
        }
        System.out.println("+");
    }

    private static String fitAsciiWidth(String text, int width) {
        if (text == null) text = "";
        if (text.length() > width) {
            if (width >= 3) {
                text = text.substring(0, width - 3) + "...";
            } else {
                text = text.substring(0, width);
            }
        }
        while (text.length() < width) {
            text += " ";
        }
        return text;
    }


    public void categoryView(boolean benefitCheck) {
        if(benefitCheck) {
            System.out.println("SALARY : 월급");
            System.out.println("BONUS : 보너스");
            System.out.println("INVESTMENT : 투자수익");
            System.out.println("ALLOWANCE : 용돈");
        } else {
            System.out.println("FOOD : 식비");
            System.out.println("TRANSPORT : 교통비");
            System.out.println("ENTERTAINMENT : 문화생활");
            System.out.println("DAILY_NECESSITIES : 생필품");
            System.out.println("CLOTHING : 의류");
        }
    }

    public void DayAccountBookPage(DayAccountBook dayAccountBook, int month, int day) {
        System.out.println("📅 " + month + " 월 " + day + " 일 가계부");
        System.out.println("==========================================");

        if (dayAccountBook != null) {
            for (TransactionAccountBook transaction : dayAccountBook.getTransactionAccountBooks()) {
                String title = transaction.getMoney() + "원";
                String value = transaction.getCreateDate() + ", " +
                        (transaction.isBenefit() ? "수입" : "지출") + ", " +
                        transaction.getAccountCategory().getDescription();

                System.out.printf("%-20s | %s%n", title, value);
            }

            System.out.println("------------------------------------------");
            System.out.println("메모: " + dayAccountBook.getMemo());
        } else {
            System.out.println("내역이 없습니다");
        }

        System.out.println("==========================================");
        System.out.println();
    }

}
