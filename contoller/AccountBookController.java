package contoller;

import DTO.CreateAccountBookDTO;
import DTO.CreateTransactionAccountBookDTO;
import lombok.RequiredArgsConstructor;
import service.AccountBookService;

@RequiredArgsConstructor
public class AccountBookController {
    /** 가계부 서비스 */
    AccountBookService accountBookService = new AccountBookService();

    // 상세 가계부 추가
    public void createDayAccountBook(CreateAccountBookDTO AccountBookDTO,
                                     CreateTransactionAccountBookDTO transactionAccountBookDTO,
                                     int month, int day, String userNickName){
    accountBookService.createAccountBook(AccountBookDTO, transactionAccountBookDTO, month, day, userNickName);

    }

    // 특정 날짜의 가계부 가져오기
    public void getDayAccountBook(int month, int day, String userNickName){
        accountBookService.getDayAccountBook(month, day, userNickName);
    }
}

