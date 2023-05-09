package me.dragosghinea.services;

import me.dragosghinea.services.enums.AuditAction;
import org.apache.logging.log4j.Level;

/**
 The AuditService interface provides methods for logging audit actions.
 You only need to override the {@link #logAction(AuditAction, String, String, Level)} method,
 as the other methods are implemented using it.
 */
public interface AuditService {

    /**
     Logs the specified audit action with the specified details, by the specified user, and at the specified log level.
     @param action the audit action to log
     @param details the details of the audit action
     @param byWho the user who performed the audit action
     @param logLevel the log level to use for the audit action
     */
    void logAction(AuditAction action, String details, String byWho, Level logLevel);

    default void logAction(AuditAction action, String details, String byWho) {
        logAction(action, details, byWho, Level.INFO);
    }

    default void logAction(AuditAction action, String details){
        logAction(action, details, "UNSPECIFIED");
    }

    default void logInfoAction(AuditAction action, String details, String byWho){
        logAction(action, details, byWho, Level.INFO);
    }

    default void logWarnAction(AuditAction action, String details, String byWho){
        logAction(action, details, byWho, Level.WARN);
    }

    default void logErrorAction(AuditAction action, String details, String byWho){
        logAction(action, details, byWho, Level.ERROR);
    }

    default void logFatalAction(AuditAction action, String details, String byWho){
        logAction(action, details, byWho, Level.FATAL);
    }

    default void logInfoAction(AuditAction action, String details){
        logAction(action, details, "UNSPECIFIED", Level.INFO);
    }

    default void logWarnAction(AuditAction action, String details){
        logAction(action, details, "UNSPECIFIED", Level.WARN);
    }

    default void logErrorAction(AuditAction action, String details){
        logAction(action, details, "UNSPECIFIED", Level.ERROR);
    }

    default void logFatalAction(AuditAction action, String details){
        logAction(action, details, "UNSPECIFIED", Level.FATAL);
    }
}
