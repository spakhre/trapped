package com.trapped;

import com.google.gson.Gson;
import com.trapped.utilities.FileManager;
import com.trapped.utilities.Sounds;
import com.trapped.utilities.TextParser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.trapped.utilities.TextColor.MAGENTA_UNDERLINE;
import static com.trapped.utilities.TextColor.RESET;

class ActionController implements ActionListener {

    private static GameHandler gHandler;

    public ActionController(GameHandler gHandler) {
        this.gHandler = gHandler;
    }

    static List<Boolean> bedArr = Arrays.asList(true, false, false, false, false, false, false, false, false, false, false, false, false, false);
    static List<Boolean> doorArr = Arrays.asList(false, true, false, false, false, false, false, false, false, false, false, false, false, false);
    static List<Boolean> safeArr = Arrays.asList(false, false, true, false, false, false, false, false, false, false, false, false, false, false);
    static List<Boolean> deskArr = Arrays.asList(false, false, false, true, false, false, false, false, false, false, false, false, false, false);
    static List<Boolean> lampArr = Arrays.asList(false, false, false, false, true, false, false, false, false, false, false, false, false, false);
    static List<Boolean> chairArr = Arrays.asList(false, false, false, false, false, true, false, false, false, false, false, false, false, false);
    static List<Boolean> windowArr = Arrays.asList(false, false, false, false, false, false, true, false, false, false, false, false, false, false);

