package com.trapped.utilities;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Audio {

    public static final int DEFAULT_VOLUME_LEVEL = 5;


    private Clip clip;
    private FloatControl gainControl;
    private Scanner scanner = new Scanner(System.in);
    private boolean musicOn;

    private double volumePreference;

    //volumeLevel is in 'int' equivalent of (int) (volumePreference / .1d)
    private int volumeLevel;


    public Audio() {
        this(DEFAULT_VOLUME_LEVEL * .1d);
    }

    public Audio(double volumePreference) {
        setVolumeLevel(volumePreference);
    }

    public void play(String directory, String fileName, int count) {
        play(directory + File.separator + fileName, count);
    }

    public void play(String filePath, int count) {
        try {
            // Stop previous audio clip, if any.
            if (clip != null) {
                clip.stop();
            }
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            //set the volume with master gain control
            gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            //start playing clip
            clip.start();
            clip.loop(count);
            //check if volume is not zero
            if (volumePreference != 0) {
                setVolumeLevel(volumePreference);
            } else {
                setVolumeLevel(DEFAULT_VOLUME_LEVEL);
            }
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }


    /**
     * The volume level must be in the range 1 to 10.
     *
     * @param vol
     */
    public void setVolumeLevel(int vol) {
        setVolumeLevel(vol * .1d);
    }

    /**
     * The volume level must be in the range .1d to 1d.
     *
     * @param vol
     */
    public void setVolumeLevel(double vol) {
        this.volumePreference = vol;
        this.volumeLevel = (int) (volumePreference / .1d);

        if (clip == null || !clip.isActive() || !clip.isOpen()) {
            return;
        }
        double gain = vol;
        float db = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
        gainControl.setValue(db);
    }

    public int getVolumeLevel() {
        return volumeLevel;
    }
}
