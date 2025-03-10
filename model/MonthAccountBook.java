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
	
    private YearMonth month; //해당 월 

    private long totalMoney; //총 보유금액

    private IncomeCategory maxIncomeCategory; //가장 많이 사용된 수입 카테고리
    
    private ExpenseCategory maxExpenseCategory; //가장 많이 사용된 수입 카테고리

    private Map<Integer, DayAccountBook> dayAccountBookMaps = new HashMap<>(); //일별 가계부를 저장(Key: 해당 월의 날짜, Value: 해당 날짜의 가계부)

    /**
     * 특정 날짜에 거래 내역을 추가하는 메서드
     * @param date 거래가 발생한 날짜
     * @param transaction 추가할 거래 내역
     */
    public void addTransaction(LocalDate date, TransactionAccountBook transaction) {
        DayAccountBook dayBook = dayAccountBookMaps.computeIfAbsent(date.getDayOfMonth(), d -> new DayAccountBook());
        dayBook.addTransaction(transaction);

        // 거래 금액을 반영하여 월별 총 잔액 업데이트
        updateTotalMoney(transaction.getAmount());
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
     * 월별 총 잔액 업데이트 (거래 내역 추가 시 호출)
     * @param amount 거래 금액 (양수: 수입, 음수: 지출)
     */
    private void updateTotalMoney(long amount) {
        this.totalMoney += amount;
    }

    /**
     * 가장 많이 사용된 카테고리 업데이트
     */
    private void updateMaxCategory() {
    	Map<IncomeCategory, Double> categoryIncomeSum = new HashMap<>();
        Map<ExpenseCategory, Double> categoryExpenseSum = new HashMap<>();
        for (DayAccountBook book : dayAccountBookMaps.values()) {
            for (TransactionAccountBook transaction : book.getTransactions()) {
                categoryIncomeSum.put(transaction.getIncomeCategory(),
                        categoryIncomeSum.getOrDefault(transaction.getIncomeCategory(), 0.0) + transaction.getAmount());
                categoryExpenseSum.put(transaction.getExpenseCategory(),
                        categoryExpenseSum.getOrDefault(transaction.getExpenseCategory(), 0.0) + transaction.getAmount());
            }
        }
        maxIncomeCategory = categoryIncomeSum.entrySet().stream()
                      									.max(Map.Entry.comparingByValue())
                      									.map(Map.Entry::getKey)
                      									.orElse(null);
        maxExpenseCategory = categoryExpenseSum.entrySet().stream()
                										  .max(Map.Entry.comparingByValue())
                										  .map(Map.Entry::getKey)
                										  .orElse(null);
    }

	public MonthAccountBook(YearMonth month) {
		// TODO Auto-generated constructor stub
	}
}
