package com.github.br.gdx.simple.animation;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.github.br.gdx.simple.animation.io.FolderAnimDto;
import com.github.br.gdx.simple.animation.io.FolderAnimationConverter;

import java.io.UnsupportedEncodingException;

public class SimpleAnimationSyncLoader extends SynchronousAssetLoader<SimpleAnimation, SimpleAnimationSyncLoader.SimpleAnimationParameter> {

    private final FolderAnimationConverter animationConverter = new FolderAnimationConverter();

    public SimpleAnimationSyncLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public SimpleAnimation load(AssetManager assetManager, String fileName, FileHandle file, SimpleAnimationParameter parameter) {
        // we are waiting for 'fileName.afsm' in input data
        String name = file.parent().child(getAtlasName(fileName)).path();
        TextureAtlas textureAtlas = assetManager.get(name, TextureAtlas.class);

        FolderAnimDto folderAnimDto = new FolderAnimDto();
        folderAnimDto.setTextureAtlas(textureAtlas);
        try {
            folderAnimDto.setFsm(new String(file.readBytes(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return animationConverter.from(folderAnimDto);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, SimpleAnimationParameter parameter) {
        FileHandle dir = file.parent();

        TextureAtlasLoader.TextureAtlasParameter textureAtlasParameter = null;
        if (parameter != null) {
            textureAtlasParameter = new TextureAtlasLoader.TextureAtlasParameter(parameter.flip);
        }
        Array<AssetDescriptor> dependencies = new Array(1);
        dependencies.add(
                new AssetDescriptor(
                        dir.child(getAtlasName(fileName)),
                        TextureAtlas.class,
                        textureAtlasParameter)
        );

        return dependencies;
    }

    private String getAtlasName(String fileName) {
        String[] paths = fileName.split("/");
        String[] split = paths[paths.length - 1].split("\\.");
        return split[0] + ".atlas";
    }

    public static class SimpleAnimationParameter extends AssetLoaderParameters<SimpleAnimation> {
        public boolean flip;

        public SimpleAnimationParameter(boolean flip) {
            this.flip = flip;
        }
    }

}
