package com.trapped.gui.controller;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Container;

public class Gui {
    public static JFrame gameWindow; //for main game window

    public Gui() {
        gameWindow = new JFrame("Trapped: NoWhere to Escape!"); //game window design
        gameWindow.setSize(1200, 800); //sets the x-dimension and y-dimension of the frame

        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Exit out of application
        gameWindow.setResizable(false); //prevent frame from being resized
        gameWindow.getContentPane().setBackground(new Color(100, 200, 200)); //set background color
        gameWindow.setVisible(true); //make frame visible

        GuiBackgroundImageLabelPanel backgroundPanel = new GuiBackgroundImageLabelPanel(this);

        gameWindow.add(backgroundPanel);

       // GuiStartPanel guiStartPanel = new GuiStartPanel(this);
       // setMainPanel(guiStartPanel);
    }

    public JFrame getMainWindow() {
        return this.gameWindow;
    }

//    private void setMainPanel(JPanel panel) {
//        Container contentPane = gameWindow.getContentPane();
//        contentPane.removeAll();
//        contentPane.add(panel);
//        gameWindow.revalidate();
//    }


}
