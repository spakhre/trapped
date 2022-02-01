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

    public static void inspectItem(String loc_itm) {
        Map<String, Object> furniture = map.get(location);
        String furniture_name = (String) furniture.get("furniture_name");


        String furniture_items = (String)furniture.get("furniture_items");

        // if user means inspect furniture
        if ((loc_itm.equals(furniture_name))){
            if(inventory.contains(furniture_items)){
                System.out.println("Inspecting...\n"+location + " is empty.");
            } else{
                System.out.println("Inspecting...\nYou found: "+ furniture_items);
            }
        }
        // if user means inspect items
        else if ((furniture_items.contains(loc_itm))){
            System.out.println("it's just a "+ furniture_items);
        };





    }

    // eg. use key with Window; solve puzzle
    public void useItem() {

    }

    public static void checkCurrentInventory() {
            System.out.println("Your current inventory: " + inventory);
    }




    public static void move() throws IOException, URISyntaxException, InterruptedException, UnsupportedAudioFileException, LineUnavailableException {
        String[] validDirection = {"left", "right", "up", "down"};

        // Extract the current furniture to get the inner available directions
        Map<String, Object> furniture = map.get(location);
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
        System.out.println("\nYou are currently in front of " + location);

        System.out.println(furniture_desc);
        inspectItem(location);
        pickUpItem(location);
        checkCurrentInventory();
        solvePuzzle(location);
        System.out.println("Which direction do you want to go? Available directions: " + furniture_available_directions + " (or enter [quit] to quit game, " +
                "[play again] to replay the game,[drop] an item from current inventory)");
        String dir = scan.nextLine();
        quitGame(dir);
        playAgain(dir);

        boolean contain = furniture_available_directions.contains(dir);
        if (Arrays.asList(validDirection).contains(dir)) {
            if (contain) {
                String newlocation = (String) furniture.get(dir);
                location = newlocation;
                System.out.println("Now you are in front of " + location);
            } else {
                System.out.println("Sorry, you can't go that way. Please select again.");
            }
        }
        else if(dir.equals("drop")){
            dropItem();
            checkCurrentInventory();
        }
        else {
            System.out.println("invalid input, please try again.");
        }


    }
    public static void pickUpItem(String location) {
        Map<String, Object> furniture = map.get(location);
        String furniture_items = (String)furniture.get("furniture_items");;

        if(inventory.contains(furniture_items)){
            System.out.println(location + " is empty. Nothing can be added.");
        } else{
            System.out.println("You've found: "+furniture_items+" from " + location);
                System.out.println(furniture_items + " has been picked up and added to your inventory");
                inventory.add(furniture_items);
            }
    }
    public static void checkCurrentLocation() {
        System.out.println(location);
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

    public static void dropItem(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Which item you'd like to drop from your current inventory?");
        String droppedItem = scan.nextLine();
        inventory.remove(droppedItem);
        System.out.println(droppedItem+" has been removed from your inventory. ");
    }

    public static void goFurniture(String loc){

        Map<String, Object> furniture = map.get(loc);
        List furniture_available_directions = (ArrayList<String>)furniture.get("furniture_available_directions");
        String furniture_desc = (String) furniture.get("furniture_desc");

        System.out.println("Now you are in front of " + loc);
        System.out.println(furniture_desc);

    }

    public static void solvePuzzle(String loc) {
        Map<String, Object> furniture = map.get(loc);

        String puzzle_desc = (String) furniture.get("puzzle_desc");
        String puzzle_exist = (String) furniture.get("puzzle_exist");
        String puzzle_verb = (String) furniture.get("puzzle_verb");
        String puzzle_itemsNeeded = (String) furniture.get("puzzle_itemsNeeded");
        String puzzle_reward = (String) furniture.get("puzzle_reward");
        ArrayList<Object> puzzle_filename = (ArrayList<Object>) furniture.get("puzzle_filename");
        ArrayList<String> converted_puzzle_filename = (ArrayList<String>) (ArrayList<?>) (puzzle_filename);

        String puzzle_answer = (String) furniture.get("puzzle_answer");

        String puzzle_reward_item = (String) furniture.get("puzzle_reward_item");

        //check if the furniture has puzzle
        // if furniture has "puzzle_desc"
        if (puzzle_exist.equals("Y")) {
            // riddles
            if (puzzle_filename.size() != 0) {
                Random r = new Random();
                int randomitem = r.nextInt(converted_puzzle_filename.size());
                String randomPuzzle = converted_puzzle_filename.get(randomitem);
                String randomAnswer = converted_puzzle_filename.get(randomitem);

                System.out.println(puzzle_desc + "\n" + randomPuzzle);
                Scanner scan = new Scanner(System.in);
                System.out.println("Your answer: ");
                String ans = scan.nextLine();

                if (ans.equals(randomAnswer)) {
                    System.out.println(furniture.get("reward"));
                    inventory.add(puzzle_reward_item);
                    System.out.println("Added " + puzzle_reward_item + "to your inventory");
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
            }
            else{
                if(loc.equals("door")){
                    System.out.println(puzzle_desc);
                    System.out.println("What's the password?");
                    Scanner scan = new Scanner(System.in);
                    String ans = scan.nextLine();
                    if(ans.equals(puzzle_answer)){
                        System.out.println(puzzle_reward);
                    }
                }
                else {
                    System.out.println(puzzle_desc);
                    System.out.println("What you'd like to do?");
                    Scanner scan = new Scanner(System.in);
                    String ans = scan.nextLine();
                    if (ans.equals(puzzle_verb +" "+ puzzle_itemsNeeded)) {
                        System.out.println(puzzle_reward);
                    }
                }
            }


        }
        else{
            ;
        }
    }


        //solve puzzle


    public static void playerInput() throws IOException, UnsupportedAudioFileException, LineUnavailableException, URISyntaxException, InterruptedException {
        Scanner scan = new Scanner(System.in);
        String userInput = scan.nextLine();
        String[] words = userInput.split(" ");
        String verb = (String) Array.get(words, 0);
        String noun = (String) Array.get(words, words.length - 1);

        Reader reader = Files.newBufferedReader(Paths.get("./resources/keywords.json"));
        Gson gson = new Gson();
        Map<String, ArrayList<String>> map1 = gson.fromJson(reader, Map.class);

        Map<String, Object> furniture = map.get(location);
        String furniture_items = (String) furniture.get("furniture_items");

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
            if (noun.equals(location)) {
                goFurniture(location);
            }
        }

        // get
        Object get = map1.keySet().toArray()[3];
        Object get_value = map1.get(get);
        if (verb.equals(get) || get_value.toString().contains(verb)) {
            if (furniture_items.contains(noun)) {
                pickUpItem(location);
            } else {
                System.out.println("item not found. Please enter again");
            }
        }

        // inspect
        Object inspect = map1.keySet().toArray()[4];
        Object inspect_value = map1.get(inspect);
        if (verb.equals(inspect) || inspect_value.toString().contains(verb)) {
            inspectItem(noun);
        }




        reader.close();



    }

}




