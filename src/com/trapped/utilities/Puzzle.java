package com.trapped.utilities;

import com.google.gson.Gson;
import com.trapped.player.Inventory;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static com.trapped.utilities.TextColor.MAGENTA_UNDERLINE;
import static com.trapped.utilities.TextColor.RESET;

public class Puzzle{
    private static Gson gson = new Gson();
    public static final Map<String, Map<String, Object>> MAP = furniturePuzzleGenerator();
    private int max_attempts = 3;

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
            System.out.println("The puzzle has been solved. Please feel free to explore other furnitures :)");
//            new_command();
        } else {
            solveRiddle();
        }
    }

    public void solveRiddle() {
        System.out.println("A puzzle has been found in " + this.currentLocation + ".");
        System.out.println(getPuzzleDesc());

        System.out.println("Would you like to solve this puzzle now? Y/N");
        String solve_ans = Prompts.getStringInput();

        if ("Y".equalsIgnoreCase(solve_ans)) {
            // riddles puzzle
            Random r = new Random();
            int randomitem = r.nextInt(getConvertedPuzzleFilename().size());
            String randomPuzzle = getConvertedPuzzleFilename().get(randomitem);
            ArrayList<String> randomAnswer = (ArrayList<String>) getMultiplePuzzleAnswer().get(randomitem);

            FileManager.getResource(randomPuzzle);  //print random puzzle.
            // Scanner scan = new Scanner(System.in);
            System.out.println("\nYour answer:      (If it's too hard to answer, please enter [easy] to get a easier question.)");
            String ans = Prompts.getStringInput();

            if (randomAnswer.contains(ans.toLowerCase())) {
                successRiddleAttempt();
                // if user pick easy question
            } else if (ans.equalsIgnoreCase("easy")) {
                getEasyRiddle();
            }// if answer is wrong
            else {
                System.out.println("you didn't solve the puzzle. Try again later.");
            }

        } else if ("N".equalsIgnoreCase(solve_ans)) {
            return;
        } else {
            System.out.println("Sorry I don't understand your command. The puzzle has not been solved. Please come back later.");
        }
    }

    public void successRiddleAttempt() {
        System.out.println(getPuzzleReward());
        Sounds.playSounds(getPuzzleSounds(), 1000);
        System.out.println("You found " + getPuzzleRewardItem().get(0) + ".");
        inventory.checkInvLimit();
        inventory.addItem(getPuzzleRewardItem().get(0));
    }

    public void getEasyRiddle() {
        System.out.println(MAP.get(currentLocation).get("easy_question"));
        String easyInput = Prompts.getStringInput();

        if (easyInput.equals(MAP.get(currentLocation).get("easy_answer"))) {
            System.out.println(getPuzzleReward());
            Sounds.playSounds(getPuzzleSounds(), 1000);
            System.out.println("You found " + this.getPuzzleRewardItem().get(0) + ".");
            inventory.checkInvLimit();
            inventory.addItem(getPuzzleRewardItem().get(0));
        }
    }

    public void useTool(List<String> inventory, String loc) {
        this.currentInventory = inventory;
        this.currentLocation = loc;

        if (inventory.contains(this.getPuzzleRewardItem().get(0))) {
            System.out.println("The puzzle has been solved. Please feel free to explore other furnitures :)");
        } else if ((inventory.contains("key") && loc.equals("safe")) ||
                (inventory.contains("a piece of paper with number 104") && loc.equals("window")) ||
                (inventory.contains("a piece of paper with number 104") && loc.equals("safe"))) {
            System.out.println("The puzzle has been solved. Please feel free to explore other furniture :)");

        } else {
            System.out.println("A puzzle has been found in " + loc + ".");
            System.out.println(getPuzzleDesc());
            System.out.println("Would you like to solve this puzzle now? Y/N");
            String solve_ans = Prompts.getStringInput();
            checkItemsPuzzle(solve_ans);
        }
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


    public void finalPuzzle() {
        System.out.println(getPuzzleDesc());

        System.out.println("What's the password? You have " + MAGENTA_UNDERLINE + max_attempts + RESET + " attempts remaining. If you's like to try later, enter[later]");

        String ans = Prompts.getStringInput();

        if (ans.trim().equals("later") || ans.trim().equals("")) {
            System.out.println("No worries! Try next time!");
        } else {
            while (max_attempts-- > 0) {
                if (ans.trim().equals(getPuzzleAnswer())) {
                    System.out.println(getPuzzleReward());
                    Sounds.playSounds(getPuzzleSounds(), 2000);
                    System.out.println("You won the game! Thanks for playing!");
                    System.exit(0);

                } else if (max_attempts == 0) {
                    System.out.println("You loss the game! You are Trapped. Please try again later.");
                    System.exit(0);
                } else {
                    System.out.println("Wrong password. Try again next time! " + MAGENTA_UNDERLINE + max_attempts + RESET + " attempts remaining");
                }
            }
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