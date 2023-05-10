package me.dragosghinea.application.menus;

import me.dragosghinea.model.User;
import me.dragosghinea.repository.impl.postgres.UserRepositoryImpl;
import me.dragosghinea.services.AuditService;
import me.dragosghinea.services.UserService;
import me.dragosghinea.services.enums.AuditAction;
import me.dragosghinea.services.impl.AuditServiceImpl;
import me.dragosghinea.services.impl.UserServiceImpl;

import java.util.Scanner;

public class LoginMenu implements Menu {

    private final Scanner scanner = new Scanner(System.in);
    private static final AuditService auditService = AuditServiceImpl.getInstance();

    @Override
    public Scanner getInputSource() {
        return scanner;
    }

    private boolean shouldExit = false;
    private final UserService userService = new UserServiceImpl(new UserRepositoryImpl());
    private User user;
    private int wrongPasswordCount = 0;

    @Override
    public boolean shouldExit() {
        return shouldExit;
    }

    @Override
    public void receiveInput(String input) {
        if (user != null) {
            if (user.getUserDetails().checkPassword(input)) {
                auditService.logInfoAction(
                        AuditAction.USER_LOGIN,
                        "User "+user.getUserDetails().getUsername()+" logged in"+(wrongPasswordCount > 1 ? " after typing the password wrongly "+wrongPasswordCount+" times." : "."),
                        user.getUserDetails().getUsername()
                );
                new LoggedUserMenu(user).start();
                shouldExit = true;
            } else {
                auditService.logErrorAction(AuditAction.USER_ERROR, "Login for user "+user.getUserDetails().getUsername()+" failed due to incorrect password!", "Anonymous User");
                getOutputSource().println("Incorrect password.");
                wrongPasswordCount++;
            }
            return;
        }

        userService.getUserByUsernameOrEmail(input)
                .ifPresentOrElse(
                        (userFound) -> user = userFound,
                        () -> {
                            auditService.logErrorAction(AuditAction.USER_ERROR, "No user found for input: "+input, "Anonymous User");
                            getOutputSource().println("No user found!");
                        }
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
