package com.trapped.gui.plain;

import com.trapped.player.Player;
import com.trapped.utilities.Puzzle;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class MainFrame extends JFrame {
    public static final Font btnFont = new Font("Times New Roman", Font.BOLD, 10); // ORIGINAL
    public static final Font displayAreaFont = new Font("Times New Roman", Font.ITALIC, 18); // ORIGINAL
    public static final int GUI_WIDTH = 1200;
    public static final int GUI_HEIGHT = 800;
    JPanel imagePanel;
    JPanel textPanel;
    JPanel buttonsPanel;
    JPanel inventoryPanel;
    JTextArea textArea;
    Player player = Player.getInstance();

    public MainFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setResizable(false);
        this.setSize(GUI_WIDTH, GUI_HEIGHT);
        createPanel();
        this.setVisible(true);
    }

    private void createPanel() {
       createTextPanel();
       createImagePanel();
       createButtonsPanel();
       createInventoryPanel();
    }

    private void createTextPanel() {
        textPanel = new JPanel();
        textPanel.setBounds(0, 0, 400, 400);
        textPanel.setBackground(Color.blue);
        textPanel.setForeground(Color.white);

        textArea = new JTextArea();
        textArea.setBounds(textPanel.getBounds());
        textArea.setFont(displayAreaFont);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        // get intro location description
       Map<String, Object> currentLoc = Puzzle.MAP.get(player.getLocation());
       String desc = (String) currentLoc.get("furniture_desc");
       changeTextShow(desc);

        textPanel.add(textArea);
        this.add(textPanel);
    }

    private void createImagePanel() {
        imagePanel = new JPanel();
        imagePanel.setBounds(400, 0, 800, 400);
        imagePanel.setBackground(Color.yellow);
        imagePanel.setForeground(Color.white);
        this.add(imagePanel);
    }

    private void createButtonsPanel() {
        buttonsPanel = new JPanel();
        buttonsPanel.setBounds(0, 400, 400, 400);
        buttonsPanel.setBackground(Color.black);
        buttonsPanel.setForeground(Color.white);
        this.add(buttonsPanel);
    }

    private void createInventoryPanel() {
        inventoryPanel = new JPanel();
        inventoryPanel.setBounds(400, 400, 800, 400);
        inventoryPanel.setBackground(Color.gray);
        inventoryPanel.setForeground(Color.white);
        this.add(inventoryPanel);
    }



    public void changeTextShow(String text) {
        String showText = "Location: "+ player.getLocation() +"\n\n" +text;
        textArea.setText(showText);
    }



}