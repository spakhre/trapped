package com.trapped.utilities;

import com.trapped.GameEngine;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sounds {

    public static void playSounds(String location_soundFile, int milliseconds) {
        File file = new File("resources/sounds/"+location_soundFile);

        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            Thread.sleep(milliseconds);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void changeSoundVolume(String location_soundFile, int milliseconds, float volume){
        File file = new File("resources/sounds/"+location_soundFile);

        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            FloatControl gainControl =
                    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(volume);
            clip.start();
            Thread.sleep(milliseconds);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkIfFileExists(String location_soundFile){
        File tempFile = new File("resources/sounds/"+location_soundFile);
        return tempFile.exists();
    }
}
