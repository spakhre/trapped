package com.trapped;

import com.trapped.player.Player;
import com.trapped.utilities.FileManager;
import com.trapped.utilities.Sounds;
import com.trapped.utilities.TextColor;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.Scanner;

import static com.trapped.utilities.TextColor.*;

public class GameEngine implements Serializable {
    private static FileManager fileManager = new FileManager();    //handles .txt display to screen
    private boolean quitGame = false;

    public static void startGame() throws IOException, InterruptedException, URISyntaxException, UnsupportedAudioFileException, LineUnavailableException {
        Scanner scanner = new Scanner(System.in);   //setting up for user input
        while (true) {
            fileManager.getResource("./splash_screen.txt");   // displaying splash screen

            System.out.println(TextColor.RED + "\nPlease select an option from the menu." + TextColor.RESET);
            int userInput = scanner.nextInt();


            switch (userInput) {
                case 1:   //currently, being occupied by the quit option
                    FileManager.readMessageSlowly("greeting.txt", 0);
                    System.out.println("\n--------------------------------");
                    System.out.println("What is your name: ");
                    String name = scanner.next();
                    System.out.println("\n\nHello, " + BLUE_BOLD + name.toUpperCase() + RESET);
                    FileManager.readMessageSlowly("introstory.txt", 0);
                    Sounds.playSounds("phone.wav");
                    Thread.sleep(4000);
                    FileManager.readMessageSlowly("intro_after_phone.txt", 0);

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

    public static void playGame() throws IOException, URISyntaxException, InterruptedException, UnsupportedAudioFileException, LineUnavailableException {
        while (true) {
            Player.viewRoom();

        }
    }


}



