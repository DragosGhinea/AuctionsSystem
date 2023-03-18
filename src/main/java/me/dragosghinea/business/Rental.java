package me.dragosghinea.business;

import java.math.BigDecimal;

public class Rental extends BusinessAction{

    public Rental(Long startDate, Long endDate, BigDecimal price){
        super(startDate);
        if(endDate == null)
            throw new NullPointerException("The end date parameter needs to be a real date.");
        setEndDateEpoch(endDate);
        setPrice(price);
    }
}
