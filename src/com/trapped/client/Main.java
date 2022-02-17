package com.trapped.client;

import com.trapped.gui.MainWindow;

import java.io.Serializable;

public class Main implements Serializable {
    public static MainWindow mainWindow;
    public static void main(String[] args) {
         mainWindow = new MainWindow();
    }
}
