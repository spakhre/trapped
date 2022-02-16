package com.trapped.gui;

import com.google.gson.internal.LinkedTreeMap;
import com.trapped.utilities.FileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonMap {

    /**
     * Example of Mapping in JSON:
     * parentKey is bed or currentLocation
     * childKey is "furniture_desc" / "name" / "furniture_items".
     *
     * So, to get description, must need to pass currentLocation and furniture_desc
     * So, to get items, we need to pass, currentLocation and "furniture_items"
     *
     * The return value could be of either String or ArrayList or Something else,
     * Hence, the return type is generic 'Object'
     * Must need to cast to right type.
     * @param parentKey
     * @param childKey
     * @return
     */

    private static Object getChildKeyForParentKey(String parentKey, String childKey) {

        //any possible object values(Can be String, Array or anything)
        Map<String, ?> keywordMap = loadMap();

        Set<String> keySet = keywordMap.keySet();
        for(String k: keySet) {
            if(k.equals(parentKey)) {
                //get value map for given key(for example, key = bed
                LinkedTreeMap<String, LinkedTreeMap> entry = (LinkedTreeMap<String, LinkedTreeMap>) keywordMap.get(k);
                Object childKeyValue = entry.get(childKey);
                return childKeyValue;
            }
        }
        return null;
    }

    private static Map<String, ?> loadMap() {
        Map<String, ?> keyWordMap = FileManager.fromJsonAsMap("furniture_puzzles.json");
        return keyWordMap;
    }

    static List<String> getFurnitureItems(String location) {
        List<String> list = new ArrayList<>();
        String parentKey = location;
        String childKey = "furniture_items";
        Object value = JsonMap.getChildKeyForParentKey(parentKey, childKey);

        //Expected type of value is java.util.ArrayList<String>
        list = (List<String>) value;

        return list;
    }
}
