package me.dragosghinea.model;

import lombok.Getter;
import lombok.Setter;
import me.dragosghinea.model.abstracts.Auction;
import me.dragosghinea.model.abstracts.Bid;

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
}
