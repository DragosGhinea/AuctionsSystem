package me.dragosghinea.services.impl;

import me.dragosghinea.services.AuditService;
import me.dragosghinea.services.enums.AuditAction;
import me.dragosghinea.services.updater.AuctionStateChangeListener;
import me.dragosghinea.services.updater.InMemoryAuctionStateUpdaterImpl;
import me.dragosghinea.services.updater.StateChangeEventData;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class AuditServiceImpl implements AuditService, AuctionStateChangeListener {
    private static final AuditServiceImpl instance = new AuditServiceImpl(LogManager.getLogger(AuditServiceImpl.class));

    public static AuditServiceImpl getInstance() {
        return instance;
    }

    @Override
    public void onStateChange(StateChangeEventData data) {
        //we don't use this method for logging as multiple instances of the same auction can exist
        //inside AuctionStateUpdaterImpl which will cause duplicate rows in the audit log
    }

    @Override
    public void onStateChange(List<StateChangeEventData> allEventData) {
        Set<UUID> auctionIds = new HashSet<>();
        for(StateChangeEventData data : allEventData){
            if(auctionIds.add(data.getAuctionId())){
                logInfoAction(AuditAction.AUCTION_STATE_CHANGE, "Auction with id "+data.getAuctionId()+" ("+data.getAuctionType()+")"+" changed state from "+data.getOldState()+" to "+data.getNewState(), "AuctionStateUpdater");
            }
        }
    }

    private AuditServiceImpl(Logger logger){
        this.logger = logger;
        InMemoryAuctionStateUpdaterImpl.getInstance().attach(this);
    }
    private final Logger logger;

    @Override
    public void logAction(AuditAction action, String details, String byWho, Level logLevel) {
        logger.log(logLevel, action.name(), details, byWho);
    }

    @Override
    public void logInfoAction(AuditAction action, String details, String byWho) {
        logger.info(action.name(), details, byWho);
    }

    @Override
    public void logWarnAction(AuditAction action, String details, String byWho) {
        logger.warn(action.name(), details, byWho);
    }

    @Override
    public void logErrorAction(AuditAction action, String details, String byWho) {
        logger.error(action.name(), details, byWho);
    }

    @Override
    public void logFatalAction(AuditAction action, String details, String byWho) {
        logger.fatal(action.name(), details, byWho);
    }
}
