package com.trapped.player;

import com.trapped.utilities.Prompts;
import com.trapped.utilities.Sounds;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Inventory {
    private static final Inventory INVENTORY = new Inventory();

    List<String> invList = new ArrayList<>();
    public static List<String> rewardsList = List.of(new String[]{"crowbar", "key", "a piece of paper with number 104"});

    // Singleton ctor
    private Inventory() {};

    public static Inventory getInstance(){
        return INVENTORY;
    }

    // check current inventory
    public List<String> checkInv() {
        System.out.println("Your current inventory: " + invList);
        return invList;
    }

    // pickup item method.
    public void pickUpItem(String location, Map<String, Map<String, Object>> furniturePuzzleMap) {
        Map<String, Object> furniture;
        String locationItem;

        if (furniturePuzzleMap.get(location) == null) {
            System.out.println("Invalid selection, Location is NULL");
        }
        else {
            furniture = furniturePuzzleMap.get(location);
            locationItem = ((ArrayList<String>) furniture.get("furniture_items")).get(0);
            // if inventory is full. player needs to drop an item
            if (locationItem.isEmpty()) {
                System.out.println(location + " is empty. Nothing can be added.");
            }
            //if furniture has an item available to be picked up
            else if (!invList.contains(locationItem)) {
                System.out.println("\nDo you want to add " + locationItem + " to your inventory? [Y/N]");
                String response = Prompts.getStringInput();
                switch (response){
                    case "Y":
                    case "y":
                        checkInvLimit();
                        addItem(locationItem);
                        break;
                    case "N":
                    case "n":
                        System.out.println("You did not pick up anything from " + location);
                        break;
                    default:
                        System.out.println("Sorry, I don't understand your entry. You did not pick anything from " + location);
                }
            }
        }
    }

    // Check Inventory size to ensure only 5 items
    // loop until 5 items maximum
    public void checkInvLimit(){
        if (invList.size() >= 5) {
            System.out.println("Inventory cannot take more than 5 items. Please drop one item.");
            String droppedItem;
            do {
                droppedItem = dropSelect();
            }
            while (droppedItem == null || droppedItem.isEmpty());
        }
    }

    // Drop item -- provide current inventory first then let player pick.
    // Also used when inventory is full
    public String dropSelect() {
        System.out.println("Your inventory: " + invList);
        System.out.println("Which item would you like to drop? Please enter item name: ");
        String selection = "";
        selection = drop(selection);
        return selection;
    }

    // Drop a specific item
    // Used for input "drop xxx"
    public String dropSpecificItem(String item) {
        drop(item);
        return item;
    }

    // Loop for dropping an item
    // Ensures the item is in the inventory and then not required
    public String drop(String selection) {
        // while inventory DOES NOT contain item OR item is a reward
        while (!invList.contains(selection) || rewardsList.contains(selection)) {
            selection = Prompts.getStringInput().toLowerCase();
            if (!invList.contains(selection)){
                System.out.println("Sorry, you cannot drop " + selection + ". It is not in your inventory.\n" +
                        "Please try again -->");
            }
            else if(rewardsList.contains(selection)){
                System.out.println("Sorry, you cannot drop " + selection + ". It is a required item to complete the game.\n" +
                        "Please try again -->");
            }
        }
        // dropSpecific ignores above while-loop
        // DROP actions
        invList.remove(selection);
        Sounds.playSounds("drop.wav",1000);
        System.out.println(selection + " has been dropped from your inventory.");
        return selection;
    }

    // Adding an item actions and sound
    // DOES NOT verify item passed here is a proper inventory item
    public String addItem(String item){
        invList.add(item);
        System.out.println(item + " has been added to your inventory");
        Sounds.playSounds("pick.wav",1000);
        return item;
    }

    // getters and setters
    public List<String> getInvList() {
        return invList;
    }
}