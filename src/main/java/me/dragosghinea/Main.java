package me.dragosghinea;

import me.dragosghinea.application.DefaultEntitiesGenerator;
import me.dragosghinea.application.menus.MainMenu;
import me.dragosghinea.config.DatabaseConnection;
import me.dragosghinea.exceptions.IncompatibleAuctionException;
import me.dragosghinea.model.BundleReward;
import me.dragosghinea.model.User;
import me.dragosghinea.model.abstracts.Auction;
import me.dragosghinea.model.abstracts.Reward;
import me.dragosghinea.repository.impl.postgres.BlitzAuctionRepositoryImpl;
import me.dragosghinea.repository.impl.postgres.LongAuctionRepositoryImpl;
import me.dragosghinea.repository.impl.postgres.RewardRepositoryImpl;
import me.dragosghinea.repository.impl.postgres.UserRepositoryImpl;
import me.dragosghinea.services.AuctionService;
import me.dragosghinea.services.RewardService;
import me.dragosghinea.services.UserService;
import me.dragosghinea.services.impl.BlitzAuctionServiceImpl;
import me.dragosghinea.services.impl.LongAuctionServiceImpl;
import me.dragosghinea.services.impl.RewardServiceImpl;
import me.dragosghinea.services.impl.UserServiceImpl;
import me.dragosghinea.services.updater.InMemoryAuctionStateUpdaterImpl;

import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        InMemoryAuctionStateUpdaterImpl.getInstance(); //making sure the instance is loaded right away

        try {
            DatabaseConnection.resetAllData(); //just for testing purposes
            DatabaseConnection.generateTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //loading default users
        UserService userService = new UserServiceImpl(new UserRepositoryImpl());
        for (User u : DefaultEntitiesGenerator.getDefaultUsers()) {
            userService.addUser(u);
        }

        //loading default auctions
        RewardService rewardService = new RewardServiceImpl(new RewardRepositoryImpl());
        AuctionService auctionServiceBlitz = new BlitzAuctionServiceImpl(new BlitzAuctionRepositoryImpl());
        AuctionService auctionServiceLong = new LongAuctionServiceImpl(new LongAuctionRepositoryImpl());

        List<Auction> auctionList = DefaultEntitiesGenerator.getDefaultAuctions();

        //loading inner rewards into the database first
        for (Auction a : auctionList) {
            Reward reward = a.getReward();
            if(reward instanceof BundleReward bundleReward){
                for(Reward included : bundleReward.getRewards())
                    rewardService.addReward(included);
            }
            rewardService.addReward(reward);
        }

        //loading auctions
        for (Auction a : auctionList) {
            try{
                auctionServiceLong.addAuction(a);
            }catch(IncompatibleAuctionException x){
                auctionServiceBlitz.addAuction(a);
            }
        }

        new MainMenu().start();
    }
}
