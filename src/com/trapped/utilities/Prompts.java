package com.trapped.utilities;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.lang.ProcessBuilder;


public class Prompts {
    static Scanner scan = new Scanner(System.in);

    /*
     * gets String from user input
     * uses next() with new line delimiter vs nextLine()
     */

    public static String getStringInput() {
        String userInput = scan.nextLine();
        return userInput;
    }

    /*
     * returns integer value that user enters
     * if invalid input will loop until a valid int is typed in showing error message for invalid input
     */

    public static int getIntInput() {
        try {
            int userInput = scan.nextInt();
            return userInput;
        }
        catch (InputMismatchException e) {
            System.out.println("Invalid input. Input must be an integer.");
        }
        finally {
            scan.nextLine();
        }
          return 0;
    }

    //clear the console
    public static void ClearConsole(){
        try{
            String operatingSystem = System.getProperty("os.name"); //Check the current operating system
            if(operatingSystem.contains("Windows")){
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");
                pb.inheritIO().start().waitFor();
            } else {
                ProcessBuilder pb = new ProcessBuilder("clear");
                pb.inheritIO().start().waitFor();
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}