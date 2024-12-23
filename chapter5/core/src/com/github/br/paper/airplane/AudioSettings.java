package com.github.br.paper.airplane;

import com.badlogic.gdx.utils.Array;

public class AudioSettings {

    private float soundVolume;
    private boolean turnOffSound = false;

    private float musicVolume;
    private boolean turnOffMusic = false;

    private final Array<AudioChangeListener> listeners = new Array<>(false, 16);

    public interface AudioChangeListener {
        void change(AudioSettings audioSettings);
    }

    public boolean isTurnOffSound() {
        return turnOffSound;
    }

    public void setTurnOffSound(boolean turnOffSound) {
        this.turnOffSound = turnOffSound;
    }

    public boolean isTurnOffMusic() {
        return turnOffMusic;
    }

    public void setTurnOffMusic(boolean turnOffMusic) {
        this.turnOffMusic = turnOffMusic;
    }

    public float getSoundVolume() {
        return soundVolume;
    }

    public void setSoundVolume(float soundVolume) {
        this.soundVolume = soundVolume;
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(float musicVolume) {
        this.musicVolume = musicVolume;
    }

    // observer

    public void addListener(AudioChangeListener changeListener) {
        listeners.add(changeListener);
    }

    public void removeListener(AudioChangeListener changeListener) {
        listeners.removeValue(changeListener, true);
    }

    public void notifyListeners() {
        for (AudioChangeListener listener : listeners) {
            listener.change(this);
        }
    }

}
