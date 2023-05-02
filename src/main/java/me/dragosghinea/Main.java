package me.dragosghinea;

import me.dragosghinea.application.DefaultEntitiesGenerator;
import me.dragosghinea.application.menus.MainMenu;
import me.dragosghinea.config.DatabaseConnection;
import me.dragosghinea.exceptions.IncompatibleAuction;
import me.dragosghinea.model.BundleReward;
import me.dragosghinea.model.User;
import me.dragosghinea.model.abstracts.Auction;
import me.dragosghinea.model.abstracts.Reward;
import me.dragosghinea.services.AuctionService;
import me.dragosghinea.services.AuctionRewardsManagerService;
import me.dragosghinea.services.RewardService;
import me.dragosghinea.services.UserService;
import me.dragosghinea.services.impl.*;

import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            DatabaseConnection.resetAllData(); //just for testing purposes
            DatabaseConnection.generateTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //loading default users
        UserService userService = new UserServiceImpl();
        for (User u : DefaultEntitiesGenerator.getDefaultUsers()) {
            userService.addUser(u);
        }

        //loading default auctions
        RewardService rewardService = new RewardServiceImpl();
        AuctionService auctionServiceBlitz = new BlitzAuctionServiceImpl();
        AuctionService auctionServiceLong = new LongAuctionServiceImpl();

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
            }catch(IncompatibleAuction x){
                auctionServiceBlitz.addAuction(a);
            }
        }

        new MainMenu().start();
    }
}
