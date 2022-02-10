package com.trapped.gui.controller;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuiStartPanel extends GuiBackgroundImageLabelPanel {
    public GuiStartPanel(Gui gui) {
        super(gui);

        //start panel design

        JPanel startPanel = createStartPanel();
        // exit panel design
        JPanel exitPanel = createExitPanel();

        JPanel btnPanel = new JPanel();

        btnPanel.add(startPanel);
        btnPanel.add(exitPanel);

        setToBackgroundLabel(btnPanel);
    }

    private JPanel createStartPanel() {
        JPanel startPanel = new JPanel();
        startPanel.setOpaque(false); //content pane opaque is false, has no effect

        Dimension dimension = new Dimension(200, 300);
        JButton startButton = createStartButton(dimension);
        startPanel.add(startButton);
        startButton.setFocusPainted(false);

        return startPanel;
    }

    private JPanel createExitPanel() {
        JPanel exitPanel = new JPanel();
        exitPanel.setBounds(400, 400, 200, 300);
        exitPanel.setOpaque(false); //content pane opaque is false, has no effect

        Dimension dimension = new Dimension(400, 300);
        JButton exitButton = createExitButton(dimension);
        exitPanel.add(exitButton);
        exitButton.setFocusPainted(false);

        return exitPanel;
    }

    private JButton createStartButton(Dimension dimension) {
        JButton startButton = new JButton(" START ");

        startButton.setSize(dimension);
        startButton.setFont(Gui.btnFont);
        startButton.setOpaque(false);
        startButton.setForeground(Color.BLACK);
        startButton.setBackground(Color.GRAY);

        //adding event listener
        TitleScreen startScreen = new TitleScreen();
        startButton.addActionListener(startScreen);
        return startButton;
    }

    private JButton createExitButton(Dimension dimension) {
        JButton exitButton = new JButton(" EXIT ");
        exitButton.setSize(dimension);
        exitButton.setFont(Gui.btnFont);
        exitButton.setOpaque(false);
        exitButton.setForeground(Color.BLACK);
        exitButton.setBackground(Color.GRAY);

        //adding exit event listener

        ExitScreen exitScreen = new ExitScreen();
        exitButton.addActionListener(exitScreen);
        return exitButton;
    }

    // action to perform when start button is clicked
    public class TitleScreen implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //Create Game Screen
//            getGui().createGameScreen();
        }
    }

    //action to execute when exit button is clicked

    public class ExitScreen implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //JOptionPane.showMessageDialog(GuiStartPanel.this.getGui(), "Good Bye!");
            System.exit(0);
        }
    }
}
