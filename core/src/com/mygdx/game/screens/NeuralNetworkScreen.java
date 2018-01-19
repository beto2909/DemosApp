package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.Helper;
import com.mygdx.game.objects.Matrix;

/**
 * Created by alberto on 7/7/2017.
 */
public class NeuralNetworkScreen extends AbstractReturnScreen {

    public NeuralNetworkScreen(Game game, MainMenu mainMenu) {
        super(game, mainMenu);
    }

    @Override
    public void show() {
        super.show();

        Matrix m = new Matrix(3, 2);
        Helper.print(m.toString());
        m.randomize();
        Helper.print(m.toString());

        Matrix m2 = new Matrix(3, 2);
        m2.randomize();
        Helper.print(m2.toString());
        m2.add(1);
        Helper.print(m2.toString());

        m.add(m2);
        Helper.print(m.toString());

        Matrix m3 = new Matrix(2, 3);
        m3.add(m);
    }

    @Override
    public void render(float delta) {
        super.render(delta);


        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    }
}
