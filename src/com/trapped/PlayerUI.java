package com.trapped;

class PlayerUI {

    GameHandler gameHandler;
    public int hasKey;

    public PlayerUI(GameHandler gameHandler){
        this.gameHandler=gameHandler;
    }

    public void playerDefault(){
        // initial items
        hasKey =0;
        updateItems();
    }

    public void updateItems(){

        if (hasKey ==0){
            gameHandler.mainFrame.keyLabel.setVisible(false);
        }
        else if (hasKey ==1){
            gameHandler.mainFrame.keyLabel.setVisible(true);
        }

    }
}