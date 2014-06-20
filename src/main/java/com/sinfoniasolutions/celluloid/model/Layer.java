package com.sinfoniasolutions.celluloid.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cameron Seebach on 6/20/14.
 */
public class Layer {
    private final String imagePath;
    private final String name;
    private int opacity;
    private boolean flippedX;
    private boolean flippedY;
    private int positionX;
    private int positionY;
    private List<ActionWithTime> actions;

    public Layer(String imagePath, String name) {
        this.imagePath = imagePath;
        this.name = name;
        actions = new ArrayList<ActionWithTime>();
        opacity = 255;

    }

    public List<ActionWithTime> getActions() {
        return actions;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public int getOpacity() {
        return opacity;
    }

    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }

    public boolean isFlippedX() {
        return flippedX;
    }

    public void setFlippedX(boolean flippedX) {
        this.flippedX = flippedX;
    }

    public boolean isFlippedY() {
        return flippedY;
    }

    public void setFlippedY(boolean flippedY) {
        this.flippedY = flippedY;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void addAction(float time, Action action) {
        actions.add(new ActionWithTime(time, action));
    }

    public class ActionWithTime {

        public final float time;
        public final Action action;

        public ActionWithTime(float time, Action action) {

            this.time = time;
            this.action = action;
        }
    }
}
