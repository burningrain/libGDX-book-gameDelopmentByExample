package com.github.br.paper.airplane.audio;

import com.badlogic.gdx.audio.Music;
import com.github.br.paper.airplane.AudioSettings;

public class MusicProxy implements Music, AudioSettings.AudioChangeListener {

    private final Music music;
    private final AudioSettings audioSettings;

    public MusicProxy(AudioSettings audioSettings, Music music) {
        this.music = music;
        this.audioSettings = audioSettings;
        this.audioSettings.addListener(this);
    }

    @Override
    public void change(AudioSettings audioSettings) {
        music.setVolume(audioSettings.getMusicVolume());
    }

    @Override
    public void play() {
        music.play();
        setVolume(audioSettings.getMusicVolume());
    }

    @Override
    public void pause() {
        music.pause();
    }

    @Override
    public void stop() {
        music.stop();
    }

    @Override
    public boolean isPlaying() {
        return music.isPlaying();
    }

    @Override
    public void setLooping(boolean isLooping) {
        music.setLooping(isLooping);
    }

    @Override
    public boolean isLooping() {
        return music.isLooping();
    }

    @Override
    public void setVolume(float volume) {
        music.setVolume(volume);
    }

    @Override
    public float getVolume() {
        return music.getVolume();
    }

    @Override
    public void setPan(float pan, float volume) {
        music.setPan(pan, volume);
    }

    @Override
    public void setPosition(float position) {
        music.setPosition(position);
    }

    @Override
    public float getPosition() {
        return music.getPosition();
    }

    @Override
    public void dispose() {
        audioSettings.removeListener(this);
        music.dispose();
    }

    @Override
    public void setOnCompletionListener(OnCompletionListener listener) {
        music.setOnCompletionListener(listener);
    }

}
