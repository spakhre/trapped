package com.trapped.utilities;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Prompts {
    //static Scanner scan = new Scanner(System.in);

    /*
     * gets String from user input
     * uses next() with new line delimiter vs nextLine()
     */

    public static String getStringInput() {
        Scanner scan = new Scanner(System.in);
        scan.useDelimiter("\r\n");
        String userInput = scan.next();
        //scan.nextLine();
        //scan.close();
        return userInput;
    }

    /*
     * returns integer value that user enters
     * if invalid input will loop until a valid int is typed in showing error message for invalid input
     */

    public static int getIntInput() {
        Scanner scan = new Scanner(System.in);
        try {
            int userInput = scan.nextInt();
            //scan.nextLine();
            //scan.close();
            return userInput;
        }
        catch (InputMismatchException e) {
            System.out.println("Invalid input. Input must be an integer.");
            //scan.nextLine();    //clear buffer to ready for next input
        }
        finally {
            scan.nextLine();
            //scan.close();
        }

//        boolean invalid = true;
//        while(invalid) {
//            try {
//                int userInput = scan.nextInt();
//                scan.nextLine();
//                //if(userInput instanceof Integer) {
//                invalid = false;
//                scan.close();
//                return userInput;
//                //}
//            }
//            catch (InputMismatchException e) {
//                System.out.println("Invalid input. Input must be an integer.");
//                scan.nextLine();    //clear buffer to ready for next input
//            }
//        }
//        scan.close();
          return 0;
    }
    
}
