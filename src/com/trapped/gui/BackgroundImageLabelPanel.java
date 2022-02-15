package com.trapped.gui;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.net.URL;

public class BackgroundImageLabelPanel extends GuiPanel {
    private JLabel backgroundImageLabel;

    public BackgroundImageLabelPanel(MainWindow gui) {
        super(gui);
        JPanel backgroundPanel = createBackgroundPanel(gui);
        this.add(backgroundPanel);
    }

    private JPanel createBackgroundPanel(JFrame gameWindow) {
        gameWindow.setResizable(false);
        JPanel backgroundPanel = new JPanel();

        backgroundPanel.setBounds(0, 0, gameWindow.getWidth(), gameWindow.getHeight());

        // background label design
        backgroundImageLabel = new JLabel();
        backgroundImageLabel.setSize(backgroundPanel.getWidth(), backgroundPanel.getHeight());

        // setting background image
        URL imageURL = BackgroundImageLabelPanel.class.getResource("/image/image.jpg");
        ImageIcon backgroundImage = new ImageIcon(new ImageIcon(imageURL).getImage()
                .getScaledInstance(backgroundImageLabel.getWidth(), backgroundImageLabel.getHeight(), Image.SCALE_SMOOTH));
        backgroundImageLabel.setIcon(backgroundImage);
        backgroundPanel.add(backgroundImageLabel);
        return backgroundPanel;
    }

    protected void setToBackgroundLabel(Component comp) {
        backgroundImageLabel.removeAll();
        backgroundImageLabel.setLayout(new GridBagLayout());
        backgroundImageLabel.add(comp);
    }
}
