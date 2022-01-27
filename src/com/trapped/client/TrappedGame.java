package com.trapped.client;

import com.trapped.GameEngine;
import com.trapped.player.Player;
import com.trapped.utilities.FileManager;
import com.trapped.utilities.Furnitures;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class TrappedGame {
//    ArrayList<Furnitures> map;
//    Player player;

    public static void main(String[] args) throws IOException, InterruptedException {
        GameEngine game = new GameEngine();
        game.startGame();
    }

//    public static void startGame() throws IOException, InterruptedException {
//        Scanner input = new Scanner(System.in);
//        FileManager.readMessageSlowly("greeting.txt",5);
//
//
//        System.out.println();
//        System.out.println("--------------------------------");
//
//        System.out.println("What is your name: ");
//        String name = input.next();
//        System.out.println();
//        System.out.println("--------------------------------");
//
//        FileManager.readMessageSlowly("introstory.txt",10);
//
//    }









}
