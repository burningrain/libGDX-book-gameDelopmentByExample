package com.github.br.gdx.simple.structure.audio;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.github.br.gdx.simple.structure.AudioSettings;

public class AudioAssetManager {

    private final AssetManager assetManager;
    private final AudioSettings audioSettings;

    public AudioAssetManager(AssetManager assetManager, AudioSettings audioSettings) {
        this.assetManager = assetManager;
        this.audioSettings = audioSettings;
    }

    public Music getMusic(String path) {
        return new MusicProxy(audioSettings, assetManager.get(path, Music.class));
    }

    public SoundProxy getSound(String path) {
        return new SoundProxy(audioSettings, assetManager.get(path, Sound.class));
    }

}
