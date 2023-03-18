package me.dragosghinea.business;

import java.math.BigDecimal;
import java.util.Optional;

public abstract class BusinessAction {
    private Long startDateEpoch;
    private Long endDateEpoch;

    private BigDecimal price;
    private Boolean isPurchased = false;

    protected BusinessAction(Long startDateEpoch){
        if(startDateEpoch == null)
            throw new NullPointerException("The start date parameter needs to be a real date.");

        this.startDateEpoch = startDateEpoch;
    }

    public void setEndDateEpoch(Long endDateEpoch) {
        this.endDateEpoch = endDateEpoch;
    }

    public void setStartDateEpoch(Long startDateEpoch) {
        this.startDateEpoch = startDateEpoch;
    }

    public Optional<Long> getEndDateEpoch() {
        return Optional.ofNullable(endDateEpoch);
    }

    public Long getStartDateEpoch() {
        return startDateEpoch;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setPrice(double price) {
        this.price = BigDecimal.valueOf(price);
    }

    public Boolean markPurchased(){
        if(isPurchased)
            return false;

        return isPurchased = true;
    }

    public Boolean cancelPurchase(){
        if(!isPurchased)
            return false;

        isPurchased = false;
        return true;
    }

    public Boolean isPurchased(){
        return isPurchased;
    }
}
