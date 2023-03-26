package me.dragosghinea.user.sessions;


import me.dragosghinea.display.Display;
import me.dragosghinea.user.User;
import me.dragosghinea.menus.Menu;

public interface UserSession {

    Display getDisplay();
    Menu getOpenMenu();
    boolean changeMenu(Menu newMenu);
    User getUser();

}
