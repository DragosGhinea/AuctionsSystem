package me.dragosghinea.application.menus;

import me.dragosghinea.model.UserDetails;
import me.dragosghinea.repository.impl.postgres.UserRepositoryImpl;
import me.dragosghinea.services.impl.UserServiceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class CreateUserMenu implements Menu{

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public Scanner getInputSource() {
        return scanner;
    }

    private boolean shouldExit = false;
    private final UserDetails.UserDetailsBuilder userDetailsBuilder = UserDetails.builder();
    private final List<OPTION> creationFlow = new LinkedList<>(List.of(
            OPTION.FIRST_NAME,
            OPTION.LAST_NAME,
            OPTION.USERNAME,
            OPTION.EMAIL,
            OPTION.BIRTH_DATE,
            OPTION.PASSWORD
    ));

    @Override
    public boolean shouldExit() {
        return shouldExit;
    }

    @Override
    public void receiveInput(String input) {
        OPTION op = creationFlow.get(0);
        switch(op) {
            case FIRST_NAME -> {
                if(input.matches(op.regexValidator)){
                    userDetailsBuilder.firstName(input);
                    creationFlow.remove(0);
                }
                else{
                    getOutputSource().println(op.incorrectMessage);
                }
            }
            case LAST_NAME -> {
                if(input.matches(op.regexValidator)){
                    userDetailsBuilder.lastName(input);
                    creationFlow.remove(0);
                }
                else{
                    getOutputSource().println(op.incorrectMessage);
                }
            }
            case USERNAME -> {
                if(input.matches(op.regexValidator)){
                    userDetailsBuilder.username(input);
                    creationFlow.remove(0);
                }
                else{
                    getOutputSource().println(op.incorrectMessage);
                }
            }
            case EMAIL -> {
                if(input.matches(op.regexValidator)){
                    userDetailsBuilder.email(input);
                    creationFlow.remove(0);
                }
                else{
                    getOutputSource().println(op.incorrectMessage);
                }
            }
            case BIRTH_DATE -> {
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                LocalDate date;
                try {
                    date = format.parse(input).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    userDetailsBuilder.birthDate(date);
                    creationFlow.remove(0);
                } catch (ParseException e) {
                    getOutputSource().println(op.incorrectMessage);
                }
            }
            case PASSWORD -> {
                if(input.matches(op.regexValidator)){
                    UserDetails details = userDetailsBuilder.build();
                    details.setPassword(input);
                    if(new UserServiceImpl(new UserRepositoryImpl()).createUser(details).isEmpty()){
                        getOutputSource().println("Could not create the user! It might have the same email or username as someone else.");
                    }
                    else{
                        getOutputSource().println("User has been created successfully!");
                    }
                    shouldExit = true;
                }
                else{
                    getOutputSource().println(op.incorrectMessage);
                }
            }
        }
    }

    @Override
    public void start() {
        getOutputSource().println("Creating a new user account!");
        getOutputSource().println("Type 'exit' any time to cancel.");
        Menu.super.start();
    }

    @Override
    public String menuOptions() {
        return creationFlow.get(0).prompt;
    }

    private enum OPTION{
        FIRST_NAME("^[A-Z][a-z]+(-[A-Z][a-z]+)*$", "The first name must be 'Firstname' or 'Firstname1-Firstname2', each first name must be at least 2 characters long", "First name: "),
        LAST_NAME("^[A-Z][a-z]+(-[A-Z][a-z]+)*$", "The last name must be 'Lastname' or 'Lastname1-Lastname2', each last name must be at least 2 characters long", "Last name: "),
        USERNAME("^[a-zA-Z0-9_.]{2,}$", "The username must be alphanumeric with _ and . at most, and at least 2 characters", "Username: "),
        EMAIL("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", "Your email format is not valid, please enter a valid one.", "Email: "),
        BIRTH_DATE("", "The date you entered is invalid! Please respect the format: MM/dd/yyyy", "Birth Date: "),
        PASSWORD("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{4,}$", "The password needs to contain minimum four letters, at least one letter, one number and one special character.", "Password: ");

        private final String regexValidator;
        private final String incorrectMessage;
        private final String prompt;

        OPTION(String regexValidator, String incorrectMessage, String prompt){
            this.regexValidator = regexValidator;
            this.incorrectMessage = incorrectMessage;
            this.prompt = prompt;
        }
    }
}
