package com.sinfoniasolutions.celluloid.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cameron Seebach on 6/20/14.
 */
public class Clip {
    private final List<Layer> layers;

    public Clip(String name) {
        this.layers = new ArrayList<Layer>();
    }

    public List<Layer> getLayers() {
        return layers;
    }

    public void addLayer(Layer layer) {
        this.layers.add(layer);
    }

    public Layer getLayer(String name) {
        for (Layer layer : layers) {
            if (layer.getName().equals(name)) {
                return layer;
            }
        }
        return null;
    }


}
