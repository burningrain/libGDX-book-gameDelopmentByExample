package com.github.br.ecs.simple.fsm;

/**
 * Created by user on 09.04.2017.
 */
public class FsmTransition {

    private String from;
    private String to;
    private FsmPredicate predicate;

    public FsmTransition(String from, String to, FsmPredicate predicate){
        this.from = from;
        this.to = to;
        this.predicate = predicate;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public FsmPredicate getPredicate() {
        return predicate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FsmTransition that = (FsmTransition) o;

        if (from != null ? !from.equals(that.from) : that.from != null) return false;
        if (to != null ? !to.equals(that.to) : that.to != null) return false;
        return !(predicate != null ? !predicate.equals(that.predicate) : that.predicate != null);

    }

    @Override
    public int hashCode() {
        int result = from != null ? from.hashCode() : 0;
        result = 31 * result + (to != null ? to.hashCode() : 0);
        result = 31 * result + (predicate != null ? predicate.hashCode() : 0);
        return result;
    }
}
