package com.trapped.player;

import com.google.gson.Gson;
import com.trapped.GameEngine;
import com.trapped.utilities.*;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static com.trapped.utilities.TextColor.MAGENTA_UNDERLINE;
import static com.trapped.utilities.TextColor.RESET;

public class Player implements Serializable{
    // Needed to move, were out of scope in one section
    public static String userInput;
    public static String verb;
    public static ArrayList<String> nouns = new ArrayList<>();
    //Map<String, Items> inventory = new HashMap<String, Items>(); // player's inventory
    public static String location="bed";
    static List<String> inventory = new ArrayList<>();
    static List<String> rewarded_item = List.of(new String[]{"crowbar", "key", "a piece of paper with number 104"});

    static GameEngine game = new GameEngine();
    static boolean incorrectPass = true; // scope
    static int max_attempts = 3;



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
        new_command();
    }

    // inspect room, player can inspect either a furniture or an item. If inspect a furniture, the output gonna have: Description of this furniture, items in here.
    // And if the furniture has a puzzle, it will show the puzzle's description.
    public static void inspectItem(String something) {
        //furniture
        if(something.equals("inventory")){
            checkCurrentInventory();
        }

        else if (map.containsKey(something)) {
            location = something;
            Map<String, Object> furniture = map.get(something);
            String furniture_desc = (String) furniture.get("furniture_desc");
            String furniture_picture = (String) furniture.get("furniture_picture");


            if (furniture.get("furniture_items") != null) {
                ArrayList<String> furniture_items = (ArrayList<String>) furniture.get("furniture_items");
                if(!furniture_items.isEmpty()){
                    if (inventory.contains(furniture_items.get(0))){
                        FileManager.getResource(furniture_picture);
                        System.out.println("Inspecting...\nNo items found here in "+something);
                        solvePuzzle(something);
                    }
                    else if (!inventory.contains(furniture_items.get(0))){
                        FileManager.getResource(furniture_picture);
                        System.out.println("Inspecting...\nYou found: " + TextColor.RED + furniture_items.get(0) + TextColor.RESET);
                        pickUpItem(something);
                        solvePuzzle(something);
                    }

            }
                if(furniture_items.isEmpty()){
                    FileManager.getResource(furniture_picture);
                    System.out.println("Inspecting...\nNo items found here.");
                    solvePuzzle(something);
                }
            }
            else if (furniture.get("furniture_items") == null){
                FileManager.getResource(furniture_picture);
                System.out.println("Inspecting...\nNo items found here.");
                solvePuzzle(something);
            }
        }

        //item

        else if (map.get(location).get("furniture_items") != null) {
            ArrayList<String> furniture_items = (ArrayList<String>) map.get(location).get("furniture_items");
            if (furniture_items.contains(something)) {
                System.out.println("It's just a " + something);
                new_command();
            }
            else if (inventory.contains(something)){
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
    public static void useItem(String item) {
        inventory.remove(item);
    }

    // check current inventory
    public static void checkCurrentInventory() {
        System.out.println("Your current inventory: " + inventory);
        new_command();
    }



    // pickup item method.
    public static void pickUpItem(String location) {

        if (map.get(location) != null) {
            Map<String, Object> furniture = map.get(location);
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
    public static void dropSpecificItem(String item) {
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
    public static void solvePuzzle(String loc) {
        Map<String, Object> furniture = map.get(loc);
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
                if ((inventory.contains(puzzle_reward_item.get(0)))|| (inventory.contains("key") && loc.equals("safe")) ||
                        (inventory.contains("a piece of paper with number 104") && loc.equals("window")) ||
                        (inventory.contains("a piece of paper with number 104") && loc.equals("safe"))){
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
                            pickUpItem(puzzle_reward_item.get(0));
                            if (inventory.size() >= 5) {
                                System.out.println("Please drop one item. Inventory cannot take more than 5 items.");
                                dropItem();
                                inventory.add(puzzle_reward_item.get(0));
                                Sounds.playSounds("pick.wav",1000);
                                System.out.println("Added " + puzzle_reward_item.get(0) + " to your inventory");
                                new_command();
                            } else if (inventory.size() < 5) {
                                inventory.add(puzzle_reward_item.get(0));
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
                                if (inventory.size() >= 5) {
                                    System.out.println("Please drop one item. Inventory cannot take more than 5 items.");
                                    System.out.println("Your current inventory: " + inventory);
                                    playerInput();
                                    inventory.add(puzzle_reward_item.get(0));
                                    Sounds.playSounds("pick.wav",1000);
                                    System.out.println(puzzle_reward_item.get(0) + "has been added to your inventory");
                                    new_command();

                                } else if (inventory.size() < 5) {
                                    inventory.add(puzzle_reward_item.get(0));
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
                if (inventory.contains(puzzle_reward_item.get(0))) {
                    System.out.println("The puzzle has been solved. Please feel free to explore other furnitures :)");
                    new_command();
                } else if ((inventory.contains("key") && loc.equals("safe")) ||
                        (inventory.contains("a piece of paper with number 104") && loc.equals("window")) ||
                        (inventory.contains("a piece of paper with number 104") && loc.equals("safe"))) {
                    System.out.println("The puzzle has been solved. Please feel free to explore other furniture :)");
                    new_command();
                } else {
                    System.out.println("A puzzle has been found in " + loc + ".");
                    System.out.println(puzzle_desc);
                    System.out.println("Would you like to solve this puzzle now? Y/N");
                    String solve_ans = Prompts.getStringInput();
                    if (solve_ans.equalsIgnoreCase("Y")) {
                        System.out.println("You need to use an item from your inventory. Let's see if you got needed item in your inventory...");
                        System.out.println("Your current inventory: " + inventory);
                        if (!inventory.contains(puzzle_itemsNeeded.get(0))) {
                            System.out.println("Sorry, you don't have the tools. Explore the room and see if you can find anything");
                            new_command();
                        } else if (inventory.contains(puzzle_itemsNeeded.get(0))) {
                            //Scanner scan = new Scanner(System.in);
                            System.out.println("Which of the item you'd like to use?");
                            String ans = Prompts.getStringInput();
                            if ((ans.equalsIgnoreCase(puzzle_verb + " " + puzzle_itemsNeeded.get(0)))|| ans.equalsIgnoreCase(puzzle_itemsNeeded.get(0))) {
                                Sounds.playSounds(puzzle_sounds,1000);
                                System.out.println(puzzle_reward + " and you've found " + puzzle_reward_item.get(0));
                                    inventory.remove(puzzle_itemsNeeded.get(0));
                                    inventory.add(puzzle_reward_item.get(0));
                                    Sounds.playSounds("pick.wav",1000);
                                    System.out.println("Added " + puzzle_reward_item.get(0) + " to your inventory");
                                    new_command();
                                }

                            else if((inventory.contains(ans) &&(!ans.equals(puzzle_itemsNeeded)))){
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

                        } else if (max_attempts <= 0) {
                            System.out.println("You loss the game! Do you want to play again?");
                            String reply = scan.nextLine();
                            if (reply.equalsIgnoreCase("Y") || reply.equalsIgnoreCase("yes")) {
                                //restart the game
                                //clearScreen();
                                //GameEngine.startGame();
                            } else {
                                System.exit(0);
                            }

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


        Map<String, Object> furniture = map.get(location);
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
//            if (verb.equalsIgnoreCase("start")) {    // || start_value.toString().contains(verb)) {
//                if (nouns.contains("game") || (nouns.isEmpty())) {
//                    GameEngine.startGame();
//                }
//            }

            if (verb.equals("commands")){
                FileManager.getResource("commands.txt");
                playerInput();
            }

            else if (verb.equals("quit")) {          // || quit_value.toString().contains(verb)) {
                if (nouns.contains("game") || (nouns.isEmpty())) {
                    quitGame();
                }
            }

            // go
            //Object go = map1.keySet().toArray()[2];
            //Object go_value = map1.get(go);
            else if (verb.equals("go")) {    // || go_value.toString().contains(verb)) {
                // Currently this is only pointing to the first index of the parsed array
                if (nouns.isEmpty()){
                    System.out.println("Sorry, I don't understand your input, please enter again. ");
                    FileManager.getResource("commands.txt");
                    playerInput();
                }
                else if (map.containsKey(nouns.get(0))) {
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

            // get
            //Object get = map1.keySet().toArray()[3];
            //Object get_value = map1.get(get);
            else if (verb.equals("get")) {   // || get_value.toString().contains(verb)) {
                if (nouns.isEmpty()) {
                    System.out.println("Sorry, I don't understand your input, please enter again. ");
                    FileManager.getResource("commands.txt");
                    playerInput();
                } else {
                    if (furniture_items.contains(nouns.get(0))) {
                        pickUpItem(location);
                        new_command();
                    }
                    else if (puzzle_reward_item.contains(nouns.get(0))) {
                        inventory.add(nouns.get(0));
                    }
                    else{
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
                if (inventory.isEmpty()) {
                    System.out.println("Sorry, your inventory is empty now and you cannot drop item.");
                    new_command();
                }
                else if (nouns.isEmpty()) {
                    dropItem();
                }
                else {
                    dropSpecificItem(nouns.get(0));
                }
            }

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

        Map<String, Object> furniture = map.get(location);
        String newlocation = (String) furniture.get(direction);
        location = newlocation;
        Map<String, Object> new_furniture = map.get(newlocation);
        String furniture_desc = (String) new_furniture.get("furniture_desc");
        String furniture_picture = (String) new_furniture.get("furniture_picture");
        FileManager.getResource(furniture_picture);
        System.out.println("Now you are in front of " + newlocation);
        System.out.println(furniture_desc);
        new_command();
    }

    public static void goFurniture(String destinationaLoc) {
        Map<String, Object> furniture = map.get(destinationaLoc);
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
        String selection = Prompts.getStringInput();
        if (selection.equalsIgnoreCase("where am I?") || selection.equals("1")) {
            System.out.println("you are at " + location);
        } else if (selection.equals("2")) {
            checkCurrentInventory();

        }
        else if (selection.equals("3")) {
            System.out.println("zoe test...");
            System.out.println("What you'd like to do");
            playerInput();

        }else if (selection.equalsIgnoreCase("continue") || selection.equals("4")) {
            System.out.println("Returning to game");
            new_command();
        } else if (selection.equalsIgnoreCase("exit") || selection.equals("5")) {
            System.out.println("Quitting the Game. See you next time.");
            quitGame();
        }
    }
    public static void new_command (){
        System.out.println("What you'd like to do next? (type [commands] to check available commands and [help] to see other help items)");
        playerInput();
    }

}




