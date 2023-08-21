package com.github.br.gdx.simple.visual.novel.api.scene;

import com.github.br.gdx.simple.visual.novel.Utils;
import com.github.br.gdx.simple.visual.novel.api.generator.DefaultGeneratorEdgeId;
import com.github.br.gdx.simple.visual.novel.api.generator.DefaultGeneratorNodeId;
import com.github.br.gdx.simple.visual.novel.api.generator.GeneratorEdgeId;
import com.github.br.gdx.simple.visual.novel.api.generator.GeneratorNodeId;

public class SceneConfig {

    private final GeneratorNodeId generatorNodeId;
    private final GeneratorEdgeId generatorEdgeId;

    private SceneConfig(SceneConfig.Builder builder) {
        this.generatorNodeId = Utils.checkNotNull(builder.generatorNodeId, "generatorNodeId");
        this.generatorEdgeId = Utils.checkNotNull(builder.generatorEdgeId, "generatorEdgeId");
    }

    public static SceneConfig.Builder builder() {
        return new SceneConfig.Builder();
    }

    public GeneratorNodeId getGeneratorNodeId() {
        return generatorNodeId;
    }

    public GeneratorEdgeId getGeneratorEdgeId() {
        return generatorEdgeId;
    }

    public static class Builder {

        private GeneratorNodeId generatorNodeId;
        private GeneratorEdgeId generatorEdgeId;

        public Builder setGeneratorNodeId(GeneratorNodeId generatorNodeId) {
            this.generatorNodeId = generatorNodeId;
            return this;
        }

        public Builder setGeneratorEdgeId(GeneratorEdgeId generatorEdgeId) {
            this.generatorEdgeId = generatorEdgeId;
            return this;
        }

        public SceneConfig build() {
            if(generatorNodeId == null) {
                generatorNodeId = new DefaultGeneratorNodeId();
            }
            if(generatorEdgeId == null) {
                generatorEdgeId = new DefaultGeneratorEdgeId();
            }
            return new SceneConfig(this);
        }

    }


}
