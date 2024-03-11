package com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.settings.painter;

import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.data.NodeElementType;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.data.NodeElementVizData;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.settings.DotVizSettings;

public interface DotVizModePainter {

    String createLegend(DotVizSettings settings);

    String createNodeInfo(
            DotVizSettings settings,
            NodeElementType nodeType,
            String nodeId,
            String label,
            NodeElementVizData value,
            boolean isVisited,
            boolean isExceptionNode
    );

}
