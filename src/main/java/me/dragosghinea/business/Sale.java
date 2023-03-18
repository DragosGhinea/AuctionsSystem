package me.dragosghinea.business;

import java.math.BigDecimal;
import java.util.Optional;

public class Sale extends BusinessAction{

    public Sale(Long saleDate, BigDecimal price){
        super(saleDate);
        setPrice(price);
    }

    @Override
    public Optional<Long> getEndDateEpoch() {
        return Optional.empty();
    }
}
