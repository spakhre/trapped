package com.trapped.gui;

import javax.swing.JPanel;

public class GuiPanel extends JPanel {

    public MainWindow mainWindow;

    public GuiPanel(MainWindow mainWindow) {
        this.mainWindow=mainWindow;
    }
    protected MainWindow getMainWindow() {
        return mainWindow;
    }
}
