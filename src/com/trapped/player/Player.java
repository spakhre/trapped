package com.trapped.player;

import com.google.gson.Gson;
import com.trapped.GameEngine;
import com.trapped.utilities.FileManager;
import com.trapped.utilities.Prompts;
import com.trapped.utilities.TextColor;
import com.trapped.utilities.TextParser;
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
    static List<String> inventory = new ArrayList<>();

    static GameEngine game = new GameEngine();
    static boolean incorrectPass = true; // scope
    static int max_attempts = 3;



    // read Json file
    //static Gson gson = new Gson();
    //public static Reader reader;
    // loading json file
/*    static {
        try {
            reader = Files.newBufferedReader(Paths.get("resources/furniture_puzzles.json"));
            //Map<String, Map<String, Object>> map = gson.fromJson(reader, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    static Map<String, Map<String, Object>> map = furniturePuzzleGenerator();

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
        System.out.println("Look around this room. There is a door, a window, a bed, a drawer, a safe, a chair, and a lamp. What do you want to do?");
        playerInput();
    }

    // inspect room, player can inspect either a furniture or an item. If inspect a furniture, the output gonna have: Description of this furniture, items in here.
    // And if the furniture has a puzzle, it will show the puzzle's description.
    public static void inspectItem(String something) {
        location = something;
        Map<String, Object> furniture = map.get(something);
        String furniture_items = (String)furniture.get("furniture_items");
        String furniture_desc = (String) furniture.get("furniture_desc");
        String puzzle_exist = (String) furniture.get("puzzle_exist");


        // if user means inspecting a furniture
        if (map.keySet().contains(something)){
            // Show item not found if : 1. an item has been collected; 2. an item has been use in order to solve a puzzle in another furnitures.
            if((inventory.contains(furniture_items))||(inventory.contains("keyD") && something.equals("safe")) || (inventory.contains("a piece of paper with number 104") && something.equals("keyD")) ||
                    (inventory.contains("a piece of paper with number 104") && something.equals("safe"))){
                System.out.println("Inspecting...\nNo items found here.");
                // see if a puzzle still haven't been resolved.
                solvePuzzle(something);
            }
            // Show item not found if it doesn't have an item as designed.
            else if(furniture_items.equals("")){
                System.out.println(furniture_desc);
                System.out.println("Inspecting...\nNo items found here.");
                solvePuzzle(something);
            }
            // if furniture's item has not been collected yet.
            else if(!inventory.contains(furniture_items)){
                   System.out.println("Inspecting...\nYou found: " + TextColor.RED  + furniture_items+ TextColor.RESET);
                   pickUpItem(something);
                   if (puzzle_exist.equals("Y")){
                        System.out.println("A puzzle has been found in "+something+".");
                        solvePuzzle(something);
                    }
                }
            System.out.println("What would like to do next?");
            playerInput();
        }

        // if user means inspecting an item of current location
        else if (furniture_items.contains(something)){
            System.out.println("It's just a " + furniture_items);
        }
        System.out.println("What would you like to do?");
        playerInput();
    }


    // item disappear from inventory
    public static void useItem(String item) {
       inventory.remove(item);
    }

    // check current inventory
    public static void checkCurrentInventory() {
        System.out.println("Your current inventory: " + inventory);
        System.out.println("What would you like to do next?");
        playerInput();
    }




//    public static void move(String fromLocation) throws IOException, URISyntaxException, InterruptedException, UnsupportedAudioFileException, LineUnavailableException {
//        String[] validDirection = {"left", "right", "up", "down"};
//        // Extract the current furniture to get the inner available directions
//        Map<String, Object> furniture = map.get(fromLocation);
//        List furniture_available_directions = (ArrayList<String>)furniture.get("furniture_available_directions");
//
//        if (furniture.containsKey("picture")){
//            String picture = (String) furniture.get("picture");
//            FileManager.getResource(picture);
//        }
//
//        String furniture_desc = (String) furniture.get("furniture_desc");
//
//        Scanner scan = new Scanner(System.in);
//        if(furniture.keySet().contains(furniture.get("furniture_sounds"))) {
//            if (!furniture.get("furniture_sounds").equals("")) {
//                String soundFileName = (String) furniture.get("furniture_sounds");
//                Sounds.playSounds(soundFileName);
//            } else {
//                ;
//            }
//        }
//        System.out.println("\nYou are currently in front of " +  TextColor.RED + fromLocation + TextColor.RESET);
//
//        System.out.println(furniture_desc);
//        inspectItem(fromLocation);
//        pickUpItem(fromLocation);
//        checkCurrentInventory();
//        solvePuzzle(fromLocation);
//        System.out.println("Which direction do you want to go? Available directions: " + furniture_available_directions + " (or enter [quit] to quit game, " +
//                "[play again] to replay the game,[drop] an item from current inventory)");
//        String dir = scan.nextLine();
//
//        //quitGame(dir);
//        //playAgain(dir);
//
//        boolean contain = furniture_available_directions.contains(dir);
//        if (Arrays.asList(validDirection).contains(dir)) {
//            if (contain) {
//                String newlocation = (String) furniture.get(dir);
//                fromLocation = newlocation;
//                System.out.println("Now you are in front of " + fromLocation);
//            } else {
//                System.out.println("Sorry, you can't go that way. Please select again.");
//            }
//        }
//        else if(dir.equals("drop")){
//            //  dropItem();
//            checkCurrentInventory();
//        }
//        else {
//            System.out.println("invalid input, please try again.");
//        }
//
//
//    }

    // pickup item method.
    public static void pickUpItem(String location) {
        Map<String, Object> furniture = map.get(location);
        String furniture_items = (String)furniture.get("furniture_items");
        // if inventory is full. player need to drop an item, then item found in current location will be added to inventory.
        if(inventory.size()==5){
            System.out.println("inventory cannot take 5 or more items. Please drop one item.");
            System.out.println("Your current inventory is: "+inventory);
            dropItem();
            inventory.add(furniture_items);
            System.out.println(furniture_items + " has been added to your inventory");
        }


        // if inventory is not full
        else {
            //if furniture has no item available to be picked up
            if (furniture_items.isEmpty()) {
                System.out.println(location + " is empty. Nothing can be added.");
            }
            //if furniture has an item available to be picked up
            else if (!inventory.contains(furniture_items)) {
                System.out.println("\nDo you want to add " +furniture_items+ " to inventory? [Y/N]");
                Scanner scan = new Scanner(System.in);
                String response = scan.nextLine();
                if (response.equalsIgnoreCase("Y")) {
                    System.out.println(furniture_items + " has been picked up and added to your inventory");
                    inventory.add(furniture_items);
                } else if (response.equalsIgnoreCase("N")) {
                    System.out.println("You did not pick anything from " + location);
                }
            }
        }
        System.out.println("What you'd like to do next? [zoe note: test in pickupitem method]");
        playerInput();
    }

    // check current location
    public static void checkCurrentLocation() {
        System.out.println("Your current location: "+location);
        playerInput();
    }

    // quit game
    public static void quitGame(){
        System.out.println("Quiting game... Have a nice day!");
        System.exit(0);
    }

    // play again
    public static void playAgain(String input) {
        if (input.equals("again") || (input.equals("play again"))){
            GameEngine.startGame();
        }
    }

    // Drop item -- will provide current inventory first then let player pick. This method will be used when inventory is full and player being asked to drop an item.
    public static void dropItem() {
            System.out.println("Your inventory: " + inventory);
            // Scanner scan = new Scanner(System.in);
            System.out.println("Which item you'd like to drop? Please enter item name. ");
            String selected_drop = Prompts.getStringInput(); // scan.nextLine();
            if (inventory.contains(selected_drop.toLowerCase())) {
                inventory.remove(selected_drop);
                System.out.println(selected_drop + " has been dropped from your inventory.");
                System.out.println("What you'd like to do next?");
                playerInput();
            } else {
                while (!inventory.contains(selected_drop.toLowerCase())) {
                    System.out.println("Sorry, the item you entered is not in inventory, please select again.");
                    selected_drop = Prompts.getStringInput();
                }
                inventory.remove(selected_drop);
                System.out.println(selected_drop + " has been dropped from your inventory.");
                System.out.println("What you'd like to do next?");
                playerInput();
            }
    }

    // Drop a specific item - this will be used when player input "drop xxx"
    public static void dropSpecificItem(String item) {
            if (inventory.contains(item.toLowerCase())) {
            inventory.remove(item);
            System.out.println(item + " has been dropped from your inventory.");
            System.out.println("What you'd like to do next?");
            playerInput();
        } else {
                System.out.println("Sorry, the item you entered is not in inventory, please select again.");
                dropItem();
            }
        }
//        public static void addItemToCurrentLocation(String item, String loc){
//            Map<String, Object> furniture = map.get(loc);
//            String furniture_items = (String) furniture.get("furniture_items");
//            furniture_items+=item;
//
//
//
//        }

    // solve puzzle
    public static void solvePuzzle(String loc) {
        Map<String, Object> furniture = map.get(loc);
        location = loc;
        String puzzle_desc = (String) furniture.get("puzzle_desc");
        String puzzle_exist = (String) furniture.get("puzzle_exist");
        String puzzle_verb = (String) furniture.get("puzzle_verb");
        String puzzle_itemsNeeded = (String) furniture.get("puzzle_itemsNeeded");
        String puzzle_reward = (String) furniture.get("puzzle_reward");
        ArrayList<Object> puzzle_filename = (ArrayList<Object>) furniture.get("puzzle_filename");
        ArrayList<String> converted_puzzle_filename = (ArrayList<String>) (ArrayList<?>) (puzzle_filename);

        String puzzle_answer = (String) furniture.get("puzzle_answer");

        ArrayList<Object> multiple_puzzle_answer = (ArrayList<Object>) furniture.get("multiple_puzzle_answer");
        ArrayList<String> converted_multiple_puzzle_answer = (ArrayList<String>) (ArrayList<?>) (multiple_puzzle_answer);
        String puzzle_reward_item = (String) furniture.get("puzzle_reward_item");


        //check if the furniture has puzzle
        // if furniture has a puzzle, then execute.
        if (puzzle_exist.equals("Y")) {
            // puzzle has been resolved
            if ((inventory.contains(puzzle_reward_item)) ||(inventory.contains("keyD") && loc.equals("safe"))||
              (inventory.contains("a piece of paper with number 104") && loc.equals("keyD")) ||
                    (inventory.contains("a piece of paper with number 104") && loc.equals("safe"))) {
                        System.out.println("The puzzle has been solved. Please feel free to explore other furnitures :)");
            }
            // puzzle need to be resolved
            else {
                // introduce the puzzle and ask if player would like to solve it
                System.out.println(puzzle_desc);
                //Scanner solve = new Scanner(System.in);
                System.out.println("Would you like to solve this puzzle now? Y/N");
                String solve_ans = Prompts.getStringInput();
                // if player is willing to solve now
            if (solve_ans.equalsIgnoreCase("Y")) {
                // riddles puzzle
                if (puzzle_filename.size() != 0) {
                    Random r = new Random();
                    int randomitem = r.nextInt(converted_puzzle_filename.size());
                    String randomPuzzle = converted_puzzle_filename.get(randomitem);
                    String randomAnswer = converted_multiple_puzzle_answer.get(randomitem);
                    FileManager.getResource(randomPuzzle);

                    // Scanner scan = new Scanner(System.in);
                    System.out.println("\nYour answer:      (If it's too hard to answer, please enter [easy] to get a easier question.)");
                    String ans = Prompts.getStringInput();

                    // if user input correct answer
                    if (ans.equals(randomAnswer)) {
                        System.out.println(furniture.get("puzzle_reward"));
                        System.out.println("You found " + puzzle_reward_item + ".");
                        pickUpItem(puzzle_reward_item);
                        if(inventory.size()==5){
                            System.out.println("Please drop one item. Inventory cannot take 5 or more items.");
                            dropItem();
                            inventory.add(puzzle_reward_item);
                            System.out.println("Added " + puzzle_reward_item + " to your inventory");
                            System.out.println("What you'd like to do next?");
                            playerInput();
                        } else if (inventory.size() < 5) {
                            inventory.add(puzzle_reward_item);
                            System.out.println("Added " + puzzle_reward_item + " to your inventory");
                            System.out.println("What you'd like to do next?");
                            playerInput();
                        }
                    // if user pick easy question
                    } else if (ans.equalsIgnoreCase("easy")) {
                        System.out.println(furniture.get("easy_question"));
                        //Scanner easy = new Scanner(System.in);
                        String easyInput = Prompts.getStringInput();
                        if (easyInput.equals(furniture.get("easy_answer"))) {
                            System.out.println(furniture.get("puzzle_reward"));
                            System.out.println("You found " + puzzle_reward_item + ".");
                            if(inventory.size()==5) {
                                System.out.println("Please drop one item. Inventory cannot take 5 or more items.");
                                System.out.println("Your current inventory: "+inventory);
                                playerInput();
                                inventory.add(puzzle_reward_item);
                                System.out.println(puzzle_reward_item + "has been added to your inventory");
                                System.out.println("What you'd like to do next?");
                                playerInput();

                            }else if (inventory.size() < 5) {
                                inventory.add(puzzle_reward_item);
                                System.out.println("Added " + puzzle_reward_item + " to your inventory");
                                System.out.println("What you'd like to do next?");
                                playerInput();
                            }
                        }
                    }// if answer is wrong
                    else {
                        System.out.println("you didn't solve the puzzle. Try again later.");
                        System.out.println("What you'd like to do next?");
                        playerInput();
                    }
                }
                // door puzzle
                else if (loc.equals("door")) {
                    System.out.println(puzzle_desc);

                    System.out.println("What's the password? You have " +MAGENTA_UNDERLINE+ max_attempts +RESET +  " attempts remaining. If you's like to try later, enter[later]");
                    Scanner scan = new Scanner(System.in);
                    String ans = scan.nextLine();

                    if (ans.trim().equals("later")|| ans.trim().equals("")){
                        System.out.println("No worries! Try next time!");
                        playerInput();

                    } else{
                        while( max_attempts-- > 0 ){ 
                            if (ans.trim().equals(puzzle_answer)) {
                                System.out.println(puzzle_reward);
                                System.out.println("You won the game! Thanks for playing!");
                                System.exit(0);

                            } else if (max_attempts <= 0){
                                System.out.println("You loss the game! Do you want to play again?");
                                String reply = scan.nextLine();
                                if(reply.equalsIgnoreCase("Y") || reply.equalsIgnoreCase("yes")){
                                    //restart the game
                                    //clearScreen();
                                    //GameEngine.startGame();
                                }else{

                                    System.exit(0);
                                }

                            } else {
                                System.out.println("Wrong password. Try again next time! " +MAGENTA_UNDERLINE+ max_attempts +RESET + " attempts remaining");
                                System.out.println("What would you like to do?");
                                playerInput();
                            }
                        }
                    }
                }
                // use item to solve puzzle
                else if(((String) furniture.get("puzzle_verb")).equalsIgnoreCase("use")){
                        System.out.println("You need to use an item from your inventory. Let's see if you got needed item in your inventory...");
                        System.out.println("Your current inventory: "+inventory);
                        if(!inventory.contains(puzzle_itemsNeeded)){
                            System.out.println("Sorry, you don't have the tools. Explore the room and see if you can find anything");
                            System.out.println("What you'd like to do next?");
                            playerInput();
                        }
                        else if (inventory.contains(puzzle_itemsNeeded)) {
                            //Scanner scan = new Scanner(System.in);
                            String ans = Prompts.getStringInput();
                            if (ans.equalsIgnoreCase(puzzle_verb + " " + puzzle_itemsNeeded)) {
                                System.out.println(puzzle_reward+ " and you've found "+puzzle_reward_item);
                                if(inventory.size()==5){
                                    System.out.println("Please drop one item. Inventory cannot take 5 or more items.");
                                    dropItem();
                                    inventory.add(puzzle_reward_item);
                                    inventory.remove(puzzle_itemsNeeded);
                                    System.out.println("Added " + puzzle_reward_item + " to your inventory");
                                    System.out.println("What you'd like to do next?");
                                    playerInput();
                                }else if (inventory.size() < 5) {
                                    inventory.add(puzzle_reward_item);
                                    inventory.remove(puzzle_itemsNeeded);
                                    System.out.println("Added " + puzzle_reward_item + " to your inventory");
                                    System.out.println("What you'd like to do next?");
                                    playerInput();
                                }

                            }
                        }
                }

                // If there is no puzzle in current location, nothing happens.
            } else if (solve_ans.equals("N")) {
                System.out.println("What you'd like to do next?");
                playerInput();

            }
            }
        }


    }




    //solve puzzle


    public static void playerInput() {
        userInput = Prompts.getStringInput(); // gets userInput as a string from Prompts
        // now extract verb/nouns from parsedInput
        verb = TextParser.getVerb(userInput);
        nouns = TextParser.getNouns(userInput);

        //Reader reader = Files.newBufferedReader(Paths.get("./resources/cfg/verbs.json"));
        //Gson gson = new Gson();
        //Map<String, ArrayList<String>> map1 = gson.fromJson(reader, Map.class);

        Map<String, Object> furniture = map.get(location);
        String furniture_items = (String) furniture.get("furniture_items");
        String puzzle_reward_item = (String) furniture.get("puzzle_reward_item");

        // start
        //Object start = map1.keySet().toArray()[0];
        //Object start_value = map1.get(start);
        if (verb.equals("start")) {    // || start_value.toString().contains(verb)) {
            if (nouns.contains("game") || (nouns.isEmpty())) {
                GameEngine.startGame();
            }
        }


        // quit
        //Object quit = map1.keySet().toArray()[1];
        //Object quit_value = map1.get(quit);

        if (verb.equals("quit")) {          // || quit_value.toString().contains(verb)) {
            if (nouns.contains("game") || (nouns.isEmpty())) {
                quitGame();
            }
        }

        // go
        //Object go = map1.keySet().toArray()[2];
        //Object go_value = map1.get(go);
        if (verb.equals("go")) {    // || go_value.toString().contains(verb)) {
            // Currently this is only pointing to the first index of the parsed array
            if (map.keySet().contains(nouns.get(0))) {
                goFurniture(nouns.get(0));
            }
            else if (nouns.get(0).equals("left")||(nouns.get(0).equals("right"))){
                moveDirection(nouns.get(0));
            }
        }

        // get
        //Object get = map1.keySet().toArray()[3];
        //Object get_value = map1.get(get);
        if (verb.equals("get")) {   // || get_value.toString().contains(verb)) {
            if (furniture_items.contains(nouns.get(0))) {
                pickUpItem(location);
            }
            else if (puzzle_reward_item.contains(nouns.get(0))){
                inventory.add(nouns.get(0));
            }
            else {
                System.out.println("item not found. Please enter again");
            }
        }


        // inspect
        //Object inspect = map1.keySet().toArray()[4];
        //Object inspect_value = map1.get(inspect);

        if (verb.equals("inspect")) {   // || inspect_value.toString().contains(verb)) {
            inspectItem(nouns.get(0));
        }


        // help
        //Object help = map1.keySet().toArray()[6];
        //Object help_value = map1.get(help);

        if (verb.equals("help")) {  // || help_value.toString().contains(verb)) {
            gameMenu();
            playerInput();
        }

        // use
        //Object use = map1.keySet().toArray()[6];
        //Object use_value = map1.get(help);

        if (verb.equals("use")) {   // || use_value.toString().contains(verb)) {
            useItem(nouns.get(0));
        }

        // drop item
        //Object drop = map1.keySet().toArray()[7];
        //Object drop_value = map1.get(drop);
        if (verb.equals("drop")) {  // || drop_value.toString().contains(verb)) {
            // TODO: What does this do, verb should never equal noun
            if(verb.equals(nouns.get(0))){
                dropItem();
            }
            else{
                dropSpecificItem(nouns.get(0));
            }
        }

        if (verb.equals("check") && nouns.get(0).equals("inventory")){
            checkCurrentInventory();
        }

        if (verb.equals("check") && nouns.get(0).equals("location")) {
            checkCurrentLocation();
        }


        //reader.close();

    }

    public static void moveDirection(String direction) {

        Map<String, Object> furniture = map.get(location);

        String newlocation = (String) furniture.get(direction);
        location = newlocation;
        System.out.println("Now you are in front of " + newlocation);
        System.out.println("What you'd like to do next?");
        playerInput();
    }

    public static void goFurniture(String destinationaLoc) {
        Map<String, Object> furniture = map.get(destinationaLoc);
        String furniture_desc = (String) furniture.get("furniture_desc");
        System.out.println("\nYou are currently in front of " + destinationaLoc);
        location = destinationaLoc;
        System.out.println(furniture_desc);
        System.out.println("What would you like to do?");
        playerInput();
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
        String selection = Prompts.getStringInput();
        if (selection.equalsIgnoreCase("where am I?") || selection.equals("1")) {
            System.out.println("you are at " + location);
        }
        else if (selection.equalsIgnoreCase("Restart")|| selection.equals("2")) {
            System.out.println("Game Restarting");
            GameEngine.startGame();
        }
        else if (selection.equalsIgnoreCase("continue")|| selection.equals("3")) {
            System.out.println("Returning to game");
            System.out.println("What you'd like to do next?");
            playerInput();
        }
        else if (selection.equalsIgnoreCase("exit")|| selection.equals("4")) {
            System.out.println("Quitting the Game. See you next time.");
            quitGame();
        }
    }

}




