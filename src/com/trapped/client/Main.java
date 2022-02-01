package com.trapped.client;

import com.trapped.GameEngine;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;

public class Main implements Serializable {
    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException, UnsupportedAudioFileException, LineUnavailableException {
        GameEngine game = new GameEngine();
        game.startGame();







    }


    }


