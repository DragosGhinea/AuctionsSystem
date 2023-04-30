package me.dragosghinea;

import me.dragosghinea.application.DefaultEntitiesGenerator;
import me.dragosghinea.application.menus.MainMenu;
import me.dragosghinea.config.DatabaseConnection;
import me.dragosghinea.exceptions.IncompatibleAuction;
import me.dragosghinea.model.User;
import me.dragosghinea.model.abstracts.Auction;
import me.dragosghinea.services.AuctionService;
import me.dragosghinea.services.UserService;
import me.dragosghinea.services.impl.BlitzAuctionServiceImpl;
import me.dragosghinea.services.impl.LongAuctionServiceImpl;
import me.dragosghinea.services.impl.UserServiceImpl;

import java.sql.SQLException;

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
        AuctionService auctionServiceBlitz = new BlitzAuctionServiceImpl();
        AuctionService auctionServiceLong = new LongAuctionServiceImpl();

        for (Auction a : DefaultEntitiesGenerator.getDefaultAuctions()) {
            try{
                auctionServiceLong.addAuction(a);
            }catch(IncompatibleAuction x){
                auctionServiceBlitz.addAuction(a);
            }
        }

        new MainMenu().start();
    }
}
