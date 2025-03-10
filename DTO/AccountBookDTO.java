package DTO;

import lombok.Getter;
import lombok.Setter;
import model.ExpenseCategory;
import model.IncomeCategory;
import java.time.LocalDate;

//가계부 데이터를 주고받기 위한 DTO (Data Transfer Object)
@Getter
@Setter
public class AccountBookDTO {
    private LocalDate date;
    private double amount;
    private IncomeCategory incomecategory;
    private ExpenseCategory expensecategory;
    
}

