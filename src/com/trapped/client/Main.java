package com.trapped.client;

import com.trapped.GameEngine;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        GameEngine game = new GameEngine();
        game.startGame();
    }

}