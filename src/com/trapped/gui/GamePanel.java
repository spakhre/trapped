package com.trapped.gui;

import com.gui.utility.GuiUtil;
import com.trapped.player.Player;
import com.trapped.utilities.Puzzle;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class GamePanel extends GuiPanel {
    //public static final Font btnFont = new Font("Times New Roman", Font.BOLD, 20); // ORIGINAL
    private static final Font displayAreaFont = new Font("Times New Roman", Font.ITALIC, 24); // ORIGINAL
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 20);
    //public static final int GUI_WIDTH = 1200;
    //public static final int GUI_HEIGHT = 800;
    private static final int IMAGE_WIDTH = 800;
    private static final int IMAGE_HEIGHT = 400;

    JPanel imagePanel;
    JPanel textPanel;
    JPanel buttonsPanel;
    JPanel inventoryPanel;
    JTextArea textArea;

    Player player = Player.getInstance();
    private JLabel imageLabel;

    public GamePanel(MainWindow mainWindow) {
        super(mainWindow);
        this.setLayout(null);
        this.setSize(MainWindow.GUI_WIDTH, MainWindow.GUI_HEIGHT);
        createPanel();

        setLocationDetails();

    }

    private void setLocationDetails() {
        //get intro location description
        //mainWindow.setTitle("Current Location: " + player.getLocation());

        Map<String, Object> currentLoc = Puzzle.MAP.get(player.getLocation());
        String desc = (String) currentLoc.get("furniture_desc");
        textArea.setText("Current Location: " + player.getLocation());
        textArea.setText(desc);
       // textPanel.add(textArea);
        setLocationImage();
    }

    private void createPanel() {
       createTextPanel();
       createImagePanel();
       createButtonsPanel();
       createInventoryPanel();
    }

    private void createTextPanel() {
        textPanel = new JPanel();
        textPanel.setBounds(0, 0, 400, 400);
        textPanel.setBackground(Color.WHITE);
        //textPanel.setForeground(Color.white);

        textArea = new JTextArea();
        textArea.setBounds(textPanel.getBounds());
        textArea.setFont(displayAreaFont);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

//        // get intro location description
//       Map<String, Object> currentLoc = Puzzle.MAP.get(player.getLocation());
//       String desc = (String) currentLoc.get("furniture_desc");
//       changeTextShow(desc);

        textPanel.add(textArea);
        this.add(textPanel);
    }

    private void createImagePanel() {
        imagePanel = new JPanel();
        imagePanel.setBounds(400, 0, 800, 400);
//        imagePanel.setBackground(Color.yellow);
//        imagePanel.setForeground(Color.white);
        this.add(imagePanel);
    }

    private void setLocationImage() {
        String filePath = "/image/" + Player.getLocation() + ".jpg";
        imageLabel = GuiUtil.getImageLabel(filePath, IMAGE_WIDTH, IMAGE_HEIGHT);

        //Set bounds (x, y, width, height) of the image same as that of the imagePanel
        imageLabel.setBounds(imagePanel.getBounds());
        //remove old image
        imagePanel.removeAll();
        imagePanel.add(imageLabel);
    }

    private void createButtonsPanel() {
        buttonsPanel = new JPanel();
        buttonsPanel.setBounds(0, 400, 400, 400);
//        buttonsPanel.setBackground(Color.black);
//        buttonsPanel.setForeground(Color.white);

        JButton leftButton = new JButton("Left");
        leftButton.setFont(BUTTON_FONT);

        JButton rightButton = new JButton("Right");
        rightButton.setFont(BUTTON_FONT);

        JButton inspectButton = new JButton("Inspect Room");
        inspectButton.setFont(BUTTON_FONT);

        JButton helpButton = new JButton("Help");
        helpButton.setFont(BUTTON_FONT);

        JButton quitButton = new JButton("Quit");
        quitButton.setFont(BUTTON_FONT);

        JPanel p1 = new JPanel();
        p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
        p1.add(leftButton);
        p1.add(new JLabel("    "));
        p1.add(rightButton);

        JPanel p2 = new JPanel();
        p2.setLayout(new BoxLayout(p2, BoxLayout.X_AXIS));
        p2.add(inspectButton);
        p2.add(new JLabel("     "));
        p2.add(helpButton);

        JPanel p3 = new JPanel();
        p3.setLayout(new BoxLayout(p3, BoxLayout.X_AXIS));
        p3.add(quitButton);

        JPanel panelY = new JPanel();
        panelY.setLayout(new BoxLayout(panelY, BoxLayout.Y_AXIS));

        //add space at the top of buttons
        panelY.add(new JLabel(" "));
        panelY.add(new JLabel(" "));
        panelY.add(new JLabel(" "));
        panelY.add(new JLabel(" "));
        panelY.add(p1);
        panelY.add(new JLabel(" "));
        panelY.add(new JLabel(" "));

        panelY.add(p2);
        panelY.add(new JLabel(" "));
        panelY.add(new JLabel(" "));

        panelY.add(p3);
        panelY.add(new JLabel(" "));

        //Set bounds (x, y, width, height) of the panelY same as that of the buttonsPanel
        panelY.setBounds(buttonsPanel.getBounds());
        buttonsPanel.add(panelY);

        leftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.moveDirection("left");
                setLocationDetails();
            }
        });

        rightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.moveDirection("right");
                setLocationDetails();
            }
        });

        inspectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //pending
            }
        });

        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //pending
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               int response = JOptionPane.showConfirmDialog(mainWindow, "Are you sure you'd like to quit the game right now? Your prgoress will not be saved.", "QuitDialog", JOptionPane.YES_NO_OPTION);
               if(response == JOptionPane.YES_OPTION) {
                   System.exit(0);
               } else
                   return;

            }
        });


        this.add(buttonsPanel);
    }

    private void createInventoryPanel() {
        inventoryPanel = new JPanel();
        inventoryPanel.setBounds(400, 400, 800, 400);
        inventoryPanel.setBackground(Color.gray);
        inventoryPanel.setForeground(Color.white);
        this.add(inventoryPanel);
    }

    public void changeTextShow(String text) {
        String showText = "Location: "+ player.getLocation() +"\n\n" +text;
        textArea.setText(showText);
    }
}