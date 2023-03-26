package me.dragosghinea;

import me.dragosghinea.display.TerminalDisplay;
import menus.user.CreateUser;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        while (true) {
            if(input.equals("quit") || input.equals("q"))
                break;
            System.out.print("Main Console >> ");
            input = scanner.nextLine();

            switch(input){
                case "create user" -> {
                    new Thread(){
                        @Override
                        public void run() {
                            TerminalDisplay display = new TerminalDisplay();
                            CreateUser createMenu = new CreateUser(display);
                        }
                    }.start();

                }
                default -> {}
            }
        }
        scanner.close();
    }
}
