package com.trapped.player;


import com.trapped.GameEngine;

import com.trapped.utilities.*;

import java.io.Serializable;
import java.util.*;

import static com.trapped.utilities.TextColor.*;

public class Player implements Serializable{
    // Needed to move, were out of scope in one section
    public static String userInput;
    public static String verb;
    public static ArrayList<String> nouns = new ArrayList<>();
    //Map<String, Items> inventory = new HashMap<String, Items>(); // player's inventory
    public static String location="bed";

    //private List<String> inventory = new ArrayList<>();
    static List<String> rewarded_item = List.of(new String[]{"crowbar", "key", "a piece of paper with number 104"});


    private Puzzle puzzle = Puzzle.getInstance();
//    private Map<String, Object> furniture = puzzle.generatePuzzle(location);

    public static Inventory inventory = new Inventory();
//    static List<String> inventory = new ArrayList<>();


    static boolean incorrectPass = true; // scope


//    static Map<String, Map<String, Object>> map = furniturePuzzleGenerator();
//
//    //Utility class for the map generated above, trying to ensure json file is closed
//    public static Map<String, Map<String, Object>> furniturePuzzleGenerator() {
//        Gson gson = new Gson();
//        try {
//            Reader reader = Files.newBufferedReader(Paths.get("resources/furniture_puzzles.json"));
//            Map<String, Map<String, Object>> map = gson.fromJson(reader, Map.class);
//            reader.close();
//            return map;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    // After the intro story, player will see current location and the view of the whole room.
    public void viewRoom() {
        System.out.println("\nYou are currently in front of " + TextColor.RED  + location + TextColor.RESET);
        System.out.println(TextColor.GREEN + "\nWhat you'd like to do next? (type [commands] to check available commands and [help] to see other help items)" + TextColor.RESET);
        playerInput();
    }

    // inspect room, player can inspect either a furniture or an itgoem. If inspect a furniture, the output gonna have: Description of this furniture, items in here.
    // And if the furniture has a puzzle, it will show the puzzle's description.
    public void inspectItem(String something) {
        //furniture
        Prompts.ClearConsole();
        if(something.equals("inventory")){
            inventory.checkInv();
            new_command();
        }


        else if (Puzzle.MAP.containsKey(something)) {
            location = something;
            Map<String, Object> furniture = Puzzle.MAP.get(something);

            String furniture_desc = (String) furniture.get("furniture_desc");
            String furniture_picture = (String) furniture.get("furniture_picture");
            if (furniture.get("furniture_items") != null) {
                ArrayList<String> furniture_items = (ArrayList<String>) furniture.get("furniture_items");
                if (!furniture_items.isEmpty()) {
                    if (inventory.getInvList().contains(furniture_items.get(0))) {
                        FileManager.getResource(furniture_picture);
                        System.out.println("Inspecting...\nNo items found here in " + something);
                        solvePuzzle(something);
                    } else if (!inventory.getInvList().contains(furniture_items.get(0))) {
                        FileManager.getResource(furniture_picture);

                        System.out.println("Inspecting...\nYou found: " + RED + furniture_items.get(0) + RESET);
                        pickUpItem(something);

                        solvePuzzle(something);
                    }
                }
                if (furniture_items.isEmpty()) {
                    FileManager.getResource(furniture_picture);
                    System.out.println("Inspecting...\nNo items found here.");
                    solvePuzzle(something);
                }
            } else if (furniture.get("furniture_items") == null) {
                FileManager.getResource(furniture_picture);
                System.out.println("Inspecting...\nNo items found here.");
                solvePuzzle(something);
            }
        }

        //item


        else if (Puzzle.MAP.get(location).get("furniture_items") != null) {
            ArrayList<String> furniture_items = (ArrayList<String>) Puzzle.MAP.get(location).get("furniture_items");

            if (furniture_items.contains(something)) {
                System.out.println("It's just a " + something);
                new_command();
            }
            else if (inventory.getInvList().contains(something)){
                System.out.println("It's just a " + something);
                new_command();
            }
            else {
                System.out.println("Sorry, I don't understand your input, please enter again. ");
                FileManager.getResource("commands.txt");
                playerInput();
            }
        }
        else{
            System.out.println("Sorry, I don't understand your input, please enter again. ");
            FileManager.getResource("commands.txt");
            playerInput();
        }

    }


