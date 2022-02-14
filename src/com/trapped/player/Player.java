package com.trapped.player;


import com.trapped.GameEngine;

import com.trapped.utilities.*;

import java.io.Serializable;
import java.util.*;

import static com.trapped.utilities.TextColor.*;

public class Player implements Serializable {
    private static final Player PLAYER = new Player();
    // Needed to move, were out of scope in one section
    private String userInput;
    private String verb;
    private List<String> nouns = new ArrayList<>();
    public static String location = "bed";
    private Puzzle puzzle = Puzzle.getInstance();
    private Inventory inventory = Inventory.getInstance();

    private void Player() {}

    public static Player getInstance(){
        return PLAYER;
    }

    // inspect room, player can inspect either a furniture or an itgoem. If inspect a furniture, the output gonna have: Description of this furniture, items in here.
    // And if the furniture has a puzzle, it will show the puzzle's description.
    public boolean inspectItem(String loc) {
        //furniture
        Prompts.ClearConsole();
//        if (loc.equals("inventory")) {
//            inventory.checkInv();
//            new_command();
//        }
        if (Puzzle.MAP.containsKey(loc)) {
            location = loc;
            Map<String, Object> furniture = Puzzle.MAP.get(loc);

//            String furniture_desc = (String) furniture.get("furniture_desc");
//            String furniture_picture = (String) furniture.get("furniture_picture");
            if (furniture.get("furniture_items") != null) {
                ArrayList<String> furniture_items = (ArrayList<String>) furniture.get("furniture_items");
                if (!furniture_items.isEmpty()) {
                    if (inventory.getInvList().contains(furniture_items.get(0))) {
//                        FileManager.getResource(furniture_picture);
                        System.out.println("Inspecting...\nNo items found here in " + loc);
                        return false;
//                        solvePuzzle(something);

                    } else if (!inventory.getInvList().contains(furniture_items.get(0))) {
//                        FileManager.getResource(furniture_picture);

                        System.out.println("Inspecting...\nYou found: " + RED + furniture_items.get(0) + RESET);
//                        inventory.pickUpItem(loc, Puzzle.MAP);
                        return true;

//                        solvePuzzle(something);
                    }
                }
                if (furniture_items.isEmpty()) {
//                    FileManager.getResource(furniture_picture);
                    System.out.println("Inspecting...\nNo items found here.");
//                    solvePuzzle(loc);
                }
            } else if (furniture.get("furniture_items") == null) {
//                FileManager.getResource(furniture_picture);
                System.out.println("Inspecting...\nNo items found here.");
//                solvePuzzle(loc);
            }
        }

        //item
        else if (Puzzle.MAP.get(location).get("furniture_items") != null) {
            ArrayList<String> furniture_items = (ArrayList<String>) Puzzle.MAP.get(location).get("furniture_items");

            if (furniture_items.contains(loc)) {
                System.out.println("It's just a " + loc);
//                new_command();
            } else if (inventory.getInvList().contains(loc)) {
                System.out.println("It's just a " + loc);
//                new_command();
            } else {
                System.out.println("Sorry, I don't understand your input, please enter again. ");
                FileManager.getResource("commands.txt");
//                playerInput();
            }
        } else {
            System.out.println("Sorry, I don't understand your input, please enter again. ");
            FileManager.getResource("commands.txt");
//            playerInput();
        }
        return false;
    }

    public static void quitGame() {
        System.out.println("Quitting the Game. See you next time.");
        System.exit(0);
    }

    // solve puzzle
    public void solvePuzzle(String loc) {
        location = loc;
        puzzle.generatePuzzle(loc);

        if ("Y".equals(puzzle.getPuzzleExist())) {
            if ("riddles".equals(puzzle.getPuzzleType())) {
                puzzle.checkRiddle(inventory.invList, loc);
            } else if ("use tool".equals(puzzle.getPuzzleType())) {
                puzzle.useTool(inventory.invList, loc);
            } else if ("final".equals(puzzle.getPuzzleType())) {
                puzzle.finalPuzzle();
            }
        }
//        new_command();
    }

    public Map<String, String> moveDirection(String direction) {
        Map<String, String> result = new HashMap<>();

        Map<String, Object> furniture = puzzle.MAP.get(location);
        String newlocation = (String) furniture.get(direction);
        location = newlocation;
        Map<String, Object> new_furniture = puzzle.MAP.get(newlocation);

        String furniture_desc = (String) new_furniture.get("furniture_desc");
        String furniture_picture = (String) new_furniture.get("furniture_picture");

        result.put("description", furniture_desc);
        result.put("picture", furniture_picture);
        return result;
    }

    // Called when "help" is input
    public String gameMenu() {
        String returnText = "You are at " + location;
        return returnText;
    }

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public List<String> getNouns() {
        return nouns;
    }

