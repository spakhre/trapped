package com.trapped.player;

import org.junit.After;
import org.junit.Assert;
import com.trapped.GameEngine;
import com.trapped.utilities.*;
import org.junit.Before;
import org.junit.Test;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static org.junit.Assert.*;

public class PlayerTest {
    Player player;

    @Before
    public void setup() {
        player = new Player();
        player.location = "bed";
        List<String> inventory = new ArrayList<>();
        Map<String, Object> furniture = player.map.get(player.location);
    }

    @After
    public void after(){
        FileManager.writeDefaults();
    }

    @Test
    public void playerShouldMoveLeftWhenUserClicksLeft() {
        String noun = "left";
        String verb = "go";
        String newLoc = "door";
        player.playerInputLogic(verb, noun);
        assertEquals(player.location, newLoc);
    }

    @Test
    public void playerShouldMoveRightWhenUserClicksRight() {
        String noun = "right";
        String verb = "go";
        String newLoc = "safe";
        player.playerInputLogic(verb, noun);
        assertEquals(player.location, newLoc);
    }

    @Test
    public void playerShouldDropItemWhenUserDropsItem() {
        Map<String, Object> furniture = player.map.get(player.location);
        ArrayList<String> furniture_items = (ArrayList<String>) furniture.get("furniture_items");
        player.inventory.add("laptop");
        player.drop("laptop");
        assertTrue(furniture_items.contains("laptop"));
    }

    @Test
    public void playerShouldPickUpItemWhenUserPicksUpItem() {
        player.location= "bed";
        assertEquals(true,player.pickUpItem("matches"));
    }

    @Test
    public void playerShouldNotPickUpItemWhenItemNotThere() {
        player.location= "bed";
        assertEquals(false,player.pickUpItem("key"));
    }

    @Test
    public void playerShouldInspectItemWhenInputValid() {
        assertTrue(player.inspect("bed"));
    }

    @Test
    public void playerShouldNotInspectItemWhenInputNull(){
        assertFalse(player.inspect(null));
    }

    @Test
    public void playerShouldGoToFurnitureWhenInputValid() {

    }
}