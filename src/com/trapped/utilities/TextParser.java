package com.trapped.utilities;

import com.google.gson.internal.LinkedTreeMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class TextParser {
    public static ArrayList<String> parseText (String userInput) {
        String trimmed, strArr[];
        ArrayList<String> parsedArr = new ArrayList<>();
        String stripRegex = "[^A-Za-z]";

        if (userInput.isEmpty() || userInput == null) {
            System.out.println("Empty, or null string passed to parser");
            return null;
        }

        trimmed = userInput.trim();

        strArr = trimmed.split(stripRegex);

        for (int i = 0; i < strArr.length; i++) {
            if (strArr[i].equals("")) {
                continue;
            }
            else if (strArr[i].equalsIgnoreCase("the")) {
                continue;
            }
            else {
                parsedArr.add(strArr[i]);
            }
        }
        return parsedArr;
    }

    public static String getVerb (String userInput) throws IOException {
        ArrayList<String> parsedArr;

        parsedArr = parseText(userInput);
        //keywords from JSON as LinkedTreeMap
        Map<String, ArrayList<String>> keywordMap = FileManager.loadJson("keywords.json");

        assert parsedArr != null;
        for (String word: parsedArr) {
            if(keywordMap.containsKey(word)) {
                return word;
            }

            for(Map.Entry<String, ArrayList<String>> entry:  keywordMap.entrySet()) {
                if (entry.getValue().contains(word.toLowerCase())) {
                    return entry.getKey();
                }
            }
        }
        return "Command not found in list of keywords";
    }
}
