package me.dragosghinea.mapper;

import me.dragosghinea.model.BidRecord;
import me.dragosghinea.model.abstracts.Bid;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BidMapper {

    private static BidMapper instance = new BidMapper();

    private BidMapper(){};

    public static BidMapper getInstance() {
        return instance;
    }

    private Bid directMapToBid(ResultSet set) throws SQLException {
        return new BidRecord(
                set.getObject("user_id", UUID.class),
                set.getObject("auction_id", UUID.class),
                set.getBigDecimal("points_bid"),
                set.getBigDecimal("total_bid_value"),
                set.getTimestamp("bid_date").toLocalDateTime()
        );
    }

    public Bid mapToBid(ResultSet set) throws SQLException {
        if(!set.next())
            return null;

        return directMapToBid(set);
    }

    public List<Bid> mapToBids(ResultSet set) throws SQLException {
        List<Bid> bids = new ArrayList<>();
        while(set.next()){
            bids.add(directMapToBid(set));
        }
        return bids;
    }
}
