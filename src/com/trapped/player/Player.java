package com.trapped.player;

import com.google.gson.Gson;
import com.trapped.GameEngine;
import com.trapped.utilities.*;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


import static com.trapped.utilities.TextColor.*;


public class Player implements Serializable {
    // Needed to move, were out of scope in one section
    public static String userInput;
    public static String verb;
    public static String noun;
    public static String location = "bed";
    public static float volume;
    private static List<String> inventory = new ArrayList<>();
    private static String furniturePuzzlesJsonPath = "./resources/furniture_puzzles.json";
    private static int max_attempts = 3;
    private static String ANSWER;
    private static Scanner scan = new Scanner(System.in);
    private static final Player instance = new Player();


    static Map<String, Map<String, Object>> map = furniturePuzzleGenerator();

    //Utility class for the map generated above, trying to ensure json file is closed
    private static Map<String, Map<String, Object>> furniturePuzzleGenerator() {
        Gson gson = new Gson();
        try {
            Reader reader = Files.newBufferedReader(Path.of("resources/furniture_puzzles.json"));
            Map<String, Map<String, Object>> map = gson.fromJson(reader, Map.class);
            reader.close();
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    // After the intro story, player will see current location and the view of the whole room.
    public static void viewRoom() {
        System.out.println("\nYou are currently in front of " + TextColor.RED + location + TextColor.RESET);
        System.out.println(TextColor.GREEN + "\nWhat you'd like to do next? (type [commands] to check available commands and [help] to see other help items)" + TextColor.RESET);
        playerInput();
    }

    // inspect room, player can inspect either a furniture or an item.
    // If inspect a furniture, the output will have: Description of this furniture, items in here.
    // And if the furniture has a puzzle, it will show the puzzle's description.
    private static void inspectItem(String something) {
        //furniture

        Map<String, Object> furniture = map.get(something);

        if (something.equals("inventory")) {
            checkCurrentInventory();
        } else if (map.containsKey(something)) {
            location = something;
            if (furniture.get("furniture_items") != null) {
                ArrayList<String> furniture_items = (ArrayList<String>) furniture.get("furniture_items");
                if (!furniture_items.isEmpty()) {
                    roomWithItems(something);
                }
                if (furniture_items.isEmpty()) {
                    noItemsInRoom(something);
                }
            } else if (furniture.get("furniture_items") == null) {
                noItemsInRoom(something);
            }
        }

        //item

        else if (map.get(location).get("furniture_items") != null) {
            ArrayList<String> furniture_items = (ArrayList<String>) map.get(location).get("furniture_items");
            if (furniture_items.contains(something)) {
                System.out.println("It's just a " + something);

            } else if (inventory.contains(something)) {
                System.out.println("It's just a " + something);

            } else {
                System.out.println("Sorry, I don't understand your input, please enter again. ");
                FileManager.getResource("commands.txt");
                playerInput();
            }

        } else {
            System.out.println("Sorry, I don't understand your input, please enter again. ");
            FileManager.getResource("commands.txt");
        }

        playerInput();
    }

    private static void roomWithItems(String something) {
        Map<String, Object> furniture = map.get(something);
        String furniture_picture = (String) furniture.get("furniture_picture");
        ArrayList<String> furniture_items = (ArrayList<String>) furniture.get("furniture_items");
        ArrayList<String> tempArr = new ArrayList<>();

        FileManager.getResource(furniture_picture);
        System.out.println("Inspecting...\nYou found: " + TextColor.RED + furniture_items + TextColor.RESET);
        furniture_items.removeAll(tempArr);
        furniture.put("furniture_items", furniture_items);
        map.put("location", furniture);
        FileManager.writeJSON(map, furniturePuzzlesJsonPath);
        solvePuzzle(something);
    }

    private static void noItemsInRoom(String something) {
        Map<String, Object> furniture = map.get(something);
        String furniture_picture = (String) furniture.get("furniture_picture");

        FileManager.getResource(furniture_picture);
        System.out.println("Inspecting...\nNo items found here.");
        solvePuzzle(something);
    }

    // check current inventory
    private static void checkCurrentInventory() {
        System.out.println("Your current inventory: " + inventory);
        playerInput();
    }

    // pickup item method.
    private static void pickUpItem(String noun) {

        if (map.get(location) != null) {
            Map<String, Object> furniture = map.get(location);
            ArrayList<String> furniture_items = (ArrayList<String>) furniture.get("furniture_items");
            // if inventory is full. player need to drop an item, then item found in current location will be added to inventory.
            if (!furniture_items.isEmpty()) {
                while (inventory.size() >= 5) {
                    System.out.println("You can't hold more than 5 items. Please drop one item.");
                    System.out.println("Which item would you like to drop?");
                    String selection = scan.nextLine();
                    while (!inventory.contains(selection.toLowerCase())) {
                        System.out.println("Sorry, the item you entered is not in your inventory, please select again.");
                        selection = scan.nextLine();
                    }
                    dropItem(selection);
                }
                addItemAndUpdateJson(furniture_items,furniture);
            }
            //if furniture has no item available to be picked up
            if (furniture_items.isEmpty()) {
                System.out.println("There's nothing here...");
            }
            //if furniture has an item available to be picked up
            else if (!inventory.contains(noun)) {
                System.out.println("\nDo you want to add " + noun + " to inventory? [Y/N]");
                String response = scan.nextLine();
                if (response.isEmpty()) {
                    System.out.println("Sorry, I didn't understand your entry. You did not pick anything up from " + location);
                } else if (response.equalsIgnoreCase("Y")) {
                    addItemAndUpdateJson(furniture_items,furniture);
                } else if (response.equalsIgnoreCase("N")) {
                    System.out.println("You did not pick anything from " + location);
                } else {
                    System.out.println("Sorry, I didn't understand your entry. You did not pick anything up from " + location);
                }
            }
        }
    }

    private static void addItemAndUpdateJson(ArrayList<String> furniture_items,Map<String, Object> furniture){
        inventory.add(noun);
        furniture_items.remove(noun);
        furniture.put("furniture_items", furniture_items);
        map.put("location", furniture);
        FileManager.writeJSON(map, furniturePuzzlesJsonPath);
        System.out.println(noun + " has been added to your inventory");
        Sounds.playSounds("pick.wav", 1000);
    }

    // quit game
    private static void quitGame() {
        System.out.println("Quitting the Game. See you next time.");
        System.exit(0);
    }

    // Drop item -- will provide current inventory first then let player pick.
    // This method will be used when inventory is full and player being asked to drop an item.
    private static void dropItem() {
        System.out.println("Your inventory: " + inventory);
        System.out.println("Which item you'd like to drop? Please enter item name. ");
        String selected_drop = scan.nextLine();
        dropItem(selected_drop);
    }

    // Drop a specific item - this will be used when player input "drop xxx"
    private static void dropItem(String item) {
        Map<String, Object> furniture = map.get(location);
        ArrayList<String> furniture_items = (ArrayList<String>) furniture.get("furniture_items");
        if (inventory.contains(item.toLowerCase())) {
            if (inventory.contains(item.toLowerCase())) {
                inventory.remove(item);
                Sounds.playSounds("drop.wav", 1000);
                furniture_items.add(item);
                furniture.put("furniture_items", furniture_items);
                map.put(location, furniture);
                FileManager.writeJSON(map, furniturePuzzlesJsonPath);
                System.out.println(item + " has been dropped from your inventory.");
            } else {
                System.out.println("Sorry, the item you entered is not in your inventory.");
            }
            playerInput();
        }
    }

    private static void solvePuzzle(String loc) {
        Map<String, Object> furniture = map.get(loc);
        location = loc;
        String puzzle_exist = (String) furniture.get("puzzle_exist");
        String puzzle_type = (String) furniture.get("puzzle_type");
        // check if a location has puzzle
        if (puzzle_exist.equals("Y")) {
            switch (puzzle_type) {
                case "riddles":
                    //  if solved
                    riddles(location);
                    break;
                case "use tool":
                    //  if solved
                    toolPuzzle(location);
                    break;
                case "final":
                    doorPuzzle(location);
                    break;
            }
        }
        playerInput();
    }

    public static void playerInput() {
        /*
         * This seems to be the main logic loop for the game. It takes in the player's input and then calls functions
         * based on the input results. We've swapped the massive if else block for a switch statement. -MS
         */

        System.out.println("What would you like to do next?");
        userInput = scan.nextLine(); // gets userInput as a string from Prompts
        // now extract verb/nouns from parsedInput
        verb = TextParser.getVerb(userInput);
        noun = TextParser.getNoun(userInput);

        if (verb == null && (noun == null)) {
            System.out.println("Sorry, I don't understand your input, please try again.");
            FileManager.getResource("commands.txt");
        } else if (verb == null) {
            System.out.println("Sorry, I don't understand your input, please enter again.");
            FileManager.getResource("commands.txt");
        } else {
            switch (verb) {
                case "commands":
                    FileManager.getResource("commands.txt");
                    break;
                case "quit":
                    quitGame();
                    break;
                case "go":
                    // Currently, this is only pointing to the first index of the parsed array
                    move();
                    break;
                case "get":
                    get();
                    break;
                case "inspect":
                    inspect();
                    break;
                case "help":
                    helpMenu();
                    break;
                case "drop":
                    drop();
                    break;
                default:
                    System.out.println("Sorry, I don't understand your input, please enter again. ");
                    FileManager.getResource("commands.txt");
            }
        }
    }

    private static void drop() {
        if (noun != null) {
            if (inventory.isEmpty()) {
                System.out.println("You have nothing to drop");
            } else if (noun.isEmpty()) {
                dropItem();
            } else
                dropItem(noun);
        } else {
            System.out.println("Sorry, I don't understand your input, please enter again. ");
            FileManager.getResource("commands.txt");
        }
        playerInput();
    }

    private static void inspect() {
        if (noun != null) {
            inspectItem(noun);
        } else {
            System.out.println("Sorry, I don't understand your input, please enter again. ");
            FileManager.getResource("commands.txt");
            playerInput();
        }
    }

    private static void get() {
        Map<String, Object> furniture = map.get(location);
        ArrayList<String> furniture_items = (ArrayList<String>) furniture.get("furniture_items");
        ArrayList<String> puzzle_reward_item = (ArrayList<String>) furniture.get("puzzle_reward_item");

        if (noun != null) {
            if (furniture_items.contains(noun)) {
                System.out.println("You added " + noun + " to your inventory");
                pickUpItem(noun);
            } else if (puzzle_reward_item.contains(noun)) {
                inventory.add(noun);
            } else {
                System.out.println("Sorry, I don't understand your input, please enter again. ");
                FileManager.getResource("commands.txt");
            }
        } else {
            System.out.println("Sorry, I don't understand your input, please enter again. ");
            FileManager.getResource("commands.txt");
        }
        playerInput();
    }

    private static void move() {
        if (noun != null) {
            if (map.containsKey(noun)) {
                goFurniture(noun);
            } else if (noun.equals("left") || (noun.equals("right"))) {
                moveDirection(noun);
            } else {
                System.out.println("Sorry, I don't understand your input, please enter again. ");
                FileManager.getResource("commands.txt");
                playerInput();
            }
        } else {
            System.out.println("Sorry, I don't understand your input, please enter again. ");
            FileManager.getResource("commands.txt");
            playerInput();
        }
    }


    private static void moveDirection(String direction) {
        Map<String, Object> furniture = map.get(location);
        String newlocation = (String) furniture.get(direction);
        location = newlocation;
        Map<String, Object> new_furniture = map.get(location);
        String furniture_desc = (String) new_furniture.get("furniture_desc");
        String furniture_picture = (String) new_furniture.get("furniture_picture");
        FileManager.getResource(furniture_picture);
        System.out.println("Now you are in front of " + location);
        System.out.println(furniture_desc);
        playerInput();
    }

    private static void goFurniture(String destinationaLoc) {
        Map<String, Object> furniture = map.get(destinationaLoc);
        String furniture_desc = (String) furniture.get("furniture_desc");
        String furniture_picture = (String) furniture.get("furniture_picture");
        FileManager.getResource(furniture_picture);
        System.out.println("\nYou are currently in front of " + destinationaLoc);
        location = destinationaLoc;
        System.out.println(furniture_desc);
        playerInput();
    }


    private static void helpMenu() {
        FileManager.getResource("helperMenu.txt");
        System.out.println("\nTo select from the options above enter a number 1-4.");
        //Scanner scan = new Scanner(System.in);
        int selection = scan.nextInt();
        switch (selection) {
            case 1:
                System.out.println("you are at " + location);
                break;
            case 2:
                checkCurrentInventory();
                break;
            case 3:
                System.out.println("Returning to game");
                break;
            case 4:
                quitGame();
                break;
            default:
                System.out.println("Sorry you did not select a number from 1-4.");
        }
        playerInput();
    }

    private static void riddles(String loc) {
        //Reading the JSON and pulling variables
        //Don't mind us...
        Map<String, Object> furniture = map.get(loc);
        ArrayList<String> puzzle_reward_item = (ArrayList<String>) furniture.get("puzzle_reward_item");
        String puzzle_desc = (String) furniture.get("puzzle_desc");
        ArrayList<Object> puzzle_filename = (ArrayList<Object>) furniture.get("puzzle_filename");
        ArrayList<String> converted_puzzle_filename = (ArrayList<String>) (ArrayList<?>) (puzzle_filename);
        String puzzle_sounds = (String) furniture.get("puzzle_sounds");
        ArrayList<Object> multiple_puzzle_answer = (ArrayList<Object>) furniture.get("multiple_puzzle_answer");

        if (!puzzleSolved()) {
            System.out.println("A puzzle has been found in " + loc + ".");
            System.out.println(puzzle_desc);
            System.out.println("Would you like to solve this puzzle now? Y/N");
            ANSWER = scan.nextLine().strip().toLowerCase();
            if (ANSWER.equals("y")) {
                // riddles puzzle
                boolean solved = false;
                Random r = new Random();
                int randomItem = r.nextInt(converted_puzzle_filename.size());
                String randomPuzzle = converted_puzzle_filename.get(randomItem);
                ArrayList<String> randomAnswer = (ArrayList<String>) multiple_puzzle_answer.get(randomItem);
                FileManager.getResource(randomPuzzle);
                System.out.println("\nYour answer:      (If it's too hard to answer, please enter [easy] to get a easier question.)");
                while (!solved) {
                    ANSWER = scan.nextLine().strip().toLowerCase();
                    // if user input correct answer
                    if (ANSWER.equals(furniture.get("easy_answer")) || randomAnswer.contains(ANSWER)) {
                        System.out.println(furniture.get("puzzle_reward"));
                        Sounds.changeVolume(puzzle_sounds, 1000, volume);
                        System.out.println("You found " + puzzle_reward_item.get(0) + ".");
                        pickUpItem(puzzle_reward_item.get(0));
                        solved = true;
                    } else if (ANSWER.equalsIgnoreCase("easy")) {
                        System.out.println(furniture.get("easy_question"));
                    } else {
                        System.out.println("you didn't solve the puzzle. Try again.");
                    }
                }
            } else if (ANSWER.equals("n")) {
                System.out.println("You decided not to solve the puzzle");
            } else {
                System.out.println("Sorry I don't understand your command. The puzzle has not been solved. Please come back later.");
            }
        }
    }

    private static boolean puzzleSolved() {
        //checks to see if the player has solved any of the puzzles, if they have, returns true to the caller!
        Map<String, Object> furniture = map.get(location);
        ArrayList<String> puzzle_reward_item = (ArrayList<String>) furniture.get("puzzle_reward_item");
        Boolean solved = false;
        if ((inventory.contains(puzzle_reward_item.get(0))) || (inventory.contains("key") && location.equals("safe")) ||
                (inventory.contains("a piece of paper with number 104") && location.equals("window")) ||
                (inventory.contains("a piece of paper with number 104") && location.equals("safe"))) {
            System.out.println("The puzzle has been solved. Please feel free to explore other furniture :)");
            solved = true;
        }
        return solved;
    }

    private static void toolPuzzle(String loc) {
        //reading the JSON file, walking fast, faces past and I'm homebound
        Map<String, Object> furniture = map.get(loc);
        ArrayList<String> puzzle_reward_item = (ArrayList<String>) furniture.get("puzzle_reward_item");
        String puzzle_desc = (String) furniture.get("puzzle_desc");
        String puzzle_sounds = (String) furniture.get("puzzle_sounds");
        ArrayList<String> puzzle_itemsNeeded = (ArrayList<String>) furniture.get("puzzle_itemsNeeded");
        String puzzle_verb = (String) furniture.get("puzzle_verb");
        String puzzle_reward = (String) furniture.get("puzzle_reward");
        //
        //
        if (!puzzleSolved()) {
            System.out.println("A puzzle has been found in " + loc + ".");
            System.out.println(puzzle_desc);
            System.out.println("Would you like to solve this puzzle now? Y/N");
            String solve_ans = scan.nextLine();
            if (solve_ans.equalsIgnoreCase("Y")) {
                System.out.println("You need to use an item from your inventory. Let's see if you got needed item in your inventory...");
                System.out.println("Your current inventory: " + inventory);
                if (!inventory.contains(puzzle_itemsNeeded.get(0))) {
                    System.out.println("Sorry, you don't have the tools. Explore the room and see if you can find anything");
                } else if (inventory.contains(puzzle_itemsNeeded.get(0))) {
                    System.out.println("Which of the item you'd like to use?");
                    String ans = scan.nextLine();
                    if ((ans.equalsIgnoreCase(puzzle_verb + " " + puzzle_itemsNeeded.get(0))) || ans.equalsIgnoreCase(puzzle_itemsNeeded.get(0))) {
                        Sounds.changeVolume(puzzle_sounds, 1000, volume);
                        System.out.println(puzzle_reward + " and you've found " + puzzle_reward_item.get(0));
                        inventory.remove(puzzle_itemsNeeded.get(0));
                        inventory.add(puzzle_reward_item.get(0));
                        Sounds.changeVolume("pick.wav", 1000, volume);
                        System.out.println("Added " + puzzle_reward_item.get(0) + " to your inventory");
                        playerInput();
                    } else if ((inventory.contains(ans) && (!ans.equals(puzzle_itemsNeeded)))) {
                        System.out.println("Wrong item. The puzzle has not been solved. Please come back later.");
                    } else {
                        System.out.println("Sorry I don't understand your command. The puzzle has not been solved. Please come back later.");
                    }
                }
            } else if (solve_ans.equalsIgnoreCase("N")) {
                System.out.println("You chose not to solve the puzzle.");
            } else {
                System.out.println("Sorry I don't understand your command. The puzzle has not been solved. Please come back later.");
            }
        }
    }

    private static void doorPuzzle(String loc) {
        //Final puzzle in the game, can be solved at any time if you know the secret number (104)
        Map<String, Object> furniture = map.get(loc);
        String puzzle_desc = (String) furniture.get("puzzle_desc");
        String puzzle_sounds = (String) furniture.get("puzzle_sounds");
        String puzzle_answer = (String) furniture.get("puzzle_answer");
        String puzzle_reward = (String) furniture.get("puzzle_reward");
        System.out.println(puzzle_desc);
        System.out.println("What's the password? You have " + MAGENTA_UNDERLINE + max_attempts + RESET + " attempts remaining. If you's like to try later, enter[later]");
        ANSWER = scan.nextLine();

        if (ANSWER.trim().equals("later") || ANSWER.trim().equals("")) {
            System.out.println("No worries! Try next time!");
        } else {
            while (max_attempts-- > 0) {
                if (ANSWER.trim().equals(puzzle_answer)) {
                    System.out.println(puzzle_reward);
                    Sounds.changeVolume(puzzle_sounds, 2000, volume);
                    System.out.println("You won the game! Thanks for playing!");
                    System.exit(0);
                } else if (max_attempts == 0) {
                    System.out.println("You lost the game! You are Trapped. Please try again later.");
                    System.exit(0);
                } else {
                    System.out.println("Wrong password. Try again next time! " + MAGENTA_UNDERLINE + max_attempts + RESET + " attempts remaining");
                }
            }
        }
    }
}