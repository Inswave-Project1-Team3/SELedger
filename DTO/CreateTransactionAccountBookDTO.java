package DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import model.AccountCategory;

@Data
@AllArgsConstructor
public class CreateTransactionAccountBookDTO {
    private boolean benefit;
    private long money;
    private AccountCategory accountCategory;

}
