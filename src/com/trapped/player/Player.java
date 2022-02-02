package com.trapped.player;

import com.google.gson.Gson;
import com.trapped.GameEngine;
import com.trapped.utilities.FileManager;
import com.trapped.utilities.Sounds;

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

    // constructor
    public Player() throws IOException {

    }
    public static void viewRoom() throws UnsupportedAudioFileException, LineUnavailableException, IOException, URISyntaxException, InterruptedException {
        System.out.println("\nYou are currently in front of bed" );
//        inspectItem(location);
//        pickUpItem(location);

        System.out.println("Look around this room. There is a door, a window with curtain, a drawer, picture with safe. Where do you want to do?");
//        Scanner scan = new Scanner(System.in);
//        String gowhere = scan.nextLine();
//        if (map.keySet().contains(gowhere)){
//            goFurniture(gowhere);
//        }
//        else if(gowhere.equals("left")|| gowhere.equals("right")){
//            move(gowhere);
//        }
        playerInput();
    }
    public static void inspectItem(String something) throws UnsupportedAudioFileException, LineUnavailableException, IOException, URISyntaxException, InterruptedException {
        Map<String, Object> furniture_inspectItem = map.get(location);
        String furniture_items1 = (String)furniture_inspectItem.get("furniture_items");
        // if user means inspecting furniture
        if (map.keySet().contains(something)){
            Map<String, Object> furniture = map.get(something);
            String furniture_name = (String) furniture.get("furniture_name");
            String furniture_items = (String)furniture.get("furniture_items");
            String furniture_desc = (String) furniture.get("furniture_desc");
            String puzzle_exist = (String) furniture.get("puzzle_exist");
            if(furniture_items.equals("")){
                System.out.println(furniture_desc);
                System.out.println("Inspecting...\nNo items found here.");
                if (puzzle_exist.equals("Y")){
                    System.out.println("A puzzle has been found in "+something+".");
                    solvePuzzle(something);
                }
            }else if(!furniture_items.equals("")){
                if (inventory.contains(furniture_items)) {
                    System.out.println("Inspecting...\n" + something + " is empty.");
                } else {
                    System.out.println("Inspecting...\nYou found: " + furniture_items);
                    System.out.println("What would you want to do?");
                    playerInput();

                }
            }
        }


        // if user means inspecting item of current location
        else if ((furniture_items1.contains(something))) {
            System.out.println("it's just a " + furniture_items1);
        }
        System.out.println("What would you like to do?");
        playerInput();
    }


    // item disappear from inventory and json
    public static void useItem(String item) {
        Map<String, Object> furniture = map.get(location);
        inventory.remove(item);
        String item_used = (String)furniture.get(location);
        item_used.replace(item_used,"");
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
        System.out.println("\nYou are currently in front of " + fromLocation);

        System.out.println(furniture_desc);
        inspectItem(fromLocation);
        pickUpItem(fromLocation);
        checkCurrentInventory();
        solvePuzzle(fromLocation);
        System.out.println("Which direction do you want to go? Available directions: " + furniture_available_directions + " (or enter [quit] to quit game, " +
                "[play again] to replay the game,[drop] an item from current inventory)");
        String dir = scan.nextLine();
        gameMenu(dir);
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
    public static void pickUpItem(String location) {
        Map<String, Object> furniture = map.get(location);
        String furniture_items = (String)furniture.get("furniture_items");;

        if(furniture_items.isEmpty()){
            System.out.println(location + " is empty. Nothing can be added.");
        } else if(!inventory.contains(furniture_items)){
            System.out.println("\nYou've found: "+furniture_items+" from " + location + "\nDo you want to add it? [Y/N]");
            Scanner scan = new Scanner(System.in);
            String response = scan.nextLine();
            if(response.equalsIgnoreCase("Y")){
                System.out.println(furniture_items + " has been picked up and added to your inventory");
                inventory.add(furniture_items);
            }else if(response.equalsIgnoreCase("N")){
                System.out.println("You did not pick anything from " + location );
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
            checkCurrentInventory();

        }
        else if(!inventory.contains(item)){
            System.out.println("Sorry, "+item + "is not in your inventory, it cannot be dropped");
        }
        playerInput();
    }



    public static void solvePuzzle(String loc) throws IOException, InterruptedException, UnsupportedAudioFileException, LineUnavailableException, URISyntaxException {
        Map<String, Object> furniture = map.get(loc);

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
            System.out.println(puzzle_desc);
            Scanner solve = new Scanner(System.in);
            System.out.println("would you like to solve this puzzle now? Y/N");
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
                    System.out.println("Your answer: ");
                    String ans = scan.next();

                    if (ans.equals(randomAnswer)) {
                        System.out.println(furniture.get("puzzle_reward"));
                        System.out.println("You found "+puzzle_reward_item+". What you'd like to do");
                        playerInput();

                        System.out.println("Added " + puzzle_reward_item + " to your inventory");
                        checkCurrentInventory();

                        if (inventory.contains(puzzle_reward_item)) {
                            System.out.println(location + " is empty. Nothing can be added.");
                        } else {
                            inventory.add(puzzle_reward_item);
                            System.out.println("Added " + puzzle_reward_item + "to your inventory");
                        }
                    } else {
                        System.out.println("you didn't solve the puzzle. Try again later.");
                    }
                } else {
                    if (loc.equals("door")) {
                        System.out.println(puzzle_desc);
                        System.out.println("What's the password?");
                        Scanner scan = new Scanner(System.in);
                        String ans = scan.nextLine();
                        if (ans.equals(puzzle_answer)) {
                            System.out.println(puzzle_reward);
                            System.out.println("You won the game! Thanks for playing!");
                            System.exit(0);
                        }
                    } else {
                        System.out.println(puzzle_desc);
                        checkCurrentInventory();
                        System.out.println("You need to use an item from your inventory. What you'd like to use?");
                        Scanner scan = new Scanner(System.in);
                        String ans = scan.nextLine();
                        if (inventory.contains(puzzle_itemsNeeded)) {
                            if (ans.equals(puzzle_verb + " " + puzzle_itemsNeeded)) {
                                System.out.println(puzzle_reward);
                                inventory.add(puzzle_reward_item);
                                inventory.remove(puzzle_itemsNeeded);
                            }
                        }
                        else if (!inventory.contains(puzzle_itemsNeeded)){
                            System.out.println("Sorry, you don't have the tools. Explore the room and see if you can find anything");
                            location = loc;
                            playerInput();
                        }
                    }
                }

            }else if(solve_ans.equals("N")){
                location = loc;

            }
        }


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
//        else if() {
//            System.out.println("invalid entry, please enter again");
//        }

        // help
        Object help = map1.keySet().toArray()[6];
        Object help_value = map1.get(help);

        if (verb.equals(help) || help_value.toString().contains(verb)) {
            gameMenu(noun);
            playerInput();
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

        if (verb.equals("check") && noun.equals("location"))
            checkCurrentLocation();

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




