package com.trapped.client;

import com.trapped.GameEngine;
import com.trapped.GuiGameEngine;
import com.trapped.gui.controller.GuiMainWindow;
import com.trapped.gui.plain.MainFrame;
import com.trapped.player.Player;

import java.io.Serializable;

public class Main implements Serializable {
    public static void main(String[] args) {
//        GameEngine game = new GameEngine();
//        game.startGame();

//        GuiGameEngine gameEngine = new GuiGameEngine();
//        new GuiMainWindow();
//        gameEngine.playGame();

        new MainFrame();
    }
}
