package com.trapped;

import com.trapped.player.Player;
import com.trapped.utilities.FileManager;
import com.trapped.utilities.Prompts;
import com.trapped.utilities.Sounds;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

import static com.trapped.utilities.TextColor.BLUE_BOLD;
import static com.trapped.utilities.TextColor.RESET;

public class GameEngine implements Serializable {
    private boolean quitGame = false;


    public static void startGame() {
        while (true) {
            FileManager.getResource("./splash_screen.txt");   // displaying splash screen

            //System.out.println(TextColor.RED + "\nPlease select an option from the menu." + TextColor.RESET);
            System.out.println("\nPlease select an option from the menu.");

            int userInput = Prompts.getIntInput();

            switch (userInput) {
                case 1:
                    FileManager.readMessageSlowly("greeting.txt", 20);
                    FileManager.readMessageSlowly("warning.txt", 0);
                    System.out.println("\n--------------------------------");
                    System.out.println("What is your name: ");
                    String userName = Prompts.getStringInput();
                    System.out.println("\n\nHello, " + BLUE_BOLD + userName.toUpperCase() + RESET);

                    FileManager.readMessageSlowly("introstory.txt", 10);

                    Sounds.playSounds("phone.wav", 3000);

                    FileManager.readMessageSlowly("intro_after_phone.txt", 10);
                    Timer timer = new Timer();
                    TimerTask task = new TimerTask(){
                        @Override
                        public void run(){
                            System.out.println("You lost the game! See you next time!");
                            FileManager.readMessageSlowly("exploded.txt", 0);
                            System.exit(0);
                        }
                    };
                    timer.schedule(task,480000);

                    playGame();
                case 2: // quit game option
                    System.out.println("Exiting the game. Thank you for playing");
                    System.exit(0);

                default:
                    System.out.println("Invalid input! Please enter a number corresponding to one of the menu options.");
            }
        }
    }

    public static void playGame() {
        while (true) {
            Player.viewRoom();
        }
    }


}



