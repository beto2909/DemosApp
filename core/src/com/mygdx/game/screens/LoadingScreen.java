package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Assets;
import com.mygdx.game.Helper;

/**
 * Created by alberto on 3/21/2016.
 */
public class LoadingScreen extends AbstractScreen {

    private ShapeRenderer sr;

    public LoadingScreen(Game game){
        super(game);
    }

    @Override
    public void show() {
        super.show();

        //load assets
        Assets.load();

        sr = new ShapeRenderer();
		/*
		When getting the assets we use
			Assets.manager.get(Assets.assetDescriptor)

			ex.
			Sprite mySprite = Assets.manager.get(Assets.mySpriteTexture);
		 */

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(!Assets.manager.update()){
            //System.out.println(Assets.manager.getProgress() * 100 + "%");

        } else {
            Helper.print("Done loading!");
            game.setScreen(new MainMenu(game));
        }

        float percent = Assets.manager.getProgress();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setProjectionMatrix(camera.combined);

        sr.line(0, 0, 200, 200);
        sr.end();

        sr.begin(ShapeRenderer.ShapeType.Filled);

        sr.setColor(Color.BLACK);
        sr.rect(50, 50, 200, 50);

        sr.setColor(Color.WHITE);
        sr.rect(55,55, 190 * percent, 40);

        sr.end();
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
