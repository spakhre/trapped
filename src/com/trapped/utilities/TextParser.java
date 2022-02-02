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
        Map<String, ArrayList<String>> keywordMap = FileManager.loadJson("verbs.json");

        assert parsedArr != null;
        for (String word: parsedArr) {
            if(keywordMap.containsKey(word)) {  // if userinput matches specific keyword
                return word;
            }
            // checking synonyms, then return keyword if there is a match
            for(Map.Entry<String, ArrayList<String>> entry:  keywordMap.entrySet()) {
                if (entry.getValue().contains(word.toLowerCase())) {
                    return entry.getKey();
                }
            }
        }
        // TODO: maybe change this to return null instead of message
        return null;
    }

    /*
     * accepts userInput string, parses to leave verbs and nouns,
     * then move through parsed array removing the nouns
     * will return an array with verb keywords removed
     */

    public static ArrayList<String> getNouns (String userInput) throws IOException {
        ArrayList<String> parsedArr;
        ArrayList<String> nouns = new ArrayList<>();
        parsedArr = parseText(userInput);
        //keywords from JSON as LinkedTreeMap
        Map<String, ArrayList<String>> verbMap = FileManager.loadJson("verbs.json");
        Map<String, ArrayList<String>> nounMap = FileManager.loadJson("nouns.json");

        assert parsedArr != null;
        String word;
        for (int i = 0; i < parsedArr.size(); i++) {
            word = parsedArr.get(i);
            if(nounMap.containsKey(word)) {
                nouns.add(word);
            }
            for (Map.Entry<String, ArrayList<String>> entry : nounMap.entrySet()) {
                if (entry.getValue().contains(word.toLowerCase())) {
                    nouns.add(entry.getKey());
                }
            }
        }
        return nouns;
    }
}
