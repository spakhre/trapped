package com.trapped.utilities;

import com.trapped.GameEngine;

import java.io.*;

public class SaveAndLoadGame {
    public static final String filename = "Trapped.sav";

    public static void main(String[] args) {
        GameEngine game = new GameEngine();
        System.out.println(game.toString());
        SaveAndLoadGame objectIO = new SaveAndLoadGame();

    }
    static GameEngine game = new GameEngine();
    public static void saveGame() throws IOException {

        try {
            FileOutputStream fos = new FileOutputStream("Trapped.sav");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(game);
            oos.flush();
            oos.close();
            System.out.println("Your Trapped game has been saved.\n");
            System.exit(0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static boolean checkFileExists(){
        return new File("Trapped.sav").isFile();
    }

    public static Object loadGame(){
            try{
                FileInputStream fis = new FileInputStream("Trapped.sav");
                ObjectInputStream ois = new ObjectInputStream(fis);
                Object obj = ois.readObject();

                System.out.println("Your game loaded");
                ois.close();
                return obj;

            }catch (ClassNotFoundException | IOException e){
                e.printStackTrace();
               return null;
            }
        }
}




