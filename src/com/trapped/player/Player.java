package com.trapped.player;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Player {
    //Map<String, Items> inventory = new HashMap<String, Items>(); // player's inventory
    public static String location = "bed";
    String[] inventory = {};

    // constructor
    public Player() {

    }

    public void inspectItem() {

    }

    public void pickUpItem() {

    }

    // eg. use key with Window; solve puzzle
    public void useItem() {

    }

    public void checkCurrentInventory() {

    }

    public static void move() throws IOException {
        String[] validDirection = {"left", "right", "up", "down"};
        // read Json file
        Gson gson = new Gson();
        Reader reader = Files.newBufferedReader(Paths.get("resources/everything.json"));
        Map<String, Map<String, Object>> map = gson.fromJson(reader, Map.class);

        // Extract the current furniture to get the inner available directions
        Map<String, Object> furniture = map.get(location);
        List availableDirections = (ArrayList<String>) furniture.get("availableDirections");
        String desc = (String) furniture.get("desc");


        Scanner scan = new Scanner(System.in);
        System.out.println("You are currently in front of " + location);
        System.out.println("It is "+ desc);
        System.out.println("Which direction do you want to go? Available directions: " + availableDirections);
        String dir = scan.next();
        boolean contain = availableDirections.contains(dir);
        if (Arrays.asList(validDirection).contains(dir)) {
            if (contain) {
                location = (String) furniture.get(dir);
                System.out.println("Now you are in front of " + location);
            } else {
                System.out.println("Sorry, you can't go that way. Please select again.");
            }
        } else {
            System.out.println("invalid input, please try again.");
        }

    }

    public static String checkCurrentLocation() {
        return location;
    }

    public static String checkResult() {
        return "";
    }
}

