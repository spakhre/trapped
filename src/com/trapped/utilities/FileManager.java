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
     * Reads ASCII data from file and outputs to screen with no delay
     * Primarily used for ASCII art and menus
     * Expects to be passed the filename as a string
     */
    public static void getResource(String fileName) {
        String art = "./resources/art/" + fileName;
        try {
            Files.lines(Path.of(art)).forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Reads text data from file and outputs with a delay in milliseconds
     * Primarily used for displaying story text atmospherically
     * Expects to be passed a string and a time delay for the text being displayed
     */
    public static void readMessageSlowly(String fileName, int millis) {
        String message = convertTxtToString(fileName);
        char[] chars = message.toCharArray();
        for (char aChar : chars) {
            System.out.print(aChar);
            try {
                Thread.sleep(millis);
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

    //Attempt at a more generic load json, assume at least the key would be a String with unknown value
    public static Map<String, ?> fromJsonAsMap(String fileName) {
        String file = "./resources/cfg/" + fileName;
        Gson gson = new Gson();

        try {
            if (Files.exists(Path.of(file))) {
                Reader reader = Files.newBufferedReader(Paths.get(file));
                Map<String, ?> map = gson.fromJson(reader, Map.class);
                reader.close();
                return map;
            }
        }
        catch(IOException e) {
            System.out.println(file + " not found");
        }
        return null;
    }
}