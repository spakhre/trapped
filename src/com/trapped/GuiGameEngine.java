package com.trapped;

import com.trapped.player.Player;

public class GuiGameEngine {
    private Player player;

    public void playGame() {
        player = Player.getInstance();
    }
}