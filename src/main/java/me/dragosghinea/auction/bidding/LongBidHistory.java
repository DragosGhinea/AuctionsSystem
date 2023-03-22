package me.dragosghinea.auction.bidding;

import me.dragosghinea.auction.Auction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LongBidHistory implements BidHistory {
    private Auction auction;

    private List<Bid> bids = new ArrayList<>();

    public LongBidHistory(Auction auction) {
        this.auction = auction;
    }

    @Override
    public Auction getAuction() {
        return auction;
    }

    @Override
    public Optional<Bid> getLatestBid() {
        if (bids.size() == 0)
            return Optional.empty();

        return Optional.of(bids.get(bids.size()-1));
    }

    @Override
    public Optional<Bid> getWinner() {
        return null;
    }

    @Override
    public List<Bid> getBids() {
        return new ArrayList<>(bids);
    }

    @Override
    public List<Bid> getBidsDistinctUsers() {
        return null;
    }
}
