package com.trapped.gui.controller;

import javax.swing.JFrame;
import java.awt.Color;

public class Gui {
    public static JFrame gameWindow; //for main game window

    public Gui() {
        gameWindow = new JFrame("Trapped: NoWhere to Escape!"); //game window design
        gameWindow.setSize(1200, 800); //sets the x-dimension and y-dimension of the frame
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Exit out of application
        gameWindow.setResizable(false); //prevent frame from being resized
        gameWindow.getContentPane().setBackground(new Color(100, 200, 200)); //set background color
        gameWindow.setVisible(true); //make frame visible

        GuiBackGroundImageLabelPanel backgroundPanel = new GuiBackGroundImageLabelPanel(this);

        gameWindow.add(backgroundPanel);
    }

    public JFrame getMainWindow() {
        return this.gameWindow;
    }

}