    // item disappear from inventory
    public void useItem(String item) {
        inventory.remove(item);
    }

    // check current inventory
    public void checkCurrentInventory() {
        System.out.println("Your current inventory: " + inventory);
        new_command();
    }



    // pickup item method.
    public void pickUpItem(String location) {

        if (Puzzle.MAP.get(location) != null) {
            Map<String, Object> furniture = Puzzle.MAP.get(location);
            ArrayList<String> furniture_items = (ArrayList<String>) furniture.get("furniture_items");
            // if inventory is full. player need to drop an item, then item found in current location will be added to inventory.
            if (inventory.size() >= 5) {
                System.out.println("inventory cannot take more than 5 items. Please drop one item.");
                System.out.println("Which item you'd like to drop?");
                String selection = Prompts.getStringInput();
                while (!inventory.contains(selection.toLowerCase())) {
                    System.out.println("Sorry, the item you entered is not in inventory, please select again.");
                    selection = Prompts.getStringInput();
                }
                inventory.remove(selection);
                Sounds.playSounds("drop.wav",1000);
                System.out.println(selection + " has been dropped from your inventory.");
                inventory.add(furniture_items.get(0));
                System.out.println(furniture_items.get(0) + " has been added to your inventory");
                Sounds.playSounds("pick.wav",1000);
            }
            // if inventory is not full
            else {
                //if furniture has no item available to be picked up
                if (furniture_items.isEmpty()) {
                    System.out.println(location + " is empty. Nothing can be added.");
                }
                //if furniture has an item available to be picked up
                else if (!inventory.contains(furniture_items.get(0))) {
                    System.out.println("\nDo you want to add " + furniture_items.get(0) + " to inventory? [Y/N]");
                    Scanner scan = new Scanner(System.in);
                    String response = scan.nextLine();
                    if (response.isEmpty()) {
                        System.out.println("Sorry, I don't understand your entry. You did not pick anything from " + location);
                    } else if (response.equalsIgnoreCase("Y")) {
                        System.out.println(furniture_items.get(0) + " has been picked up and added to your inventory");
                        Sounds.playSounds("pick.wav", 1000);
                        inventory.add(furniture_items.get(0));
                    } else if (response.equalsIgnoreCase("N")) {
                        System.out.println("You did not pick anything from " + location);
                    } else {
                        System.out.println("Sorry, I don't understand your entry. You did not pick anything from " + location);
                    }
                }
            }
        }
    }

    // check current location
    public  void checkCurrentLocation() {
        System.out.println("Your current location: " + location);
        playerInput();
    }

    // quit game
    public static void quitGame(){
        System.out.println("Quitting the Game. See you next time.");
        System.exit(0);
    }


    // play again
//    public static void playAgain(String input) {
//        if (input.equals("again") || (input.equals("play again"))){
//            GameEngine.startGame();
//        }
//    }

    // Drop item -- will provide current inventory first then let player pick. This method will be used when inventory is full and player being asked to drop an item.
    public void dropItem() {

        System.out.println("Your inventory: " + inventory);

        System.out.println("Which item you'd like to drop? Please enter item name. ");
        String selected_drop = Prompts.getStringInput(); // scan.nextLine();
        if (inventory.contains(selected_drop.toLowerCase())) {
            if(rewarded_item.contains(selected_drop)){
                if (rewarded_item.contains(selected_drop)) {
                    System.out.println("Sorry, you cannot drop "+selected_drop +". It was automatically added by your solved puzzle");
                    new_command();
                }
                else {
                    inventory.remove(selected_drop);
                    Sounds.playSounds("drop.wav",1000);
                    System.out.println(selected_drop + " has been dropped from your inventory.");
                    new_command();
                }
            }

        } else {
            System.out.println("Sorry, you cannot drop "+selected_drop +". It is not in your inventory");
            new_command();
        }
    }

