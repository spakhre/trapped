package com.trapped.gui.controller;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;

public class Gui extends JFrame {
    // Font and styling
    public static final Font btnFont = new Font("Times New Roman", Font.BOLD, 10); // ORIGINAL
    public static final Font displayAreaFont = new Font("Times New Roman", Font.ITALIC, 18); // ORIGINAL
    public static final int GUI_WIDTH = 1200;
    public static final int GUI_HEIGHT = 800;


    /**
     * CONSTRUCTOR.
     */
    public Gui() {
        super("Trapped: Try to escape!"); //game window design
        this.setSize(GUI_WIDTH, GUI_HEIGHT);//sets the x-dimension and y-dimension of the frame

        GuiBackgroundImageLabelPanel backgroundPanel =
                new GuiBackgroundImageLabelPanel(this);

        this.add(backgroundPanel);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Exit out of application(default is hide application)
//        this.setResizable(false); //prevent frame from being resized
        this.getContentPane().setBackground(new Color(100, 200, 200)); //set background collor

        GuiStartPanel guiStartPanel = new GuiStartPanel(this);
        setMainPanel(guiStartPanel);

        //make frame visible
        this.setVisible(true);
    }

    public void setMainPanel(JPanel panel) {
        Container contentPane = this.getContentPane();
        contentPane.removeAll();
        contentPane.add(panel);
        this.revalidate();
    }

    public void createGameScreen() {
        GuiIntroPanel panel = new GuiIntroPanel(this);
        setMainPanel(panel);
    }
}
