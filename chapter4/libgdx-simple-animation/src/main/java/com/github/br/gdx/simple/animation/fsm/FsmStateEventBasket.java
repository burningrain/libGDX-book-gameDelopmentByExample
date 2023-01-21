package com.github.br.gdx.simple.animation.fsm;

import com.badlogic.gdx.utils.Array;

/**
 * Created by user on 16.04.2017.
 */
class FsmStateEventBasket {

    private final FsmStateEvent event;
    private final Array<FsmStateEventHandler> handlers;

    public FsmStateEventBasket(FsmStateEvent event, Array<FsmStateEventHandler> handlers) {
        if (event == null) {
            throw new IllegalArgumentException("Событие должно быть задано!");
        }
        this.event = event;
        this.handlers = new Array<FsmStateEventHandler>(handlers);
    }

    void notifyHandlers(FsmStateEvent event) {
        for (FsmStateEventHandler handler : handlers) {
            handler.handleEvent(event);
        }
    }

    public void addEventHandler(FsmStateEventHandler handler) {
        handlers.add(handler);
    }

    public void removeEventHandler(FsmStateEventHandler handler) {
        handlers.removeValue(handler, false);
    }

    public FsmStateEvent getEvent() {
        return event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FsmStateEventBasket that = (FsmStateEventBasket) o;

        return event.equals(that.event);

    }

    @Override
    public int hashCode() {
        return event.hashCode();
    }
}
