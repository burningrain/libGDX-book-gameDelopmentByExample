package com.github.br.gdx.simple.animation.io;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.github.br.gdx.simple.animation.SimpleAnimation;
import org.junit.Ignore;
import org.junit.Test;

public class AnimationLoaderTest {

    private final AnimationLoader animationLoader = new AnimationLoader() {
        protected FileHandle getFileHandler(String path) {
            return new FileHandle(path);
        }
    };

    @Ignore("разобраться с подгрузкой с-классов, запускать перед тестом либгдх или делать биндинг")
    @Test
    public void testLoadAnimations() {
        FileHandle fileHandle = new FileHandle("D:\\projects\\libGDX-book-gameDelopmentByExample\\chapter4\\libgdx-simple-animation\\src\\test\\resources\\animation");
        Array<SimpleAnimation> animations = animationLoader.load(fileHandle);
        System.out.println(animations);
    }
}
