package me.dragosghinea.model;

import lombok.Getter;
import lombok.Setter;
import me.dragosghinea.model.abstracts.Auction;

import java.util.List;

@Getter
@Setter
public class UserAuctions {

    private List<Auction> auctions;
}
