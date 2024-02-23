package com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.data;

import com.github.br.gdx.simple.visual.novel.api.node.CompositeNode;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.inner.SceneLinkNode;
import com.github.br.gdx.simple.visual.novel.utils.Utils;

import java.util.Map;

public class DefaultNodeElementVizDataFactory implements NodeElementVizDataFactory {

    private NodeElementVizDataParamExtractor vizDataParamExtractor = new DefaultNodeElementVizDataParamExtractor();
    private NodeElementVizDataColorSchema vizDataColorSchema = new DefaultNodeElementVizDataColorSchema();

    public DefaultNodeElementVizDataFactory setVizDataParamExtractor(NodeElementVizDataParamExtractor vizDataParamExtractor) {
        this.vizDataParamExtractor = Utils.checkNotNull(vizDataParamExtractor, "vizDataParamExtractor");
        return this;
    }

    public DefaultNodeElementVizDataFactory setVizDataColorSchema(NodeElementVizDataColorSchema vizDataColorSchema) {
        this.vizDataColorSchema = Utils.checkNotNull(vizDataColorSchema, "vizDataColorSchema");
        return this;
    }

    @Override
    public String getNodeShapeForShortInfo(Node<?, ?> node) {
        if (node instanceof CompositeNode) {
            return NodeElementType.COMPOSITE_NODE.getDotShape();
        } else if (node instanceof SceneLinkNode) {
            return NodeElementType.SCENE_LINK.getDotShape();
        } else {
            return NodeElementType.SIMPLE_NODE.getDotShape();
        }
    }

    @Override
    public String createLabelFullNodeInfo(String nodeId, Node<?, ?> node) {
        StringBuilder builder = new StringBuilder();
        builder
                .append("<<TABLE BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\">")
                    .append(createLabelFullNodeInfo(nodeId, node, 0))
                .append("</TABLE>>");

        return builder.toString();
    }

    private String createLabelFullNodeInfo(String nodeId, Node<?, ?> node, int level) {
        StringBuilder result = new StringBuilder();
        result.append(createFullInfoHeader(nodeId, node, level));
        result.append(createFullInfoBody(nodeId, node, vizDataParamExtractor.extractNodeParams(node)));

        if (node instanceof CompositeNode) {
            level++;
            CompositeNode<?, ?> compositeNode = ((CompositeNode<?, ?>) node);
            for (Node<?, ?> compositeInnerNode : compositeNode.getNodes()) {
                result.append(createLabelFullNodeInfo("", compositeInnerNode, level));
            }
        }

        return result.toString();
    }

    protected String createFullInfoHeader(String nodeId, Node<?, ?> node, int level) {
        String spaces = Utils.repeatString(" ", level * 2);
        String fullNodeId = nodeId + " " + createAdditionalHeaderInfoNodeId(node);

        String align = "";
        if (level > 0) {
            fullNodeId = spaces + level + fullNodeId;
            align = "ALIGN=\"LEFT\"";
            spaces += "    "; // для отступа в названиях классов и параметрах
        }

        StringBuilder builder = new StringBuilder();
        builder
                .append("<TR>")
                    .append("<TD BGCOLOR=\"lightgray\" ").append(align).append(">")
                        .append(fullNodeId)
                    .append("</TD>")
                .append("</TR>")

                .append("<TR>")
                    .append("<TD BGCOLOR=\"lightgray\" ").append(align).append(">")
                        .append(spaces).append(node.getClass().getName())
                    .append("</TD>")
                .append("</TR>");

        return builder.toString();
    }

    protected String createFullInfoBody(String nodeId, Node<?, ?> node, Map<String, String> customParams) {
        if (customParams == null || customParams.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : customParams.entrySet()) {
            builder
                    .append("<TR>")
                        .append("<TD>")
                            .append(entry.getKey())
                        .append("</TD>")
                        .append("<TD>")
                            .append(entry.getValue())
                        .append("</TD>")
                    .append("</TR>")
            ;
        }

        return builder.toString();
    }

    protected String createAdditionalHeaderInfoNodeId(Node<?, ?> node) {
        String result = "";
        if (node instanceof SceneLinkNode) {
            result += "(scene link)" + "<BR/>" + "sceneId=[" + ((SceneLinkNode<?, ?>) node).getSceneTitle() + "]";
        } else if (node instanceof CompositeNode) {
            result += "(composite)";
        } else {
            result += "(simple node)";
        }

        return result;
    }

}
