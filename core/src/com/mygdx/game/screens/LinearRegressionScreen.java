package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Helper;

/**
 * Created by alberto on 6/13/2017.
 */
public class LinearRegressionScreen extends AbstractReturnScreen {

    private static final float LEARNING_RATE = 0.05f;

    private ShapeRenderer sr;

    private Array<Vector2> data;
    private float m, b;
    private int oldSize;

    public LinearRegressionScreen(Game game, MainMenu main) {
        super(game, main);
    }

    @Override
    public void show() {
        super.show();
        sr = new ShapeRenderer();

        data = new Array<Vector2>();
        oldSize = 0;

        m = 1;
        b = 0;
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Helper.centerCamera(camera);
        Helper.positionCamera(camera, Helper.WORLD_HEIGHT/2, Helper.WORLD_HEIGHT/2);
        Helper.handleCamera(camera);

        // add points when touched
        if (Gdx.input.isTouched()) {
            Vector2 click = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            //Helper.print(click.toString());
            click.x = Helper.map(click.x, 0, Helper.WORLD_HEIGHT, 0, 1);
            click.y = Helper.map(click.y, 0, Helper.WORLD_HEIGHT, 0, 1);
            data.add(new Vector2(click.x, click.y));
        }

        //rulesSquare();
        gradientDescent();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setProjectionMatrix(camera.combined);
        sr.setColor(Color.WHITE);

        drawGraphBox();
        drawData();
        drawLine();

        sr.end();


        super.drawStage();
    }


    public void gradientDescent() {
        float guess = 0, error = 0;
        for (int i = 0; i < data.size; i++) {
            guess = m * data.get(i).x + b;
            error = data.get(i).y - guess;
            m = m + (error * data.get(i).x) * LEARNING_RATE;
            b = b + error * LEARNING_RATE;
        }
        //Helper.print(b + "");
    }

    /**
        m = sum( (x - xmean)(y - ymean) ) / sum ( (x - xmean) ^ 2 )
        b = ymean - m * xmean
     */
    public void rulesSquare() {
        if (oldSize < data.size) { // try to loop as little as possible
            oldSize = data.size;
            // calc xmean | ymean
            float xmean = 0, ymean = 0;
            for (int i = 0; i < data.size; i++) {
                xmean += data.get(i).x;
                ymean += data.get(i).y;
            }
            xmean = xmean / data.size;
            ymean = ymean / data.size;

            // do m formula
            float num = 0, den = 0;
            for (int i = 0; i < data.size; i++) {
                num += (data.get(i).x - xmean) * (data.get(i).y - ymean);
                den += (data.get(i).x - xmean) * (data.get(i).x - xmean);
            }
            m = num / den;
            b = ymean - m * xmean;
        }
    }

    public void drawData() {
        float x,y;
        for (int i = 0; i < data.size; i++) {
            x = data.get(i).x * Helper.WORLD_HEIGHT;
            y = data.get(i).y * Helper.WORLD_HEIGHT;
            sr.circle(x, y, 3);
        }
    }

    public void drawLine() {
        sr.line(0, b * Helper.WORLD_HEIGHT , Helper.WORLD_HEIGHT, m * Helper.WORLD_HEIGHT + b );
    }

    public void drawGraphBox() {
        sr.rect(0, 0, Helper.WORLD_HEIGHT, Helper.WORLD_HEIGHT );
    }

//    @Override
//    public void resize(int width, int height) {
//        super.resize(width, height);
//        Helper.resizeScreen(viewport, camera, width, height);
//    }

    @Override
    public void dispose() {
        super.dispose();
        sr.dispose();
    }
}
