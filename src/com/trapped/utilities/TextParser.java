package com.trapped.utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/*
 * Text parser will remove "the" and any whitespace in the text returning an array of the remaining words.
 * Only alpha characters will remain after replace is done. Expects to be passed the user input as a string,
 * will then return an ArrayList of the words left after parsing.
 * If an invalid userInput string is passed (blank or null) will return a null ArrayList.
 */

public class TextParser {
    public static ArrayList<String> parseText (String userInput) {
        String trimmed, strArr[];
        ArrayList<String> parsedArr = new ArrayList<>();
        String stripRegex = "[^A-Za-z]";    // specifies that only upper and lower letters acceptable

        if (userInput.isEmpty() || userInput == null) {
            System.out.println("Empty, or null string passed to parser");   // generate notification for invalid input
            return null;
        }

        trimmed = userInput.trim();

        strArr = trimmed.split(stripRegex); // splits string, but keeps alpha characters

        // cycle through array of parsed words, adds them to parsed array if they are a word and not "the"
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

    /*
     * Returns the verb(action keyword) contained within the passed userInput.
     * Expects to be passed the user input as a string,
     * will then return the action word from the userInput if it matches a keyword or synonym.
     * the keywords and synonyms are loaded from json using the FileManager class.
     * If search finds no verbs returns null.
     */

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
        return null;
    }

    /*
     * Returns the noun(object keyword) contained within the passed userInput.
     * Expects to be passed the user input as a string,
     * will then return an ArrayList of acceptable nouns from the userInput if it matches a keyword or synonym.
     * If search finds no keywords returns an empty list.
     */

    public static ArrayList<String> getNouns (String userInput) throws IOException {
        ArrayList<String> parsedArr;
        ArrayList<String> nouns = new ArrayList<>();
        parsedArr = parseText(userInput);

        //keywords from JSON file
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
