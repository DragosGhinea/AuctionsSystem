package me.dragosghinea;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        while (true) {
            if(input.equals("quit") || input.equals("q"))
                break;
            input = scanner.nextLine();


        }
        scanner.close();
    }
}
