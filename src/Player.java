import java.util.HashMap;
import java.util.Map;

public class Player {
    Map<String, Items> inventory = new HashMap<String, Items>(); // player's inventory
    Furnitures location;
    // constructor
    public Player(String name, Furnitures location){
        this.location = location; // player wakes up in the bed.
    }

    public void inspectItem(){

    }
    public void pickUpItem(){

    }

    // eg. use key with Window; solve puzzle
    public void useItem(){

    }

    public void checkCurrentInventory(){

    }
    public void move(){

    }
    public void checkCurrentLocation(){

    }




}
