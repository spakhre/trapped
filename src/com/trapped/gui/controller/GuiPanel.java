package com.trapped.gui.controller;

import javax.swing.JPanel;

public class GuiPanel extends JPanel {

    private GuiMainWindow guiMainWindow;

    public GuiPanel(GuiMainWindow guiMainWindow) {
        this.guiMainWindow=guiMainWindow;
    }
    protected GuiMainWindow getGuiMainWindow() {
        return guiMainWindow;
    }
}
