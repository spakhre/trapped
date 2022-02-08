package com.trapped.gui.controller;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.net.URL;

public class GuiBackGroundImageLabelPanel extends JPanel {

    private Gui gui;
    private JLabel backgroundImageLabel;

    public GuiBackGroundImageLabelPanel(Gui gui) {
        this.gui = gui;

        JPanel backgroundPanel = createBackgroundPanel(gui.getMainWindow());
        this.add(backgroundPanel);
    }

    private JPanel createBackgroundPanel(JFrame gameWindow) {
        gameWindow.setResizable(false);
        JPanel backgroundPanel = new JPanel(); //functions as a container to hold other components

        backgroundPanel.setBounds(0, 0, gameWindow.getWidth(), gameWindow.getHeight());

        //background label desigh
        backgroundImageLabel = new JLabel();
        backgroundImageLabel.setSize(backgroundPanel.getWidth(), backgroundPanel.getHeight());

        //setting background image
        URL resource = GuiBackGroundImageLabelPanel.class.getResource("/resources/image/images.jpg");
        ImageIcon backgroundImage = new ImageIcon(new ImageIcon(resource).getImage()
                .getScaledInstance(backgroundImageLabel.getWidth(), backgroundImageLabel.getHeight(), Image.SCALE_SMOOTH));
        backgroundImageLabel.setIcon(backgroundImage);
        backgroundPanel.add(backgroundImageLabel);
        return backgroundPanel;
    }

    protected Gui getGui() {
        return gui;
    }

    protected void setToBackgroundLabel(Component comp) {
        backgroundImageLabel.removeAll();
        backgroundImageLabel.setLayout(new GridBagLayout());
        backgroundImageLabel.add(comp);
    }
}
