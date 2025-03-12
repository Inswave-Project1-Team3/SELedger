package DTO.VO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import model.AccountCategory;
import model.DayMoney;

import java.util.Map;

@AllArgsConstructor
@Getter
public class GetMonthDataVO {
    private Map<Integer, DayMoney> daysMoney;
    private AccountCategory accountCategory;
    private long maxCategoryMoney;
    private long monthTotalMoney;

}
