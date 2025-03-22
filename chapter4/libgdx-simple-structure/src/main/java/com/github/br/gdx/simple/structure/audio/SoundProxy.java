package com.github.br.gdx.simple.structure.audio;

import com.badlogic.gdx.audio.Sound;
import com.github.br.gdx.simple.structure.AudioSettings;

public class SoundProxy implements Sound, AudioSettings.AudioChangeListener {

    private final Sound sound;
    private final AudioSettings audioSettings;
    private long id = -1L;

    public SoundProxy(AudioSettings audioSettings, Sound sound) {
        this.sound = sound;
        this.audioSettings = audioSettings;
        this.audioSettings.addListener(this);
    }

    @Override
    public void change(AudioSettings audioSettings) {
        if (id == -1L) {
            return;
        }
        sound.setVolume(id, audioSettings.getSoundVolume());
    }

    @Override
    public long play() {
        id = sound.play();
        setVolume(id, audioSettings.getSoundVolume()); //TODO подумать, нужно ли? Вообще, над оберткой надо подумать получше
        return id;
    }

    @Override
    public long play(float volume) {
        id = sound.play(volume);
        return id;
    }

    @Override
    public long play(float volume, float pitch, float pan) {
        id = sound.play(volume, pitch, pitch);
        return id;
    }

    @Override
    public long loop() {
        id = sound.loop();
        return id;
    }

    @Override
    public long loop(float volume) {
        id = sound.loop(volume);
        return id;
    }

    @Override
    public long loop(float volume, float pitch, float pan) {
        id = sound.loop(volume, pitch, pan);
        return id;
    }

    @Override
    public void stop() {
        sound.stop();
    }

    @Override
    public void pause() {
        sound.pause();
    }

    @Override
    public void resume() {
        sound.resume();
    }

    @Override
    public void dispose() {
        this.audioSettings.removeListener(this);
        sound.dispose();
    }

    @Override
    public void stop(long soundId) {
        sound.stop(soundId);
    }

    @Override
    public void pause(long soundId) {
        sound.pause(soundId);
    }

    @Override
    public void resume(long soundId) {
        sound.resume(soundId);
    }

    @Override
    public void setLooping(long soundId, boolean looping) {
        sound.setLooping(soundId, looping);
    }

    @Override
    public void setPitch(long soundId, float pitch) {
        sound.setPitch(soundId, pitch);
    }

    @Override
    public void setVolume(long soundId, float volume) {
        sound.setVolume(soundId, volume);
    }

    @Override
    public void setPan(long soundId, float pan, float volume) {
        sound.setPan(soundId, pan, volume);
    }

    // --------
    public void stopCurrent() {
        sound.stop(id);
    }

    public void pauseCurrent() {
        sound.pause(id);
    }

    public void resumeCurrent() {
        sound.resume(id);
    }

    public void setLoopingCurrent(boolean looping) {
        sound.setLooping(id, looping);
    }

    public void setPitchCurrent(float pitch) {
        sound.setPitch(id, pitch);
    }

    public void setVolumeCurrent(float volume) {
        sound.setVolume(id, volume);
    }

    public void setPanCurrent(float pan, float volume) {
        sound.setPan(id, pan, volume);
    }

}
