package model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthAccountBook {
    /** 해당 월을 나타내는 변수 (예: 2025-03) */
    private YearMonth month;

    /** 해당 월의 총 잔액 (수입 - 지출) */
    private long totalMoney;

    /** 가장 많이 사용된 카테고리 */
    private Category maxCategory;

    /** 
     * 일별 가계부를 저장하는 맵 (Key: 해당 월의 날짜, Value: 해당 날짜의 가계부)
     */
    private Map<Integer, DayAccountBook> dayAccountBookMaps = new HashMap<>();

    /**
     * 특정 날짜에 거래 내역을 추가하는 메서드
     * @param date 거래가 발생한 날짜
     * @param transaction 추가할 거래 내역
     */
    public void addTransaction(LocalDate date, TransactionAccountBook transaction) {
        int day = date.getDayOfMonth();
        dayAccountBookMaps.putIfAbsent(day, new DayAccountBook(date));
        dayAccountBookMaps.get(day).addTransaction(transaction);
        updateTotalMoney();
        updateMaxCategory();
    }

    /**
     * 특정 날짜의 가계부 정보를 가져오는 메서드
     * @param date 조회할 날짜
     * @return 해당 날짜의 가계부 정보 (없으면 null)
     */
    public DayAccountBook getDailyAccountBookInfo(LocalDate date) {
        return dayAccountBookMaps.get(date.getDayOfMonth());
    }

    /**
     * 특정 날짜의 총 수입 반환
     */
    public double getTotalIncomeByDate(LocalDate date) {
        DayAccountBook daily = getDailyAccountBookInfo(date);
        return (daily != null) ? daily.getTotalIncome() : 0;
    }

    /**
     * 특정 날짜의 총 지출 반환
     */
    public double getTotalExpenseByDate(LocalDate date) {
        DayAccountBook daily = getDailyAccountBookInfo(date);
        return (daily != null) ? daily.getTotalExpense() : 0;
    }

    /**
     * 월별 총 잔액 업데이트
     */
    private void updateTotalMoney() {
        totalMoney = dayAccountBookMaps.values().stream()
                      .mapToLong(DayAccountBook::getNetAmount)
                      .sum();
    }

    /**
     * 가장 많이 사용된 카테고리 업데이트
     */
    private void updateMaxCategory() {
        Map<Category, Double> categorySums = new HashMap<>();
        for (DayAccountBook book : dayAccountBookMaps.values()) {
            for (TransactionAccountBook transaction : book.getTransactions()) {
                categorySums.put(transaction.getCategory(),
                        categorySums.getOrDefault(transaction.getCategory(), 0.0) + transaction.getAmount());
            }
        }
        maxCategory = categorySums.entrySet().stream()
                      .max(Map.Entry.comparingByValue())
                      .map(Map.Entry::getKey)
                      .orElse(null);
    }
}
