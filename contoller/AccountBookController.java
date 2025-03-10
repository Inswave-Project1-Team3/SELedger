package contoller;

import DTO.CreateAccountBookDTO;
import DTO.CreateTransactionAccountBookDTO;
import service.AccountBookService;

public class AccountBookController {
    AccountBookService accountBookService = new AccountBookService();
    public void createDayAccountBook(CreateAccountBookDTO AccountBookDTO,
                                     CreateTransactionAccountBookDTO transactionAccountBookDTO){
        accountBookService.createAccountBook(AccountBookDTO, transactionAccountBookDTO);

    }

    public void getDayAccountBook(){
        accountBookService.getToFile();
    }

}
