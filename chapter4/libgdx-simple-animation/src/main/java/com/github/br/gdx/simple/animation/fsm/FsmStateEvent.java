package com.github.br.gdx.simple.animation.fsm;

/**
 * Created by user on 15.04.2017.
 */
public class FsmStateEvent {

    private final String title;
    StateScript script;


    public FsmStateEvent(final String title) {
        if (title == null) {
            throw new IllegalArgumentException("Название события FSM не должно быть null");
        }
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public StateScript getScript() {
        return script;
    }

    void setScript(StateScript script) {
        this.script = script;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FsmStateEvent fsmEvent = (FsmStateEvent) o;

        return !(title != null ? !title.equals(fsmEvent.title) : fsmEvent.title != null);

    }

    @Override
    public int hashCode() {
        return title != null ? title.hashCode() : 0;
    }

}
