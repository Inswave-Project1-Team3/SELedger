package contoller;

import DTO.CreateAccountBookDTO;
import service.AccountBookService;

public class AccountBookController {
    AccountBookService accountBookService = new AccountBookService();
    public void createDayAccountBook(CreateAccountBookDTO dto){
        accountBookService.createAccountBook(dto);

    }

    public void getDayAccountBook(){
        accountBookService.getToFile();
    }

}
