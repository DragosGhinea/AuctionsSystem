package me.dragosghinea.model;

import lombok.*;
import me.dragosghinea.model.enums.Currency;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet {

    private UUID ownerId;
    private Currency preferredCurrency;

    private BigDecimal points = BigDecimal.valueOf(0);

    public Wallet(UUID ownerId){
        this.ownerId = ownerId;
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

    @Override
    public String toString() {
        if(preferredCurrency.getCurrencyAmount(points).compareTo(BigDecimal.ONE) == 0){
            return "1.000 "+preferredCurrency.getSingularName();
        }

        return preferredCurrency.getCurrencyAmount(points).setScale(3, RoundingMode.HALF_EVEN).toPlainString()+" "+preferredCurrency.getPluralName();
    }

    public String toString(BigDecimal points){
        if(preferredCurrency.getCurrencyAmount(points).compareTo(BigDecimal.ONE) == 0){
            return "1.000 "+preferredCurrency.getSingularName();
        }

        return preferredCurrency.getCurrencyAmount(points).setScale(3, RoundingMode.HALF_EVEN).toPlainString()+" "+preferredCurrency.getPluralName();
    }
}
