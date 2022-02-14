package com.gui.utility;

import com.trapped.player.Player;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TextCollector {
    private String currentCommand;
    private Player player = Player.getInstance();

    private static TextCollector TEXTCOLLECTOR = new TextCollector();

    private static void TextCollector() {}

    public static TextCollector getInstance(){
        return TEXTCOLLECTOR;
    }

    public class inputActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    public void executeCommand(String command) {
        System.out.println("Attempt playerInput: " + command);
//        String returnValue = player.playerInput(command);
//        System.out.println("Did we get it?: " + returnValue);
    }
}