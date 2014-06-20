package com.sinfoniasolutions.celluloid.model;

import java.util.List;

/**
 * Created by Cameron Seebach on 6/20/14.
 */
public class Action {
    private final Type type;
    private final List<Integer> params;
    private final float duration;
    private final Interpolation interpolation;

    public Action(Type type, List<Integer> params, float duration, Interpolation interpolation) {

        this.type = type;
        this.params = params;
        this.duration = duration;
        this.interpolation = interpolation;
    }

    public Type getType() {
        return type;
    }

    public float getDuration() {
        return duration;
    }

    public Interpolation getInterpolation() {
        return interpolation;
    }

    public List<Integer> getParams() {
        return params;
    }

    public enum Type {MOVE_BY, FADE_IN}

    public enum Interpolation {FADE, LINEAR}
}
