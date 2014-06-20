package com.sinfoniasolutions.celluloid.ast;

import java.util.List;
import java.util.Map;

/**
 * Created by Cameron Seebach on 6/20/14.
 */
public class ClipAST {
    public List<String> layers;
    public Map<String, LayerParamsAST> layer_params;
    public Map<String, String> macros;
    public Map<String, List<String>> timeline;
}
