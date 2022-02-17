package com.trapped.gui;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class VolumeSliderPanel extends GuiPanel {

    /**
     * Creates a new simple slider with horizontal orientation
     *
     * @param mainWindow
     * @param defaultVolumeLevel
     */
    public VolumeSliderPanel(MainWindow mainWindow, int defaultVolumeLevel) {
        super(mainWindow);

        JSlider volumeSlider = new JSlider(0, 10, 1);
        volumeSlider.setValue(mainWindow.getAudio().getVolumeLevel());

        volumeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = volumeSlider.getValue();
                mainWindow.getAudio().setVolumeLevel(value);
            }
        });
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(new JLabel("Audio Volume"));
        this.add(volumeSlider);
    }
}
