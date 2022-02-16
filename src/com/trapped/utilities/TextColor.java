package com.trapped.utilities;

public class TextColor {
    // Declaring RESET so that we can reset the color and returns console to its default
    public static final String RESET = "\u001B[0m";

    // Plain and bold Text Color
    public static final String RED = "\u001B[31m";
    public static final String BLUE_BOLD = "\033[0;94m";
    public static final String GREEN = "\033[1;32m";
    public static final String MAGENTA_UNDERLINE = "\033[4;35m"; //bold_underline

    //Background Colors
    public static final String RED_BACKGROUND = "\u001B[41m";
    public static final String YELLOW_BACKGROUND = "\u001B[43m";
}