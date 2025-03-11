package contoller;

import DTO.CreateAccountBookDTO;
import DTO.CreateTransactionAccountBookDTO;
import lombok.RequiredArgsConstructor;
import model.DayAccountBook;
import service.AccountBookService;

import java.util.Map;


@RequiredArgsConstructor
public class AccountBookController {
    /** 가계부 서비스 */
    AccountBookService accountBookService = new AccountBookService();

    // 상세 가계부 추가
    public void createDayAccountBook(CreateAccountBookDTO AccountBookDTO,
                                     CreateTransactionAccountBookDTO transactionAccountBookDTO,
                                     int month,
                                     int day){
    accountBookService.createAccountBook(AccountBookDTO, transactionAccountBookDTO, month, day);

    }
    // 특정 날짜의 가계부 가져오기
    public void getDayAccountBook(int month, int day){
        accountBookService.getDayAccountBook(month, day);
    }

    public Map<Integer, DayAccountBook> getMonthAccountBook(int month){
        return accountBookService.getMonthAccountBook(month);
    }
}

