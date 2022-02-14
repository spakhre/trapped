package com.trapped.utilities;

import com.trapped.view.MainFrame;

import javax.swing.*;

public class Volume {
    static float volume;

    public void changeVolume(){
        JFrame frame = new JFrame("JoptionPane Test");
        frame.setSize(200, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        JOptionPane.showMessageDialog(frame, "Hello, this is the volume selector");
        int result = JOptionPane.showConfirmDialog(null, "Do wish to change the volume? "
               );
        switch (result) {
            case JOptionPane.YES_OPTION:
                String name = JOptionPane.showInputDialog(null,
                        "Please enter a number from -80 to 6 to change volume");
                float num = Float.parseFloat(name);

                Sounds.changeSoundVolume("startsound.wav", 0, num);
                break;
            case JOptionPane.NO_OPTION:
            case JOptionPane.CANCEL_OPTION:
                break;
        }
    }
}