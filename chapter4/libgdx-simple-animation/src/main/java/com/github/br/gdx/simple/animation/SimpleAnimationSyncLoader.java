package com.github.br.gdx.simple.animation;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

public class SimpleAnimationSyncLoader extends SynchronousAssetLoader<SimpleAnimation, SimpleAnimationSyncLoader.SimpleAnimationParameter> {

    public SimpleAnimationSyncLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public SimpleAnimation load(AssetManager assetManager, String fileName, FileHandle file, SimpleAnimationParameter parameter) {
        //textureAtlasLoader.load()
        return null;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, SimpleAnimationParameter parameter) {
        FileHandle dir = file.parent();
        //textureAtlasLoader.getDependencies();

        return null;
    }

    public static class SimpleAnimationParameter extends AssetLoaderParameters<SimpleAnimation> {

    }

    private static class Data {

    }

}
