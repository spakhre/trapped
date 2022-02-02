package com.trapped.utilities;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sounds {
    public static void playSounds(String location_soundFile) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
            File file = new File("resources/sounds/"+location_soundFile);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

    }
    public static boolean checkIfFileExists(String location_soundFile){
        File tempFile = new File("resources/sounds/"+location_soundFile);
        boolean exists = tempFile.exists();
        return exists;
    }
}
