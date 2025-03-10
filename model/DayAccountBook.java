package model;

import lombok.Getter;
import util.Timestamped;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class DayAccountBook extends Timestamped implements Serializable {
    private static final long serialVersionUID = 1L; // 직렬화 버전 ID
    private String createDate;
    private String updateDate;
    private boolean benefitCheck;
    private long money;
    private String memo;

    public DayAccountBook(boolean benefitCheck, long money, String memo) {
        this.createDate = LocalDateTime.now().withNano(0)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.updateDate = LocalDateTime.now().withNano(0)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.benefitCheck = benefitCheck;
        this.money = money;
        this.memo = memo;
    }
}
