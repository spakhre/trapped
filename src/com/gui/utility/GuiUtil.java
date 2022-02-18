package com.gui.utility;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class GuiUtil {
    /**
     * Returns ScrollPane with the given view component.
     * Horizontal as needed
     * @param view
     * @return
     */
    public static JScrollPane createScrollPane(Component view) {

        JScrollPane scroll = new JScrollPane(view, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setAlignmentX(Component.CENTER_ALIGNMENT);
        return scroll;
    }

    /**
     * Adds the given component to a JPanel with border.
     *
     */
    public static JPanel getBorderedPanel(JComponent comp) {
        JPanel panel = new JPanel();
        Border blueLine = BorderFactory.createLineBorder(Color.RED);
        Border empty = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        CompoundBorder compound = BorderFactory.createCompoundBorder(blueLine, empty);
        panel.setBorder(compound);

        // Add component to the panel
        panel.add(comp);
        return panel;
    }

    /**
     *
     * @param jTextArea
     * @param message
     */

    public static void setMessageSlowly(JTextArea jTextArea, String message, Timer subsequentTimer) {
        CharacterDisplay typewriter = new CharacterDisplay(jTextArea, " "+message, subsequentTimer);
        typewriter.startDisplay();
    }

    /**
     *
     * Creates a label with image without text
     * @param imagePath
     * @param width
     * @param height
     * @return
     */
    public static JLabel getImageLabel(String imagePath, int width, int height) {
        Image img = transformImage(createImageIcon(imagePath, ""), width, height);
        ImageIcon icon = new ImageIcon(img); //transform it back
        JLabel imageLabel = new JLabel("", icon, JLabel.CENTER);
        return imageLabel;
    }

    public static JButton getButtonImage(String imagePath, int width, int height) {
        Image img = transformImage(createImageIcon(imagePath, ""), width, height);
        ImageIcon icon = new ImageIcon(img); //transform it back
        JButton imageButton = new JButton();
        imageButton.setIcon(icon);
        imageButton.setHorizontalTextPosition(JButton.CENTER);
        imageButton.setVerticalTextPosition(JButton.BOTTOM);
        return imageButton;
    }

    /**
     * Returns an ImageIcon or null if the path is not valid
     * @param path
     * @param description
     * @return
     */
    public static ImageIcon createImageIcon(String path, String description) {
        URL imgURL = GuiUtil.class.getResource(path);
        if(imgURL!=null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find the file path " + path);
            return null;
        }
    }

    /**
     * Transforms the given image icon's to scaled instance based on the given width and height
     * @param icon
     * @param width
     * @param height
     * @return
     */
    public static Image transformImage(ImageIcon icon, int width, int height) {
        Image image = icon.getImage(); // transform it
        Image newImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return newImage;
    }

    public static void displayText(List<String> filesList, JTextArea jTextArea, boolean append, Component mainWindow, Timer subsequentTimer) {
        displayText(null, filesList, jTextArea, append, mainWindow, subsequentTimer);
    }

    public static void displayText(List<String> lines, List<String> filesList, JTextArea jTextArea, boolean append, Component mainWindow, Timer subsequentTimer) {
        if(!append) {
            //clear previous text
            jTextArea.setText("");
        }
        try {
            if(lines == null){
                lines = new ArrayList<>();
            }
            else{
                lines.add("\n\n");
            }
            for (String filePath: filesList) {
                List<String> listLines = Files.readAllLines(Path.of(filePath));
                lines.addAll(listLines);
                lines.add("\n\n");
            }
            String text = String.join("\n", lines);
            GuiUtil.setMessageSlowly(jTextArea, text, subsequentTimer);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainWindow, "Error reading intro text.");
            return;
        }
    }
}
