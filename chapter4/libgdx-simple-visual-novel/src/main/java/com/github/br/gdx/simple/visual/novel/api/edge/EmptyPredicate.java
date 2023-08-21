package com.github.br.gdx.simple.visual.novel.api.edge;

import com.github.br.gdx.simple.visual.novel.api.context.PlotContext;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;

public class EmptyPredicate<UC extends UserContext> implements Predicate<UC> {

    @Override
    public boolean test(PlotContext<UC> context) {
        return true;
    }

    public static <UC extends UserContext> Predicate<UC> empty() {
        return new EmptyPredicate<>();
    }

}
