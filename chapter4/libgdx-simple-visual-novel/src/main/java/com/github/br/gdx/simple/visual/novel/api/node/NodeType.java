package com.github.br.gdx.simple.visual.novel.api.node;

public enum NodeType {

    WAITING_INPUT,  // встает на паузу до следующего plot.execute() от пользователя
    IMMEDIATELY     // процесс сразу идет исполняться дальше без ожидания

}
