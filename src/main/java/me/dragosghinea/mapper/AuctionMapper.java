package me.dragosghinea.mapper;

import me.dragosghinea.model.BlitzAuction;
import me.dragosghinea.model.LongAuction;
import me.dragosghinea.model.abstracts.Reward;
import me.dragosghinea.model.enums.AuctionState;
import me.dragosghinea.repository.RewardRepository;
import me.dragosghinea.repository.impl.postgres.RewardRepositoryImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuctionMapper {

    private final static AuctionMapper auctionMapper = new AuctionMapper();
    private final RewardRepository<Reward> rewardRepository = new RewardRepositoryImpl();

    private AuctionMapper() {
    }

    ;

    public static AuctionMapper getInstance() {
        return auctionMapper;
    }

    private BlitzAuction directMapToBlitzAuction(ResultSet set) throws SQLException {
        return new BlitzAuction().toBuilder()
                .auctionId(set.getObject("auction_id", UUID.class))
                .startDate(set.getTimestamp("start_date").toLocalDateTime())
                .endDate(set.getTimestamp("end_date").toLocalDateTime())
                .auctionState(AuctionState.valueOf(set.getString("auction_state")))
                .startingBidAmount(set.getBigDecimal("starting_bid_amount"))
                .minimumBidGap(set.getBigDecimal("minimum_bid_gap"))
                .reward(rewardRepository.getReward(set.getObject("reward_id", UUID.class)).get())
                .bidDuration(Duration.ofSeconds(set.getLong("bid_duration")))
                .preparingDuration(Duration.ofSeconds(set.getLong("preparing_duration")))
                .build();
    }

    private LongAuction directMapToLongAuction(ResultSet set) throws SQLException {
        return new LongAuction().toBuilder()
                .auctionId(set.getObject("auction_id", UUID.class))
                .startDate(set.getTimestamp("start_date").toLocalDateTime())
                .endDate(set.getTimestamp("end_date").toLocalDateTime())
                .auctionState(AuctionState.valueOf(set.getString("auction_state")))
                .startingBidAmount(set.getBigDecimal("starting_bid_amount"))
                .minimumBidGap(set.getBigDecimal("minimum_bid_gap"))
                .reward(rewardRepository.getReward(set.getObject("reward_id", UUID.class)).get())
                .extendTime(Duration.ofSeconds(set.getLong("extend_time")))
                .overTime(set.getTimestamp("overtime").toLocalDateTime())
                .build();
    }

    public BlitzAuction mapToBlitzAuction(ResultSet set) throws SQLException {
        if (!set.next())
            return null;

        return directMapToBlitzAuction(set);
    }

    public List<BlitzAuction> mapToBlitzAuctionList(ResultSet set) throws SQLException {
        List<BlitzAuction> auctions = new ArrayList<>();

        while(set.next()){
            auctions.add(directMapToBlitzAuction(set));
        }

        return auctions;
    }

    public LongAuction mapToLongAuction(ResultSet set) throws SQLException {
        if (!set.next())
            return null;

        return directMapToLongAuction(set);
    }

    public List<LongAuction> mapToLongAuctionList(ResultSet set) throws SQLException {
        List<LongAuction> auctions = new ArrayList<>();

        while(set.next()){
            auctions.add(directMapToLongAuction(set));
        }

        return auctions;
    }
}
