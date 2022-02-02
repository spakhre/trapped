package com.trapped.player;

import com.google.gson.Gson;
import com.trapped.GameEngine;
import com.trapped.utilities.FileManager;
import com.trapped.utilities.Sounds;
import com.trapped.utilities.TextColor;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Player implements Serializable{
    //Map<String, Items> inventory = new HashMap<String, Items>(); // player's inventory
    public static String location="bed";
    static List<String> inventory = new ArrayList<>();
    static GameEngine game = new GameEngine();

    // read Json file
    static Gson gson = new Gson();
    static Reader reader;

    static {
        try {
            reader = Files.newBufferedReader(Paths.get("resources/furniture_puzzles.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static Map<String, Map<String, Object>> map = gson.fromJson(reader, Map.class);

    // constructor1
    public Player() throws IOException {

    }
    public static void viewRoom() throws UnsupportedAudioFileException, LineUnavailableException, IOException, URISyntaxException, InterruptedException {
        System.out.println("\nYou are currently in front of " + TextColor.RED  + location + TextColor.RESET);

        System.out.println("Look around this room. There is a door, a window, a bed, a drawer, a safe, a chair, and a lamp. What do you want to do?");

        playerInput();
    }
    public static void inspectItem(String something) throws UnsupportedAudioFileException, LineUnavailableException, IOException, URISyntaxException, InterruptedException {
        //Map<String, Object> furniture_inspectItem = map.get(location);
       // String furniture_items1 = (String)furniture_inspectItem.get("furniture_items");
        location = something;
        Map<String, Object> furniture = map.get(something);
        String furniture_items = (String)furniture.get("furniture_items");
        String furniture_desc = (String) furniture.get("furniture_desc");
        String puzzle_exist = (String) furniture.get("puzzle_exist");


        // if user means inspecting furniture
        if (map.keySet().contains(something)){
            if((inventory.contains(furniture_items))||(inventory.contains("keyD") && something.equals("safe")) || (inventory.contains("a piece of paper with number 104") && something.equals("keyD")) ||
                    (inventory.contains("a piece of paper with number 104") && something.equals("safe"))){
                System.out.println("Inspecting...\nNo items found here.");
                solvePuzzle(something);
            }
            else if(furniture_items.equals("")){
                System.out.println(furniture_desc);
                System.out.println("Inspecting...\nNo items found here.");
                solvePuzzle(something);
            }else {
                if (inventory.contains(furniture_items)) {
                    System.out.println("Inspecting...\n" + something + " is empty.");
                    solvePuzzle(something);
                } else {
                    System.out.println("Inspecting...\nYou found: " + TextColor.RED  + furniture_items+ TextColor.RESET);
//                    pickUpItem(something);
                    playerInput();
                    if (puzzle_exist.equals("Y")){
                        System.out.println("A puzzle has been found in "+something+".");
                        solvePuzzle(something);
                    }
                    playerInput();

                }
            }

        }

        // if user means inspecting item of current location
        else if ((furniture_items.contains(something))) {
            System.out.println("it's just a " + furniture_items);
        }
        System.out.println("What would you like to do?");
        playerInput();
    }


    // item disappear from inventory and json
    public static void useItem(String item, String loc) {
       inventory.remove(item);
    }

    public static void checkCurrentInventory() throws UnsupportedAudioFileException, LineUnavailableException, IOException, URISyntaxException, InterruptedException {
        System.out.println("Your current inventory: " + inventory);
        playerInput();
    }




    public static void move(String fromLocation) throws IOException, URISyntaxException, InterruptedException, UnsupportedAudioFileException, LineUnavailableException {

        String[] validDirection = {"left", "right", "up", "down"};

        // Extract the current furniture to get the inner available directions
        Map<String, Object> furniture = map.get(fromLocation);
        List furniture_available_directions = (ArrayList<String>)furniture.get("furniture_available_directions");


        if (furniture.containsKey("picture")){
            String picture = (String) furniture.get("picture");
            FileManager.getResource(picture);
        }

        String furniture_desc = (String) furniture.get("furniture_desc");

        Scanner scan = new Scanner(System.in);
        if(furniture.keySet().contains(furniture.get("furniture_sounds"))) {
            if (!furniture.get("furniture_sounds").equals("")) {
                String soundFileName = (String) furniture.get("furniture_sounds");
                Sounds.playSounds(soundFileName);
            } else {
                ;
            }
        }
        System.out.println("\nYou are currently in front of " +  TextColor.RED + fromLocation + TextColor.RESET);

        System.out.println(furniture_desc);
        inspectItem(fromLocation);
        pickUpItem(fromLocation);
        checkCurrentInventory();
        solvePuzzle(fromLocation);
        System.out.println("Which direction do you want to go? Available directions: " + furniture_available_directions + " (or enter [quit] to quit game, " +
                "[play again] to replay the game,[drop] an item from current inventory)");
        String dir = scan.nextLine();

        //quitGame(dir);
        //playAgain(dir);

        boolean contain = furniture_available_directions.contains(dir);
        if (Arrays.asList(validDirection).contains(dir)) {
            if (contain) {
                String newlocation = (String) furniture.get(dir);
                fromLocation = newlocation;
                System.out.println("Now you are in front of " + fromLocation);
            } else {
                System.out.println("Sorry, you can't go that way. Please select again.");
            }
        }
        else if(dir.equals("drop")){
            //  dropItem();
            checkCurrentInventory();
        }
        else {
            System.out.println("invalid input, please try again.");
        }


    }
    public static void pickUpItem(String location) throws UnsupportedAudioFileException, LineUnavailableException, IOException, URISyntaxException, InterruptedException {
        Map<String, Object> furniture = map.get(location);
        String furniture_items = (String)furniture.get("furniture_items");
        if(inventory.size()==5){
            System.out.println("inventory cannot take 5 or more items. Please drop one item.");
            playerInput();
        } else {
            if (furniture_items.isEmpty()) {
                System.out.println(location + " is empty. Nothing can be added.");
            } else if (!inventory.contains(furniture_items)) {
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

//        Map<String, Object> furniture = map.get(location);
//        String furniture_items = (String)furniture.get("furniture_items");
//
//
//        if(inventory.contains(furniture_items)){
//            System.out.println(location + " is empty. Nothing can be added.");
//        } else{
//            //System.out.println("You've found: "+furniture_items+" from " + location);
//                System.out.println(furniture_items + " has been picked up and added to your inventory");
//                inventory.add(furniture_items);
//            }
    }

    public static void checkCurrentLocation() throws UnsupportedAudioFileException, LineUnavailableException, IOException, URISyntaxException, InterruptedException {
        System.out.println("Your current location: "+location);
        playerInput();
    }



    public static String checkResult() {
        return "";
    }
    public static void quitGame(String input){
        System.out.println("Quiting game... Have a nice day!");
        System.exit(0);

    }
    public static void playAgain(String input) throws IOException, URISyntaxException, InterruptedException, UnsupportedAudioFileException, LineUnavailableException {
        if (input.equals("again") || (input.equals("play again"))){
            GameEngine.startGame();
        }
    }

    public static void dropItem(String item) throws UnsupportedAudioFileException, LineUnavailableException, IOException, URISyntaxException, InterruptedException {
        if (inventory.contains(item)) {
            inventory.remove(item);
            System.out.println(item + " has been removed from your inventory. ");
            }
        else if(!inventory.contains(item)){
            System.out.println("Sorry, "+item + "is not in your inventory, it cannot be dropped");
        }

    }



    public static void solvePuzzle(String loc) throws IOException, InterruptedException, UnsupportedAudioFileException, LineUnavailableException, URISyntaxException {
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
        // if furniture has "puzzle_desc"
        if (puzzle_exist.equals("Y")) {
            if ((inventory.contains(puzzle_reward_item)) ||(inventory.contains("keyD") && loc.equals("safe"))||
              (inventory.contains("a piece of paper with number 104") && loc.equals("keyD")) ||
                    (inventory.contains("a piece of paper with number 104") && loc.equals("safe"))) {
                        System.out.println("The puzzle has been solved. Please feel free to explore other furnitures :)");
            }
            else {
            System.out.println(puzzle_desc);
            Scanner solve = new Scanner(System.in);
            System.out.println("Would you like to solve this puzzle now? Y/N");
            String solve_ans = solve.nextLine();
            if (solve_ans.equals("Y")) {
                // riddles
                if (puzzle_filename.size() != 0) {
                    Random r = new Random();
                    int randomitem = r.nextInt(converted_puzzle_filename.size());
                    String randomPuzzle = converted_puzzle_filename.get(randomitem);
                    String randomAnswer = converted_multiple_puzzle_answer.get(randomitem);
                    FileManager.getResource(randomPuzzle);

                    Scanner scan = new Scanner(System.in);
                    System.out.println("\nYour answer:      (If it's too hard to answer, please enter [easy] to get a easier question.)");
                    String ans = scan.next();

                    if (ans.equals(randomAnswer)) {
                        System.out.println(furniture.get("puzzle_reward"));
                        System.out.println("You found " + puzzle_reward_item + ".");
                        if(inventory.size()==5){
                            System.out.println("Please drop one item. Inventory cannot take 5 or more items.");
                            playerInput();
                            if (inventory.size()<5) {
                            inventory.add(puzzle_reward_item);
                            }
                        } else if (inventory.size() < 5) {
                            inventory.add(puzzle_reward_item);
                        }

                        System.out.println("Added " + puzzle_reward_item + " to your inventory");
                    } else if (ans.equals("easy")) {
                        System.out.println(furniture.get("easy_question"));
                        Scanner easy = new Scanner(System.in);
                        String easyInput = easy.nextLine();
                        if (easyInput.equals(furniture.get("easy_answer"))) {
                            System.out.println(furniture.get("puzzle_reward"));
                            System.out.println("You found " + puzzle_reward_item + ".");
                            if(inventory.size()==5) {
                                System.out.println("Please drop one item. Inventory cannot take 5 or more items.");
                                System.out.println("Your current inventory: "+inventory);
                                playerInput();
                                inventory.add(puzzle_reward_item);
                                System.out.println(puzzle_reward_item + "has been added to your inventory");

                            }else if (inventory.size() < 5) {
                                inventory.add(puzzle_reward_item);
                                System.out.println("Added " + puzzle_reward_item + " to your inventory");
                            }


                        }
                    } else {
                        System.out.println("you didn't solve the puzzle. Try again later.");
                    }
                }
                // door puzzle
                else if (loc.equals("door")) {
                    System.out.println(puzzle_desc);
                    System.out.println("What's the password?     If you's like to try later, enter[later]");
                    Scanner scan = new Scanner(System.in);
                    String ans = scan.nextLine();
                    if (ans.equals(puzzle_answer)) {
                        System.out.println(puzzle_reward);
                        System.out.println("You won the game! Thanks for playing!");
                        System.exit(0);
                    } else if (ans.equals("later")) {
                        System.out.println("No worries! Try next time!");
                        playerInput();
                    } else {
                        System.out.println("Wrong password. Try again next time!");
                        System.out.println("What would you like to do?");
                        playerInput();
                    }
                }
                // use item to solve puzzle
                else if(furniture.get("puzzle_verb").equals("use")){
                        System.out.println("You need to use an item from your inventory. Let's see if you got needed item in your inventory...");
                        System.out.println("Your current inventory: "+inventory);
                        if(!inventory.contains(puzzle_itemsNeeded)){
                            System.out.println("Sorry, you don't have the tools. Explore the room and see if you can find anything");
                            playerInput();
                        }
                        else if (inventory.contains(puzzle_itemsNeeded)) {
                            Scanner scan = new Scanner(System.in);
                            String ans = scan.nextLine();
                            if (ans.equals(puzzle_verb + " " + puzzle_itemsNeeded)) {
                                System.out.println(puzzle_reward+ " and you've found "+puzzle_reward_item);
                                if(inventory.size()==5){
                                    System.out.println("Please drop one item. Inventory cannot take 5 or more items.");
                                    System.out.println("Your current inventory: "+inventory);
                                    playerInput();
                                    inventory.add(puzzle_reward_item);
                                    System.out.println("Added " + puzzle_reward_item + " to your inventory");

                                }else if (inventory.size() < 5) {
                                    inventory.add(puzzle_reward_item);
                                    System.out.println("Added " + puzzle_reward_item + " to your inventory");
                                }
                                inventory.remove(puzzle_itemsNeeded);
                            }
                        }
                    }


            } else if (solve_ans.equals("N")) {
                ;

            }
        } }


    }


    //solve puzzle


    public static void playerInput() throws IOException, UnsupportedAudioFileException, LineUnavailableException, URISyntaxException, InterruptedException {
        Scanner scan = new Scanner(System.in);
        String userInput = scan.nextLine();
        String[] words = userInput.split(" ");
        String verb = (String) Array.get(words, 0);
        String noun = (String) Array.get(words, words.length - 1);

        Reader reader = Files.newBufferedReader(Paths.get("./resources/cfg/verbs.json"));
        Gson gson = new Gson();
        Map<String, ArrayList<String>> map1 = gson.fromJson(reader, Map.class);

        Map<String, Object> furniture = map.get(location);
        String furniture_items = (String) furniture.get("furniture_items");
        String puzzle_reward_item = (String) furniture.get("puzzle_reward_item");

        // start
        Object start = map1.keySet().toArray()[0];
        Object start_value = map1.get(start);
        if (verb.equals(start) || start_value.toString().contains(verb)) {
            if (noun.equals("game") || (noun.equals(""))) {
                GameEngine.startGame();
            }
        }


        // quit
        Object quit = map1.keySet().toArray()[1];
        Object quit_value = map1.get(quit);
        if (verb.equals(quit) || quit_value.toString().contains(verb)) {
            if (noun.equals("game") || (noun.equals(""))) {
                quitGame("quit");
            }
        }

        // go
        Object go = map1.keySet().toArray()[2];
        Object go_value = map1.get(go);
        if (verb.equals(go) || go_value.toString().contains(verb)) {
            if (map.keySet().contains(noun)) {
                goFurniture(noun);
            }
            else if (noun.equals("left")||(noun.equals("right"))){
                moveDirection(noun);
            }
        }

        // get
        Object get = map1.keySet().toArray()[3];
        Object get_value = map1.get(get);
        if (verb.equals(get) || get_value.toString().contains(verb)) {
            if (furniture_items.contains(noun)) {
                pickUpItem(location);
            }
            else if (puzzle_reward_item.contains(noun)){
                inventory.add(noun);
            }
            else {
                System.out.println("item not found. Please enter again");
            }
        }

        // inspect
        Object inspect = map1.keySet().toArray()[4];
        Object inspect_value = map1.get(inspect);

        if (verb.equals(inspect) || inspect_value.toString().contains(verb)) {
            inspectItem(noun);
        }


        // help
        Object help = map1.keySet().toArray()[6];
        Object help_value = map1.get(help);

        if (verb.equals(help) || help_value.toString().contains(verb)) {
            gameMenu(noun);
            playerInput();
        }

        // use
        Object use = map1.keySet().toArray()[6];
        Object use_value = map1.get(help);

        if (verb.equals(use) || use_value.toString().contains(verb)) {
            useItem(noun,location);
        }

        // drop item
        Object drop = map1.keySet().toArray()[7];
        Object drop_value = map1.get(drop);
        if (verb.equals(drop) || drop_value.toString().contains(verb)) {
            dropItem(noun);
        }

        if (verb.equals("check") && noun.equals("inventory")){
            checkCurrentInventory();
        }

        if (verb.equals("check") && noun.equals("location")) {
            checkCurrentLocation();
        }


        reader.close();

    }

    public static void moveDirection(String direction) throws UnsupportedAudioFileException, LineUnavailableException, IOException, URISyntaxException, InterruptedException {

        Map<String, Object> furniture = map.get(location);

        String newlocation = (String) furniture.get(direction);
        location = newlocation;
        System.out.println("Now you are in front of " + newlocation);
        playerInput();
    }

    public static void goFurniture(String destinationaLoc) throws UnsupportedAudioFileException, LineUnavailableException, IOException, URISyntaxException, InterruptedException {
        Map<String, Object> furniture = map.get(destinationaLoc);
        String furniture_desc = (String) furniture.get("furniture_desc");
        System.out.println("\nYou are currently in front of " + destinationaLoc);
        location = destinationaLoc;
        System.out.println(furniture_desc);
        System.out.println("What would you like to do?");
        playerInput();

    }
    public static void gameMenu(String input) throws IOException, UnsupportedAudioFileException, LineUnavailableException, URISyntaxException, InterruptedException {

        if(input.equalsIgnoreCase("Help")){
            System.out.println("-----------------------");
            System.out.println("\nHere are your options?");
            FileManager.getResource("helperMenu.txt");

            System.out.println("\nPlease select options above. enter number 1-4?");
            Scanner scan = new Scanner(System.in);
            String selection = scan.nextLine();
            if (selection.equalsIgnoreCase("where am I?") || selection.equals("1")) {
                System.out.println("you are at " + location );
            } else if (selection.equalsIgnoreCase("Restart")|| selection.equals("2")) {
                System.out.println("Game Restarting");
                GameEngine.startGame();
            } else if (selection.equalsIgnoreCase("exit")|| selection.equals("4"))  {
                System.out.println("Quitting the Game. See you next time.");
                System.exit(0);
            }
        }
    }
}




