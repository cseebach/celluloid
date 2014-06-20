package com.sinfoniasolutions.celluloid.ast;

/**
 * Created by Cameron Seebach on 6/20/14.
 */
public class LayerParamsAST {
    public String position;
    public String flipped;
    public int opacity;

    public LayerParamsAST() {
        position = "0 0";
        flipped = "";
        opacity = 255;
    }
}
