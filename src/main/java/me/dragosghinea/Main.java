package me.dragosghinea;

import me.dragosghinea.application.DefaultEntitiesGenerator;
import me.dragosghinea.application.menus.MainMenu;
import me.dragosghinea.model.BidHistory;
import me.dragosghinea.model.User;
import me.dragosghinea.model.abstracts.Auction;
import me.dragosghinea.services.AuctionService;
import me.dragosghinea.services.UserService;
import me.dragosghinea.services.impl.BlitzAuctionServiceImpl;
import me.dragosghinea.services.impl.LongAuctionServiceImpl;
import me.dragosghinea.services.impl.UserServiceImpl;

public class Main {

    public static void main(String[] args) {
        //loading default users
        UserService userService = new UserServiceImpl();
        for (User u : DefaultEntitiesGenerator.getDefaultUsers()) {
            userService.addUser(u);
        }

        //loading default auctions
        AuctionService auctionServiceBlitz = new BlitzAuctionServiceImpl();
        AuctionService auctionServiceLong = new LongAuctionServiceImpl();

        for (Auction a : DefaultEntitiesGenerator.getDefaultAuctions()) {
            if (!auctionServiceLong.addAuction(a))
                auctionServiceBlitz.addAuction(a);

            if(a.getBidHistory() == null)
                System.out.println("TEST");
            if(a.getAuctionState() == null)
                System.out.println("TEST2");
        }

        new MainMenu().start();
    }
}
