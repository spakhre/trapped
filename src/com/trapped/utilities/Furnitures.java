package com.trapped.utilities;

import java.util.List;

public class Furnitures {
    String name; // furniture name
    String desc; // furniture description

    // constructor of a furniture
    public Furnitures(String name, String desc, List<String> pickable_item, List<String> puzzle, String left_furniture,
                      String right_furniture, String up_furniture, String down_furniture){
        this.name=name;
        this.desc=desc;

    }

    // description and put back on screen.
    public void inspectFurniture(){

    }



}