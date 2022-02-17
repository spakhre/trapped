package com.trapped.gui;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

class CountdownPanel extends JPanel {
    private JLabel timer;
    public CountdownPanel() {
        this.setBounds(JLabel.CENTER, JLabel.CENTER, 200,200);
        this.setBackground(new Color(0,0,0));
        this.setVisible(true);
        createTimerLabel();

        startTimer(30);
    }

    private void createTimerLabel() {
        timer = new JLabel();
        timer.setForeground(Color.white);
        timer.setBackground(Color.black);
        timer.setBounds(this.getBounds());
        this.add(timer);
    }

    private void startTimer(int time) {

        Timer count = new Timer();
        count.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int countdown = time;
                timer.setText("Time left: " + countdown);
                countdown--;

                if(countdown < 0) {
                    count.cancel();
                    timer.setText("TIME OVER");
                }
            }
        }, 0, 1000);
    }

    public JLabel getTimer() {
        return timer;
    }
}