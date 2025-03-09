package contoller;

import lombok.RequiredArgsConstructor;
import model.DayAccountBook;
import model.MonthAccountBook;
import service.AccountBookService;

import java.time.LocalDate;
import java.time.YearMonth;

import DTO.AccountBookDTO;

@RequiredArgsConstructor
public class AccountBookController {
    /** 가계부 서비스 */
    private final AccountBookService accountBookService;

    /**
     * 거래 내역 추가 요청 처리
     * @param dto 거래 정보 DTO
     */
    public void addTransaction(AccountBookDTO dto) {
        accountBookService.addTransaction(dto);
    }

    /**
     * 특정 날짜의 가계부 조회 요청 처리
     */
    public DayAccountBook getDailyAccountBookInfo(LocalDate date) {
        return accountBookService.getDailyAccountBookInfo(date);
    }

    /**
     * 특정 날짜의 총 수입 조회 요청 처리
     */
    public double getTotalIncomeByDate(LocalDate date) {
        return accountBookService.getTotalIncomeByDate(date);
    }

    /**
     * 특정 날짜의 총 지출 조회 요청 처리
     */
    public double getTotalExpenseByDate(LocalDate date) {
        return accountBookService.getTotalExpenseByDate(date);
    }

    /**
     * 특정 월의 가계부 조회 요청 처리
     * @param month 조회할 월
     * @return 해당 월의 가계부 정보
     */
    public MonthAccountBook getMonthAccountBook(YearMonth month) {
        // 서비스 계층에 월별 가계부 조회 요청
        return accountBookService.getMonthAccountBook(month);
    }
}

