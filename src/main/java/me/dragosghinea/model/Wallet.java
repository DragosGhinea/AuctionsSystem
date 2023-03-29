package me.dragosghinea.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.dragosghinea.model.enums.Currency;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {

    private User owner;
    private Currency preferredCurrency;

    private BigDecimal points = BigDecimal.valueOf(0);

    public Wallet(User owner){
        this.owner = owner;
        this.preferredCurrency = Currency.PNT;
    }

    public boolean removePoints(BigDecimal points){
        if(this.points.compareTo(points) < 0)
            return false;
        this.points = this.points.subtract(points);
        return true;
    }

    public void addPoints(BigDecimal points){
        this.points = this.points.add(points);
    }

}
