package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Helper;

/**
 * Created by alberto on 3/21/2016.
 */
public abstract class AbstractScreen implements Screen{
    public Game game;

    public OrthographicCamera camera;
    public Viewport viewport;

    public AbstractScreen(Game game){
        this.game = game;
    }

    @Override
    public void show() {
        camera = Helper.createCamera();
        viewport = Helper.createViewport(camera);
        viewport.apply(true);
        //Helper.repositionCamera(camera);
    }

    @Override
    public void render(float delta) {
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
    }
}
