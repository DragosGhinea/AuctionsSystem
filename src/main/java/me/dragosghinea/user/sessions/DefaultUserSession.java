package me.dragosghinea.user.sessions;

import me.dragosghinea.display.Display;
import me.dragosghinea.user.User;
import menus.Menu;

public class DefaultUserSession implements UserSession{

    public DefaultUserSession(User user){

    }


    @Override
    public Display getDisplay() {
        return null;
    }

    @Override
    public Menu getOpenMenu() {
        return null;
    }

    @Override
    public boolean changeMenu(Menu newMenu) {
        return false;
    }

    @Override
    public User getUser() {
        return null;
    }
}
