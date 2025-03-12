package DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import model.AccountCategory;

@Data
@AllArgsConstructor
public class UpdateTransactionAccountBookDTO {
    private boolean benefit;
    private long money;
    private AccountCategory accountCategory;

}
