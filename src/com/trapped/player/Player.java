package com.trapped.player;

import com.google.gson.Gson;
import com.trapped.utilities.*;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static com.trapped.utilities.TextColor.*;

public class Player implements Serializable{
    // Needed to move, were out of scope in one section
    public static String userInput;
    public static String verb;
    public static ArrayList<String> nouns = new ArrayList<>();
    //Map<String, Items> inventory = new HashMap<String, Items>(); // player's inventory
    public static String location="bed";
    public static Inventory inventory = new Inventory();
//    static List<String> inventory = new ArrayList<>();

    static boolean incorrectPass = true; // scope
    static int max_attempts = 3;

    static Map<String, Map<String, Object>> furniturePuzzleMap = furniturePuzzleGenerator();

    //Utility class for the map generated above, trying to ensure json file is closed
    public static Map<String, Map<String, Object>> furniturePuzzleGenerator() {
        Gson gson = new Gson();
        try {
            Reader reader = Files.newBufferedReader(Paths.get("resources/furniture_puzzles.json"));
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
        System.out.println("\nYou are currently in front of " + TextColor.RED  + location + TextColor.RESET);
        System.out.println(TextColor.GREEN + "\nWhat you'd like to do next? (type [commands] to check available commands and [help] to see other help items)" + TextColor.RESET);
        playerInput();
    }

    // inspect room, player can inspect either a furniture or an itgoem. If inspect a furniture, the output gonna have: Description of this furniture, items in here.
    // And if the furniture has a puzzle, it will show the puzzle's description.
    public static void inspectItem(String something) {
        //furniture
        Prompts.ClearConsole();
        if(something.equals("inventory")){
            inventory.checkInv();
            new_command();
        }
        else if (furniturePuzzleMap.containsKey(something)) {
            location = something;
            Map<String, Object> furniture = furniturePuzzleMap.get(something);
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
                        System.out.println("Inspecting...\nYou found: " + TextColor.RED + furniture_items.get(0) + TextColor.RESET);
                        inventory.pickUpItem(something, furniturePuzzleMap);
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
        else if (furniturePuzzleMap.get(location).get("furniture_items") != null) {
            ArrayList<String> furniture_items = (ArrayList<String>) furniturePuzzleMap.get(location).get("furniture_items");
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



    // quit game
    public static void quitGame(){
        System.out.println("Quitting the Game. See you next time.");
        System.exit(0);
    }

    // solve puzzle
    public static void solvePuzzle(String loc) {
        Map<String, Object> furniture = furniturePuzzleMap.get(loc);
        location = loc;
        String puzzle_desc = (String) furniture.get("puzzle_desc");
        String puzzle_exist = (String) furniture.get("puzzle_exist");
        String puzzle_verb = (String) furniture.get("puzzle_verb");
        ArrayList<String> puzzle_itemsNeeded = (ArrayList<String>) furniture.get("puzzle_itemsNeeded");
        String puzzle_reward = (String) furniture.get("puzzle_reward");
        ArrayList<Object> puzzle_filename = (ArrayList<Object>) furniture.get("puzzle_filename");
        ArrayList<String> converted_puzzle_filename = (ArrayList<String>) (ArrayList<?>) (puzzle_filename);
        String puzzle_answer = (String) furniture.get("puzzle_answer");
        ArrayList<Object> multiple_puzzle_answer = (ArrayList<Object>) furniture.get("multiple_puzzle_answer");
        ArrayList<String> converted_multiple_puzzle_answer = (ArrayList<String>) (ArrayList<?>) (multiple_puzzle_answer);
        ArrayList<String> puzzle_reward_item = (ArrayList<String>) furniture.get("puzzle_reward_item");
        String puzzle_type = (String) furniture.get("puzzle_type");
        String puzzle_sounds = (String) furniture.get("puzzle_sounds");

        // check if a location has puzzle
        if (puzzle_exist.equals("Y")) {

            if (puzzle_type.equals("riddles")) {
                //  if solved
                if ((inventory.getInvList().contains(puzzle_reward_item.get(0)))|| (inventory.getInvList().contains("key") && loc.equals("safe")) ||
                        (inventory.getInvList().contains("a piece of paper with number 104") && loc.equals("window")) ||
                        (inventory.getInvList().contains("a piece of paper with number 104") && loc.equals("safe"))){
                    System.out.println("The puzzle has been solved. Please feel free to explore other furnitures :)");
                    new_command();
                }
                // if not solved
                else {
                    System.out.println("A puzzle has been found in " + loc + ".");
                    System.out.println(puzzle_desc);
                    System.out.println("Would you like to solve this puzzle now? Y/N");
                    String solve_ans = Prompts.getStringInput();
                    if (solve_ans.equalsIgnoreCase("Y")) {
                        // riddles puzzle
                        Random r = new Random();
                        int randomitem = r.nextInt(converted_puzzle_filename.size());
                        String randomPuzzle = converted_puzzle_filename.get(randomitem);
                        ArrayList<String> randomAnswer = (ArrayList<String>) multiple_puzzle_answer.get(randomitem);
                        FileManager.getResource(randomPuzzle);

                        // Scanner scan = new Scanner(System.in);
                        System.out.println("\nYour answer:      (If it's too hard to answer, please enter [easy] to get a easier question.)");
                        String ans = Prompts.getStringInput();

                        // if user input correct answer
                        if (randomAnswer.contains(ans.toLowerCase())) {
                            System.out.println(furniture.get("puzzle_reward"));
                            Sounds.playSounds(puzzle_sounds,1000);
                            System.out.println("You found " + puzzle_reward_item.get(0) + ".");
                            inventory.pickUpItem(puzzle_reward_item.get(0), furniturePuzzleMap);
                            if (inventory.getInvList().size() >= 5) {
                                System.out.println("Please drop one item. Inventory cannot take more than 5 items.");
                                inventory.dropItem();
                                new_command();
                                inventory.getInvList().add(puzzle_reward_item.get(0));
                                Sounds.playSounds("pick.wav",1000);
                                System.out.println("Added " + puzzle_reward_item.get(0) + " to your inventory");
                                new_command();
                            } else if (inventory.getInvList().size() < 5) {
                                inventory.getInvList().add(puzzle_reward_item.get(0));
                                Sounds.playSounds("pick.wav",1000);
                                System.out.println("Added " + puzzle_reward_item.get(0) + " to your inventory");
                                new_command();
                            }
                            // if user pick easy question
                        } else if (ans.equalsIgnoreCase("easy")) {
                            System.out.println(furniture.get("easy_question"));
                            //Scanner easy = new Scanner(System.in);
                            String easyInput = Prompts.getStringInput();
                            if (easyInput.equals(furniture.get("easy_answer"))) {
                                System.out.println(furniture.get("puzzle_reward"));
                                Sounds.playSounds(puzzle_sounds,1000);
                                System.out.println("You found " + puzzle_reward_item.get(0) + ".");
                                if (inventory.getInvList().size() >= 5) {
                                    System.out.println("Please drop one item. Inventory cannot take more than 5 items.");
                                    System.out.println("Your current inventory: " + inventory);
                                    playerInput();
                                    inventory.getInvList().add(puzzle_reward_item.get(0));
                                    Sounds.playSounds("pick.wav",1000);
                                    System.out.println(puzzle_reward_item.get(0) + "has been added to your inventory");
                                    new_command();

                                } else if (inventory.getInvList().size() < 5) {
                                    inventory.getInvList().add(puzzle_reward_item.get(0));
                                    Sounds.playSounds("pick.wav",1000);
                                    System.out.println("Added " + puzzle_reward_item.get(0) + " to your inventory");
                                    new_command();
                                }
                            }
                        }// if answer is wrong
                        else {
                            System.out.println("you didn't solve the puzzle. Try again later.");
                            new_command();
                        }
                    } else if (solve_ans.equalsIgnoreCase("N")) {
                        new_command();
                    }
                    else {
                        System.out.println("Sorry I don't understand your command. The puzzle has not been solved. Please come back later.");
                        new_command();
                    }
                }

            } else if (puzzle_type.equals("use tool")) {
                //  if solved
                if (inventory.getInvList().contains(puzzle_reward_item.get(0))) {
                    System.out.println("The puzzle has been solved. Please feel free to explore other furnitures :)");
                    new_command();
                } else if ((inventory.getInvList().contains("key") && loc.equals("safe")) ||
                        (inventory.getInvList().contains("a piece of paper with number 104") && loc.equals("window")) ||
                        (inventory.getInvList().contains("a piece of paper with number 104") && loc.equals("safe"))) {
                    System.out.println("The puzzle has been solved. Please feel free to explore other furniture :)");
                    new_command();
                } else {
                    System.out.println("A puzzle has been found in " + loc + ".");
                    System.out.println(puzzle_desc);
                    System.out.println("Would you like to solve this puzzle now? Y/N");
                    String solve_ans = Prompts.getStringInput();
                    if (solve_ans.equalsIgnoreCase("Y")) {
                        System.out.println("You need to use an item from your inventory. Let's see if you got needed item in your inventory...");
                        System.out.println("Your current inventory: " + inventory.getInvList());
                        if (!inventory.getInvList().contains(puzzle_itemsNeeded.get(0))) {
                            System.out.println("Sorry, you don't have the tools. Explore the room and see if you can find anything");
                            new_command();
                        } else if (inventory.getInvList().contains(puzzle_itemsNeeded.get(0))) {
                            //Scanner scan = new Scanner(System.in);
                            System.out.println("Which of the item you'd like to use?");
                            String ans = Prompts.getStringInput();
                            if ((ans.equalsIgnoreCase(puzzle_verb + " " + puzzle_itemsNeeded.get(0)))|| ans.equalsIgnoreCase(puzzle_itemsNeeded.get(0))) {
                                Sounds.playSounds(puzzle_sounds,1000);
                                System.out.println(puzzle_reward + " and you've found " + puzzle_reward_item.get(0));
                                    inventory.getInvList().remove(puzzle_itemsNeeded.get(0));
                                    inventory.getInvList().add(puzzle_reward_item.get(0));
                                    Sounds.playSounds("pick.wav",1000);
                                    System.out.println("Added " + puzzle_reward_item.get(0) + " to your inventory");
                                    new_command();
                                }

                            else if((inventory.getInvList().contains(ans) &&(!ans.equals(puzzle_itemsNeeded)))){
                                System.out.println("Wrong item. The puzzle has not been solved. Please come back later.");
                                new_command();
                            }
                            else {
                                System.out.println("Sorry I don't understand your command. The puzzle has not been solved. Please come back later.");
                                new_command();
                            }
                        }
                    } else if (solve_ans.equalsIgnoreCase("N")) {
                        new_command();

                    } else {
                        System.out.println("Sorry I don't understand your command. The puzzle has not been solved. Please come back later.");
                        new_command();
                    }
                }

            } else if (puzzle_type.equals("final")) {
                System.out.println(puzzle_desc);

                System.out.println("What's the password? You have " + MAGENTA_UNDERLINE + max_attempts + RESET + " attempts remaining. If you's like to try later, enter[later]");
                Scanner scan = new Scanner(System.in);
                String ans = scan.nextLine();

                if (ans.trim().equals("later") || ans.trim().equals("")) {
                    System.out.println("No worries! Try next time!");
                    new_command();

                } else {
                    while (max_attempts-- > 0) {
                        if (ans.trim().equals(puzzle_answer)) {
                            System.out.println(puzzle_reward);
                            Sounds.playSounds(puzzle_sounds,2000);
                            System.out.println("You won the game! Thanks for playing!");
                            System.exit(0);

                        } else if (max_attempts == 0) {
                            System.out.println("You loss the game! You are Trapped. Please try again later.");
                            System.exit(0);
                        } else {
                            System.out.println("Wrong password. Try again next time! " + MAGENTA_UNDERLINE + max_attempts + RESET + " attempts remaining");
                            new_command();
                        }
                    }
                }
            }
        }else if (puzzle_exist.equals("N")) {
                new_command();
            }
        }

    public static void playerInput() {

        userInput = Prompts.getStringInput(); // gets userInput as a string from Prompts
        // now extract verb/nouns from parsedInput
        verb = TextParser.getVerb(userInput);
        nouns = TextParser.getNouns(userInput);

        Map<String, Object> furniture = furniturePuzzleMap.get(location);
        ArrayList<String> inventoryItems = (ArrayList<String>) furniture.get("furniture_items");
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
                else if (furniturePuzzleMap.containsKey(nouns.get(0))) {
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

    public static void moveDirection(String direction) {
        Prompts.ClearConsole();
        // set location to new location based on direction typed
        location = (String) furniturePuzzleMap.get(location).get(direction);
        Map<String, Object> new_furniture = furniturePuzzleMap.get(location);
        String furniture_desc = (String) new_furniture.get("furniture_desc");
        String furniture_picture = (String) new_furniture.get("furniture_picture");
        FileManager.getResource(furniture_picture);
        System.out.println("Now you are in front of " + location);
        System.out.println(furniture_desc);
        new_command();
    }

    public static void goFurniture(String destinationaLoc) {
        Prompts.ClearConsole();
        Map<String, Object> furniture = furniturePuzzleMap.get(destinationaLoc);
        String furniture_desc = (String) furniture.get("furniture_desc");
        String furniture_picture = (String) furniture.get("furniture_picture");
        FileManager.getResource(furniture_picture);
        System.out.println("\nYou are currently in front of " + destinationaLoc);
        location = destinationaLoc;
        System.out.println(furniture_desc);
        new_command();
    }

    // Removed String as input, seemed redundant
    public static void gameMenu() {
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

    public static void new_command (){
        System.out.println(TextColor.GREEN + "\nWhat you'd like to do next? (type [commands] to check available commands and [help] to see other help items)" + TextColor.RESET);
        playerInput();
    }
}