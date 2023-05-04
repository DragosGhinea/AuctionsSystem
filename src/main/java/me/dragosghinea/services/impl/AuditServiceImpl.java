package me.dragosghinea.services.impl;

import me.dragosghinea.services.AuditService;
import me.dragosghinea.services.enums.AuditAction;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class AuditServiceImpl implements AuditService {
    private static final AuditServiceImpl instance = new AuditServiceImpl(LogManager.getLogger(AuditServiceImpl.class));

    public static AuditServiceImpl getInstance() {
        return instance;
    }

    private AuditServiceImpl(Logger logger){
        this.logger = logger;
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
