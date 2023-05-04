package me.dragosghinea.services;

import me.dragosghinea.services.enums.AuditAction;
import org.apache.logging.log4j.Level;

public interface AuditService {

    void logAction(AuditAction action, String details, String byWho, Level logLevel);

    default void logAction(AuditAction action, String details, String byWho) {
        logAction(action, details, byWho, Level.INFO);
    };

    default void logAction(AuditAction action, String details){
        logAction(action, details, "UNSPECIFIED");
    };

    default void logInfoAction(AuditAction action, String details, String byWho){
        logAction(action, details, byWho, Level.INFO);
    };

    default void logWarnAction(AuditAction action, String details, String byWho){
        logAction(action, details, byWho, Level.WARN);
    };

    default void logErrorAction(AuditAction action, String details, String byWho){
        logAction(action, details, byWho, Level.ERROR);
    };

    default void logFatalAction(AuditAction action, String details, String byWho){
        logAction(action, details, byWho, Level.FATAL);
    };

    default void logInfoAction(AuditAction action, String details){
        logAction(action, details, "UNSPECIFIED", Level.INFO);
    };

    default void logWarnAction(AuditAction action, String details){
        logAction(action, details, "UNSPECIFIED", Level.WARN);
    };

    default void logErrorAction(AuditAction action, String details){
        logAction(action, details, "UNSPECIFIED", Level.ERROR);
    };

    default void logFatalAction(AuditAction action, String details){
        logAction(action, details, "UNSPECIFIED", Level.FATAL);
    };
}
