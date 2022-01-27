package com.trapped.utilities;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;


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
        String art = "resources/" + fileName;
        var out = new BufferedOutputStream(System.out);
        Files.copy(Path.of(art), out);
        out.flush();
    }

}

