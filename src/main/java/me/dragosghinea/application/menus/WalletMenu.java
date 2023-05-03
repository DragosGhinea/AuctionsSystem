package me.dragosghinea.application.menus;

import me.dragosghinea.model.User;
import me.dragosghinea.model.enums.Currency;
import me.dragosghinea.services.WalletService;
import me.dragosghinea.services.impl.WalletServiceImpl;

import java.math.BigDecimal;
import java.util.Scanner;

public class WalletMenu implements Menu{

    private final Scanner scanner = new Scanner(System.in);
    private final WalletService walletService;

    @Override
    public Scanner getInputSource() {
        return scanner;
    }
    private final User user;

    public WalletMenu(User user) {
        this.user = user;
        walletService = new WalletServiceImpl(user.getWallet());
    }

    @Override
    public void receiveInput(String input) {
        OPTION op = OPTION.getById(input);

        switch (op) {
            case CHANGE_CURRENCY -> {
                getOutputSource().println("Current Currency: "+user.getWallet().getPreferredCurrency().getCurrencyName());
                getOutputSource().println("Available currencies:");
                for(Currency currency : Currency.values())
                    getOutputSource().println(" - "+currency);
                getOutputSource().println("Enter currency:");
                String currencyName = getInputSource().nextLine();
                while(true){
                    try{
                        Currency newPref = Currency.valueOf(currencyName.toUpperCase());
                        walletService.setPreferredCurrency(newPref);
                        break;
                    }catch(IllegalArgumentException x){
                        getOutputSource().println("Currency not found! Reenter:");
                        currencyName = getInputSource().nextLine();
                    }
                }
            }
            case DEPOSIT -> {
                getOutputSource().println("Choose a currency which you want to deposit.");
                getOutputSource().println("Available currencies:");
                for(Currency currency : Currency.values())
                    getOutputSource().println(" - "+currency);
                getOutputSource().println("Enter currency:");
                String currencyName = getInputSource().nextLine();
                Currency currency;
                while(true){
                    try{
                        currency = Currency.valueOf(currencyName.toUpperCase());
                        break;
                    }catch(IllegalArgumentException x){
                        getOutputSource().println("Currency not found! Reenter:");
                        currencyName = getInputSource().nextLine();
                    }
                }

                BigDecimal toAdd;
                getOutputSource().println("Enter amount: ");
                String amount = getInputSource().nextLine();
                while(true){
                    try{
                        toAdd = BigDecimal.valueOf(Double.parseDouble(amount)).abs();
                        break;
                    }catch(Exception x){
                        getOutputSource().println("Invalid amount, reenter: ");
                        amount = getInputSource().nextLine();
                    }
                }

                walletService.addPointsToWallet(currency.getPointsAmount(toAdd));
            }
            case WITHDRAW -> {
                getOutputSource().println("Your password: ");
                String password = getInputSource().nextLine();
                if(user.getUserDetails().checkPassword(password)){
                    getOutputSource().println("Choose a currency in which you want to withdraw.");
                    getOutputSource().println("Available currencies:");
                    for(Currency currency : Currency.values())
                        getOutputSource().println(" - "+currency);
                    getOutputSource().println("Enter currency:");
                    String currencyName = getInputSource().nextLine();
                    Currency currency;
                    while(true){
                        try{
                            currency = Currency.valueOf(currencyName.toUpperCase());
                            break;
                        }catch(IllegalArgumentException x){
                            getOutputSource().println("Currency not found! Reenter:");
                            currencyName = getInputSource().nextLine();
                        }
                    }

                    BigDecimal toRemove;
                    getOutputSource().println("Enter amount: ");
                    String amount = getInputSource().nextLine();
                    while(true){
                        try{
                            toRemove = BigDecimal.valueOf(Double.parseDouble(amount)).abs();
                            break;
                        }catch(Exception x){
                            getOutputSource().println("Invalid amount, reenter: ");
                            amount = getInputSource().nextLine();
                        }
                    }

                    if(!walletService.removePointsFromWallet(currency.getPointsAmount(toRemove)))
                        getOutputSource().println("Not enough points to withdraw!");
                }
                else{
                    getOutputSource().println("Incorrect password!");
                }
            }
            default -> {
                getOutputSource().println("Unknown option '" + input + "'!");
            }
        }
    }

    @Override
    public String menuOptions() {
        return String.format("""
                 Your Wallet (%s)
                   1 - Change preferred currency
                   2 - Deposit money
                   3 - Withdraw money
                 Type 'exit' to go back
                """, user.getWallet());
    }

    private enum OPTION {
        CHANGE_CURRENCY("1"),
        DEPOSIT("2"),
        WITHDRAW("3"),
        UNKNOWN("");

        private final String id;

        OPTION(String id) {
            this.id = id;
        }

        public static OPTION getById(String id) {
            for (OPTION option : values())
                if (option.id.equals(id))
                    return option;
            return UNKNOWN;
        }
    }


}
