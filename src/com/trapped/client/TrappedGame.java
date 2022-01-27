package com.trapped.client;

import com.trapped.player.Player;
import com.trapped.utilities.Furnitures;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TrappedGame {
    ArrayList<Furnitures> map;
    Player player;

    List<String> commands = new ArrayList<>(Arrays.asList("look","inspect","pick up","use","left","right"));
    List<String> items_needed = new ArrayList<>(Arrays.asList("crowbar","yellow key","blank paper"));

    //constructor
    public TrappedGame(){
        this.map = new ArrayList<Furnitures>();
        map.add(new Furnitures("bed","a oak color bed that you wake up from",List.of("laptop","match"),List.of(""),"door","window","",""));
        map.add(new Furnitures("door","a door which is locked",List.of("laptop","match"),List.of(""),"lock","window","bed",""));
        map.add(new Furnitures("window","a window which is locked",List.of("laptop","match"),List.of(""),"bed","door","",""));

    }

    public static void main(String[] args) {

            Scanner input = new Scanner(System.in);

            System.out.println("Welcome to Trapped game, in this game you have certain quests and you can pick options each time. " +
                    "\nThere are different furnitures where you can inspect and pickup items and use items to solve puzzles.  "
                    + "\nIf you solve all the puzzles, you will get all clues to unlock the door.");
            System.out.println("--------------------------------");

            System.out.println("What is your name: ");
            String name = input.next();
            System.out.println("--------------------------------");

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

    }




}
