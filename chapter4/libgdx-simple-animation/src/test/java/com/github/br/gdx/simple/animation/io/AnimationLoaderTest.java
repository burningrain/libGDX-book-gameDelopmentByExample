package com.github.br.gdx.simple.animation.io;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.github.br.gdx.simple.animation.SimpleAnimation;
import com.github.br.gdx.simple.animation.SimpleAnimationSyncLoader;
import org.junit.Ignore;
import org.junit.Test;

public class AnimationLoaderTest {

    private final FileHandleResolver resolver = new InternalFileHandleResolver();
    private final AssetManager assetManager = new AssetManager(resolver);

    public static final String CRAB_ANIM = "animation/crab/crab.afsm";

    @Ignore("разобраться с подгрузкой с-классов, запускать перед тестом либгдх или делать биндинг")
    @Test
    public void testLoadAnimations() {
        assetManager.setLoader(SimpleAnimation.class, new SimpleAnimationSyncLoader(resolver));
        assetManager.load(CRAB_ANIM, SimpleAnimation.class);
        assetManager.finishLoading();
        SimpleAnimation simpleAnimation = assetManager.get(CRAB_ANIM, SimpleAnimation.class);
        System.out.println(simpleAnimation);

        simpleAnimation.dispose();
    }
}
