package me.dragosghinea.display.events;

import me.dragosghinea.display.events.DisplayInputListener;

import java.util.Properties;

public interface DisplayInputTrigger {
    boolean triggerInput(String gotInfo, Properties otherSettings);

    boolean subscribeInput(DisplayInputListener inputListener);

    boolean unsubscribeInput(DisplayInputListener inputListener);

}
