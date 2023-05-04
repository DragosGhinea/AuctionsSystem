package me.dragosghinea.application.menus;

import me.dragosghinea.model.User;
import me.dragosghinea.repository.impl.postgres.UserRepositoryImpl;
import me.dragosghinea.services.UserService;
import me.dragosghinea.services.impl.UserServiceImpl;

import java.util.Scanner;

public class LoginMenu implements Menu {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public Scanner getInputSource() {
        return scanner;
    }

    private boolean shouldExit = false;
    private final UserService userService = new UserServiceImpl(new UserRepositoryImpl());
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

        userService.getUserByUsernameOrEmail(input)
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
