package me.dragosghinea.application.menus;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public interface Menu {

    default InputStream getInputSource(){
        return System.in;
    }

    default PrintStream getOutputSource(){
        return System.out;
    }

    default boolean shouldExit(){
        return false;
    }

    default void onExit(){

    }

    default void start(){
        Scanner scanner = new Scanner(getInputSource());
        String input;
        while (true) {
            getOutputSource().println(menuOptions());
            input = scanner.nextLine();

            if(input.equals("quit") || input.equals("q") || input.equals("back") || input.equals("exit")) {
                onExit();
                break;
            }

            receiveInput(input);

            if(shouldExit())
                break;
        }
    }

    void receiveInput(String input);

    String menuOptions();
}
