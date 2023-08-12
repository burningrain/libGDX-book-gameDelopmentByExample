package com.github.br.gdx.simple.visual.novel.api.plot;

import com.github.br.gdx.simple.visual.novel.Utils;

import java.util.Locale;

public class PlotConfig {

    private final Locale locale;
    private final boolean isMarkVisitedNodes;

    public PlotConfig(Builder builder) {
        this.locale = Utils.checkNotNull(builder.locale, "locale");
        this.isMarkVisitedNodes = builder.isMarkVisitedNodes;
    }

    public Locale getLocale() {
        return locale;
    }

    public boolean isMarkVisitedNodes() {
        return isMarkVisitedNodes;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Locale locale = Locale.ENGLISH;
        private boolean isMarkVisitedNodes = true;

        public Builder setLocale(Locale locale) {
            this.locale = locale;
            return this;
        }

        public Builder setMarkVisitedNodes(boolean markVisitedNodes) {
            isMarkVisitedNodes = markVisitedNodes;
            return this;
        }

        public PlotConfig build() {
            return new PlotConfig(this);
        }

    }

}
