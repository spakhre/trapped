package com.trapped.player;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Player {
    //Map<String, Items> inventory = new HashMap<String, Items>(); // player's inventory
    public static String location="bed";
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

    public static void inspectItem(String location) {
        Map<String, Object> furniture = map.get(location);

        ArrayList<Object> items = (ArrayList<Object>)furniture.get("items");
        ArrayList<String> strList = (ArrayList<String>)(ArrayList<?>)(items);

        if(inventory.containsAll(strList)){
            System.out.println("Inspecting...\n"+location + " is empty.");
        } else{
            System.out.println("Inspecting...\nYou found: "+ strList);
        }


    }

    // eg. use key with Window; solve puzzle
    public void useItem() {

    }

    public static void checkCurrentInventory() {
        System.out.println("Your current inventory: "+inventory);
    }

    public static void move() throws IOException {
        String[] validDirection = {"go left", "go right", "go up", "go down"};

        // Extract the current furniture to get the inner available directions
        Map<String, Object> furniture = map.get(location);
        List availableDirections = (ArrayList<String>)furniture.get("availableDirections");
        String desc = (String) furniture.get("desc");



        Scanner scan = new Scanner(System.in);
        System.out.println("You are currently in front of " + location);
        System.out.println("It is "+ desc);
        inspectItem(location);
        pickUpItem(location);
        checkCurrentInventory();
        checkIfPuzzle();
        System.out.println("Which direction do you want to go? Available directions: " + availableDirections);
        String dir = scan.nextLine();


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
    public static void pickUpItem(String location) {
        Map<String, Object> furniture = map.get(location);

        ArrayList<Object> items = (ArrayList<Object>)furniture.get("items");
        ArrayList<String> strList = (ArrayList<String>)(ArrayList<?>)(items);

        if(inventory.containsAll(strList)){
            System.out.println(location + " is empty. Nothing can be added.");
        } else{
            inventory.addAll(strList);
            System.out.println("Added "+ strList + "to your inventory");
        }



    }
    public static void checkCurrentLocation() {
        System.out.println(location);
    }

    public static void checkIfPuzzle(){
        Map<String, Object> furniture = map.get(location);

        ArrayList<Object> puzzle = (ArrayList<Object>)furniture.get("puzzle");
        if (puzzle.size()>1){
            ArrayList<String> converted_puzzle = (ArrayList<String>)(ArrayList<?>)(puzzle);

            ArrayList<Object> answer = (ArrayList<Object>)furniture.get("answer");
            ArrayList<String> converted_answer = (ArrayList<String>)(ArrayList<?>)(answer);

            ArrayList<Object> reward_item = (ArrayList<Object>)furniture.get("reward_item");
            ArrayList<String> converted_reward_item = (ArrayList<String>)(ArrayList<?>)(reward_item);

            String puzzle_easy = (String)furniture.get("puzzle_easy");
            String answer_easy = (String)furniture.get("answer_easy");


            Random r = new Random();
            int randomitem = r.nextInt(puzzle.size());
            String randomPuzzle = converted_puzzle.get(randomitem);
            String randomAnswer = converted_answer.get(randomitem);

            System.out.println("Here you need to solve a puzzle: "+ randomPuzzle);
            Scanner scan = new Scanner(System.in);
            System.out.println("Your answer:  (if the question is too hard, enter [easy])");
            String ans = scan.nextLine();
            if (ans.equals(randomAnswer)){
                System.out.println(furniture.get("reward"));
                inventory.addAll(converted_reward_item);
                System.out.println("Added "+ converted_reward_item + "to your inventory");
                checkCurrentInventory();

                if(inventory.containsAll(converted_reward_item)){
                    System.out.println(location + " is empty. Nothing can be added.");
                } else{
                    inventory.addAll(converted_reward_item);
                    System.out.println("Added "+ converted_reward_item + "to your inventory");
                }
            }
            else if(ans.equals("easy")){
                System.out.println(puzzle_easy);
                Scanner easy_question = new Scanner(System.in);
                System.out.println("Your answer: ");
                String easy_ans = easy_question.nextLine();
                if (easy_ans.equals(answer_easy)){
                    System.out.println(furniture.get("reward"));
                    inventory.addAll(converted_reward_item);
                    System.out.println("Added "+ converted_reward_item + "to your inventory");
                    checkCurrentInventory();
                    if(inventory.containsAll(converted_reward_item)){
                        System.out.println(location + " is empty. Nothing can be added.");
                    } else{
                        inventory.addAll(converted_reward_item);
                        System.out.println("Added "+ converted_reward_item + "to your inventory");
                    }
                }
            }
            else{
                System.out.println("you didn't solve the puzzle. Try again later.");
            }
        }


    }

    public static String checkResult() {
        return "";
    }

}

