package com.trapped.gui.controller;

import javax.swing.JPanel;

public class GuiPanel extends JPanel {

    private Gui gui;

    public GuiPanel(Gui gui) {
        this.gui=gui;
    }
    protected Gui getGui() {
        return gui;
    }
}
