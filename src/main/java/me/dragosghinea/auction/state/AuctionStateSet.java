package me.dragosghinea.auction.state;

import me.dragosghinea.auction.Auction;

public enum AuctionStateSet implements AuctionState{

    NOT_STARTED("Not Started", "This auction has not started yet!"){
        @Override
        public Boolean isValid(Auction auction) {
            return null;
        }

        @Override
        public AuctionState getNextState(Auction auction) {
            return null;
        }

        @Override
        public void forceOn(Auction auction) {

        }
    },


    ONGOING("Ongoing", "This auction is live right now!"){
        @Override
        public Boolean isValid(Auction auction) {
            return null;
        }

        @Override
        public AuctionState getNextState(Auction auction) {
            return null;
        }

        @Override
        public void forceOn(Auction auction) {

        }
    },


    ENDED("Ended", "This auction has ended."){
        @Override
        public Boolean isValid(Auction auction) {
            return null;
        }

        @Override
        public AuctionState getNextState(Auction auction) {
            return null;
        }

        @Override
        public void forceOn(Auction auction) {

        }
    },


    CANCELLED("Cancelled", "This auction has been cancelled."){
        @Override
        public Boolean isValid(Auction auction) {
            return null;
        }

        @Override
        public AuctionState getNextState(Auction auction) {
            return null;
        }

        @Override
        public void forceOn(Auction auction) {

        }
    },


    OVERTIME("Overtime", "The auction end date was prolonged because of last minute bids."){
        @Override
        public Boolean isValid(Auction auction) {
            return null;
        }

        @Override
        public AuctionState getNextState(Auction auction) {
            return null;
        }

        @Override
        public void forceOn(Auction auction) {

        }
    },


    PREPARING("Preparing", "Be ready for the next item in this auction"){
        @Override
        public Boolean isValid(Auction auction) {
            return null;
        }

        @Override
        public AuctionState getNextState(Auction auction) {
            return null;
        }

        @Override
        public void forceOn(Auction auction) {

        }
    };

    private final String name;
    private final String description;
    AuctionStateSet(String name, String description){
        this.name = name;
        this.description = description;
    }


    public String getStateName() {
        return name;
    }

    public String getStateDescription() {
        return description;
    }

    public abstract Boolean isValid(Auction auction);

    public abstract AuctionState getNextState(Auction auction);

    public abstract void forceOn(Auction auction);
}
