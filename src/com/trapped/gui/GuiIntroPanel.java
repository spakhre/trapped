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
            displayTextArea.setFont(MainWindow.displayAreaFont);
            displayTextArea.setBackground(Color.CYAN);
            displayTextArea.setForeground(Color.BLACK);
            displayTextArea.setLineWrap(true);
            displayTextArea.setWrapStyleWord(true);

            //Add scrollbar
            displayTextAreaScroll = GuiUtil.createScrollPane(displayTextArea);
        }
        return displayTextAreaScroll;
    }
//
//
//    /**
//     * Creates main display text area.
//     * @return
//     */
//    public JScrollPane getIntroScrollPane() {
//        JScrollPane introTextScroll = null;
//        if (introText == null) {
//            introText = new JTextArea(12, MAIN_PANEL_TEXT_AREA_COLS);
//            introText.setEditable(false);
//            introText.setFont(displayAreaFont);
//            introText.setBackground(Color.CYAN);
//            introText.setForeground(Color.BLACK);
//            introText.setLineWrap(true);
//            introText.setWrapStyleWord(true);
//
//            //Add scrollbar
//            introTextScroll = GuiUtil.createScrollPane(introText);
//        }
//        return introTextScroll;
//    }


//    private void displayIntroText(String name) {
//        List<String> lines = new ArrayList<>();
//
//        lines.add("Hello " + name + "!");
//
//        GuiUtil.displayText(lines, Arrays.asList("resources/textfile/introstory.txt"), introText, true, getGuiMainWindow());
//    }
//
//    private JPanel createNamePanel() {
//        JPanel namePanel = new JPanel();
//        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
//
//        JTextField nameTextField = new JTextField(15);
//        JButton submitButton = new JButton("Submit");
//
//        submitButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String name = nameTextField.getText().trim();
//                if(name.isEmpty() || name.isBlank()){
//                    JOptionPane.showMessageDialog(getGuiMainWindow(), "Invalid name.");
//                    return;
//                }
//                displayIntroText(name);
//            }
//        });
//
//        namePanel.add(new JLabel("Enter Name:"));
//        namePanel.add(nameTextField);
//        namePanel.add(submitButton);
//
//        return namePanel;
//    }

    private JPanel createContinuePanel() {
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));

        JButton continueButton = new JButton("Continue");
       // JButton quitButton = new JButton("Quit");

//        quitButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                //JOptionPane.showMessageDialog(getGui(), "Good Bye!");
//                System.exit(0);
//            }
//        });


        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panel = new GamePanel(mainWindow);
                mainWindow.setMainPanel(panel);
            }
        });

        namePanel.add(continueButton);
       // namePanel.add(quitButton);

        return namePanel;
    }
}
