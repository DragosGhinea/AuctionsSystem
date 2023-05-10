package me.dragosghinea.exceptions;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Getter
public class BidTooLowException extends RuntimeException{

    private final BigDecimal latest;
    private final BigDecimal offered;
    private final UUID byWho;

    public BidTooLowException(BigDecimal latest, BigDecimal offered, UUID byWho){
        super("Bid was too low ("+offered.setScale(3, RoundingMode.HALF_EVEN).toPlainString()+" < "+latest.setScale(3, RoundingMode.HALF_EVEN).toPlainString()+") by a user with the id "+byWho);
        this.latest = latest;
        this.offered = offered;
        this.byWho = byWho;

    }
}
