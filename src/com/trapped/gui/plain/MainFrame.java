package com.trapped.gui.plain;

import com.trapped.player.Player;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public static final Font btnFont = new Font("Times New Roman", Font.BOLD, 10); // ORIGINAL
    public static final Font displayAreaFont = new Font("Times New Roman", Font.ITALIC, 18); // ORIGINAL
    public static final int GUI_WIDTH = 1200;
    public static final int GUI_HEIGHT = 800;
    JPanel imagePanel;
    JPanel textPanel;
    JPanel buttonsPanel;
    JPanel inventoryPanel;
    JLabel textArea = new JLabel();
    Player mainPlayer;

    public MainFrame(Player player) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setResizable(false);
        this.setSize(GUI_WIDTH, GUI_HEIGHT);
        this.setVisible(true);
        this.getContentPane().setBackground(new Color(0,0,0));
        createPanel();
        changeTextShow();
        mainPlayer = player;
    }

    private void createPanel() {
        imagePanel = new JPanel();
        textPanel = new JPanel();

        imagePanel.setBackground(Color.red);
        imagePanel.setBounds(0, 0, 1200, 400);
        textPanel.setBounds(0, 400, 1200, 300);
        textPanel.setBackground(Color.blue);
        textPanel.setForeground(Color.white);
        this.add(imagePanel);
        this.add(textPanel);
    }

    private void createTextPanel() {
        
    }

    private void createImagePanel() {

    }

    public void changeTextShow() {
        textArea.setText("TEST TEST TEST");
        textArea.setForeground(Color.white);
        textPanel.add(textPanel);
    }



}