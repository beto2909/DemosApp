package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Helper;
import com.mygdx.game.objects.ParticleSystem;

import java.util.ArrayList;

/**
 * Created by alberto on 5/7/2016.
 */
public class ParticleSystemDemo  extends AbstractReturnScreen {

    private SpriteBatch batch;
    private ShapeRenderer sr;

    private ArrayList<ParticleSystem> ps;

    public ParticleSystemDemo(Game game, MainMenu mainMenu){
        super(game, mainMenu);
    }

    @Override
    public void show() {
        super.show();
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        sr = new ShapeRenderer();
        sr.setProjectionMatrix(camera.combined);

        ps = new ArrayList<ParticleSystem>();
        ps.add(new ParticleSystem());

        //Gdx.input.setInputProcessor(null);    // clears previous screen buttons
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(.05f,.05f,.05f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyPressed(Input.Keys.A)){
            camera.zoom += .1f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)){
            camera.zoom -= .1f;
        }
        if (Gdx.input.isTouched()){
            Vector2 click = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()) );
            //ps.add(new ParticleSystem(click.x, click.y));
            Vector2 grav = new Vector2(0, -9);
            Vector2 wind = new Vector2(.2f, 0);
            for (ParticleSystem p: ps){
                p.applyForce(wind);
            }
        }

        camera.update();

        for (ParticleSystem s:ps){
            s.addParticle();
            s.update();
        }
        batch.begin();
        for (ParticleSystem s:ps){
            s.draw(batch);
        }
        batch.end();

        sr.setColor(0,0,.8f,1);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        //sr.ellipse(0,0, 100, 100);
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
        batch.dispose();
        sr.dispose();
    }
}
