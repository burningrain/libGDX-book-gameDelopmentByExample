package com.github.br.ecs.simple.fsm;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by user on 16.04.2017.
 */
class FsmStateEventBasket {

    private FsmStateEvent event;
    private ArrayList<FsmStateEventHandler> handlers;

    public FsmStateEventBasket(FsmStateEvent event, Collection<FsmStateEventHandler> handlers){
        if(event == null){
            throw new IllegalArgumentException("Событие должно быть задано!");
        }
        this.event = event;
        this.handlers = new ArrayList<FsmStateEventHandler>(handlers);
    }

    void notifyHandlers(FsmStateEvent event){
        for(FsmStateEventHandler handler : handlers){
            handler.handleEvent(event);
        }
    }

    public void addEventHandler(FsmStateEventHandler handler){
        handlers.add(handler);
    }

    public void removeEventHandler(FsmStateEventHandler handler){
        handlers.remove(handler);
    }

    public FsmStateEvent getEvent() {
        return event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FsmStateEventBasket that = (FsmStateEventBasket) o;

        return !(event != null ? !event.equals(that.event) : that.event != null);

    }

    @Override
    public int hashCode() {
        return event != null ? event.hashCode() : 0;
    }
}
