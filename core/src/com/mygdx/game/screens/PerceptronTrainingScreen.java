package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.Helper;
import com.mygdx.game.objects.Circle;
import com.mygdx.game.objects.Perceptron;

/**
 * Created by alberto on 6/17/2017.
 */
public class PerceptronTrainingScreen extends AbstractReturnScreen {

    ShapeRenderer sr;

    Perceptron p;
    Circle[] points = new Circle[500];

    int trainingIndex = 0;

    Label perceptronWeignts, label;

    public PerceptronTrainingScreen(Game game, MainMenu mainMenu) {
        super(game, mainMenu);
    }

    @Override
    public void show() {
        super.show();

        sr = new ShapeRenderer();

        p = new Perceptron(3);

        for (int i = 0; i < points.length; i++) {
            points[i] = new Circle();
        }

        perceptronWeignts = new Label("Hello", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        perceptronWeignts.setPosition(Helper.WORLD_HEIGHT + 50, Helper.WORLD_HEIGHT - 100);
        stage.addActor(perceptronWeignts);

        label = new Label("  PERCEPTRON\nLR: " + Perceptron.LEARNING_RATE, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        label.setPosition(Helper.WORLD_HEIGHT + 50, Helper.WORLD_HEIGHT - 50);
        stage.addActor(label);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Helper.handleCamera(camera);

        sr.setProjectionMatrix(camera.combined);
        for(Circle c: points) {
            c.draw(sr);

            float[] inputs = {c.x, c.y, c.bias};
            int target = c.label;
            int guess = p.guess(inputs);
            //p.train(inputs, target);
            if( guess == target) {
                sr.setColor(Color.GREEN);
            } else {
                sr.setColor(Color.RED);
            }
            sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.circle(c.pixelX(), c.pixelY(), 3);
            sr.end();

            // update display of weights
            perceptronWeignts.setText( "w0 = " + p.weights[0] + "\nw1 = " + p.weights[1] + "\nw2 = " + p.weights[2]
                                + "\nprogress : " + (trainingIndex + 1) + " / " + points.length );
        }

        // train every frame
        float[] inputs = {points[trainingIndex].x, points[trainingIndex].y, points[trainingIndex].bias};
        int target = points[trainingIndex].label;
        p.train(inputs, target);
        trainingIndex++;
        if (trainingIndex >= points.length) trainingIndex = 0;

        // draw line to predict
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.WHITE);
        Circle p1 = new Circle(-1, Helper.f(-1));
        Circle p2 = new Circle(1, Helper.f(1));
        sr.line(p1.pixelX(), p1.pixelY(), p2.pixelX(), p2.pixelY());

        sr.setColor(Color.PURPLE);
        Circle p3 = new Circle(-1, p.guessY(-1));
        Circle p4 = new Circle(1, p.guessY(1));
        sr.line(p3.pixelX(), p3.pixelY(), p4.pixelX(), p4.pixelY());
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
