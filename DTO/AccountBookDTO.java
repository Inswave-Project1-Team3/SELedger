package DTO;

import lombok.Getter;
import lombok.Setter;
import model.Category;

import java.time.LocalDate;

//가계부 데이터를 주고받기 위한 DTO (Data Transfer Object)
@Getter
@Setter
public class AccountBookDTO {
    /** 거래 날짜 */
    private LocalDate date;

    /** 거래 금액 */
    private double amount;

    /** 거래 카테고리 */
    private Category category;

    /** 거래 설명 */
    private String description;

    /**
     * 가계부 DTO 생성자
     */
    public AccountBookDTO(LocalDate date, double amount, Category category, String description) {
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.description = description;
    }
}

