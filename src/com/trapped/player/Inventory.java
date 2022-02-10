package com.trapped.player;

import com.trapped.utilities.Prompts;
import com.trapped.utilities.Sounds;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class Inventory {
    List<String> invList = new ArrayList<>();
    static List<String> rewardsList = List.of(new String[]{"crowbar", "key", "a piece of paper with number 104"});

    // CTOR
    public Inventory() {};

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
            if (invList.size() >= 5) {
                System.out.println("Inventory cannot take more than 5 items. Please drop one item.");
//                String droppedItem;
//                do {   droppedItem = dropItem();  }
//                while (droppedItem == null);
                String selection = Prompts.getStringInput();
                while (!invList.contains(selection.toLowerCase())) {
                    System.out.println("Sorry, the item you entered is not in inventory, please select again.");
                    selection = Prompts.getStringInput();
                }
                invList.remove(selection);
                Sounds.playSounds("drop.wav",1000);
                System.out.println(selection + " has been dropped from your inventory.");

                invList.add(locationItem);
                System.out.println(locationItem + " has been added to your inventory");
                Sounds.playSounds("pick.wav",1000);
            }
            // if inventory is not full
            else {
                //if furniture has no item available to be picked up
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
                            System.out.println(locationItem + " has been picked up and added to your inventory");
                            Sounds.playSounds("pick.wav", 1000);
                            invList.add(locationItem);
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
    }

    // Drop item -- will provide current inventory first then let player pick.
    // This method is also used when inventory is full and player being asked to drop an item.
    public String dropItem() {
        System.out.println("Your inventory: " + getInvList());
        System.out.println("Which item you'd like to drop? Please enter item name. ");

        String selection = Prompts.getStringInput(); // scan.nextLine();

        if (invList.contains(selection.toLowerCase())){
            if (rewardsList.contains(selection)) {
                System.out.println("Sorry, you cannot drop " + selection + ". It was automatically added by your solved puzzle and can not be dropped.");
                return selection + " NOT REMOVED";
            }
            else {
                invList.remove(selection);
                Sounds.playSounds("drop.wav",1000);
                System.out.println(selection + " has been dropped from your inventory.");
                return selection;
            }
        }
        else {
            System.out.println("Sorry, you cannot drop "+selection +". It is not in your inventory");
            return selection + " NOT IN INVENTORY";
        }
    }

    // Drop a specific item - this will be used when player input "drop xxx"
    public String dropSpecificItem(String item) {
        if (invList.contains(item.toLowerCase())) {
            if (rewardsList.contains(item)) {
                System.out.println("Sorry, you cannot drop "+item +". It was automatically added by your solved puzzle and can not be dropped.");
                return item + " NOT REMOVED";
            }
            else {
                invList.remove(item);
                Sounds.playSounds("drop.wav",1000);
                System.out.println(item + " has been dropped from your inventory.");
                return item;
            }
        }
        else {
            System.out.println("Sorry, the item you entered is not in your inventory.");
            return item + " NOT IN INVENTORY";
        }
    }

    // getters and setters
    public List<String> getInvList() {
        return invList;
    }
}