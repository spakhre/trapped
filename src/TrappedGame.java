import java.util.ArrayList;
import java.util.Arrays;
import java.util.*;

public class TrappedGame {
    ArrayList<Furnitures> map;
    Player player;

    List<String> commands = new ArrayList<>(Arrays.asList("look","inspect","pick up","use","left","right"));
    List<String> items_needed = new ArrayList<>(Arrays.asList("crowbar","yellow key","blank paper"));

    //constructor
    public TrappedGame(){
        this.map = new ArrayList<Furnitures>();
        map.add(new Furnitures("bed","a oak color bed that you wake up from",List.of("laptop","match"),List.of(""),"door","window","",""));
        map.add(new Furnitures("door","a door which is locked",List.of("laptop","match"),List.of(""),"lock","window","bed",""));
        map.add(new Furnitures("window","a window which is locked",List.of("laptop","match"),List.of(""),"bed","door","",""));

    }

    public static void main(String[] args) {

            Scanner input = new Scanner(System.in);

            System.out.println("Welcome to Trapped game, in this game you have certain quests and you can pick options each time. " +
                    "There are different furnitures where you can inspect and pickup items and use items to solve puzzles.  "
                    + "If you solve all the puzzles, you will get all clues to unlock the door.");
            System.out.println("--------------------------------");

            System.out.println("What is your name: ");
            String name = input.next();
            System.out.println("--------------------------------");


    }


}
