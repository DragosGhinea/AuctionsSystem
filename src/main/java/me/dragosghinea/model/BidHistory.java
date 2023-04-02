package me.dragosghinea.model;

import lombok.Getter;
import lombok.Setter;
import me.dragosghinea.model.abstracts.Auction;
import me.dragosghinea.model.abstracts.Bid;
import me.dragosghinea.model.enums.Currency;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Getter
public class BidHistory {
    private Auction auction;

    @Setter
    private List<Bid> bids = new ArrayList<>();

    public BidHistory(Auction auction) {
        this.auction = auction;
    }

    @Override
    public String toString() {
        if(bids.size() == 0)
            return "Latest Bid: None";

        return "Latest Bid: "+bids.get(bids.size() - 1).getTotalBidValue().setScale(3, RoundingMode.HALF_EVEN).toPlainString()+" "+ Currency.PNT.getPluralName();
    }
}
