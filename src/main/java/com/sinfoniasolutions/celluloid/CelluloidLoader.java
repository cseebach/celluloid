package com.sinfoniasolutions.celluloid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.sinfoniasolutions.celluloid.ast.CelluloidAST;
import com.sinfoniasolutions.celluloid.ast.ClipAST;
import com.sinfoniasolutions.celluloid.ast.LayerParamsAST;
import com.sinfoniasolutions.celluloid.model.Action;
import com.sinfoniasolutions.celluloid.model.Celluloid;
import com.sinfoniasolutions.celluloid.model.Clip;
import com.sinfoniasolutions.celluloid.model.Layer;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Cameron Seebach on 6/20/14.
 */
public class CelluloidLoader {

    public Stage load(String internalPath) {

        Yaml yaml = new Yaml();
        CelluloidAST ast = yaml.loadAs(Gdx.files.internal(internalPath).readString(), CelluloidAST.class);

        Celluloid transformed = transform(ast);

        return makeStage(transformed);
    }

    private Stage makeStage(Celluloid transformed) {
        Stage stage = new Stage();

        for (Layer layer : transformed.getClip().getLayers()) {
            Texture texture = new Texture(Gdx.files.internal(layer.getImagePath() + ".png"));
            TextureRegion region = new TextureRegion(texture);
            region.flip(layer.isFlippedX(), layer.isFlippedY());

            Image image = new Image(region);
            image.setPosition(layer.getPositionX(), layer.getPositionY());
            image.setColor(1, 1, 1, layer.getOpacity() / 255.0f);
            stage.addActor(image);

            for (Layer.ActionWithTime action : layer.getActions()) {
                com.badlogic.gdx.scenes.scene2d.Action stageAction = null;
                Interpolation interpolation = Interpolation.linear;
                if (action.action.getInterpolation().equals(Action.Interpolation.FADE)) {
                    interpolation = Interpolation.fade;
                }

                if (action.action.getType().equals(Action.Type.MOVE_BY)) {
                    stageAction = Actions.moveBy(
                            action.action.getParams().get(0),
                            action.action.getParams().get(1),
                            action.action.getDuration(),
                            interpolation);
                } else if (action.action.getType().equals(Action.Type.FADE_IN)) {
                    stageAction = Actions.fadeIn(action.action.getDuration(),
                            interpolation);
                }
                image.addAction(Actions.delay(action.time, stageAction));
            }
        }

        return stage;
    }

    private Celluloid transform(CelluloidAST ast) {

        for (Map.Entry<String, ClipAST> clipAst : ast.clips.entrySet()) {
            Clip clip = new Clip(clipAst.getKey());

            //parse layers
            for (String layerDef : clipAst.getValue().layers) {
                String[] splits = layerDef.split("\\s+");
                if (splits.length == 1) {
                    clip.addLayer(new Layer(splits[0], splits[0]));
                } else if (splits.length == 3) {
                    clip.addLayer(new Layer(splits[2], splits[0]));
                }
            }

            //parse layer_params
            Map<String, LayerParamsAST> layer_params = clipAst.getValue().layer_params;
            if (layer_params != null) {
                for (Map.Entry<String, LayerParamsAST> layerParamsAst : layer_params.entrySet()) {
                    LayerParamsAST value = layerParamsAst.getValue();

                    Layer layer = clip.getLayer(layerParamsAst.getKey());
                    layer.setOpacity(value.opacity);
                    layer.setFlippedX(value.flipped.contains("x"));
                    layer.setFlippedY(value.flipped.contains("y"));

                    String[] positionSplit = value.position.split("\\s+");
                    if (positionSplit.length == 2) {
                        layer.setPositionX(Integer.valueOf(positionSplit[0]));
                        layer.setPositionY(Integer.valueOf(positionSplit[1]));
                    }
                }
            }

            if (clipAst.getValue().timeline != null) {
                //parse timeline
                for (Map.Entry<String, List<String>> timelineEntry : clipAst.getValue().timeline.entrySet()) {
                    float seconds = Float.valueOf(timelineEntry.getKey());
                    List<String> actions = timelineEntry.getValue();
                    for (String actionAst : actions) {
                        actionAst = expandMacros(actionAst, clipAst.getValue().macros);
                        String[] actionSplits = actionAst.split("\\s+");
                        clip.getLayer(actionSplits[0]).addAction(seconds, parseAction(actionSplits));

                    }
                }
            }

            return new Celluloid(clip);
        }


        return null;
    }

    private Action parseAction(String[] actionSplits) {
        Action.Type type = null;
        List<Integer> params = new ArrayList<Integer>();
        float duration = .5f;
        Action.Interpolation interpolation = Action.Interpolation.LINEAR;

        ParseMode parseMode = ParseMode.TARGET;
        for (String split : actionSplits) {
            switch (parseMode) {
                case TARGET:
                    parseMode = ParseMode.ACTION_TYPE;
                    break;
                case ACTION_TYPE:
                    if (split.equals("move_by")) {
                        type = Action.Type.MOVE_BY;
                    } else if (split.equals("fade_in")) {
                        type = Action.Type.FADE_IN;
                        interpolation = Action.Interpolation.FADE;
                    }
                    parseMode = ParseMode.ACTION_PARAMS;
                    break;
                case ACTION_PARAMS:
                    if (split.equals("over")) {
                        parseMode = ParseMode.DURATION;
                    } else {
                        params.add(Integer.valueOf(split));
                    }
                    break;
                case DURATION:
                    if (split.equals("using")) {
                        parseMode = ParseMode.INTERPOLATION;
                    }else{
                        duration = Float.valueOf(split.replace("s", ""));
                    }
                    break;
                case INTERPOLATION:
                    if (split.equals("fade")) {
                        interpolation = Action.Interpolation.FADE;
                    }
                    break;
            }
        }
        return new Action(type, params, duration, interpolation);
    }

    private String expandMacros(String action, Map<String, String> macros) {
        for (Map.Entry<String, String> macro : macros.entrySet()) {
            action = action.replace(macro.getKey(), macro.getValue());
        }
        return action;
    }

    private enum ParseMode {TARGET, ACTION_TYPE, ACTION_PARAMS, DURATION, INTERPOLATION}
}