    // Drop a specific item - this will be used when player input "drop xxx"
    public void dropSpecificItem(String item) {
        if (inventory.contains(item.toLowerCase())) {
            if (rewarded_item.contains(item)) {
                System.out.println("Sorry, you cannot drop "+item +". It was automatically added by your solved puzzle");
                new_command();
            }
            else{
            inventory.remove(item);
            Sounds.playSounds("drop.wav",1000);
            System.out.println(item + " has been dropped from your inventory.");
            new_command();
            }
        } else {
            System.out.println("Sorry, the item you entered is not in your inventory.");
            new_command();
        }
    }

    // solve puzzle
    public void solvePuzzle(String loc) {
        location = loc;
        puzzle.generatePuzzle(loc);

        if ("Y".equals(puzzle.getPuzzleExist())) {
            if("riddles".equals(puzzle.getPuzzleType())) {
                puzzle.checkRiddle(this.inventory, loc);
            }
            else if("use tool".equals(puzzle.getPuzzleType())) {
                puzzle.useTool(this.inventory, loc);
            } else if("final".equals(puzzle.getPuzzleType())) {
                puzzle.finalPuzzle();
            }
        }

        new_command();
    }


    public void playerInput() {


        userInput = Prompts.getStringInput(); // gets userInput as a string from Prompts
        // now extract verb/nouns from parsedInput
        verb = TextParser.getVerb(userInput);
        nouns = TextParser.getNouns(userInput);


        Map<String, Object> furniture = puzzle.generatePuzzle(location);
        ArrayList<String> furniture_items = (ArrayList<String>) furniture.get("furniture_items");
        ArrayList<String> puzzle_reward_item = (ArrayList<String>) furniture.get("puzzle_reward_item");
        if(verb==null && nouns.isEmpty()){
            System.out.println("Sorry, I don't understand your input, please enter again.");
            FileManager.getResource("commands.txt");
            playerInput();
        }
        else if(verb == null){
            System.out.println("Sorry, I don't understand your input, please enter again.");
            FileManager.getResource("commands.txt");
            playerInput();
        }
        else if (verb != null) {
            // Show Commands
            if (verb.equals("commands")){
                FileManager.getResource("commands.txt");
                playerInput();
            }
            // Quit Game
            else if (verb.equals("quit")) {
                if (nouns.contains("game") || (nouns.isEmpty())) {
                    quitGame();
                }
            }
            // "Go" command
            else if (verb.equals("go")) {
                // Currently this is only pointing to the first index of the parsed array
                if (nouns.isEmpty()){
                    System.out.println("Sorry, I don't understand your input, please enter again. ");
                    FileManager.getResource("commands.txt");
                    playerInput();
                }

                else if (puzzle.MAP.containsKey(nouns.get(0))) {

                    goFurniture(nouns.get(0));
                } else if (nouns.get(0).equals("left") || (nouns.get(0).equals("right"))) {
                    moveDirection(nouns.get(0));
                }
                else{
                    System.out.println("Sorry, I don't understand your input, please enter again. ");
                    FileManager.getResource("commands.txt");
                    playerInput();
                }
            }
            // "Get" command
            else if (verb.equals("get")) {   // || get_value.toString().contains(verb)) {
                if (nouns.isEmpty()) {
                    System.out.println("Sorry, I don't understand your input, please enter again. ");
                    FileManager.getResource("commands.txt");
                    playerInput();
                }
                else {
                    if (inventoryItems.contains(nouns.get(0))) {
                        inventory.pickUpItem(location, furniturePuzzleMap);
                        new_command();
                    }
                    else if (puzzle_reward_item.contains(nouns.get(0))) {
                        inventory.getInvList().add(nouns.get(0));
                    }
                    else {
                        System.out.println("Sorry, I don't understand your input, please enter again. ");
                        FileManager.getResource("commands.txt");
                        playerInput();
                    }
                }
            }
            else if (verb.equals("inspect")) {   // || inspect_value.toString().contains(verb)) {
                if (nouns.isEmpty()){
                    System.out.println("Sorry, I don't understand your input, please enter again.");
                    FileManager.getResource("commands.txt");
                    playerInput();
                }
                else{
                    inspectItem(nouns.get(0));
                }

            }
            // help
            //Object help = map1.keySet().toArray()[6];
            //Object help_value = map1.get(help);
            else if (verb.equals("help")) {  // || help_value.toString().contains(verb)) {
                gameMenu();
                playerInput();
            }
            else if (verb.equals("drop")) {  // || drop_value.toString().contains(verb)) {
                if (inventory.getInvList().isEmpty()) {
                    System.out.println("Sorry, your inventory is empty now and you cannot drop item.");
                    new_command();
                }
                else if (nouns.isEmpty()) {
                    inventory.dropItem();
                    new_command();
                }
                else {
                    inventory.dropSpecificItem(nouns.get(0));
                    new_command();
                }
            }
            //area to playtest
            else if(userInput.equals(nouns.get(0))){
                System.out.println("Sorry, I don't understand your input, please enter again.");
                FileManager.getResource("commands.txt");
                playerInput();
            }
            else {
                System.out.println("Sorry, I don't understand your input, please enter again.");
                FileManager.getResource("commands.txt");
                playerInput();
            }
            //reader.close();
        }
    }

