package com.trapped.gui;

import com.gui.utility.GuiUtil;
import com.trapped.player.Inventory;
import com.trapped.player.Player;
import com.trapped.utilities.Puzzle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class GamePanel extends GuiPanel {

    public static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 20); // ORIGINAL

    private static final int IMAGE_WIDTH = 800;
    private static final int IMAGE_HEIGHT = 400;

    private static final int ITEM_IMAGE_WIDTH = 50;
    private static final int ITEM_IMAGE_HEIGHT = 50;

    JLayeredPane layeredPane;
    JPanel imagePanel;
    JPanel textPanel;
    JPanel buttonsPanel;
    JPanel inventoryPanel;
    JPanel keypadPanel;
    JTextArea textArea;
    JLabel imageLabel;

    Player player = Player.getInstance();

    /**
     * Constructor.
     *
     * @param mainWindow
     */
    public GamePanel(MainWindow mainWindow) {
        super(mainWindow);

        this.setLayout(null);
        this.setSize(MainWindow.GUI_WIDTH, MainWindow.GUI_HEIGHT);

        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        layeredPane.setSize(MainWindow.GUI_WIDTH, MainWindow.GUI_HEIGHT);
        this.add(layeredPane);
        createPanel();

        setLocationDetails();
    }

    private void setLocationDetails() {

        // set location description
        Map<String, Object> currentLoc = Puzzle.MAP.get(player.getLocation());
        String desc = (String) currentLoc.get("furniture_desc");
        textArea.setText(desc);

        // set location image
        setLocationImage();
    }

    private void createPanel() {
        createKeypadPanel();
        createTextPanel();
        createImagePanel();
        createButtonsPanel();
        createInventoryPanel();
    }

    private void createTextPanel() {
        textPanel = new JPanel();
        textPanel.setBounds(0, 0, 400, 400);
        textPanel.setBackground(Color.WHITE);

        textArea = new JTextArea();
        textArea.setBounds(textPanel.getBounds());
        textArea.setFont(MainWindow.DISPLAY_AREA_FONT);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        textPanel.add(textArea);
        layeredPane.add(textPanel);
    }

    private void createImagePanel() {
        imagePanel = new JPanel();
        imagePanel.setBounds(400, 0, 800, 400);

        layeredPane.add(imagePanel);
    }

    private void setLocationImage() {
        String filePath = "/image/" + Player.getLocation() + ".jpg";
        imageLabel = GuiUtil.getImageLabel(filePath, IMAGE_WIDTH, IMAGE_HEIGHT);

        //Set bounds (x, y, width, height) of the image same as that of the imagePanel
        imageLabel.setBounds(imagePanel.getBounds());
        //remove old image
        imagePanel.removeAll();
        //add new image
        imagePanel.add(imageLabel);
    }

    private void createButtonsPanel() {
        buttonsPanel = new JPanel();
        buttonsPanel.setBounds(0, 400, 400, 400);

        JButton leftB = new JButton("Left");
        leftB.setFont(BUTTON_FONT);
        JButton rightB = new JButton("Right");
        rightB.setFont(BUTTON_FONT);

        JButton inspectB = new JButton("Inspect Room");
        inspectB.setFont(BUTTON_FONT);

        JButton helpB = new JButton("Help");
        helpB.setFont(BUTTON_FONT);

        JButton quitB = new JButton("Quit");
        quitB.setFont(BUTTON_FONT);

        JPanel p1 = new JPanel();
        p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
        p1.add(leftB);
        p1.add(new JLabel("     "));
        p1.add(rightB);

        JPanel p2 = new JPanel();
        p2.setLayout(new BoxLayout(p2, BoxLayout.X_AXIS));
        p2.add(inspectB);
        p2.add(new JLabel("     "));
        p2.add(helpB);

        JPanel p3 = new JPanel();
        p3.setLayout(new BoxLayout(p3, BoxLayout.X_AXIS));
        p3.add(quitB);

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

        leftB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.moveDirection("left");
                setLocationDetails();
            }
        });

        rightB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.moveDirection("right");
                setLocationDetails();
            }
        });
        inspectB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inspectLocation();
                if (!"door".equals(player.getLocation())){
                    player.solvePuzzle(player.getLocation());
                    displayInventoryDetails();
                } else {
//                    KeypadPane
//                    createKeypadPanel();
                    keypadPanel.setVisible(true);

                }
            }
        });

        helpB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //pending
            }
        });
        quitB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainWindow, "Good Bye!");
                System.exit(0);
            }
        });

        layeredPane.add(buttonsPanel);
    }

    private void createKeypadPanel(){
        keypadPanel = new KeypadPanel(mainWindow);
        keypadPanel.setVisible(false);
        layeredPane.add(keypadPanel);
    }

    private void inspectLocation() {
        List<String> items = JsonMap.getFurnitureItems(player.getLocation());
        System.out.println(items);
        if (items.isEmpty()) {
            JOptionPane.showMessageDialog(mainWindow, "No items found at: " + player.getLocation());
            return;
        }

        String item = items.get(0);

        Inventory inventory = player.getInventory();
        if(inventory.hasItem(item)) {
            JOptionPane.showMessageDialog(mainWindow, "You already have item: " + item + " from " + player.getLocation());
            return;
        }

        int response = JOptionPane.showConfirmDialog(mainWindow, "Item " + item +
                " found in '" + player.getLocation() + "'. Do you want to add it to your inventory?", "CONFIRM", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.NO_OPTION) {
            return;
        }
        player.getInventory().addItem(item);
    }

    private JLabel createItemImageLabel(String item) {
        String filePath = "/image/items/" + item + ".jpg";
//        JLabel imageLabel = GuiUtil.getImageLabel(item, filePath, ITEM_IMAGE_WIDTH, ITEM_IMAGE_HEIGHT, SwingConstants.TOP);
        JLabel imageLabel = GuiUtil.getImageLabel(filePath, ITEM_IMAGE_WIDTH, ITEM_IMAGE_HEIGHT);
        imageLabel.setText(item);
        imageLabel.setHorizontalTextPosition(JLabel.CENTER);
        imageLabel.setVerticalTextPosition(JLabel.BOTTOM);
        return imageLabel;
    }

    private JButton createButtonImage(String item) {
        String filePath = "/image/items/" + item + ".jpg";
//        JLabel imageLabel = GuiUtil.getImageLabel(item, filePath, ITEM_IMAGE_WIDTH, ITEM_IMAGE_HEIGHT, SwingConstants.TOP);
        JButton btn = GuiUtil.getButtonImage(filePath, ITEM_IMAGE_WIDTH, ITEM_IMAGE_HEIGHT);
        btn.setText(item);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void createInventoryPanel() {
        inventoryPanel = new JPanel();
        inventoryPanel.setBounds(400, 400, 800, 400);
        inventoryPanel.setBackground(Color.white);

        JLabel label = new JLabel("Inventory List:");
        label.setBounds(inventoryPanel.getBounds());
        inventoryPanel.add(label);
        layeredPane.add(inventoryPanel);
    }

    public void displayInventoryDetails() {
        List<String> invList = player.getInventory().getInvList();
        if ((invList == null) || (invList.isEmpty())) {
            return;
        }

        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.X_AXIS));
        itemsPanel.setBackground(Color.white);

        //Add image labels
        for (String inventoryItem : invList) {
            itemsPanel.add(new JLabel("      "));
            JPanel p = createItemImagePanel(inventoryItem);
            itemsPanel.add(p);
        }

        JPanel panelY = new JPanel();
        panelY.setLayout(new BoxLayout(panelY, BoxLayout.Y_AXIS));
        panelY.setBackground(Color.white);

        //add space at the top of buttons
        panelY.add(new JLabel(" "));

        //add title label
        JLabel titleLabel = new JLabel("Inventory List:");
        panelY.add(titleLabel);
        panelY.add(new JLabel("  "));
        panelY.add(new JLabel("  "));
        panelY.add(new JLabel("  "));

        //add image panel
        panelY.add(itemsPanel);
        panelY.add(new JLabel(" "));
        panelY.add(new JLabel(" "));
        panelY.add(new JLabel(" "));

        //Set bounds (x, y, width, height) of panelY same as that of the inventoryPanel
        panelY.setBounds(inventoryPanel.getBounds());

        //Clear OLD Inventory images
        inventoryPanel.removeAll();
        //Add NEW Inventory images
        inventoryPanel.add(panelY);

        //refresh main window
        mainWindow.revalidate();
    }

    private JPanel createItemImagePanel(String inventoryItem) {
        JPanel p = new JPanel();
        p.setBackground(Color.white);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JButton btn = createButtonImage(inventoryItem);


        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map<String, String> result = player.solveUseTool(inventoryItem);
                String desc = result.get("puzzleDescription");
                String error = result.get("error");
                textArea.setText(error != null ? error : desc);
                displayInventoryDetails();
            }
        });
        p.add(btn);
        p.add(new JLabel("    "));
        return p;
    }
}