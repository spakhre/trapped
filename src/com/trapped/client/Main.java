package com.trapped.client;

import com.trapped.GameEngine;

import java.io.Serializable;

public class Main implements Serializable {
    public static void main(String[] args) {
        GameEngine game = new GameEngine();
        game.startGame();
    }
}
