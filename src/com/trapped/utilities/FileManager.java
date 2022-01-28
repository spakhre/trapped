package com.trapped.utilities;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;


/*
 * File manager can read data from files
 * getResource expects the text name of a file to be passed including file extension
 * the resource files need to be placed in the resources directory
 */

public class FileManager {
    private FileInputStream in = null;
    private FileOutputStream out = null;
    private Scanner scanner = new Scanner(System.in);

    public static void getResource(String fileName) throws IOException {
        String art = "./resources/" + fileName;
        var out = new BufferedOutputStream(System.out);
        Files.copy(Path.of(art), out);
        out.flush();
    }
    public static void readMessageSlowly(String fileName, int sec) throws InterruptedException {
        String message = convertTxtToString(fileName);
        char[] chars = message.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            System.out.print(chars[i]);
            Thread.sleep(sec);
        }
    }
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
}

