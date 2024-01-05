package com.github.br.gdx.simple.visual.novel.api.exception;

import com.github.br.gdx.simple.visual.novel.api.context.*;

import java.util.List;

public class PlotException extends RuntimeException {

    public PlotException() {
        super();
    }

    public PlotException(String message) {
        super(message);
    }

    public PlotException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlotException(Throwable cause) {
        super(cause);
    }

    protected PlotException(String message, Throwable cause,
                            boolean enableSuppression,
                            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public PlotException(PlotContext<?, ? extends UserContext> plotContext, String message, Exception ex) {
        super(createMessage(plotContext, message, ex), ex);
    }

    private static String createMessage(PlotContext<?, ? extends UserContext> plotContext, String message, Exception ex) {
        AuxiliaryContext auxiliaryContext = plotContext.getAuxiliaryContext();
        CurrentState currentState = auxiliaryContext.stateStack.peek();

        return "plotId=[" + plotContext.getPlotId() + "] sceneId=[" + currentState.sceneId
                + "] nodeId=[" + currentState.nodeId + "] message: " + ex.getMessage() + "\n" + message + "\n" +
                createPath(plotContext.getPlotId(), auxiliaryContext.getPath(), currentState) + "\n";
    }

    private static String createPath(Object plotId, List<CurrentState> path, CurrentState currentState) {
        if (path == null) {
            return "The parameter 'SavePath' at 'PlotConfig' is not set";
        }

        StringBuilder builder = new StringBuilder();
        builder.append("\nplotId=[").append(plotId).append("]").append(" PATH:").append("\n");

        int counter = 1;
        for (CurrentState fullNodeId : path) {
            builder.append(counter).append(": ").append(fullNodeId).append("\n");
            counter++;
        }
        // добавляем хаком последнее состояние, на котором все и упало
        builder
                .append(counter).append(": ")
                .append("sceneId=[")
                .append(currentState.sceneId)
                .append("] nodeId=[")
                .append(currentState.nodeId)
                .append("]")
                .append(" <- EXCEPTION!!!")
                .append("\n");

        return builder.toString();
    }

}
