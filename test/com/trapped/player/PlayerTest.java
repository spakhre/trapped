package com.trapped.player;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.Assert.*;

public class PlayerTest {
    Player player;

    @Before
    public void setup() {
        player = new Player();
    }

    @Test
    public void playerShouldMoveLeftWhenUserClicksLeft() {
        String noun = "right";
        String verb = "go";
        String newLoc = "safe";
        player.playerInputLogic(verb, noun);
        assertEquals(player.location, newLoc);
    }

    @Test
    public void playerShouldMoveRightWhenUserClicksRight() {

    }

    @Test
    public void playerShouldDropItemWhenUserDropsItem() {

    }

    @Test
    public void playerShouldPickUpItemWhenUserPicksUpItem() {

    }

    @Test
    public void playerShouldNotPickUpItemWhenItemNotThere() {

    }

    @Test
    public void playerShouldInspectItemWhenUserInspectItem() {

    }

    @Test
    public void playerShouldGoToFurnitureWhenInputValid() {

    }
}