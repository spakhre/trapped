package com.trapped;

import com.google.gson.Gson;
import com.trapped.utilities.FileManager;
import com.trapped.utilities.Sounds;
import com.trapped.utilities.TextColor;
import com.trapped.utilities.TextParser;
import com.trapped.view.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.sun.java.accessibility.util.AWTEventMonitor.addActionListener;
import static com.trapped.utilities.TextColor.MAGENTA_UNDERLINE;
import static com.trapped.utilities.TextColor.RESET;

class ActionController implements ActionListener {

    private static GameHandler gHandler;

    public ActionController(GameHandler gHandler) {
        this.gHandler = gHandler;
    }

    public static String verb;
    public static String noun;
    public static String location = "bed";
    public static float volume;
    public static List<String> inventory = new ArrayList<>();
    private static String furniturePuzzlesJsonPath = "./resources/furniture_puzzles.json";
    private static int max_attempts = 3;
    private static String ANSWER;
    private static Scanner scan = new Scanner(System.in);

    static Map<String, Map<String, Object>> map = furniturePuzzleGenerator();

    //Utility class for the map generated above, trying to ensure json file is closed
    private static Map<String, Map<String, Object>> furniturePuzzleGenerator() {
        Gson gson = new Gson();
        try {
            Reader reader = Files.newBufferedReader(Path.of(furniturePuzzlesJsonPath), StandardCharsets.UTF_8);
            Map<String, Map<String, Object>> map = gson.fromJson(reader, Map.class);
            reader.close();
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // And if the furniture has a puzzle, it will show the puzzle's description.
    private static void inspectItem(String noun) {
        //furniture
        Map<String, Object> furniture = map.get(noun);
        if (noun.equals("inventory")) {
            checkCurrentInventory();
        } else if (map.containsKey(noun)) {
            location = noun;
            if (furniture.get("furniture_items") != null) {
                ArrayList<String> furniture_items = (ArrayList<String>) furniture.get("furniture_items");
                if (!furniture_items.isEmpty()) {
                    roomWithItems(noun);
                }
                if (furniture_items.isEmpty()) {
                    noItemsInRoom(noun);
                }
            } else if (furniture.get("furniture_items") == null) {
                noItemsInRoom(noun);
            }
        }

        //item

        else if (map.get(location).get("furniture_items") != null) {
            ArrayList<String> furniture_items = (ArrayList<String>) map.get(location).get("furniture_items");
            if (furniture_items.contains(noun)) {
                gHandler.mainFrame.writeToTextArea("It's just a " + noun);

            } else if (inventory.contains(noun)) {
                gHandler.mainFrame.writeToTextArea("It's just a " + noun);

            } else {
                gHandler.mainFrame.writeToTextArea("Sorry, I don't understand your input, please enter again. ");
                FileManager.getResource("commands.txt");
            }

        } else {
            gHandler.mainFrame.writeToTextArea("Sorry, I don't understand your input, please enter again. ");
            FileManager.getResource("commands.txt");
        }

    }

    private static void roomWithItems(String something) {
        Map<String, Object> furniture = map.get(something);
        String furniture_picture = (String) furniture.get("furniture_picture");
        ArrayList<String> furniture_items = (ArrayList<String>) furniture.get("furniture_items");
        FileManager.getResource(furniture_picture);
        gHandler.mainFrame.writeToTextArea("You see: " + furniture_items + ".");
        for (String item : furniture_items) {
            unhideItemOnNavScreen(item);
        }
    }

    private static void unhideItemOnNavScreen(String item) {
        switch (item) {
            case "key":
                gHandler.mainFrame.key.setVisible(true);
                gHandler.mainFrame.keyLabel.setVisible(false);
                break;
            case "wallet":
                gHandler.mainFrame.wallet.setVisible(true);
                gHandler.mainFrame.walletLabel.setVisible(false);
                break;
            case "crowbar":
                gHandler.mainFrame.crowbar.setVisible(true);
                gHandler.mainFrame.crowbarLabel.setVisible(false);
                break;
            case "a piece of papert with number 104":
                gHandler.mainFrame.paper.setVisible(true);
                gHandler.mainFrame.paperLabel.setVisible(false);
                break;
            case "candle":
                gHandler.mainFrame.candle.setVisible(true);
                gHandler.mainFrame.candleLabel.setVisible(false);
                break;
            case "laptop":
                gHandler.mainFrame.laptop.setVisible(true);
                gHandler.mainFrame.laptopLabel.setVisible(false);
                break;
            case "matches":
                gHandler.mainFrame.matches.setVisible(true);
                gHandler.mainFrame.matchLabel.setVisible(false);
                break;
        }
    }

    private static void hideItemOnNavScreen(String item) {
        switch (item) {
            case "key":
                gHandler.mainFrame.key.setVisible(false);
                gHandler.mainFrame.keyLabel.setVisible(true);
                break;
            case "wallet":
                gHandler.mainFrame.wallet.setVisible(false);
                gHandler.mainFrame.walletLabel.setVisible(true);
                break;
            case "crowbar":
                gHandler.mainFrame.crowbar.setVisible(false);
                gHandler.mainFrame.crowbarLabel.setVisible(true);
                break;
            case "a piece of paper with number 104":
                gHandler.mainFrame.paper.setVisible(false);
                gHandler.mainFrame.paperLabel.setVisible(true);
                break;
            case "candle":
                gHandler.mainFrame.candle.setVisible(false);
                gHandler.mainFrame.candleLabel.setVisible(true);
                break;
            case "laptop":
                gHandler.mainFrame.laptop.setVisible(false);
                gHandler.mainFrame.laptopLabel.setVisible(true);
                break;
            case "matches":
                gHandler.mainFrame.matches.setVisible(false);
                gHandler.mainFrame.matchLabel.setVisible(true);
                break;
        }
    }


    private static void noItemsInRoom(String something) {
        Map<String, Object> furniture = map.get(something);
        String furniture_picture = (String) furniture.get("furniture_picture");

        FileManager.getResource(furniture_picture);
        gHandler.mainFrame.writeToTextArea("Inspecting...\nNo items found here.");
    }

    // check current inventory
    private static void checkCurrentInventory() {
        gHandler.mainFrame.writeToTextArea("Your current inventory: " + inventory);
    }


    private static void addItemAndUpdateJson(String noun) {
        Map<String, Object> furniture = map.get(location);
        ArrayList<String> furniture_items = (ArrayList<String>) furniture.get("furniture_items");
        inventory.add(noun);
        furniture_items.remove(noun);
        furniture.put("furniture_items", furniture_items);
        map.put("location", furniture);
        FileManager.writeJSON(map, furniturePuzzlesJsonPath);
        gHandler.mainFrame.writeToTextArea(noun + " has been added to your inventory");
        Sounds.playSounds("pick.wav", 1000);
        hideItemOnNavScreen(noun);
    }

    // quit game
    private static void quitGame() {
        gHandler.mainFrame.writeToTextArea("Quitting the Game. See you next time.");
        System.exit(0);
    }

    // Drop item -- will provide current inventory first then let player pick.
    // This method will be used when inventory is full and player being asked to drop an item.

    // Drop a specific item - this will be used when player input "drop xxx"
    public static void dropItem(String item) {
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
                gHandler.mainFrame.writeToTextArea(item + " has been dropped from your inventory.");
                unhideItemOnNavScreen(noun);
            } else {
                gHandler.mainFrame.writeToTextArea("Sorry, the item you entered is not in your inventory.");
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionClicked = e.getActionCommand();
        System.out.println("action clicked is: " + actionClicked);
        /*
         * Comment out this block for JUnit Testing
         */
        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        verb = TextParser.getVerb(actionClicked);
        System.out.println("printing out the verb " + verb);
        noun = TextParser.getNoun(actionClicked);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////
        playerInputLogic(verb, noun);
    }

    public static void playerInputLogic(String verb, String noun) {
        switch (verb) {
            case "commands":
                FileManager.getResource("commands.txt");
                break;
            case "quit":
                quitGame();
                break;
            case "go":
                // Currently, this is only pointing to the first index of the parsed array
                move(noun);
                break;
            case "get":
                get(noun);
                break;
            case "inspect":
                inspect(noun);
                break;
            case "help":
                helpMenu();
                break;
            case "drop":
                drop(noun);
                break;
            case "final":
                System.out.println("clicked on door");
                doorPuzzle("door");
                break;
            case "tool":
                toolPuzzle(location);
                break;
            case "riddles":
                riddles(location);
                break;
            default:
                System.out.println("Sorry, I don't understand your input, please enter again. ");
                FileManager.getResource("commands.txt");
        }
    }

    public static void drop(String noun) {
        if (noun != null) {
            if (inventory.isEmpty()) {
                System.out.println("You have nothing to drop");
            } else
                dropItem(noun);
        } else {
            System.out.println("Sorry, I don't understand your input, please enter again. ");
            FileManager.getResource("commands.txt");
        }
    }

    public static boolean inspect(String noun) {
        boolean success = false;
        if (noun != null) {
            inspectItem(noun);
            success = true;
        } else {
            gHandler.mainFrame.writeToTextArea("Sorry, I don't understand your input, please enter again. ");
            FileManager.getResource("commands.txt");
        }
        return success;
    }

    private static void get(String noun) {
        Map<String, Object> furniture = map.get(location);
        ArrayList<String> furniture_items = (ArrayList<String>) furniture.get("furniture_items");
        ArrayList<String> puzzle_reward_item = (ArrayList<String>) furniture.get("puzzle_reward_item");

        if (noun != null) {
            if (furniture_items.contains(noun)) {
                System.out.println("You added " + noun + " to your inventory");
                addItemAndUpdateJson(noun);
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
    }

    public static void move(String direction) {
        System.out.println("movement triggered by game button");
        moveDirection(direction);
    }


    private static void moveDirection(String direction) {
        Map<String, Object> furniture = map.get(location);
        location = (String) furniture.get(direction);
        Map<String, Object> new_furniture = map.get(location);
        String furniture_desc = (String) new_furniture.get("furniture_desc");
        String furniture_picture = (String) new_furniture.get("furniture_picture");
        FileManager.getResource(furniture_picture);
        System.out.println("Now you are in front of " + location);
        System.out.println(furniture_desc);
        switch (location) {
            case "bed":
                gHandler.mainFrame.bedScreen();
                break;
            case "door":
                gHandler.mainFrame.doorScreen();
                break;
            case "window":
                gHandler.mainFrame.windowScreen();

                break;
            case "drawer":
                gHandler.mainFrame.deskScreen();
                break;
            case "safe":
                gHandler.mainFrame.safeScreen();
                break;
            case "lamp":
                gHandler.mainFrame.lampScreen();
                break;
            case "chair":
                gHandler.mainFrame.chairScreen();
                break;
        }
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
                        System.out.println("You found " + puzzle_reward_item.get(0) + ".");
                        addItemAndUpdateJson(puzzle_reward_item.get(0));
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

    public static boolean puzzleSolved() {
        //checks to see if the player has solved any of the puzzles, if they have, returns true to the caller!
        Map<String, Object> furniture = map.get(location);
        Boolean solved = false;
        if (((inventory.contains("crowbar") && location.equals("safe")) ||
                (inventory.contains("key") && location.equals("window")) ||
                (inventory.contains("a piece of paper with number 104") && location.equals("drawer")))) {
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
        ArrayList<String> puzzle_itemsNeeded = (ArrayList<String>) furniture.get("puzzle_itemsNeeded");
        String puzzle_reward = (String) furniture.get("puzzle_reward");
        //
        //
        if (!puzzleSolved()) {
            gHandler.mainFrame.writeToTextArea("A puzzle has been found in " + loc + ".");
            System.out.println(puzzle_desc);
            gHandler.mainFrame.writeToTextArea("You need to use an item from your inventory. Let's see if you got needed item in your inventory...");
            gHandler.mainFrame.writeToTextArea("Your current inventory: " + inventory);
            if (!inventory.contains(puzzle_itemsNeeded.get(0))) {
                System.out.println("Sorry, you don't have the tools. Explore the room and see if you can find anything");
            } else if (inventory.contains(puzzle_itemsNeeded.get(0))) {
                gHandler.mainFrame.writeToTextArea(puzzle_reward + " and you've found " + puzzle_reward_item.get(0));
                inventory.remove(puzzle_itemsNeeded.get(0));
                switch (puzzle_itemsNeeded.get(0)){
                    case "crowbar":
                        gHandler.mainFrame.crowbar.setVisible(false);
                        gHandler.mainFrame.crowbarLabel.setVisible(false);
                        gHandler.mainFrame.windowWithKey.setVisible(false);
                        gHandler.mainFrame.windowWithoutKey.setVisible(true);
                        break;
                    case "key":
                        gHandler.mainFrame.key.setVisible(false);
                        gHandler.mainFrame.keyLabel.setVisible(false);
                        break;
                }
                inventory.add(puzzle_reward_item.get(0));
                addItemAndUpdateJson(puzzle_reward_item.get(0));
                gHandler.mainFrame.writeToTextArea("Added " + puzzle_reward_item.get(0) + " to your inventory");
            }
        } else {
            System.out.println("Sorry I don't understand your command. The puzzle has not been solved. Please come back later.");
        }
    }


    private static void doorPuzzle(String loc) {
        //Final puzzle in the game, can be solved at any time if you know the secret number (104)

        System.out.println("door puzzle GUI 1");

        Map<String, Object> furniture = map.get(loc);
        String puzzle_desc = (String) furniture.get("puzzle_desc");
        String puzzle_sounds = (String) furniture.get("puzzle_sounds");
        String puzzle_answer = (String) furniture.get("puzzle_answer");
        String puzzle_reward = (String) furniture.get("puzzle_reward");
        gHandler.mainFrame.writeToTextArea(puzzle_desc);
        gHandler.mainFrame.writeToTextArea("What's the password? You have " + max_attempts +
                " attempts remaining. If you'll like to try later, enter[later]");

        ANSWER = gHandler.mainFrame.inputText.getText();


        System.out.println("door puzzle GUI 2");

//        JFrame frame = new JFrame("Door Puzzle");
//        frame.setSize(200, 200);
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
//        JOptionPane.showMessageDialog(frame, "What's the password? You have " + max_attempts + " attempts remaining. If you'll like to try later, enter[later]");
//        int result = JOptionPane.showConfirmDialog(null, "Do wish to solve the puzzle ");
//        while (0 <= max_attempts && max_attempts <= 3){
//        switch (result) {
//            case JOptionPane.YES_OPTION:
//                String name = JOptionPane.showInputDialog(null,
//                        "Please enter the passcode");
//                int num = Integer.parseInt(name);
//                if (num == 104) {
//                    System.out.println("door puzzle 6");
//                    Sounds.changeSoundVolume("open_door.wav", 0, num);
//                    JOptionPane.showMessageDialog(frame, "Congratulations, you've exited the game");
//                    frame.dispose();
//                } else {
//                    JOptionPane.showMessageDialog(frame, "You entered the wrong number");
//                    max_attempts = max_attempts - 1;
//                    JOptionPane.showMessageDialog(frame, "You have " + max_attempts + " left.");
//                    frame.dispose();
//                    break;
//                }
//                break;
//            case JOptionPane.NO_OPTION:
//            case JOptionPane.CANCEL_OPTION:
//                frame.dispose();
//                break;
//        }

        if (ANSWER.trim().equals("later") || ANSWER.trim().equals("")) {
            System.out.println("door puzzle GUI 3");
            gHandler.mainFrame.writeToTextArea("No worries! Try next time!");
            System.out.println("door puzzle GUI 4");
        } else {
            while (max_attempts-- > 0) {
                if (ANSWER.trim().equals(puzzle_answer)) {
                    gHandler.mainFrame.writeToTextArea(puzzle_reward);
                    gHandler.mainFrame.writeToTextArea("You won the game! Thanks for playing!");
                    System.out.println("game won");
                    gHandler.mainFrame.endScreen("end_game");
                } else if (max_attempts == 0) {
                    gHandler.mainFrame.writeToTextArea("You lost the game! You are Trapped. Please try again later.");
                    gHandler.mainFrame.endScreen("end_game");
                } else {
                    gHandler.mainFrame.writeToTextArea("Wrong password. Try again next time! " + MAGENTA_UNDERLINE + max_attempts + RESET + " attempts remaining");
                }
            }
        }
    }
}
