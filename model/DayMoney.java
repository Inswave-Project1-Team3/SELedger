package model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DayMoney {
    private boolean benefit;
    private long price;

    public void DayMoney(String benefit, long price){
        this.benefit = benefit.equals("0");
        this.price = price;
    }

}
