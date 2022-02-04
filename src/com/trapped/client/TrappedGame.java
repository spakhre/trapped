package com.trapped.client;

import com.trapped.GameEngine;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URISyntaxException;

public class TrappedGame {
//    ArrayList<Furnitures> map;
//    Player player;

    public static void main(String[] args) {
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
