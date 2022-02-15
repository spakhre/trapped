package com.trapped.gui;

import javax.swing.JPanel;
import java.awt.BorderLayout;

public class GuiPlayPanel extends BackgroundImageLabelPanel {
    private JPanel mainPanel;

    public GuiPlayPanel(MainWindow gui) {
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
