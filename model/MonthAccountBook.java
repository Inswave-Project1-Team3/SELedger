package model;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

public class MonthAccountBook {
	
    private YearMonth month; //해당 월 

    private long totalMoney; //총 보유금액

    private IncomeCategory maxIncomeCategory; //가장 많이 사용된 수입 카테고리
    
    private ExpenseCategory maxExpenseCategory; //가장 많이 사용된 수입 카테고리

    private Map<Integer, DayAccountBook> dayAccountBookMaps = new HashMap<>(); //일별 가계부를 저장(Key: 해당 월의 날짜, Value: 해당 날짜의 가계부)

    
    public YearMonth getMonth() {
		return month;
	}

	public long getTotalMoney() {
		return totalMoney;
	}

	public IncomeCategory getMaxIncomeCategory() {
		return maxIncomeCategory;
	}

	public ExpenseCategory getMaxExpenseCategory() {
		return maxExpenseCategory;
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
     * 월별 총 잔액 업데이트 (거래 내역 추가 시 호출)
     * @param amount 거래 금액 (양수: 수입, 음수: 지출)
     */
    private void updateTotalMoney(long amount) {
        this.totalMoney += amount;
    }

    /**
     * 가장 많이 사용된 카테고리 업데이트
     */
    /*private void updateMaxCategory() {
    	Map<IncomeCategory, Double> categoryIncomeSum = new HashMap<IncomeCategory, Double>();
    	Map<ExpenseCategory, Double> categoryExpenseSum = new HashMap<ExpenseCategory, Double>();
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
    }*/

    public MonthAccountBook(YearMonth month) {
        this.month = month;
        this.totalMoney = 0; // 기본값 0으로 초기화
        this.maxIncomeCategory = null; // 기본값으로 null 설정 (필요에 따라 다른 값 설정 가능)
        this.maxExpenseCategory = null; // 기본값으로 null 설정 (필요에 따라 다른 값 설정 가능)
        this.dayAccountBookMaps = new HashMap<>(); // 맵 초기화
    }
}
