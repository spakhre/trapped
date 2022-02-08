package com.trapped;

import com.trapped.player.Player;
import com.trapped.utilities.FileManager;
import com.trapped.utilities.Sounds;

import java.io.Serializable;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import static com.trapped.utilities.TextColor.BLUE_BOLD;
import static com.trapped.utilities.TextColor.RESET;

public class GameEngine implements Serializable {
    private boolean quitGame = false;
    static Scanner scan = new Scanner(System.in);
    static Scanner scanner = new Scanner(System.in);

    public static void startGame()  {


            FileManager.getResource("./splash_screen.txt");   // displaying splash screen

            System.out.println("\nPlease select an option from the menu.");
            int userInput;

            try {
                userInput = scan.nextInt();
                switch (userInput) {
                    case 1:
//                    Sounds.playSounds("startsound.wav", 0);
//                    FileManager.readMessageSlowly("greeting.txt", 20);
//                    FileManager.readMessageSlowly("warning.txt", 10);
                        System.out.println("\n--------------------------------");
                        System.out.println("What is your name: ");
                        String userName = scanner.nextLine();
                        System.out.println("\n\nHello, " + BLUE_BOLD + userName.toUpperCase() + RESET);

                        FileManager.readMessageSlowly("introstory.txt", 10);
                        System.out.println("Press Enter to Continue...");
                        scan.nextLine();
                        Sounds.playSounds("phone.wav", 3000);

                        FileManager.readMessageSlowly("intro_after_phone.txt", 0);
                        Timer timer = new Timer();
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                System.out.println("You lost the game! See you next time!");
                                FileManager.readMessageSlowly("exploded.txt", 10);
                                System.exit(0);
                            }
                        };
                        timer.schedule(task, 480000);

                        playGame();
                    case 2: // quit game option
                        System.out.println("Exiting the game. Thank you for playing");
                        System.exit(0);

                    default:
                        System.out.println("Invalid input! Please enter a number corresponding to one of the menu options.");
                }
            }
            catch(InputMismatchException e){
                System.err.print("User input not valid");
                scan.next();
                startGame();
            }
        }


    public static void playGame() {
        int loopLevel=0;
        loopLevel++;
        System.out.println("You are " + loopLevel + " levels deep.");
        while (true) {

            Player.viewRoom();
        }
    }


}



