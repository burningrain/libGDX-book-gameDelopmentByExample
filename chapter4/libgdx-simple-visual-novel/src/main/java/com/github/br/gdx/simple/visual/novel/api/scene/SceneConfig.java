package com.github.br.gdx.simple.visual.novel.api.scene;

import com.github.br.gdx.simple.visual.novel.utils.Utils;
import com.github.br.gdx.simple.visual.novel.api.generator.DefaultGeneratorEdgeId;
import com.github.br.gdx.simple.visual.novel.api.generator.DefaultGeneratorNodeId;
import com.github.br.gdx.simple.visual.novel.api.generator.GeneratorEdgeId;
import com.github.br.gdx.simple.visual.novel.api.generator.GeneratorNodeId;
import com.github.br.gdx.simple.visual.novel.api.node.NodeType;

public class SceneConfig {

    private final GeneratorNodeId generatorNodeId;
    private final GeneratorEdgeId generatorEdgeId;
    private final NodeType defaultNodeType;

    private SceneConfig(SceneConfig.Builder builder) {
        this.generatorNodeId = Utils.checkNotNull(builder.generatorNodeId, "generatorNodeId");
        this.generatorEdgeId = Utils.checkNotNull(builder.generatorEdgeId, "generatorEdgeId");
        this.defaultNodeType = Utils.checkNotNull(builder.defaultNodeType, "defaultNodeType");
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

    public NodeType getDefaultNodeType() {
        return defaultNodeType;
    }

    public static class Builder {

        private GeneratorNodeId generatorNodeId;
        private GeneratorEdgeId generatorEdgeId;
        private NodeType defaultNodeType = NodeType.NOT_WAITING;

        public Builder setGeneratorNodeId(GeneratorNodeId generatorNodeId) {
            this.generatorNodeId = Utils.checkNotNull(generatorNodeId, "generatorNodeId");
            return this;
        }

        public Builder setGeneratorEdgeId(GeneratorEdgeId generatorEdgeId) {
            this.generatorEdgeId = Utils.checkNotNull(generatorEdgeId, "generatorEdgeId");
            return this;
        }

        public Builder setDefaultNodeType(NodeType defaultNodeType) {
            this.defaultNodeType = Utils.checkNotNull(defaultNodeType, "defaultNodeType");
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
