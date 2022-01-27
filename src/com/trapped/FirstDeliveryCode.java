//package com.trapped;
//
//import com.trapped.client.TrappedGame;
//import com.trapped.player.Player;
//
//import java.io.IOException;
//import java.util.Scanner;
//
//public class FirstDeliveryCode {
//     public static void main(String[] args) throws IOException, InterruptedException {
//      TrappedGame.startGame();
//      Player.move();
//
//      Scanner scan = new Scanner(System.in);
//      System.out.println("What do you want to do?");
//      System.out.println("you can [inspect] this " + Player.checkCurrentLocation()+", [move] to another, [check inventory]");
//      String ans = scan.next();
//      if(ans.equals("inspect")){
//       System.out.println("You found a small piece of paper with number [2,3,8].");
//      }
//      else if(ans.equals("move")){
//       Player.move();
//       if (Player.location.equals("door")){
//        System.out.println("The door has a a lock which require 3-digit password.");
//        Scanner input = new Scanner(System.in);
//        System.out.println("Would you like to have a try? [Y]/[N] or [check inventory]");
//         String solve = input.next();
//         if (solve.equals('Y')){
//          System.out.println("Please enter 3-digit password");
//          Scanner enter = new Scanner(System.in);
//          int pass = enter.nextInt();
//          if(pass==238){
//           System.out.println("CORRECT! You can escape from the room! Congratulations!");
//          }
//          else{
//           System.out.println("Incorrect! please try again");
//          }
//
//         }
//         else if (solve.equals("check inventory")){
//          System.out.println("You've got: [a small piece of paper with number [2,3,8]] in your inventory");
//         }
//       }
//      }
//      else if(ans.equals("check inventory")){
//       System.out.println("You've got: [a small piece of paper with number [2,3,8]] in your inventory");
//      }
//
//
//
//
//
// }
//}
