package com.packt.flappeebee;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.*;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.br.gdx.simple.animation.SimpleAnimation;
import com.github.br.gdx.simple.animation.SimpleAnimationSyncLoader;
import com.github.br.gdx.simple.structure.AbstractSimpleGame;
import com.github.br.gdx.simple.structure.GameSettings;
import com.github.br.gdx.simple.structure.screen.statemachine.GameScreenState;
import com.packt.flappeebee.screen.GameScreens;
import com.ray3k.stripe.FreeTypeSkinLoader;
import games.rednblack.editor.renderer.resources.AsyncResourceManager;
import games.rednblack.editor.renderer.resources.ResourceManagerLoader;

public class HappyCrabSimpleGame extends AbstractSimpleGame<UserFactoryImpl> {

    @Override
    public void create() {
        //Box2D.init();
        super.create();
    }

    @Override
    protected UserFactoryImpl createUserFactory() {
        return new UserFactoryImpl();
    }

    @Override
    protected GameScreenState createStartState() {
        return GameScreens.LEVEL_0;
    }

    @Override
    protected void fillGameSettings(GameSettings.Builder builder) {
    }

    @Override
    protected void initLoaders(AssetManager assetManager, InternalFileHandleResolver fileHandleResolver) {
        // графика
        assetManager.setLoader(Texture.class, new TextureLoader(fileHandleResolver));
        assetManager.setLoader(TextureAtlas.class, new TextureAtlasLoader(fileHandleResolver));
        assetManager.setLoader(Skin.class, new FreeTypeSkinLoader(fileHandleResolver));

        // эффекты частиц
        assetManager.setLoader(ParticleEffect.class, ".p", new ParticleEffectLoader(fileHandleResolver));

        // звук
        assetManager.setLoader(Sound.class, new SoundLoader(fileHandleResolver));
        assetManager.setLoader(Music.class, new MusicLoader(fileHandleResolver));

        // карты редакторов уровней
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(fileHandleResolver));

        // шрифты
        assetManager.setLoader(BitmapFont.class, new FreetypeFontLoader(fileHandleResolver));
        // анимация
        assetManager.setLoader(SimpleAnimation.class, new SimpleAnimationSyncLoader(fileHandleResolver));

        // hyperlap2D
        assetManager.setLoader(AsyncResourceManager.class, new ResourceManagerLoader(fileHandleResolver));
    }

}
