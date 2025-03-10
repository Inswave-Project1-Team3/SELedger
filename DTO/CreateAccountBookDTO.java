package DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateAccountBookDTO {
    private boolean benefitCheck;
    private long price;
    private String memo;
}
