package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DayAccountBook implements Serializable {
    private static final long serialVersionUID = 1L; // 직렬화 버전 ID
    private String createDate;
    private String updateDate;
    private String memo;
    private List<TransactionAccountBook> transactionAccountBooks;

    public DayAccountBook(){
        this.transactionAccountBooks = new ArrayList<>();
    }

	public DayAccountBook(String memo, List<TransactionAccountBook> transactionAccountBooks) {
        this.createDate = LocalDateTime.now().withNano(0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.updateDate = LocalDateTime.now().withNano(0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.memo = memo;
        this.transactionAccountBooks = transactionAccountBooks;
    }
}
