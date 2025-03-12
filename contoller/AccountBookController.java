package contoller;

import DTO.CreateAccountBookDTO;
import DTO.CreateTransactionAccountBookDTO;
import DTO.UpdateTransactionAccountBookDTO;
import DTO.VO.GetMonthDataVO;
import lombok.RequiredArgsConstructor;
import model.DayAccountBook;
import service.AccountBookService;

import static app.App.userNickName;

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


    // 특정 날짜의 상세 가계부 가져오기
    public DayAccountBook getDayAccountBook(int day, String userNickName){
        return accountBookService.getDayAccountBook(day, userNickName);
    }

    // 상세 가계부 내역 수정
    public void updateDayAccountBook(UpdateTransactionAccountBookDTO dto, int transactionNumber, int day){
        accountBookService.updateDayAccountBook(dto, transactionNumber, day);
    }

    // 상세 가계부 내역 삭제
    public void deleteDayAccountBook(int transactionNumber, int day) {
        accountBookService.deleteDayAccountBook(transactionNumber, day);
    }
}
