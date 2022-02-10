package com.gui.utility;

import javax.swing.JTextArea;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CharacterDisplay {

    private int charIndex = 0;
    private JTextArea jTextArea;
    private String text;
    private Timer timer;
    // function to display string one character at a time, as a type writer effect
    public CharacterDisplay(JTextArea jTextArea, String text) {
        this.jTextArea = jTextArea;
        this.text = text;
         timer = new Timer(8,new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String jTextAreaString = jTextArea.getText().replace("NL","\n");
                jTextAreaString += text.charAt(charIndex);
                jTextArea.setText(jTextAreaString);
                charIndex++;
                if(charIndex >= text.length()){
                    ((Timer)e.getSource()).stop();
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

