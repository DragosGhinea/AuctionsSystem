package me.dragosghinea.application.menus;

import me.dragosghinea.model.User;
import me.dragosghinea.model.UserDetails;
import me.dragosghinea.services.UserService;
import me.dragosghinea.services.impl.UserServiceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Predicate;

public class LoginMenu implements Menu {

    private boolean shouldExit = false;
    private final UserService userService = new UserServiceImpl();
    private User user;

    @Override
    public boolean shouldExit() {
        return shouldExit;
    }

    @Override
    public void receiveInput(String input) {
        if (user != null) {
            if (user.getUserDetails().checkPassword(input)) {
                new LoggedUserMenu(user).start();
                shouldExit = true;
            } else {
                getOutputSource().println("Incorrect password.");
            }
            return;
        }

        Predicate<User> searchCondition;
        if (input.contains("@"))
            searchCondition = (user) -> user.getUserDetails().getEmail().equalsIgnoreCase(input);
        else
            searchCondition = (user) -> user.getUserDetails().getUsername().equalsIgnoreCase(input);


        userService.findFirstUser(searchCondition)
                .ifPresentOrElse(
                        (userFound) -> user = userFound,
                        () -> getOutputSource().println("No user found!")
                );
    }

    @Override
    public void start() {
        getOutputSource().println("Attempting to login");
        getOutputSource().println("Type 'exit' any time to cancel.");
        Menu.super.start();
    }

    @Override
    public String menuOptions() {
        if (user == null)
            return "Enter a username or email: ";
        else
            return "Enter the password: ";
    }
}
