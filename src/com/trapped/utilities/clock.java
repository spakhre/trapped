package com.trapped.utilities;

import java.util.TimerTask;
import java.util.Timer;
public class clock extends TimerTask {
    public static int timeLeft = 5000;


    public void run() {
        System.out.println("Time: "+ --timeLeft);
    }

    public static void timer() {
        Timer time = new Timer();
        clock gameClock = new clock();
        time.schedule(gameClock,5,5000);
    }
}
