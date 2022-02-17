package com.gui.utility;

import javax.swing.JTextArea;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CharacterDisplay {
    public static final int CHARACTER_DELAY_MILLISECONDS = 6;
    private int charIndex = 0;
    private JTextArea jTextArea;
    private Timer timer;
    public CharacterDisplay(JTextArea jTextArea, String text, Timer subsequentTimer) {
        this.jTextArea = jTextArea;
        timer = new Timer(CHARACTER_DELAY_MILLISECONDS, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String jTextAreaString = jTextArea.getText().replace("NL","\n");
                jTextAreaString += text.charAt(charIndex);
                jTextArea.setText(jTextAreaString);
                charIndex++;
                if(charIndex >= text.length()){
                    ((Timer)e.getSource()).stop();
                    if (subsequentTimer != null) {
                        subsequentTimer.start();
                    }
                }
            }

        });

    }

    public void startDisplay(){
        jTextArea.setText(null);
        charIndex = 0;
        timer.start();
    }
}

