package me.dragosghinea.menus.user;


import me.dragosghinea.display.Display;
import me.dragosghinea.display.events.DisplayOutputListener;
import me.dragosghinea.menus.Menu;
import me.dragosghinea.user.details.builder.DefaultUserDetailsBuilder;
import me.dragosghinea.user.details.builder.UserDetailsAbstractBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class CreateUser implements Menu {
    private final List<DisplayOutputListener> observers = new ArrayList<>();
    private UserDetailsAbstractBuilder userDetails = new DefaultUserDetailsBuilder();

    private Display display;

    public CreateUser(Display display){
        this.display = display;
        subscribeOutput(display);
        display.subscribeInput(this);

        Properties request = new Properties();
        request.setProperty("OutputType", "Request");
        request.setProperty("TriggeredBy", "CreateUser");
        request.setProperty("RequestInfo", "FirstName");

        triggerOutput("You are now creating a new user account!\n", null);
        triggerOutput("Introduce first name: ", request);
    }

    /**
        Receives information from a display and checks
        if the information was requested by this menu
        otherwise it is not of interest.
        In case the information is requested by this menu
        it will be evaluated.

        @return
        returns false if the information received was incorrect or irrelevant
        to this menu.

        returns true if the information was evaluated and construction of UserDetails
        continues.
    */
    @Override
    public boolean inputInfo(String gotInfo, Properties otherSettings) {
        if(otherSettings == null)
            return false;

        if(!otherSettings.getProperty("OutputType", "None").equals("Request"))
            return false;

        if(!otherSettings.getProperty("TriggeredBy", "None").equals("CreateUser"))
            return false;

        String requested = otherSettings.getProperty("RequestInfo", "None");

        if(gotInfo.equals("None"))
            return false;

        Properties error = new Properties();
        error.setProperty("OutputType", "Error");
        error.setProperty("TriggeredBy", "CreateUser");

        //Properties requestNext = (Properties) otherSettings.clone(); no cloning, there might be extra properties in there
        Properties requestNext = new Properties();
        requestNext.setProperty("OutputType", "Request");
        requestNext.setProperty("TriggeredBy", "CreateUser");
        switch(requested){
            case "FirstName" -> {
                if(!gotInfo.matches("^[A-Z][a-z]+(-[A-Z][a-z]+)*$")){
                    triggerOutput("The first name must be 'Firstname' or 'Firstname1-Firstname2', each first name must be at least 2 characters long\n", error);
                    triggerOutput("First name: ", otherSettings);
                    return false;
                }

                userDetails.setFirstName(gotInfo);
                requestNext.setProperty("RequestInfo", "LastName");
                triggerOutput("Last name: ", requestNext);
                return true;
            }
            case "LastName" -> {
                if(!gotInfo.matches("^[A-Z][a-z]+(-[A-Z][a-z]+)*$")){
                    triggerOutput("The last name must be 'Lastname' or 'Lastname1-Lastname2', each last name must be at least 2 characters long\n", error);
                    triggerOutput("Last name: ", otherSettings);
                    return false;
                }

                userDetails.setLastName(gotInfo);
                requestNext.setProperty("RequestInfo", "Email");
                triggerOutput("Email: ", requestNext);
                return true;
            }
            case "Email" -> {
                if(!gotInfo.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
                    triggerOutput("Your email format is not valid, please enter a valid one.\n", error);
                    triggerOutput("Email: ", otherSettings);
                    return false;
                }

                userDetails.setEmail(gotInfo);
                requestNext.setProperty("RequestInfo", "Username");
                triggerOutput("Username: ", requestNext);
                return true;
            }
            case "Username" -> {
                if(!gotInfo.matches("^[a-zA-Z0-9_.]{2,}$")){
                    triggerOutput("The username must be alphanumeric with _ and . at most, and at least 2 characters\n", error);
                    triggerOutput("Username: ", otherSettings);
                    return false;
                }

                userDetails.setUsername(gotInfo);
                requestNext.setProperty("RequestInfo", "BirthDate");
                triggerOutput("Birth Date (MM/dd/yyyy): ", requestNext);
                return true;
            }
            case "BirthDate" -> {
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                Date date;
                try {
                    date = format.parse(gotInfo);
                } catch (ParseException e) {
                    triggerOutput("The date you entered is invalid! Please respect the format: MM/dd/yyyy\n", error);
                    triggerOutput("Birth Date: ", otherSettings);
                    return false;
                }

                userDetails.setBirthDateEpoch(date.toInstant().toEpochMilli());
                requestNext.setProperty("RequestInfo", "Password");
                requestNext.setProperty("Password", "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$");
                requestNext.setProperty("PasswordErrorMessage", "The password needs to contain minimum eight letters, at least one letter, one number and one special character.\n");
                triggerOutput("Password: ", requestNext);
                return true;
            }
            case "Password" -> {
                userDetails.setPasswordHash(gotInfo);
                System.out.println("Created user successfully!");
                System.out.print("Main Console >> ");
                display.close();
                disconnect(display);
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    @Override
    public boolean triggerOutput(String gotInfo, Properties otherSettings) {
        observers.forEach(obv -> obv.outputInfo(gotInfo, otherSettings));
        return true;
    }

    @Override
    public boolean subscribeOutput(DisplayOutputListener outputListener) {
        observers.add(outputListener);
        return true;
    }

    @Override
    public boolean unsubscribeOutput(DisplayOutputListener outputListener) {
        return observers.remove(outputListener);
    }

    @Override
    public void disconnect(Display display) {
        unsubscribeOutput(display);
        display.unsubscribeInput(this);
    }
}
