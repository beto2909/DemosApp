package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Helper;
import com.mygdx.game.objects.Pendulum;
import com.mygdx.game.objects.SimpleHarmonicMotion;
import com.mygdx.game.objects.Spring;

/**
 * Created by alberto on 5/26/2016.
 */
public class OscillatorsDemo extends AbstractReturnScreen {

    private static final Vector2 wind = new Vector2(.5f,0);
    private static final Vector2 gravity = new Vector2(0,-1.2f);

    private ShapeRenderer sr;

    private SimpleHarmonicMotion hm;
    private Pendulum pendulum;
    private Pendulum[] pends;
    private Spring spring;

    public OscillatorsDemo(Game game, MainMenu mainMenu) {
        super(game, mainMenu);
    }

    @Override
    public void show() {
        super.show();

        sr = new ShapeRenderer();

        hm = new SimpleHarmonicMotion(Helper.WORLD_WIDTH/2,Helper.WORLD_HEIGHT/2);
        pendulum = new Pendulum(100);
        pends = new Pendulum[10];
        for (int i = 0; i < pends.length; i++) {
            pends[i] = new Pendulum((i + 1) * Helper.WORLD_HEIGHT/10);
        }

        spring = new Spring(200);

        //Gdx.input.setInputProcessor(null);    // clears previous screen buttons
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, .49f , .56f ,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyPressed(Input.Keys.A)){
            camera.zoom += .1f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)){
            camera.zoom -= .1f;
        }
        if (Gdx.input.isTouched()){
            Vector2 click = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()) );
            spring.applyForce(wind);
        }
        spring.applyForce(gravity);

        camera.update();


        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setProjectionMatrix(camera.combined);// if not included zooming in doesn't work
        sr.setColor(Color.BLACK);
        //hm.draw(sr);
        for (int i = 0; i < pends.length; i++) {
            //pends[i].draw(sr);
        }
        spring.draw(sr);
        //pendulum.draw(sr);
        sr.end();

        super.drawStage();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        Helper.resizeScreen(viewport, camera, width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        sr.dispose();
    }
}
