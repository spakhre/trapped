package com.trapped;

import com.trapped.player.Player;
import com.trapped.utilities.FileManager;
import com.trapped.utilities.Sounds;
import com.trapped.utilities.TextParser;

import java.io.Serializable;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import static com.trapped.utilities.TextColor.BLUE_BOLD;
import static com.trapped.utilities.TextColor.RESET;

public class GameEngine implements Serializable {
    private boolean quitGame = false;
    static Scanner scanner = new Scanner(System.in);
    static float volume;


    public static void startGame()  {
        FileManager.writeDefaults();
        FileManager.getResource("./splash_screen.txt");   // displaying splash screen

        System.out.println("\nPlease select an option from the menu.");
        int userInput;
                userInput = TextParser.integerParse();
                switch (userInput) {
                    case 1:
                        Sounds.playSounds("startsound.wav", 0);
                        playGame();
                    case 2: // menu option sound must be between 6.0206 and -80
                        menu();
                    case 3: // quit game option
                        System.out.println("Exiting the game. Thank you for playing");
                        System.exit(0);
                    default:
                        System.out.println("Invalid input! Please enter a number corresponding to one of the menu options.");
                        System.out.println("Press enter to continue");
                        scanner.nextLine();
                        startGame();
            }
        }


    public static void menu(){
        System.out.println("You can currently only change the music, other settings will be implemented later." +
                " \nWhat would you like the music level to be?" +
                "\nEnter volume in the form of '+/- XX.X', for example -30.0 or 30.0");
        volume = TextParser.floatParse();
            if (-80.0f <= volume && volume <= 6.0206f) {
                Sounds.changeVolume("startsound.wav", 0, volume);
                Player.volume = volume;
                playGame();
            } else {
                System.out.println("volume must be between -80.0 and 6.0206");
                menu();
            }
    }
    public static void playGame() {
        //                    FileManager.readMessageSlowly("greeting.txt", 20);
//                    FileManager.readMessageSlowly("warning.txt", 10);
        System.out.println("\n--------------------------------");
        System.out.println("What is your name: ");
        String userName = scanner.nextLine();
        System.out.println("\n\nHello, " + BLUE_BOLD + userName.toUpperCase() + RESET);

//        FileManager.readMessageSlowly("introstory.txt", 10);
//        Sounds.changeVolume("phone.wav", 3000, volume);
//        System.out.println("Press Enter to Continue...");
//        scanner.nextLine();
//        FileManager.readMessageSlowly("intro_after_phone.txt", 0);
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
        while (true) {

            Player.viewRoom();
        }
    }
}



