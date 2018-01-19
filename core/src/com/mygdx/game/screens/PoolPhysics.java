package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Helper;
import com.mygdx.game.objects.Ball;
import com.mygdx.game.objects.Cue;

/**
 * Created by alberto on 7/3/2016.
 */
public class PoolPhysics extends AbstractReturnScreen {

    private ShapeRenderer sr;

    private Ball[] balls;
    Ball cueBall;
    private Cue cue;

    public PoolPhysics(Game game, MainMenu mainMenu){
        super(game, mainMenu);
    }

    @Override
    public void show() {
        super.show();

        sr = new ShapeRenderer();

        balls = new Ball[5];
        for (int i = 0; i < balls.length; i++) {
            balls[i] = new Ball(MathUtils.random() * Helper.WORLD_WIDTH, MathUtils.random() * Helper.WORLD_HEIGHT);
        }
        cueBall = balls[0];

        cue = new Cue(cueBall.getPosition().x, cueBall.getPosition().y,
                    cueBall.getRadius());

        //Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();




        for (int i = 0; i < balls.length; i++) {
            for (int j = 0; j < balls.length; j++) {
                if (j != i){ // not checking itself
                    if (balls[i].collided(balls[j])){
                        balls[i].resolveCollision(balls[j]);
                    }
                }
            }
        }

        sr.setProjectionMatrix(camera.combined);
        for (int i = 0; i < balls.length; i++) {
            if (i == 0){
                sr.setColor(Color.BLUE);
            } else {
                sr.setColor(Color.WHITE);
            }
            balls[i].update();
            balls[i].draw(sr);
        }

        cue.update(cueBall, viewport);
        cue.draw(sr, cueBall);
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
    }
}
