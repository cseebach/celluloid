package com.sinfoniasolutions.celluloid;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.sinfoniasolutions.celluloid.model.Celluloid;

/**
 * Created by Cameron Seebach on 6/20/14.
 */
public class TestBasic extends ApplicationAdapter {

    private Celluloid celluloid;
    private Stage stage;

    public static void main(String[] args) {
        LwjglApplication app = new LwjglApplication(new TestBasic(), "testing", 800, 600);
    }

    @Override
    public void create() {
        stage = new CelluloidLoader().load("cutscene2.yml");
    }

    @Override
    public void render() {
        stage.act();
        stage.draw();
    }
}
