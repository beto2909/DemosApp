package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.Helper;
import com.mygdx.game.objects.Cell;

/**
 * Created by alberto on 6/11/2016.
 */
public class ReactionDifussionDemo extends AbstractReturnScreen {

    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private static final float dA = 1f;
    private static final float dB = .5f;
    private static final float feed = .066f;
    private static final float k = .062f;

    private SpriteBatch batch;

    private Pixmap pm;
    private Texture texture;
    private Cell[][] grid, next;

    public ReactionDifussionDemo(Game game, MainMenu mainMenu){
        super(game, mainMenu);
    }

    @Override
    public void show() {
        super.show();

        batch = new SpriteBatch();

        grid = new Cell[WIDTH][HEIGHT];
        next = new Cell[WIDTH][HEIGHT];
        pm = new Pixmap(WIDTH, HEIGHT, Pixmap.Format.RGBA8888);

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT ; y++) {
                grid[x][y] = new Cell(1,0);
                next[x][y] = new Cell(1,0);
            }
        }

        for (int i = 200; i < 220 ; i++) {
            for (int j = 200; j < 220; j++) {
                grid[i][j].b = 1;
            }
        }

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT ; y++){
                Color c = new Color(
                        grid[x][y].a * 100,
                        0,
                        grid[x][y].b * 100, 1);
                pm.setColor(c);
                pm.drawPixel(x,y);
            }
        }

        texture = new Texture(pm);

        //Gdx.input.setInputProcessor(null);

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(.05f,.05f,.05f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        for (int x = 1; x < WIDTH -1; x++) {
            for (int y = 1; y < HEIGHT-1 ; y++){
                float a = grid[x][y].a;
                float b = grid[x][y].b;
                next[x][y].a =  a + (dA * laplaceA(x,y)) -
                                (a * b * b) +
                                (feed * (1 - a));
                next[x][y].b =  b + (dB * laplaceB(x,y)) +
                        (a * b * b) -
                        ((k + feed) * b);

                next[x][y].a = MathUtils.clamp(next[x][y].a, 0, 1);
                next[x][y].b = MathUtils.clamp(next[x][y].b, 0, 1);

            }
        }

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT ; y++){
                float a = next[x][y].a;
                float b = next[x][y].b;
                float c = MathUtils.floor((a-b) * 255);
                c = MathUtils.clamp(c, 0, 255);
                Color col = new Color(c,c,c, 1);
                pm.setColor(col);
                pm.drawPixel(x,y);
            }
        }
        texture.draw(pm, 0,0);
        swap();

        batch.begin();
        batch.draw(texture, 0, 0);
        batch.end();

        super.drawStage();
    }

    private float laplaceA(int x, int y){
        float sumA = 0;
        sumA += grid[x][y].a * -1;
        sumA += grid[x-1][y].a * .2f;
        sumA += grid[x+1][y].a * .2f;
        sumA += grid[x][y+1].a * .2f;
        sumA += grid[x][y-1].a * .2f;
        sumA += grid[x-1][y-1].a * .05f;
        sumA += grid[x+1][y-1].a * .05f;
        sumA += grid[x+1][y+1].a * .05f;
        sumA += grid[x-1][y+1].a * .05f;
        return sumA;
    }

    private float laplaceB(int x, int y){
        float sumB = 0;
        sumB += grid[x][y].b * -1;
        sumB += grid[x-1][y].b * .2f;
        sumB += grid[x+1][y].b * .2f;
        sumB += grid[x][y+1].b * .2f;
        sumB += grid[x][y-1].b * .2f;
        sumB += grid[x-1][y-1].b * .05f;
        sumB += grid[x+1][y-1].b * .05f;
        sumB += grid[x+1][y+1].b * .05f;
        sumB += grid[x-1][y+1].b * .05f;
        return sumB;
    }

    private void swap(){
        Cell[][] tmp = grid;
        grid = next;
        next = tmp;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        Helper.resizeScreen(viewport, camera, width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        pm.dispose();
        batch.dispose();
        texture.dispose();
    }
}
