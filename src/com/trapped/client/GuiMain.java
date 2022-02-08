package com.trapped.client;

import com.trapped.gui.controller.Gui;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class GuiMain {
    //    public static void main(String[] args) {
//        new Gui();
//    }
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalArgumentException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        new Gui();
    }
}