    public static String verb;
    public static String noun;
    public static String location = "bed";
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
        Map<String, Object> furniture = map.get(location);
        ArrayList<String> furniture_items = (ArrayList<String>) furniture.get("furniture_items");
        switch (noun) {
            case "bed":
            case "door":
            case "window":
            case "drawer":
            case "safe":
            case "lamp":
            case "chair":
                if (furniture_items.isEmpty() || furniture_items == null) {
                    noItemsInRoom(location);
                } else {
                    roomWithItems(location);
                }
                break;
            case "paper":
                gHandler.mainFrame.writeToTextArea("A Piece of paper with the numbers 104 on it.");
                break;
            case "crowbar":
                gHandler.mainFrame.writeToTextArea("Be careful with that, Mr. Freeman...");
                break;
            default:
                gHandler.mainFrame.writeToTextArea("It's just an ordinary " + noun + ".");
        }
    }

    private static void roomWithItems(String something) {
        Map<String, Object> furniture = map.get(something);
        ArrayList<String> furniture_items = (ArrayList<String>) furniture.get("furniture_items");
        String furniture_desc = (String) furniture.get("furniture_desc");
        if (location.equals("window") && puzzleSolved()) {
            gHandler.mainFrame.writeToTextArea("Someone broke this window!..." + "\nYou see: " + furniture_items + ".");
        } else {
            gHandler.mainFrame.writeToTextArea(furniture_desc + "\nYou see: " + furniture_items + ".");
            for (String item : furniture_items) {
                unhideItemOnNavScreen(item, getLocationBoolArr(location));
            }
        }
    }

    private static void unhideItemOnNavScreen(String item, List<Boolean> arr) {
        switch (item) {
            case "key":
                gHandler.mainFrame.key.setVisible(true);
                gHandler.mainFrame.keyLabel.setVisible(false);
                arr.set(8, true);
                break;
            case "wallet":
                gHandler.mainFrame.wallet.setVisible(true);
                gHandler.mainFrame.walletLabel.setVisible(false);
                arr.set(9, true);
                break;
            case "crowbar":
                gHandler.mainFrame.crowbar.setVisible(true);
                gHandler.mainFrame.crowbarLabel.setVisible(false);
                arr.set(13, true);
                break;
            case "paper":
                gHandler.mainFrame.paper.setVisible(true);
                gHandler.mainFrame.paperLabel.setVisible(false);
                arr.set(10, true);
                break;
            case "candle":
                gHandler.mainFrame.candle.setVisible(true);
                gHandler.mainFrame.candleLabel.setVisible(false);
                arr.set(12, true);
                break;
            case "matchbox":
                gHandler.mainFrame.matches.setVisible(true);
                gHandler.mainFrame.matchLabel.setVisible(false);
                arr.set(11, true);
                break;
        }
    }

    private static void hideItemOnNavScreen(String item, List<Boolean> arr) {
        switch (item) {
            case "key":
                gHandler.mainFrame.key.setVisible(false);
                gHandler.mainFrame.keyLabel.setVisible(true);
                arr.set(8, false);
                break;
            case "wallet":
                gHandler.mainFrame.wallet.setVisible(false);
                gHandler.mainFrame.walletLabel.setVisible(true);
                arr.set(9, false);
                break;
            case "crowbar":
                gHandler.mainFrame.crowbar.setVisible(false);
                gHandler.mainFrame.crowbarLabel.setVisible(true);
                arr.set(13, false);
                break;
            case "paper":
                gHandler.mainFrame.paper.setVisible(false);
                gHandler.mainFrame.paperLabel.setVisible(true);
                arr.set(10, false);
                break;
            case "candle":
                gHandler.mainFrame.candle.setVisible(false);
                gHandler.mainFrame.candleLabel.setVisible(true);
                arr.set(12, false);
                break;
            case "matchbox":
                gHandler.mainFrame.matches.setVisible(false);
                gHandler.mainFrame.matchLabel.setVisible(true);
                arr.set(11, false);
                break;
        }
    }


    private static void noItemsInRoom(String something) {
        Map<String, Object> furniture = map.get(something);
        if (location.equals("window") && puzzleSolved()) {
            gHandler.mainFrame.writeToTextArea("Someone broke this window!..." + "\nNo items found here.");
        } else {
            String furniture_desc = (String) furniture.get("furniture_desc");
            gHandler.mainFrame.writeToTextArea(furniture_desc + "\nNo items found here.");
        }
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
        hideItemOnNavScreen(noun, getLocationBoolArr(location));
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
                unhideItemOnNavScreen(noun, getLocationBoolArr(location));
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
                moveDirection(noun);
                break;
            case "get":
                get(noun);
                break;
            case "inspect":
                inspectItem(noun);
                break;
            case "help":
                helpMenu();
                break;
            case "drop":
                dropItem(noun);
                break;
            case "final":
                System.out.println("clicked on door");
                doorPuzzle();
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

    private static void get(String noun) {
        Map<String, Object> furniture = map.get(location);
        ArrayList<String> furniture_items = (ArrayList<String>) furniture.get("furniture_items");
        ArrayList<String> puzzle_reward_item = (ArrayList<String>) furniture.get("puzzle_reward_item");

        if (noun != null) {
            if (furniture_items.contains(noun)) {
                gHandler.mainFrame.writeToTextArea("You added " + noun + " to your inventory");
                addItemAndUpdateJson(noun);
            } else if (puzzle_reward_item.contains(noun)) {
                inventory.add(noun);
            } else {
                gHandler.mainFrame.writeToTextArea("Sorry, I don't understand your input, please enter again. ");
                FileManager.getResource("commands.txt");
            }
        } else {
            gHandler.mainFrame.writeToTextArea("Sorry, I don't understand your input, please enter again. ");
            FileManager.getResource("commands.txt");
        }
    }

    private static void moveDirection(String direction) {
        Map<String, Object> furniture = map.get(location);
        location = (String) furniture.get(direction);
        gHandler.mainFrame.writeToTextArea("Now you are in front of " + location);
        switch (location) {
            case "bed":
                gHandler.mainFrame.gameScreen(bedArr);
                break;
            case "door":
                gHandler.mainFrame.gameScreen(doorArr);
                break;
            case "window":
                gHandler.mainFrame.gameScreen(windowArr);
                break;
            case "drawer":
                gHandler.mainFrame.gameScreen(deskArr);
                break;
            case "safe":
                gHandler.mainFrame.gameScreen(safeArr);
                break;
            case "lamp":
                gHandler.mainFrame.gameScreen(lampArr);
                break;
            case "chair":
                gHandler.mainFrame.gameScreen(chairArr);
                break;
        }
    }


    private static void helpMenu() {
        FileManager.getResource("helperMenu.txt");
        System.out.println("\nTo select from the options above enter a number 1-4.");
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
        String riddle1 = "Double my number, I'm less than a score, Half of my number is less than four.\n" +
                "Add one to my double when bakers are near, Days of the week are still greater,\n" +
                "I fear. ";
        String riddle2 = "If there are three apples and you take away two, how many apples do you have?";
        String riddle3 = "If you buy a rooster for the purpose of laying eggs and you expect to get three eggs each\n" +
                "day for breakfast, how many eggs will you have after three weeks?";
        String ans1 = "6";
        String ans2 = "2";
        String ans3 = "0";
        ArrayList<String> riddles = new ArrayList<>();
        riddles.add(riddle1);
        riddles.add(riddle2);
        riddles.add(riddle3);
        ArrayList<String> answers = new ArrayList<>();
        answers.add(ans1);
        answers.add(ans2);
        answers.add(ans3);

        if (!puzzleSolved()) {
            System.out.println("A puzzle has been found in " + loc + ".");
            System.out.println(puzzle_desc);
            // riddles puzzle
            Random r = new Random();
            int randomItem = r.nextInt(3);
            String randomPuzzle = riddles.get(randomItem);
            gHandler.mainFrame.writeToTextArea(randomPuzzle);
            gHandler.mainFrame.SUBMITbtn.addActionListener(e -> {
                ANSWER = gHandler.mainFrame.inputText.getText();
                // if user input correct answer
                if (ANSWER.equals(answers.get(randomItem))) {
                    gHandler.mainFrame.writeToTextArea("puzzle_reward");
                    gHandler.mainFrame.writeToTextArea("You found " + puzzle_reward_item.get(0) + ".");
                    addItemAndUpdateJson(puzzle_reward_item.get(0));
                } else {
                    gHandler.mainFrame.writeToTextArea("you didn't solve the puzzle. Try again.");
                }
            });
        }
    }


    public static boolean puzzleSolved() {
        //checks to see if the player has solved any of the puzzles, if they have, returns true to the caller!
        Boolean solved = false;
        if (((inventory.contains("crowbar") && location.equals("safe")) ||
                (inventory.contains("key") && location.equals("window")) ||
                (inventory.contains("a piece of paper with number 104") && location.equals("drawer")))) {
            gHandler.mainFrame.writeToTextArea("The puzzle has been solved. You should focus on escaping!");
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
            gHandler.mainFrame.writeToTextArea("A puzzle has been found in " + loc + "." + puzzle_desc + "You need to use an item from your inventory. Let's see if you got needed item in your inventory...\n Your current inventory: " + inventory);
            if (!inventory.contains(puzzle_itemsNeeded.get(0))) {
                gHandler.mainFrame.writeToTextArea("Sorry, you don't have the right tool. Explore the room and see if you can find anything");
            } else if (inventory.contains(puzzle_itemsNeeded.get(0))) {
                gHandler.mainFrame.writeToTextArea(puzzle_reward + " and you've found " + puzzle_reward_item.get(0));
                inventory.remove(puzzle_itemsNeeded.get(0));
                switch (puzzle_itemsNeeded.get(0)) {
                    case "crowbar":
                        gHandler.mainFrame.crowbar.setVisible(false);
                        gHandler.mainFrame.crowbarLabel.setVisible(false);
                        gHandler.mainFrame.windowWithKey.setVisible(false);
                        gHandler.mainFrame.windowWithoutKey.setVisible(true);
                        windowArr.set(6, false);
                        windowArr.set(7, true);
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
            gHandler.mainFrame.writeToTextArea("Sorry I don't understand your command. The puzzle has not been solved. Please come back later.");
        }
    }


    private static void doorPuzzle() {
        //Final puzzle in the game, can be solved at any time if you know the secret number (104)
        Map<String, Object> furniture = map.get("door");
        String puzzle_desc = (String) furniture.get("puzzle_desc");
        String puzzle_answer = (String) furniture.get("puzzle_answer");
        String puzzle_reward = (String) furniture.get("puzzle_reward");
        gHandler.mainFrame.writeToTextArea("Please enter the 3-digit passcode." + "You have " + max_attempts +
                " attempts remaining. If you'll like to try later, enter 'later' in the test box below");
        gHandler.mainFrame.SUBMITbtn.addActionListener(e -> {
            ANSWER = gHandler.mainFrame.inputText.getText();

            if (ANSWER.trim().equals("later") || ANSWER.trim().equals("")) {
                gHandler.mainFrame.writeToTextArea("No worries! Try next time!");
            } else {
                if (ANSWER.trim().equals(puzzle_answer)) {
                    gHandler.mainFrame.writeToTextArea(puzzle_reward + "You won the game! Thanks for playing!");
                    System.out.println("game won");
                    gHandler.mainFrame.winScreen("end_game");
                } else if (max_attempts == 0) {
                    gHandler.mainFrame.writeToTextArea("You lost the game! You are Trapped. Please try again later.");
                    gHandler.mainFrame.loseScreen("exploded");
                } else {
                    max_attempts--;
                    gHandler.mainFrame.writeToTextArea("Wrong password. Try again next time! " + max_attempts + " attempts remaining");
                }
            }
        });
    }

    private static List<Boolean> getLocationBoolArr(String location) {
        List<Boolean> boolLi;
        switch (location) {
            case "bed":
                boolLi = bedArr;
                break;
            case "door":
                boolLi = doorArr;
                break;
            case "window":
                boolLi = windowArr;
                break;
            case "drawer":
                boolLi = deskArr;
                break;
            case "safe":
                boolLi = safeArr;
                break;
            case "lamp":
                boolLi = lampArr;
                break;
            case "chair":
                boolLi = chairArr;
                break;
            default:
                boolLi = null;
        }
        return boolLi;
    }
}
