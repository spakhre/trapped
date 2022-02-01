package com.trapped.utilities;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;

/*
 * File manager can read data from files
 * getResource expects the text name of a file to be passed including file extension
 * the resource files need to be placed in the resources directory
 */

public class FileManager {
    private FileInputStream in = null;
    private FileOutputStream out = null;
    private Scanner scanner = new Scanner(System.in);

    /*
     * Reads data from file and directly outputs to screen with no delay for characters
     * Primarily used for ASCII art and menus
     * Expects to be passed the filename as a string
     */

    public static void getResource(String fileName) throws IOException {
        String art = "./resources/art/" + fileName;
        var out = new BufferedOutputStream(System.out);
        Files.copy(Path.of(art), out);
        out.flush();
    }

    /*
     * When passed a string and int for time delay implements
     * Primarily used for displaying story text atmospherically
     * Expects to be passed a string and a time delay for the text being displayed
     */

    public static void readMessageSlowly(String fileName, int sec) throws InterruptedException {
        String message = convertTxtToString(fileName);
        char[] chars = message.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            System.out.print(chars[i]);
            Thread.sleep(sec);
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
        } catch (IOException ex) {
        }
        String contents = sb.toString();
        return contents;
    }

    /*
     * Returns a LinkedTreeMap object from read JSON file passed to method as name of file
     * Usage example: Map<String, ArrayList<String>> map = FileManager.loadJson("filename.json")
     */
    public static Map<String, ArrayList<String>> loadJson(String fileName) throws IOException {
        String file = "./resources/cfg/" + fileName;
        Reader reader = Files.newBufferedReader(Paths.get(file));
        Gson gson = new Gson();
        Map<String, ArrayList<String>> map = gson.fromJson(reader, Map.class);
        reader.close();

        return map;
    }

    /*
     *
     */

}

