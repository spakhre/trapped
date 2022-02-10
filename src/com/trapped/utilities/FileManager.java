package com.trapped.utilities;

import com.google.gson.Gson;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Stream;

public class FileManager {

    /*
     * Reads data from file and directly outputs to screen with no delay for characters
     * Primarily used for ASCII art and menus
     * Expects to be passed the filename as a string
     */

    public static void getResource(String fileName) {
        String art = "./resources/art/" + fileName;
        try (BufferedReader br = new BufferedReader(new FileReader(art))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * When passed a string and int for time delay implements
     * Primarily used for displaying story text atmospherically
     * Expects to be passed a string and a time delay for the text being displayed
     */

    public static void readMessageSlowly(String fileName, int sec) {
        String message = convertTxtToString(fileName);
        char[] chars = message.toCharArray();
        for (char aChar : chars) {
            System.out.print(aChar);
            try {
                Thread.sleep(sec);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * Reads data from file and directs it to a string
     * Primarily used for getting string for the readMessageSlowly() method
     * Expects to be passed the filename as a string
     */

    public static String convertTxtToString(String fileName){
        String file = "./resources/" + fileName;
        Path path = Paths.get(file);
        StringBuilder sb = new StringBuilder();

        try (Stream<String> stream = Files.lines(path)) {
            stream.forEach(s -> sb.append(s).append("\n"));
        } catch (IOException e) {
            System.out.println("File not found.");
        }
        return sb.toString();
    }

    /*
     * Returns a LinkedTreeMap object from read JSON file passed to method as name of file
     * Usage example: Map<String, ArrayList<String>> map = FileManager.loadJson("filename.json")
     * will return null if file not found
     */

    public static Map<String, ArrayList<String>> loadJson(String fileName) {
        String file = "./resources/cfg/" + fileName;
        Gson gson = new Gson();

        try {
            if (Files.exists(Path.of(file))) {
                Reader reader = Files.newBufferedReader(Paths.get(file));
                Map<String, ArrayList<String>> map = gson.fromJson(reader, Map.class);
                reader.close();
                return map;
            }
        }
        catch(IOException e) {
            System.out.println(file + " not found");
        }
        return null;
    }

    public static void writeJSON(Map<String, Map<String, Object>> file, String path){
        try(Writer w = new FileWriter(path)){
            new Gson().toJson(file,w);
            w.flush();
        }catch(IOException e){
            System.err.print(e);
        }
    }
    public static void writeJSONDefaults(Map<String, Map<String, Object>> file, String path){
        try(Writer w = new FileWriter(path)){
            new Gson().toJson(file,w);
            w.flush();
        }catch(IOException e){
            System.err.print(e);
        }
    }

    public static Map<String, Map<String, Object>> loadFurniturePuzzlesTestJson(String path) {
        Gson gson = new Gson();
        try {
            Reader reader = Files.newBufferedReader(Paths.get(path));
            Map<String, Map<String, Object>> map = gson.fromJson(reader, Map.class);
            reader.close();
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeDefaults(){
        String dirtyFilePath = "./resources/furniture_puzzles.json";
        String cleanFilePath = "./resources/defaults/furniture_puzzles_test_defaults.json";
        Map<String, Map<String, Object>> cleanFile = loadFurniturePuzzlesTestJson(cleanFilePath);
        writeJSONDefaults(cleanFile,dirtyFilePath);
    }
}
