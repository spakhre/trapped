package com.trapped.gui;

import com.gui.utility.GuiUtil;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class GuiIntroPanel extends GuiPanel {
    private JTextArea introText;

    //Rows and cols of text area to match the size of the map
    private static final int MAIN_PANEL_TEXT_AREA_ROWS = 90;
    private static final int MAIN_PANEL_TEXT_AREA_COLS = 90;
    private static JTextArea displayTextArea;

    /**
     * Constructor
     *
     * @param gui
     */
    public GuiIntroPanel(MainWindow gui) {
        super(gui);
        setLayout(new BorderLayout());
        this.add(createCenterPanel(), BorderLayout.CENTER);
        this.add(createSouthPanel(), BorderLayout.SOUTH);
        String[] filesArr = {"resources/textfile/greeting.txt", "resources/textfile/warning.txt", "resources/textfile/introstory.txt//"};
        GuiUtil.displayText(Arrays.asList(filesArr), displayTextArea, true, mainWindow);

    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel();
        panel.add(getDisplayScrollPane());
        return panel;
    }

    private JPanel createSouthPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.DARK_GRAY);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        //panel.add(GuiUtil.getBorderedPanel(createNamePanel()));
        //panel.add(GuiUtil.getBorderedPanel(getIntroScrollPane()));
        panel.add(GuiUtil.getBorderedPanel(createContinuePanel()));

        return panel;
    }

    /**
     * Creates main display text area.
     * @return
     */
    public JScrollPane getDisplayScrollPane() {
        JScrollPane displayTextAreaScroll = null;
        if (displayTextArea == null) {
            displayTextArea = new JTextArea(MAIN_PANEL_TEXT_AREA_ROWS, MAIN_PANEL_TEXT_AREA_COLS);
            displayTextArea.setEditable(false);
            displayTextArea.setFont(MainWindow.DISPLAY_AREA_FONT);
            displayTextArea.setBackground(Color.CYAN);
            displayTextArea.setForeground(Color.BLACK);
            displayTextArea.setLineWrap(true);
            displayTextArea.setWrapStyleWord(true);

            //Add scrollbar
            displayTextAreaScroll = GuiUtil.createScrollPane(displayTextArea);
        }
        return displayTextAreaScroll;
    }

    private JPanel createContinuePanel() {
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));

        JButton continueButton = new JButton("Continue");


        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panel = new GamePanel(mainWindow);
                mainWindow.setMainPanel(panel);
            }
        });

        namePanel.add(continueButton);

        return namePanel;
    }
}