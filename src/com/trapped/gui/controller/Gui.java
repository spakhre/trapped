package com.trapped.gui.controller;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;

public class Gui {
    public static JFrame gameWindow; // for main game window
    private GuiPlayPanel guiPlayPanel;
    // Font and styling
    public static final Font btnFont = new Font("Times New Roman", Font.BOLD, 10); // ORIGINAL

    /**
     * CONSTRUCTOR.
     */
    public Gui() {
        gameWindow = new JFrame("Trapped: Try to escape!"); //game window design
        gameWindow.setSize(1200, 800);//sets the x-dimension and y-dimension of the frame

        GuiBackgroundImageLabelPanel backgroundPanel =
                new GuiBackgroundImageLabelPanel(this);

        gameWindow.add(backgroundPanel);

        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Exit out of application(default is hide application)
        gameWindow.setResizable(false); //prevent frame from being resized
        gameWindow.getContentPane().setBackground(new Color(100, 200, 200)); //set background collor

        GuiStartPanel guiStartPanel = new GuiStartPanel(this);
        setMainPanel(guiStartPanel);

        //make frame visible
        gameWindow.setVisible(true);
    }

    private void setMainPanel(JPanel panel) {
        Container contentPane = gameWindow.getContentPane();
        contentPane.removeAll();
        contentPane.add(panel);
        gameWindow.revalidate();
    }


    public JFrame getMainWindow() {
        return this.gameWindow;
    }

    public void createGameScreen() {
        guiPlayPanel = new GuiPlayPanel(this);
        setMainPanel(guiPlayPanel);
    }

}
