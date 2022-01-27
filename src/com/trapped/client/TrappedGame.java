package com.trapped.client;

import com.trapped.player.Player;
import com.trapped.utilities.Furnitures;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.awt.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class TrappedGame {

    ArrayList<Furnitures> map;
    Player player;


    List<String> commands = new ArrayList<>(Arrays.asList("look","inspect","pick up","use","left","right"));
    List<String> items_needed = new ArrayList<>(Arrays.asList("crowbar","yellow key","blank paper"));


    public static void main(String[] args) throws IOException, InterruptedException {
            startGame();
            Player.move();
    }


    public static void readMessageSlowly(String message, int sec) throws InterruptedException {
        char[] chars = message.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            System.out.print(chars[i]);
            Thread.sleep(sec);
        }
    }

    public static void startGame() throws IOException, InterruptedException {
        Scanner input = new Scanner(System.in);
        String greeting = convertTxtToString("greeting.txt");
        readMessageSlowly(greeting,20);


        System.out.println();
        System.out.println("--------------------------------");

        System.out.println("What is your name: ");
        String name = input.next();
        System.out.println();
        System.out.println("--------------------------------");

        String intro = convertTxtToString("introstory.txt");
        readMessageSlowly(intro,10);

    }

    public static String convertTxtToString(String fileName){
        String file = "resources/" + fileName;
        Path path = Paths.get(file);

        StringBuilder sb = new StringBuilder();


        try (Stream<String> stream = Files.lines(path)) {
            stream.forEach(s -> sb.append(s).append("\n"));
        } catch (IOException ex) {
            // Handle exception
        }

            int opt[] = {1,2,3,4};
            String menuDesc[] = {"Something1", "Something2","Continue", "Quit" };
            showMenu(opt, menuDesc);

//menu loop for choices
        int selection = input.nextInt();

            while(selection!= opt[opt.length-1]){
                if(selection == opt[0]){
                    break;
                }
                else if(selection == opt[1]){
                    System.out.println(menuDesc[1]);
                    break;
                }
                else if(selection == opt[2]){
                    System.out.println(menuDesc[2]);
                    break;

                }
            }

    }

    public static void showMenu(int options[], String item[]){
        System.out.println("Your List of Options:");
        System.out.println("---------");
        for (int i = 0; i <options.length; i++){
            System.out.printf("%s  %s\n", options[i], item[i]);
        }
        System.out.println("what do you want to do");


        String contents = sb.toString();
        return contents;
    }




}
