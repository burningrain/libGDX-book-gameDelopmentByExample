package com.github.br.gdx.simple.visual.novel.api.scene;

import com.github.br.gdx.simple.visual.novel.Utils;
import com.github.br.gdx.simple.visual.novel.api.generator.DefaultGeneratorEdgeId;
import com.github.br.gdx.simple.visual.novel.api.generator.DefaultGeneratorNodeId;
import com.github.br.gdx.simple.visual.novel.api.generator.GeneratorEdgeId;
import com.github.br.gdx.simple.visual.novel.api.generator.GeneratorNodeId;
import com.github.br.gdx.simple.visual.novel.api.screen.ScreenManager;

public class SceneConfig<SM> {

    private final SM screenManager;
    private final GeneratorNodeId generatorNodeId;
    private final GeneratorEdgeId generatorEdgeId;

    private SceneConfig(SceneConfig.Builder<SM> builder) {
        this.screenManager = Utils.checkNotNull(builder.screenManager, "screenManager");
        this.generatorNodeId = Utils.checkNotNull(builder.generatorNodeId, "generatorNodeId");
        this.generatorEdgeId = Utils.checkNotNull(builder.generatorEdgeId, "generatorEdgeId");
    }

    public static <SM extends ScreenManager> SceneConfig.Builder<SM> builder() {
        return new SceneConfig.Builder<SM>();
    }

    public SM getScreenManager() {
        return screenManager;
    }

    public GeneratorNodeId getGeneratorNodeId() {
        return generatorNodeId;
    }

    public GeneratorEdgeId getGeneratorEdgeId() {
        return generatorEdgeId;
    }

    public static class Builder<SM> {

        private SM screenManager;
        private GeneratorNodeId generatorNodeId;
        private GeneratorEdgeId generatorEdgeId;

        public Builder<SM> setScreenManager(SM screenManager) {
            this.screenManager = screenManager;
            return this;
        }

        public Builder<SM> setGeneratorNodeId(GeneratorNodeId generatorNodeId) {
            this.generatorNodeId = generatorNodeId;
            return this;
        }

        public Builder<SM> setGeneratorEdgeId(GeneratorEdgeId generatorEdgeId) {
            this.generatorEdgeId = generatorEdgeId;
            return this;
        }

        public SceneConfig<SM> build() {
            if(generatorNodeId == null) {
                generatorNodeId = new DefaultGeneratorNodeId();
            }
            if(generatorEdgeId == null) {
                generatorEdgeId = new DefaultGeneratorEdgeId();
            }
            return new SceneConfig<>(this);
        }

    }


}
