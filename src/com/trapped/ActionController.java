package com.trapped;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ActionController implements ActionListener {

    private static GameHandler gHandler;

    public ActionController(GameHandler gHandler) {
        this.gHandler = gHandler;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionClicked = e.getActionCommand();

        switch (actionClicked) {
            case "INS":
                gHandler.mainFrame.writeToTextArea("These is nothing over there");
                break;
            case "GET":
                gHandler.mainFrame.writeToTextArea("You cannot get that");
                break;
            case "DROP":
                gHandler.mainFrame.writeToTextArea("You dropped everything");
                gHandler.player.playerDefault();
                break;

            case"insBed":
                gHandler.mainFrame.writeToTextArea("there is a key under the bed");
                break;
            case "getKey":
                gHandler.mainFrame.writeToTextArea("you get the key under the bed ");
                gHandler.player.hasKey++;
                gHandler.player.updateItems();
                break;

            case"openDoor":
                if (gHandler.player.hasKey ==1) {
                    gHandler.mainFrame.writeToTextArea("you open the door");
                    gHandler.player.hasKey--;
                    gHandler.player.updateItems();
                } else if (gHandler.player.hasKey ==0) {
                    gHandler.mainFrame.writeToTextArea("you need the key to open it, go find the key ");
                }
                break;


           // Screen section changes
            case "hallSection":
                gHandler.mainFrame.hallScreen();
                break;
            case "studySection":
                gHandler.mainFrame.studyScreen();
                break;
            case "restSection":
                gHandler.mainFrame.restScreen();
                break;
        }
    }


}