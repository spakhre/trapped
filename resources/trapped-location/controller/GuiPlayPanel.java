package com.trapped.gui.controller;

import javax.swing.JPanel;
import java.awt.BorderLayout;

public class GuiPlayPanel extends GuiBackgroundImageLabelPanel {
    private JPanel mainPanel;

    public GuiPlayPanel(Gui gui) {
        super(gui);
        setToBackgroundLabel(createMainPanel());
    }

    private JPanel createMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setOpaque(false);
        return mainPanel;
    }

}
