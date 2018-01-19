package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Helper;
import com.mygdx.game.objects.GoL;

/**
 * Created by alberto on 6/19/2016.
 */
public class GameOfLifeDemo extends AbstractReturnScreen {

    private SpriteBatch batch;

    private GoL goL;
    private boolean running;

    public GameOfLifeDemo(Game game, MainMenu mainMenu){
        super(game, mainMenu);
    }

    @Override
    public void show() {
        super.show();

        batch = new SpriteBatch();

        goL = new GoL(70,70);
        running = true;

        //Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        Gdx.graphics.setContinuousRendering(false);
        if (Gdx.input.isTouched()){
            Vector2 click = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()) );
            running = false;
            click.x = (int) click.x/10;
            click.y = (int) click.y/10;
            goL.fill(click);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)){
            running = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.N)){
            goL.run();
        }
        if (running) {
            goL.run();
        }

        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        goL.draw(batch);
        batch.end();

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
    }
}
