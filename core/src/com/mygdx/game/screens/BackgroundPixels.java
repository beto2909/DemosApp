package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Helper;
import com.mygdx.game.objects.Blob;
import com.mygdx.game.objects.Canvas;
import com.mygdx.game.objects.Pixel;
import com.mygdx.game.objects.Planet;
import com.mygdx.game.objects.SolarSystem;

/**
 * Created by alberto on 8/7/2016.
 */
public class BackgroundPixels extends AbstractReturnScreen {

    private static final int C = 4;
    private ShapeRenderer sr;
    private float n, a, r, x, y;
    private Array<Vector2> points;
    private Pixel[] pixels;
    private Blob[] blobs;
    private Canvas canvas;
    SolarSystem solarSystem;

    public BackgroundPixels(Game game, MainMenu mainMenu){
        super(game, mainMenu);
    }

    @Override
    public void show() {
        super.show();

        sr = new ShapeRenderer();
        n = 0;
        points = new Array<Vector2>();
        pixels = new Pixel[Helper.WORLD_HEIGHT * Helper.WORLD_WIDTH];
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = new Pixel();
        }
        blobs = new Blob[10];
        for (int i = 0; i < blobs.length; i++) {
            blobs[i] = new Blob(MathUtils.random(Helper.WORLD_WIDTH),MathUtils.random(Helper.WORLD_HEIGHT));
        }

        canvas = new Canvas();
        solarSystem = new SolarSystem();

        //Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        Helper.handleCamera(camera);

        //phyllotaxis();
        //metaballs();

        Color col;
        for (int x = 0; x < canvas.getWidth(); x++) {
            for (int y = 0; y < canvas.getHeight(); y++) {
                float sum = 0;
                for (int i = 0; i < solarSystem.getPlanets().length; i++) {
                    Planet current = solarSystem.getPlanets()[i];
                    float d = Vector2.dst(x,y, current.pos.x, current.pos.y);
                    sum += 150*current.r/d;
                }
                col = Helper.HSV_to_RGB(sum%255, 255, 255);
                canvas.setPixel(x, y, new Pixel(col.r, col.g, col.b) );
                //pixels[index] = new Color(sum/255f,sum/255f,sum/255f,1);
            }
        }


        sr.setProjectionMatrix(camera.combined);
        canvas.draw(sr);
        solarSystem.draw(sr);

        super.drawStage();
    }

    public void metaballs(){
        Color col;
        for (int x = 0; x < canvas.getWidth(); x++) {
            for (int y = 0; y < canvas.getHeight(); y++) {
                float sum = 0;
                for (Blob b: blobs){
                    float d = Vector2.dst(x,y, b.pos.x, b.pos.y);
                    sum += 150*b.r/d;
                }
                col = Helper.HSV_to_RGB(sum%255, 255, 255);
                canvas.setPixel(x, y, new Pixel(col.r, col.g, col.b) );
                //pixels[index] = new Color(sum/255f,sum/255f,sum/255f,1);
            }
        }

        for (Blob b: blobs) {
            b.update();
        }

        sr.setProjectionMatrix(camera.combined);
        canvas.draw(sr);
    }

    public void phyllotaxis(){
        //calculate phyllotaxis
        a = (n * 137.5f) * MathUtils.degreesToRadians;
        r = C * (float) Math.sqrt(n);
        x = r * MathUtils.cos(a) + Helper.WORLD_WIDTH/2;
        y = r * MathUtils.sin(a) + Helper.WORLD_HEIGHT/2;

        //add new point
        points.add(new Vector2(x,y));
        n++;

        //draw
        sr.setProjectionMatrix(camera.combined);

        sr.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < points.size; i++) {
            sr.setColor(Helper.HSV_to_RGB(i % 256, 255, 255));
            sr.circle(points.get(i).x, points.get(i).y, 2);
        }
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
