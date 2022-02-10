package com.gui.utility;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.Color;
import java.awt.Component;


public class GuiUtil {
    /**
     * Returns Scrollpane with the given view component.
     *
     * @param view
     * @return
     */
    public static JScrollPane createScrollPane(Component view) {
        JScrollPane helpTextAreaScroll;
        helpTextAreaScroll = new JScrollPane(view, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        helpTextAreaScroll.setAlignmentX(Component.CENTER_ALIGNMENT);
        return helpTextAreaScroll;
    }

    /**
     * Adds the given component to a JPanel with border.
     *
     */
    public static JPanel getBorderedPanel(JComponent comp) {
        JPanel p = new JPanel();
        Border blueLine = BorderFactory.createLineBorder(Color.RED);
        Border empty = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        CompoundBorder compound = BorderFactory.createCompoundBorder(blueLine, empty);
        p.setBorder(compound);

        // Add component to the panel
        p.add(comp);
        return p;
    }

    public static void setMessageSlowly(JTextArea jTextArea, String message) {
        CharacterDisplay typewriter = new CharacterDisplay(jTextArea, " "+message);
        typewriter.startDisplay();
    }
}
