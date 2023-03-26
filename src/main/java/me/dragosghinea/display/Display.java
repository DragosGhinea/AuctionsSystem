package me.dragosghinea.display;

import me.dragosghinea.display.events.DisplayInputTrigger;
import me.dragosghinea.display.events.DisplayOutputListener;

public interface Display extends DisplayOutputListener, DisplayInputTrigger {
    void reset();

    void close();
}
