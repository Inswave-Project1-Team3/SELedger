package contoller;

import DTO.CreateAccountBookDTO;
import model.DayAccountBook;
import service.AccountBookService;

public class AccountBookController {
    AccountBookService accountBookService = new AccountBookService();
    public void createDayAccountBook(CreateAccountBookDTO dto){
        accountBookService.createAccountBook(dto);

    }

    public DayAccountBook getDayAccountBook(int number){
        accountBookService.getToFile();
        return null;
    }

}
