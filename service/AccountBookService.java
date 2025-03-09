package service;

import lombok.RequiredArgsConstructor;
import model.DayAccountBook;
import model.MonthAccountBook;
import model.TransactionAccountBook;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

import DTO.AccountBookDTO;

@RequiredArgsConstructor
public class AccountBookService {
    /** 월별 가계부 저장소 */
    private Map<YearMonth, MonthAccountBook> accountBookRecords = new HashMap<>();

    /**
     * 특정 월의 가계부 가져오기 (없으면 생성)
     * @param month 조회할 월
     * @return 해당 월의 가계부
     */
    private MonthAccountBook getOrCreateMonthAccountBook(YearMonth month) {
        // 월이 없으면 새로운 MonthAccountBook 객체를 생성하여 반환
        return accountBookRecords.computeIfAbsent(month, null);
    }
    /**
     * 거래 내역 추가
     * @param dto 거래 정보 DTO
     */
    public void addTransaction(AccountBookDTO dto) {
        MonthAccountBook monthBook = getOrCreateMonthAccountBook(YearMonth.from(dto.getDate()));
        //TransactionAccountBook transaction = new TransactionAccountBook(dto.getAmount(), dto.getCategory(), dto.getDescription());
        //monthBook.addTransaction(dto.getDate(), transaction);
    }

    /**
     * 특정 날짜의 가계부 조회
     */
    public DayAccountBook getDailyAccountBookInfo(LocalDate date) {
        MonthAccountBook monthBook = accountBookRecords.get(YearMonth.from(date));
        return (monthBook != null) ? monthBook.getDailyAccountBookInfo(date) : null;
    }

    /**
     * 특정 날짜의 총 수입 반환
     */
    public double getTotalIncomeByDate(LocalDate date) {
        MonthAccountBook monthBook = accountBookRecords.get(YearMonth.from(date));
        return (monthBook != null) ? monthBook.getTotalIncomeByDate(date) : 0;
    }

    /**
     * 특정 날짜의 총 지출 반환
     */
    public double getTotalExpenseByDate(LocalDate date) {
        MonthAccountBook monthBook = accountBookRecords.get(YearMonth.from(date));
        return (monthBook != null) ? monthBook.getTotalExpenseByDate(date) : 0;
    }
    
    /**
     * 특정 월의 가계부 조회
     * @param month 조회할 월
     * @return 해당 월의 가계부 정보
     */
    public MonthAccountBook getMonthAccountBook(YearMonth month) {
        // 여기에서 해당 월의 가계부를 처리하는 로직을 구현
        // 예: MonthAccountBook 객체를 반환 (가계부 데이터를 DB나 메모리에서 가져옴)
        
        // 임시로 null 반환 (나중에 실제 데이터 처리 구현 필요)
        return new MonthAccountBook(); 
    }
}
