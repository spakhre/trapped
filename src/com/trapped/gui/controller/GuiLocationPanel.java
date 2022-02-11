package com.trapped.gui.controller;

import com.gui.utility.GuiUtil;
import com.trapped.player.Player;
import com.trapped.utilities.FileManager;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class GuiLocationPanel extends GuiPanel {
    private static final int IMAGE_WIDTH = Gui.GUI_WIDTH;
    private static final int IMAGE_HEIGHT = 300;

    /**
     * Constructor
     *
     * @param gui
     */
    public GuiLocationPanel(Gui gui) {
        super(gui);
        gui.setTitle(Player.location);

        setLayout(new BorderLayout());

        this.add(createNorthPanel(), BorderLayout.NORTH);

        this.add(createCenterPanel(), BorderLayout.CENTER);
//        this.add(new JLabel("NORTH"), BorderLayout.NORTH);
//        this.add(new JLabel("CENTER"), BorderLayout.CENTER);
//        this.add(new JLabel("SOUTH"), BorderLayout.SOUTH);

    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(getDescriptionLabel());
        return panel;
    }

    private JLabel getDescriptionLabel() {

        String fieldName = "furniture_desc";
        String description = null;
        try {
            description = getDescription(Player.location, fieldName);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        if(description == null){
            description = Player.location + " description";
        }
        JLabel descLabel = new JLabel(description);
        return descLabel;

    }
    private String getDescription(String key, String fieldName) {

        System.out.println("Current Location: " + Player.location);

        //keywords from JSON loaded to Map

        Map<String, ArrayList<String>> keywordMap = FileManager.loadJson("furniture_puzzles.json");
        System.out.println("keywordMap: " + keywordMap);

        for (Map.Entry<String, ArrayList<String>> entry: keywordMap.entrySet()){
            if(entry.getKey().equals(key)){
                System.out.println(">>>>> Key: " + entry.getKey());
                System.out.println(entry);
                System.out.println("Object class: "+
                entry.getValue().getClass().getName());
//                ArrayList<String> list = entry.getValue();

//                for(String text: list){
//                    return getValue(text, fieldName);
//                }
            }
        }

        Set<String> keySet = keywordMap.keySet();
        for (String k: keySet){
            if(k.equals(key)){
                System.out.println(">>>>> Key: " + k);
                System.out.println(keywordMap.get(k));
//                LinkedTreeMap<String , LinkedTreeMap> entry = keywordMap.get(k);

                //Object type: com.google.gson.internal.LinkedTreeMap
                Object entry = keywordMap.get(k);
                System.out.println(entry);
                System.out.println("Object class: "+
                        entry.getClass().getName());
//                ArrayList<String> list = entry.getValue();

//                for(String text: list){
//                    return getValue(text, fieldName);
//                }
            }
        }


        return null;
    }

    private JPanel createNorthPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        String filePath = "/image/" + Player.location + ".jpg";
        JLabel imageLabel = GuiUtil.getImageLabel(filePath, IMAGE_WIDTH, IMAGE_HEIGHT);


        panel.add(imageLabel);

        return panel;
    }
}
