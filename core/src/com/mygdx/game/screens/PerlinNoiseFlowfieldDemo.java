package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Helper;
import com.mygdx.game.objects.Flowfield;
import com.mygdx.game.objects.Point;

/**
 * Created by alberto on 7/3/2016.
 */
public class PerlinNoiseFlowfieldDemo extends AbstractReturnScreen {

    private ShapeRenderer sr;
    private Flowfield field;
    private Point[] points;
    boolean clear = false;

    public PerlinNoiseFlowfieldDemo(Game game, MainMenu mainMenu){
        super(game, mainMenu);
    }

    @Override
    public void show() {
        super.show();

        sr = new ShapeRenderer();
        field = new Flowfield(15);
        points = new Point[100];
        for (int i = 0; i < points.length; i++) {
            points[i] = new Point(MathUtils.random() * Helper.WORLD_WIDTH,
                                MathUtils.random() * Helper.WORLD_HEIGHT);
        }

        //Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        Helper.handleCamera(camera);

        sr.setProjectionMatrix(camera.combined);
        field.draw(sr);
        Vector2 f;
        for (int i = 0; i < points.length ; i++) {
            f = field.lookup(points[i].position);
            points[i].applyForce(f);
            points[i].update();
            points[i].draw(sr);
        }

        super.drawStage();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        Helper.resizeScreen(viewport, camera, width, height);
    }
}
