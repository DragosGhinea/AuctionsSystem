package me.dragosghinea.exceptions;

public class DatabaseBundleRewardException extends RuntimeException{

    public DatabaseBundleRewardException(){
        super("Bundle rewards can not store other bundle rewards.");
    }
}
