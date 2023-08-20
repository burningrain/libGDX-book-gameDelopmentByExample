package com.github.br.gdx.simple.visual.novel.api.edge;

import com.github.br.gdx.simple.visual.novel.api.context.PlotContext;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.screen.ScreenManager;

public class EmptyPredicate<UC extends UserContext, SM extends ScreenManager> implements Predicate<UC, SM> {

    @Override
    public boolean test(PlotContext<UC, SM> context) {
        return true;
    }

    public static <UC extends UserContext, SM extends ScreenManager> Predicate<UC, SM> empty() {
        return new EmptyPredicate<>();
    }

}
