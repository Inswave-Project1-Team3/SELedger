package model;

import lombok.Getter;
import util.Timestamped;

import java.io.Serializable;

@Getter
public class DayAccountBook extends Timestamped implements Serializable {
    private static final long serialVersionUID = 1L; // 직렬화 버전 ID
    private String createDate;
    private String updateDate;
    private String memo;

    public DayAccountBook(String memo) {
        super();
        this.memo = memo;
    }
}
