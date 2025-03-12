package contoller;

import DTO.CreateAccountBookDTO;
import DTO.CreateTransactionAccountBookDTO;
import DTO.VO.GetMonthDataVO;
import lombok.RequiredArgsConstructor;
import model.DayAccountBook;
import model.DayMoney;
import service.AccountBookService;
import java.util.Map;

@RequiredArgsConstructor
public class AccountBookController {
    /** 가계부 서비스 */
    AccountBookService accountBookService = new AccountBookService();

    // 상세 가계부 추가
    public void createDayAccountBook(CreateAccountBookDTO AccountBookDTO,
                                     CreateTransactionAccountBookDTO transactionAccountBookDTO,
                                     int day){
    accountBookService.createAccountBook(AccountBookDTO, transactionAccountBookDTO, day);

    }

    // 특정 날짜의 가계부 가져오기
    public DayAccountBook getDayAccountBook(int day, String userNickName){
        return accountBookService.getDayAccountBook(day, userNickName);
    }

    public GetMonthDataVO getMonthMoney(String userNickName){
        return accountBookService.getMonthMoney(userNickName);
    }
}

