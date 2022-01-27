package com.trapped;

import com.trapped.utilities.FileManager;

import java.io.IOException;
import java.util.Scanner;

public class GameEngine {
    private FileManager fileManager = new FileManager();    //handles .txt display to screen
    private boolean quitGame = false;

    public void startGame() throws IOException {
        Scanner scanner = new Scanner(System.in);   //setting up for user input
        while (true) {
            fileManager.getResource("splash_screen.txt");   // displaying splash screen

            System.out.println("\nPlease select an option from the menu.");
            int userInput = scanner.nextInt();

            switch (userInput) {
                case 1:   //currently, being occupied by the quit option
                    System.out.println("Exiting the game. Thank you for playing");
                    scanner.close();
                    System.exit(0);
                case 2:
                    System.out.println("There is nothing here");
                    break;
                default:
                    System.out.println("Invalid input! Please enter a number corresponding to one of the menu options.");
            }
        }





    }

    public void restartGame(){

    }

}
