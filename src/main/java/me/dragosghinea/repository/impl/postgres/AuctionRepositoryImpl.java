package me.dragosghinea.repository.impl.postgres;

import me.dragosghinea.config.DatabaseConnection;
import me.dragosghinea.model.BlitzAuction;
import me.dragosghinea.model.LongAuction;
import me.dragosghinea.model.abstracts.Auction;
import me.dragosghinea.model.enums.AuctionState;
import me.dragosghinea.repository.AuctionRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuctionRepositoryImpl implements AuctionRepository<Auction> {
    private final AuctionRepository<BlitzAuction> blitzAuctionAuctionRepository = new BlitzAuctionRepositoryImpl();
    private final AuctionRepository<LongAuction> longAuctionAuctionRepository = new LongAuctionRepositoryImpl();
    @Override
    public boolean addAuction(Auction auction) {
        if(auction instanceof BlitzAuction blitzAuction) {
            return blitzAuctionAuctionRepository.addAuction(blitzAuction);
        }
        else if(auction instanceof LongAuction longAuction) {
            return longAuctionAuctionRepository.addAuction(longAuction);
        }

        return false;
    }

    @Override
    public boolean removeAuctionById(UUID auctionId) {
        String sql = "DELETE FROM Auction WHERE auction_id = ?";

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setObject(1, auctionId);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean updateAuction(Auction auction) {
        if(auction instanceof BlitzAuction blitzAuction) {
            return blitzAuctionAuctionRepository.updateAuction(blitzAuction);
        }
        else if(auction instanceof LongAuction longAuction) {
            return longAuctionAuctionRepository.updateAuction(longAuction);
        }

        return false;
    }

    @Override
    public Optional<Auction> getAuctionById(UUID auctionId) {
        return blitzAuctionAuctionRepository.getAuctionById(auctionId)
                .map(a -> (Auction) a)
                .or(() -> longAuctionAuctionRepository.getAuctionById(auctionId)
                        .map(a -> (Auction) a)
                );
    }

    @Override
    public List<Auction> getAllAuctions() {
        List<Auction> toReturn = new ArrayList<>();
        toReturn.addAll(blitzAuctionAuctionRepository.getAllAuctions());
        toReturn.addAll(longAuctionAuctionRepository.getAllAuctions());
        return toReturn;
    }

    @Override
    public boolean setReward(UUID auctionId, UUID rewardId) {
        String sql = "UPDATE Auction SET reward_id = ? WHERE auction_id = ?";

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setObject(1, rewardId);
            preparedStatement.setObject(2, auctionId);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean setState(UUID auctionId, AuctionState state){
        String sql = "UPDATE Auction SET auction_state = ? WHERE auction_id = ?";

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setObject(1, state.toString());
            preparedStatement.setObject(2, auctionId);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
