package com.sinfoniasolutions.celluloid;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Cameron Seebach on 6/20/14.
 */
public class KeyFrame implements Comparable<KeyFrame> {

    private final float time;
    private final Action action;
    private final Actor target;

    public KeyFrame(float time, Action action, Actor target) {
        this.time = time;
        this.action = action;
        this.target = target;
    }

    public float getTime() {
        return time;
    }

    @Override
    public int compareTo(KeyFrame o) {
        if(time > o.time){
            return 1;
        }else if(time < o.time){
            return -1;
        }else{
            return 0;
        }
    }

    public void apply() {
        target.addAction(action);
    }
}