    public void setNouns(List<String> nouns) {
        this.nouns = nouns;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public static String getLocation() {
        return location;
    }

    /*
     * NOT USING WITH GUI - "Go" command
     */
//    private void goCommand() {
//        // Currently this is only pointing to the first index of the parsed array
//        if (puzzle.MAP.containsKey(nouns.get(0))) {
//            goFurniture(nouns.get(0));
//        } else if (nouns.get(0).equals("left") || (nouns.get(0).equals("right"))) {
//            moveDirection(nouns.get(0));
//        } else {
//            System.out.println("Sorry, I don't understand your input, please enter again. ");
//            FileManager.getResource("commands.txt");
////            playerInput();
//        }
//    }

    /*
     * NOT USING WITH GUI - "Get item" command
     */
//    private void getCommand(ArrayList<String> puzzle_reward_item) {
//        if (inventory.invList.contains(nouns.get(0))) {
//            assert Puzzle.MAP != null;
//            inventory.pickUpItem(location, Puzzle.MAP);
//            new_command();
//        } else if (puzzle_reward_item.contains(nouns.get(0))) {
//            inventory.getInvList().add(nouns.get(0));
//        } else {
//            System.out.println("Sorry, I don't understand your input, please enter again. ");
//            FileManager.getResource("commands.txt");
//            playerInput();
//        }
//    }

    /*
     * NOT USING WITH GUI - Initial ViewRoom
     */
//    // After the intro story, player will see current location and the view of the whole room.
//    public void viewRoom() {
//        System.out.println("\nYou are currently in front of " + TextColor.RED + location + TextColor.RESET);
//        System.out.println(TextColor.GREEN + "\nWhat you'd like to do next? (type [commands] to check available commands and [help] to see other help items)" + TextColor.RESET);
//        playerInput();
//    }

    /*
     * NOT USING WITH GUI - "Go Furniture" command
     */
//    public void goFurniture(String destinationaLoc) {
//        Prompts.ClearConsole();
//
//        Map<String, Object> furniture = puzzle.MAP.get(destinationaLoc);
//
//        String furniture_desc = (String) furniture.get("furniture_desc");
//        String furniture_picture = (String) furniture.get("furniture_picture");
//        FileManager.getResource(furniture_picture);
//        System.out.println("\nYou are currently in front of " + destinationaLoc);
//        location = destinationaLoc;
//        System.out.println(furniture_desc);
//        new_command();
//    }

    /*
     * NOT USING WITH GUI - Check current location
     */
//    // check current location
//    public void checkCurrentLocation() {
//        System.out.println("Your current location: " + location);
//        playerInput();
//    }
//
    // quit game

    /*
     * NOT USING WITH GUI - "playerInput"
     */
//    public void playerInput() {
//        userInput = Prompts.getStringInput(); // gets userInput as a string from Prompts
//        // now extract verb/nouns from parsedInput
//        verb = TextParser.getVerb(userInput);
//        nouns = TextParser.getNouns(userInput);
//
//        puzzle.generatePuzzle(location); // change location/furniture
//        checkCommands();
//        playerInput();
//    }

    /*
     * NOT USING WITH GUI - "checkCommands"
     */
//    public boolean checkCommands() {
//        ArrayList<String> puzzle_reward_item = puzzle.getPuzzleRewardItem();
//
//        if (verb == null) {
//            System.out.println("Sorry, I don't understand your input, please enter again.");
//            FileManager.getResource("commands.txt");
//            return false;
//        }
//        //VERB IS NOT NULL PAST HERE
//        // Show Commands
//        if (verb.equals("commands")) {
//            FileManager.getResource("commands.txt");
//            return true;
//        }
//        // Quit Game
//        else if (verb.equals("quit")) {
//            if (nouns.contains("game") || (nouns.isEmpty())) {
//                quitGame();
//            }
//        }  // "Help"
//        else if (verb.equals("help")) {  // || help_value.toString().contains(verb)) {
//            gameMenu();
//            return true;
//        }
//        // "Go" command
//        else if (verb.equals("go")) {
//            goCommand();
//        }
//        // "Get" command
//        else if (verb.equals("get")) {   // || get_value.toString().contains(verb)) {
//            getCommand(puzzle_reward_item);
//
//        } else if (verb.equals("inspect")) {   // || inspect_value.toString().contains(verb)) {
//            inspectItem(nouns.get(0));
//        }
//
//        else if (verb.equals("drop")) {  // || drop_value.toString().contains(verb)) {
//            if (inventory.getInvList().isEmpty()) {
//                System.out.println("Sorry, your inventory is empty now and you cannot drop item.");
//                new_command();
//            } else if (nouns.isEmpty()) {
//                inventory.dropSelect();
//                new_command();
//            } else {
//                inventory.dropSpecificItem(nouns.get(0));
//                new_command();
//            }
//        }
//        //area to playtest
//        else {
//            System.out.println("Sorry, I don't understand your input, please enter again.");
//            FileManager.getResource("commands.txt");
//        }
//        return false;
//    }

    /*
     * NOT USING WITH GUI - "new_command"
     */
//    public void new_command() {
//        System.out.println(TextColor.GREEN + "\nWhat you'd like to do next? (type [commands] to check available commands and [help] to see other help items)" + TextColor.RESET);
//        playerInput();
//    }

    /*
     * OLD "HELP" CALL - called gameMenu
     */
// Removed String as input, seemed redundant
//    public void gameMenu() {
//        // no other if loop needed because help command already issued
//        // if(input.equalsIgnoreCase("Help")){
//        System.out.println("-----------------------");
//        System.out.println("\nHere are your options?");
//        FileManager.getResource("helperMenu.txt");
//
//        System.out.println("\nPlease select options above. enter number 1-4?");
//        //Scanner scan = new Scanner(System.in);
//        int selection = Prompts.getIntInput();
//        if (selection == 1) {
//            System.out.println("you are at " + location);
//        } else if (selection == 2) {
//            inventory.checkInv();
//            new_command();
//        } else if (selection == 3) {
//            System.out.println("Returning to game");
//            new_command();
//        } else if (selection == 4) {
//            quitGame();
//        }
//        new_command();
//        Prompts.ClearConsole();
//    }
}