package com.trapped;

import com.trapped.player.Player;
import com.trapped.utilities.FileManager;

import java.io.IOException;
import java.util.Scanner;

public class GameEngine {
    private static FileManager fileManager = new FileManager();    //handles .txt display to screen
    private boolean quitGame = false;

    public static void startGame() throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);   //setting up for user input
        while (true) {
            fileManager.getResource("splash_screen.txt");   // displaying splash screen

            System.out.println("\nPlease select an option from the menu.");
            int userInput = scanner.nextInt();

            switch (userInput) {
                case 1:   //currently, being occupied by the quit option
                    FileManager.readMessageSlowly("greeting.txt", 0);
                    System.out.println("\n--------------------------------");
                    System.out.println("What is your name: ");
                    String name = scanner.next();
                    System.out.println("\n--------------------------------");
                    FileManager.readMessageSlowly("introstory.txt", 0);
                    playGame();
                case 2:
                    System.out.println("Exiting the game. Thank you for playing");
                    scanner.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid input! Please enter a number corresponding to one of the menu options.");
            }
        }
    }

    public static void playGame() throws IOException {
        Player.move();
        Player.checkCurrentLocation();
        Player.pickUpItem();
        Player.checkCurrentInventory();

    }

    public void gameMenu() {

    }

}
