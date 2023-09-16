package com.github.br.gdx.simple.visual.novel.impl;

import com.github.br.gdx.simple.visual.novel.api.plot.GeneratorPlotId;

import java.util.concurrent.atomic.AtomicInteger;

public class GeneratorTestPlotIdImpl implements GeneratorPlotId<Integer> {

    private final AtomicInteger counter = new AtomicInteger();

    @Override
    public Integer nextId() {
        return counter.addAndGet(1);
    }

}
