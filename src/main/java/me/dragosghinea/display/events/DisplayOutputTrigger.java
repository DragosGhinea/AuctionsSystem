package me.dragosghinea.display.events;

import me.dragosghinea.display.events.DisplayOutputListener;

import java.util.Properties;

public interface DisplayOutputTrigger {
    boolean triggerOutput(String gotInfo, Properties otherSettings);

    boolean subscribeOutput(DisplayOutputListener outputListener);

    boolean unsubscribeOutput(DisplayOutputListener outputListener);
}
