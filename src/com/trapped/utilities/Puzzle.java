package com.trapped.utilities;

import com.google.gson.Gson;
import com.trapped.client.Main;
import com.trapped.gui.MainWindow;
import com.trapped.player.Inventory;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Puzzle{
    private static Gson gson = new Gson();
    public static final Map<String, Map<String, Object>> MAP = furniturePuzzleGenerator();
    private String puzzleDesc;
    private String puzzleExist;
    private String puzzleVerb;
    private ArrayList<String> puzzleItemsNeeded;
    private String puzzleReward;
    private ArrayList<Object> puzzleFileName;
    private ArrayList<String> convertedPuzzleFilename;
    private String puzzleAnswer;
    private ArrayList<Object> multiplePuzzleAnswer;
    private ArrayList<String> convertedMultiplePuzzleAnswer;
    private ArrayList<String> puzzleRewardItem;
    private String puzzleType;
    private String puzzleSounds;

    private static final Puzzle PUZZLE = new Puzzle();
    private static Inventory inventory = Inventory.getInstance();

    private String currentLocation;
    private List<String> currentInventory;
    int attemptsLeft = 3;

    private Puzzle() {
    }

    public static Puzzle getInstance() {
        return PUZZLE;
    }

    //Utility class for the map generated above, trying to ensure json file is closed
    private static Map<String, Map<String, Object>> furniturePuzzleGenerator() {
        try (Reader reader = Files.newBufferedReader(Paths.get("resources/furniture_puzzles.json"));) {
            Map<String, Map<String, Object>> map = gson.fromJson(reader, Map.class);
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, Object> generatePuzzle(String loc) {
        Map<String, Object> furniture = MAP.get(loc);
        setPuzzleDesc((String) furniture.get("puzzle_desc"));
        setPuzzleExist((String) furniture.get("puzzle_exist"));
        setPuzzleVerb((String) furniture.get("puzzle_verb"));
        setPuzzleItemsNeeded((ArrayList<String>) furniture.get("puzzle_itemsNeeded"));
        setPuzzleReward((String) furniture.get("puzzle_reward"));
        setPuzzleFileName((ArrayList<Object>) furniture.get("puzzle_filename"));
        setConvertedPuzzleFilename((ArrayList<String>) (ArrayList<?>) (this.puzzleFileName));
        setPuzzleAnswer((String) furniture.get("puzzle_answer"));
        setMultiplePuzzleAnswer((ArrayList<Object>) furniture.get("multiple_puzzle_answer"));
        setConvertedMultiplePuzzleAnswer((ArrayList<String>) (ArrayList<?>) (this.multiplePuzzleAnswer));
        setPuzzleRewardItem((ArrayList<String>) furniture.get("puzzle_reward_item"));
        setPuzzleType((String) furniture.get("puzzle_type"));
        setPuzzleSounds((String) furniture.get("puzzle_sounds"));
        return furniture;
    }

    public void checkRiddle(List<String> inventory, String loc) {
        this.currentInventory = inventory;
        this.currentLocation = loc;
        if ((inventory.contains(getPuzzleRewardItem().get(0))) || (inventory.contains("key") && loc.equals("safe")) ||
                (inventory.contains("a piece of paper with number 104") && loc.equals("window")) ||
                (inventory.contains("a piece of paper with number 104") && loc.equals("safe"))) {
            JOptionPane.showMessageDialog(Main.mainWindow, "The puzzle has been solved. Please feel free to explore other furnitures :)");
//            new_command();
        } else {
            solveRiddle();
        }
    }



    public void solveRiddle() {
        String message = "A puzzle has been found in " + this.currentLocation + ". " + getPuzzleDesc() +
                "Would you like to solve this puzzle now?";
        int choice = JOptionPane.showConfirmDialog(Main.mainWindow, message, "CONFIRM", JOptionPane.YES_NO_OPTION);

        //String solve_ans = Prompts.getStringInput();

        if (choice == JOptionPane.YES_OPTION) {
            // riddles puzzle
            Random r = new Random();
            int randomitem = r.nextInt(getConvertedPuzzleFilename().size());
            String randomPuzzle = getConvertedPuzzleFilename().get(randomitem);
            ArrayList<String> randomAnswer = (ArrayList<String>) getMultiplePuzzleAnswer().get(randomitem);
            String puzzleText = FileManager.getResource(randomPuzzle);  //print random puzzle.
            JOptionPane.showMessageDialog(Main.mainWindow, puzzleText, "Puzzle", JOptionPane.OK_OPTION);
            // Scanner scan = new Scanner(System.in);
            //System.out.println("\nYour answer:      (If it's too hard to answer, please enter [easy] to get a easier question.)");

            String ans = JOptionPane.showInputDialog(Main.mainWindow, "Your answer: If it's too hard to answer, please enter [easy] to get a easier question. ");
            if (randomAnswer.contains(ans.toLowerCase())) {
                successRiddleAttempt();
                // if user pick easy question
            } else if (ans.equalsIgnoreCase("easy")) {
                getEasyRiddle();
            }// if answer is wrong
            else {
                JOptionPane.showMessageDialog(Main.mainWindow, "you didn't solve the puzzle. Try again later.");
            }
        }
    }

    public void successRiddleAttempt() {
        JOptionPane.showMessageDialog(Main.mainWindow, getPuzzleReward());
        Sounds.playSounds(getPuzzleSounds(), 1000);
        JOptionPane.showMessageDialog(Main.mainWindow,"You found " + getPuzzleRewardItem().get(0) + ".");
        inventory.checkInvLimit();
        inventory.addItem(getPuzzleRewardItem().get(0));
    }

    public void getEasyRiddle() {
        //System.out.println(MAP.get(currentLocation).get("easy_question"));
        String easyInput = JOptionPane.showInputDialog(Main.mainWindow, MAP.get(currentLocation).get("easy_question"));

        if (easyInput.equals(MAP.get(currentLocation).get("easy_answer"))) {
            //System.out.println(getPuzzleReward());
            JOptionPane.showMessageDialog(Main.mainWindow, getPuzzleReward());
            Sounds.playSounds(getPuzzleSounds(), 1000);
            JOptionPane.showMessageDialog(Main.mainWindow, "You found " + this.getPuzzleRewardItem().get(0) + ".");
            inventory.checkInvLimit();
            inventory.addItem(getPuzzleRewardItem().get(0));
            MainWindow.getGamePanel().displayInventoryDetails();
        } else {
            JOptionPane.showMessageDialog(Main.mainWindow, "Your answer was wrong!");
        }
    }

    public Map<String, String> useTool(String location, String item) {
        Map<String, String> result = new HashMap<>();
        generatePuzzle(location);
        if (!"use tool".equals(getPuzzleType())) {
            System.out.println("Sorry cannot use this item here");
            result.put("error", "Sorry cannot use this item here");
            return result;
        }


        if(inventory.getInvList().contains(this.getPuzzleRewardItem().get(0))) {
            System.out.println("The puzzle has been solved. Please feel free to explore other furnitures :)");
        }
        if (item.equals(getPuzzleItemsNeeded().get(0))) {

            result.put("puzzleDescription", getPuzzleDesc());
            System.out.println(getPuzzleReward() + " and you've found " + this.getPuzzleRewardItem().get(0));
            inventory.addItem(getPuzzleRewardItem().get(0));
//            System.out.println("A puzzle has been found in " + loc + ".");
//            System.out.println(getPuzzleDesc());
//            System.out.println("Would you like to solve this puzzle now? Y/N");
//            String solve_ans = Prompts.getStringInput();
//            checkItemsPuzzle(solve_ans);
        }
        return result;
    }

    private void checkItemsPuzzle(String solve_ans) {

        if (solve_ans.equalsIgnoreCase("Y")) {
            System.out.println("You need to use an item from your inventory. " +
                    "Let's see if you've got the needed item in your inventory...");
            System.out.println("Your current inventory: " + this.currentInventory);
            if (!this.currentInventory.contains(getPuzzleItemsNeeded().get(0))) {
                System.out.println("Sorry, you don't have the tools. Explore the room and see if you can find anything");
            } else if (this.currentInventory.contains(getPuzzleItemsNeeded().get(0))) {
                System.out.println("Which item would you like to use?");
                String ans = Prompts.getStringInput();
                // Perhaps insert option to remake choice if not correct item but correct item is in inventory?
                if ((ans.equalsIgnoreCase(getPuzzleVerb() + " " + getPuzzleItemsNeeded().get(0))) || ans.equalsIgnoreCase(getPuzzleItemsNeeded().get(0))) {
                    Sounds.playSounds(getPuzzleSounds(), 1000);
                    System.out.println(getPuzzleReward() + " and you've found " + this.getPuzzleRewardItem().get(0));
                    if (!"crowbar".equals(ans)){
                        this.currentInventory.remove(getPuzzleItemsNeeded().get(0));
                    }
                    inventory.checkInvLimit();
                    inventory.addItem(getPuzzleRewardItem().get(0));
                } else if ((this.currentInventory.contains(ans) && (!ans.equals(getPuzzleItemsNeeded())))) {
                    System.out.println("Wrong item. The puzzle has not been solved. Please come back later.");

                } else {
                    System.out.println("Sorry I don't understand your command. The puzzle has not been solved. Please come back later.");
                }
            }
        } else if (solve_ans.equalsIgnoreCase("N")) {
            return;
        } else {
            System.out.println("Sorry I don't understand your command. The puzzle has not been solved. Please come back later.");
        }
    }


//    public void finalPuzzle() {
//        System.out.println(getPuzzleDesc());
//
//        System.out.println("What's the password? You have " + MAGENTA_UNDERLINE + attemptsLeft + RESET + " attempts remaining. If you's like to try later, enter[later]");
//
//        String ans = Prompts.getStringInput();
//
//        if (ans.trim().equals("later") || ans.trim().equals("")) {
//            System.out.println("No worries! Try next time!");
//        } else {
//            while (attemptsLeft-- > 0) {
//                if (ans.trim().equals(getPuzzleAnswer())) {
//                    System.out.println(getPuzzleReward());
//                    Sounds.playSounds(getPuzzleSounds(), 2000);
//                    System.out.println("You won the game! Thanks for playing!");
//                    System.exit(0);
//
//                } else if (attemptsLeft == 0) {
//                    System.out.println("You loss the game! You are Trapped. Please try again later.");
//                    System.exit(0);
//                } else {
//                    System.out.println("Wrong password. Try again next time! " + MAGENTA_UNDERLINE + attemptsLeft + RESET + " attempts remaining");
//                }
//            }
//        }
//    }

    public String finalPuzzle(String attempt){
        this.currentLocation = "door";

        if ("104".equals(attempt)){
            Sounds.playSounds(getPuzzleSounds(), 2000);
            return "Success";
        }
        else {
            attemptsLeft--;
            return attemptsLeft + " left";
        }
    }

    public String getPuzzleDesc() {
        return puzzleDesc;
    }

    public void setPuzzleDesc(String puzzleDesc) {
        this.puzzleDesc = puzzleDesc;
    }

    public String getPuzzleExist() {
        return puzzleExist;
    }

    public void setPuzzleExist(String puzzleExist) {
        this.puzzleExist = puzzleExist;
    }

    public String getPuzzleVerb() {
        return puzzleVerb;
    }

    public void setPuzzleVerb(String puzzleVerb) {
        this.puzzleVerb = puzzleVerb;
    }

    public ArrayList<String> getPuzzleItemsNeeded() {
        return puzzleItemsNeeded;
    }

    public void setPuzzleItemsNeeded(ArrayList<String> puzzleItemsNeeded) {
        this.puzzleItemsNeeded = puzzleItemsNeeded;
    }

    public String getPuzzleReward() {
        return puzzleReward;
    }

    public void setPuzzleReward(String puzzleReward) {
        this.puzzleReward = puzzleReward;
    }

    public ArrayList<Object> getPuzzleFileName() {
        return puzzleFileName;
    }

    public void setPuzzleFileName(ArrayList<Object> puzzleFilename) {
        this.puzzleFileName = puzzleFilename;
    }

    public ArrayList<String> getConvertedPuzzleFilename() {
        return convertedPuzzleFilename;
    }

    public void setConvertedPuzzleFilename(ArrayList<String> convertedPuzzleFilename) {
        this.convertedPuzzleFilename = convertedPuzzleFilename;
    }

    public String getPuzzleAnswer() {
        return puzzleAnswer;
    }

    public void setPuzzleAnswer(String puzzleAnswer) {
        this.puzzleAnswer = puzzleAnswer;
    }

    public ArrayList<Object> getMultiplePuzzleAnswer() {
        return multiplePuzzleAnswer;
    }

    public void setMultiplePuzzleAnswer(ArrayList<Object> multiplePuzzleAnswer) {
        this.multiplePuzzleAnswer = multiplePuzzleAnswer;
    }

    public ArrayList<String> getConvertedMultiplePuzzleAnswer() {
        return convertedMultiplePuzzleAnswer;
    }

    public void setConvertedMultiplePuzzleAnswer(ArrayList<String> convertedMultiplePuzzleAnswer) {
        this.convertedMultiplePuzzleAnswer = convertedMultiplePuzzleAnswer;
    }

    public ArrayList<String> getPuzzleRewardItem() {
        return this.puzzleRewardItem;
    }

    public void setPuzzleRewardItem(ArrayList<String> puzzleRewardItem) {
        this.puzzleRewardItem = puzzleRewardItem;
    }

    public String getPuzzleType() {
        return puzzleType;
    }

    public void setPuzzleType(String puzzleType) {
        this.puzzleType = puzzleType;
    }

    public String getPuzzleSounds() {
        return puzzleSounds;
    }

    public void setPuzzleSounds(String puzzleSounds) {
        this.puzzleSounds = puzzleSounds;
    }

}