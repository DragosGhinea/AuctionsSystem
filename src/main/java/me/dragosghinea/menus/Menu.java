package me.dragosghinea.menus;

import me.dragosghinea.display.Display;
import me.dragosghinea.display.events.DisplayInputListener;
import me.dragosghinea.display.events.DisplayOutputTrigger;

public interface Menu extends DisplayInputListener, DisplayOutputTrigger {

    void disconnect(Display display);
}
