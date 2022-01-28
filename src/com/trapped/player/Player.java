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
    static List<String> inventory = new ArrayList<>();

    // read Json file
    static Gson gson = new Gson();
    static Reader reader;

    static {
        try {
            reader = Files.newBufferedReader(Paths.get("resources/everything.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static Map<String, Map<String, Object>> map = gson.fromJson(reader, Map.class);

    // constructor
    public Player() throws IOException {

    }

    public static void inspectItem() {
        Map<String, Object> furniture = map.get(location);
        System.out.println("Inspecting...\nYou found: "+ furniture.get("items"));
    }



    // eg. use key with Window; solve puzzle
    public void useItem() {

    }

    public static void checkCurrentInventory() {
        inventory.add("matches");
        System.out.println(inventory);
    }

    public static void move() throws IOException {
        String[] validDirection = {"left", "right", "up", "down"};

        // Extract the current furniture to get the inner available directions
        Map<String, Object> furniture = map.get(location);
        List availableDirections = (ArrayList<String>)furniture.get("availableDirections");
        String desc = (String) furniture.get("desc");


        Scanner scan = new Scanner(System.in);
        System.out.println("You are currently in front of " + location);
        System.out.println("It is "+ desc);
        System.out.println("Which direction do you want to go? Available directions: " + availableDirections);
        String dir = scan.next();
        boolean contain = availableDirections.contains(dir);
        if (Arrays.asList(validDirection).contains(dir)) {
            if (contain) {
                String newlocation = (String) furniture.get(dir);
                location = newlocation;
                System.out.println("Now you are in front of " + location);
            } else {
                System.out.println("Sorry, you can't go that way. Please select again.");
            }
        } else {
            System.out.println("invalid input, please try again.");
        }


    }
    public static void pickUpItem() {
        Map<String, Object> furniture = map.get(location);

        ArrayList<Object> items = (ArrayList<Object>)furniture.get("items");
        ArrayList<String> strList = (ArrayList<String>)(ArrayList<?>)(items);
        inventory.addAll(strList);

        System.out.println("Added "+ strList + "to your inventory");
    }
    public static void checkCurrentLocation() {
        System.out.println(location);
    }

    public static String checkResult() {
        return "";
    }
}

