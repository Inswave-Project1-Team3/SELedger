package DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateTransactionAccountBookDTO {
    private boolean benefit;
    private long money;
}
