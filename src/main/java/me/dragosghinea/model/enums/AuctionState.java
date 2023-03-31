package me.dragosghinea.model.enums;

public enum AuctionState {
    NOT_STARTED("Not Started", "The auction has not started yet!"),
    ONGOING("Ongoing", "The auction is live and people can bid."),
    ENDED("Ended", "The auction has ended."),
    CANCELLED("Cancelled", "This auction has been cancelled."),
    OVERTIME("Overtime", "The auction end date was prolonged because of last minute bids."),
    PREPARING("Preparing", "Be ready for the next item in this auction"),

    UNKNOWN("Unknown", "Could not determine the state of the auction.");

    private final String stateName;
    private final String stateDescription;

    public String getStateName(){
        return stateName;
    }

    public String getStateDescription(){
        return stateDescription;
    }

    AuctionState(String stateName, String stateDescription){
        this.stateName = stateName;
        this.stateDescription = stateDescription;
    }
}
