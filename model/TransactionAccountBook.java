package model;

import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class TransactionAccountBook implements Serializable {
    private static final long serialVersionUID = 1L; // 직렬화 버전 ID
    private String createDate;
    private String updateDate;
    private boolean benefit;
    private long money;

    public TransactionAccountBook(boolean benefit, long money) {
        this.createDate = LocalDateTime.now().withNano(0)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.updateDate = LocalDateTime.now().withNano(0)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.benefit = benefit;
        this.money = money;
    }
}
