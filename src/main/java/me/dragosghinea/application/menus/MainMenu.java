package me.dragosghinea.application.menus;

public class MainMenu implements Menu{

    @Override
    public void receiveInput(String input) {
        OPTION op = OPTION.getById(input);

        switch(op){
            case CREATE_ACCOUNT -> {
                new CreateUserMenu().start();
            }
            case LOGIN -> {
                new LoginMenu().start();
            }
            default -> {
                getOutputSource().println("Unknown option '"+input+"'!");
            }
        }
    }

    @Override
    public String menuOptions() {
        return """
                Main Menu Options:
                  1 - Create new account
                  2 - Login
                Type 'exit' to leave
               """;
    }

    private enum OPTION{
        CREATE_ACCOUNT("1"),
        LOGIN("2"),
        UNKNOWN("");

        private final String id;
        OPTION(String id){
            this.id = id;
        }

        public static OPTION getById(String id){
            for(OPTION option : values())
                if(option.id.equals(id))
                    return option;
            return UNKNOWN;
        }
    }
}