    public void moveDirection(String direction) {
        Prompts.ClearConsole();

        Map<String, Object> furniture = puzzle.MAP.get(location);
        String newlocation = (String) furniture.get(direction);
        location = newlocation;
        Map<String, Object> new_furniture = puzzle.MAP.get(newlocation);

        String furniture_desc = (String) new_furniture.get("furniture_desc");
        String furniture_picture = (String) new_furniture.get("furniture_picture");
        FileManager.getResource(furniture_picture);
        System.out.println("Now you are in front of " + location);
        System.out.println(furniture_desc);
        new_command();
    }

    public void goFurniture(String destinationaLoc) {
        Prompts.ClearConsole();

        Map<String, Object> furniture = puzzle.MAP.get(destinationaLoc);

        String furniture_desc = (String) furniture.get("furniture_desc");
        String furniture_picture = (String) furniture.get("furniture_picture");
        FileManager.getResource(furniture_picture);
        System.out.println("\nYou are currently in front of " + destinationaLoc);
        location = destinationaLoc;
        System.out.println(furniture_desc);
        new_command();
    }

    // Removed String as input, seemed redundant
    public void gameMenu() {
        // no other if loop needed because help command already issued
        // if(input.equalsIgnoreCase("Help")){
        System.out.println("-----------------------");
        System.out.println("\nHere are your options?");
        FileManager.getResource("helperMenu.txt");

        System.out.println("\nPlease select options above. enter number 1-4?");
        //Scanner scan = new Scanner(System.in);
        int selection = Prompts.getIntInput();
        if (selection == 1) {
            System.out.println("you are at " + location);
        } else if (selection == 2) {
            inventory.checkInv();
            new_command();
        } else if (selection == 3) {
            System.out.println("Returning to game");
            new_command();
        } else if (selection == 4) {
            quitGame();
        }
        new_command ();
        Prompts.ClearConsole();
    }
    public void new_command (){

        System.out.println(TextColor.GREEN + "\nWhat you'd like to do next? (type [commands] to check available commands and [help] to see other help items)" + TextColor.RESET);
        playerInput();
    }
}