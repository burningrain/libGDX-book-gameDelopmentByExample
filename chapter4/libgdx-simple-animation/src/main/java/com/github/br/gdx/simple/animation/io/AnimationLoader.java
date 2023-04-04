package com.github.br.gdx.simple.animation.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.github.br.gdx.simple.animation.AnimationExtensions;
import com.github.br.gdx.simple.animation.SimpleAnimation;

public class AnimationLoader {

    private final FolderAnimationConverter converter = new FolderAnimationConverter();

    /**
     * Loading animation from the next structure:
     * animation
     * |_ folder1
     * |_____ anim_example.atlas
     * |_____ anim_example.png
     * |_____ anim_example.afsm
     * |_ folder2
     * |_  ...
     * |_ folderN
     *
     * or just from one-level folder
     *
     * @param animationsDir the parent folder that consist of child folders with animation files
     */
    public Array<SimpleAnimation> load(FileHandle animationsDir) {
        Array<SimpleAnimation> result = new Array<SimpleAnimation>();

        Array<FolderAnimDto> folderAnimDtos = new Array<FolderAnimDto>();
        FileHandle[] list = animationsDir.list();
        if(list == null || list.length == 0) {
            return new Array<SimpleAnimation>(0);
        }
        for (FileHandle entry : list) {
            folderAnimDtos.add(createFolderAnimDto(entry));
        }
        for (FolderAnimDto folderAnimDto : folderAnimDtos) {
            SimpleAnimation simpleAnimation = converter.from(folderAnimDto);
            result.add(simpleAnimation);
        }

        return result;
    }

    protected FileHandle getFileHandler(String path) {
        return Gdx.files.internal(path);
    }

    private FolderAnimDto createFolderAnimDto(FileHandle animationDir) {
        FolderAnimDto folderAnimDto = new FolderAnimDto();
        for (FileHandle fileHandle : animationDir.list()) {
            String ext = Utils.getFileExtension(fileHandle.name());

            if (AnimationExtensions.ATLAS.equals(ext)) {
                folderAnimDto.setTextureAtlas(new TextureAtlas(getFileHandler(fileHandle.path()))); // todo так нельзя делать, убрать класс после написания нормальных лоадеров
                continue;
            }
            if (AnimationExtensions.PNG.equals(ext)) {
                // automatically loaded by using the TextureAtlas
                continue;
            }
            if (AnimationExtensions.ASFM.equals(ext)) {
                try {
                    folderAnimDto.setFsm(new String(getFileHandler(fileHandle.path()).readBytes(), "UTF-8"));
                    continue;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            // to ignore a file with an unknown type
            //throw new IllegalArgumentException("An unknown file type [" + fileHandle.name() + "]");
        }
        return folderAnimDto;
    }

}
