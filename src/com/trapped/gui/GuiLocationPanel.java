package com.trapped.gui;

import com.google.gson.internal.LinkedTreeMap;
import com.gui.utility.GuiUtil;
import com.gui.utility.TextCollector;
import com.trapped.player.Player;
import com.trapped.utilities.FileManager;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Set;

public class GuiLocationPanel extends GuiPanel {
    private Player player = Player.getInstance();
    private TextCollector textCollector = TextCollector.getInstance();

    private static final int IMAGE_WIDTH = MainWindow.GUI_WIDTH;
    private static final int IMAGE_HEIGHT = 300;
    private JTextArea inventoryText;
    private JTextArea availableCommandsText;

    //Font and Styling
    public static final Font DESCRIPTION_FONT = new Font("Times New Roman", Font.BOLD, 24);


    /**
     * Constructor
     *
     * @param gui
     */
    public GuiLocationPanel(MainWindow gui) {
        super(gui);
        gui.setTitle(Player.location);

        setLayout(new BorderLayout());

        this.add(createNorthPanel(), BorderLayout.NORTH);
        this.add(createCenterPanel(), BorderLayout.CENTER);
        this.add(createSouthPanel(), BorderLayout.SOUTH);

        displayInventory();
        displayAvailableCommand();

    }

    private void displayInventory() {
        inventoryText.setText("Inventory");
    }

    private void displayAvailableCommand() {
        availableCommandsText.setText("Available Commands");
    }

    private JPanel createNorthPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        String filePath = "/image/" + Player.location + ".jpg";
        JLabel imageLabel = GuiUtil.getImageLabel(filePath, IMAGE_WIDTH, IMAGE_HEIGHT);


        panel.add(imageLabel);

        return panel;
    }

    /**
     * Creating a Center Panel
     * @return
     */
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new Label(" "));
        panel.add(new JLabel(" "));

        panel.add(getDescriptionLabel());
        panel.add(new Label(" "));
        panel.add(new JLabel(" "));

        return panel;
    }

    private JLabel getDescriptionLabel() {

        String parentKey = Player.location;
        String childKey = "furniture_desc";
        Object value = getChildKeyForParentKey(parentKey, childKey);

        //Expected type of value is String
        String description = String.valueOf(value);
        JLabel label = new JLabel(description);
        label.setFont(DESCRIPTION_FONT);
        return label;
    }

    /**
     * Example of Mapping in JSON:
     * parentKey is bed or currentLocation
     * childKey is "furniture_desc" / "name" / "furniture_items".
     *
     * So, to get description, must need to pass currentLocation and furniture_desc
     * So, to get items, we need to pass, currentLocation and "furniture_items"
     *
     * The return value could be of either String or ArrayList or Something else,
     * Hence, the return type is generic 'Object'
     * Must need to cast to right type.
     * @param parentKey
     * @param childKey
     * @return
     */

    private Object getChildKeyForParentKey(String parentKey, String childKey) {

        //any possible object values(Can be String, Array or anything)
        Map<String, ?> keywordMap = FileManager.fromJsonAsMap("furniture_puzzles.json");

        Set<String> keySet = keywordMap.keySet();
        for(String k: keySet) {
            if(k.equals(parentKey)) {
                //get value map for given key(for example, key = bed
                LinkedTreeMap<String, LinkedTreeMap> entry = (LinkedTreeMap<String, LinkedTreeMap>) keywordMap.get(k);
                Object childKeyValue = entry.get(childKey);
                return childKeyValue;
            }
        }
        return null;
    }

    /**
     *
     * @return
     */
    private JPanel createSouthPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(GuiUtil.getBorderedPanel(createCommandPanel()));
        panel.add(new JLabel(" "));
        panel.add(GuiUtil.getBorderedPanel(getInventoryScrollPane()));
        panel.add(new JLabel(" "));
        panel.add(GuiUtil.getBorderedPanel(getAvailableCommandsScrollPane()));
        return panel;
    }

    private JPanel createCommandPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));


        JTextField commandTextField = new JTextField(15);
        JButton submitButton = new JButton("Submit");

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = commandTextField.getText().trim();
                if(command.isEmpty() || command.isBlank()) {
                    JOptionPane.showMessageDialog( getMainWindow(), "Invalid Command!!!!");
                }
//                TextCollector.executeCommand(command);
                executeCommand(command);
            }
        });
        panel.add(new JLabel("Enter Command"));
        panel.add(commandTextField);
        panel.add(submitButton);

        return panel;
    }

    public JScrollPane getInventoryScrollPane() {
        JScrollPane scroll = null;
        if(inventoryText == null) {
            inventoryText = new JTextArea(10, 25);
            inventoryText.setEditable(false);
            inventoryText.setFont(MainWindow.displayAreaFont);
            inventoryText.setBackground(Color.CYAN);
            inventoryText.setForeground(Color.BLACK);
            inventoryText.setWrapStyleWord(true);

            //add scrollbar
            scroll = GuiUtil.createScrollPane(inventoryText);
        }
        return scroll;
    }

    /**
     * Creates AvailableCommands display text area
     * @return
     */

    public JScrollPane getAvailableCommandsScrollPane() {
        JScrollPane scroll = null;
        if(availableCommandsText == null) {
            availableCommandsText = new JTextArea(10, 30);
            availableCommandsText.setEditable(false);
            availableCommandsText.setFont(MainWindow.displayAreaFont);
            availableCommandsText.setBackground(Color.CYAN);
            availableCommandsText.setForeground(Color.BLACK);
            availableCommandsText.setLineWrap(true);
            availableCommandsText.setWrapStyleWord(true);

            //Add Scrollbar

            scroll = GuiUtil.createScrollPane(availableCommandsText);

        }
        return scroll;
    }

    private void executeCommand(String command) {
        System.out.println("Command: " + command);
        textCollector.executeCommand(command);
    }
}