package me.dragosghinea.services.updater;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.dragosghinea.model.abstracts.Auction;
import me.dragosghinea.model.enums.AuctionState;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class StateChangeEventData {
    private UUID auctionId;
    private AuctionState oldState;
    private AuctionState newState;
    private String auctionType;

    Auction auction;
}
