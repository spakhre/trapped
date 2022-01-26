// Items class defines furnitures, items players gonna use to solve puzzles, and puzzles.

public class Items {
    String name;
    String desc;


    // constructor
    public Items(String name, String desc){
        this.name = name;
        this.desc = desc;
    }

    public void inspectItems(){

    }

    public String getName(){
        return name;
    }
    public String getDesc(){
        return desc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
