package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Helper;

/**
 * Created by alberto on 8/22/2016.
 */
public class ArtScreen extends AbstractReturnScreen {

    private static final int TOTAL_LINES = 300;//126/2;
    private static final int TOTAL_POINTS = 150;
    private static final int R = 50;
    private static final int L = 50;


    private ShapeRenderer sr;
    private float t;
    private float sAngle, cAngle;
    private float sVelocity, cVelocity;
    private Array<Vector3> circles;
    // W = static circle radius
    // r = rotating circle radius
    // o = offset of penpoint
    private float W = 50, r = 40, o = 10;



    public ArtScreen(Game game, MainMenu mainMenu){
        super(game, mainMenu);
    }

    @Override
    public void show() {
        super.show();
        sr = new ShapeRenderer();
        t = 0;

        sAngle = 0;
        cAngle = 0;
        sVelocity = 5;
        cVelocity = 50;

        circles = new Array<Vector3>();

        //Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Helper.centerCamera(camera);
        Helper.handleCamera(camera);

        sr.setProjectionMatrix(camera.combined);

        parametricArt(delta);
        //raveArt(delta);
        super.drawStage();
    }

    public void raveArt(float delta){

        sAngle += sVelocity * (delta/2);
        cAngle += cVelocity * (delta/2);

        // x = circle.x
        // y = circle.y
        // z = angle of circle
        Vector3 circle = new Vector3();

        circle.x = MathUtils.cos(sAngle) * L;
        circle.y = MathUtils.sin(sAngle) * L;
        circle.z = cAngle;

        circles.add(circle);

        if( circles.size > TOTAL_POINTS){
            circles.removeIndex(0);
        }


//
//        sr.begin(ShapeRenderer.ShapeType.Line);
//        sr.setColor(Color.GRAY);
//
//        sr.line(0,0, circle.x, circle.y);
//
//        sr.circle(circle.x, circle.y, R);
//        sr.end();

        // light
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.RED);

        for (int i = 0; i < circles.size-1; i++) {
            circle = circles.get(i);
            Vector3 other = circles.get(i + 1);

//            sr.circle(circle.x + MathUtils.cos(circle.z) * R,
//                    circle.y + MathUtils.sin(circle.z) * R, 2);
            sr.line(circle.x + MathUtils.cos(circle.z) * R,
                    circle.y + MathUtils.sin(circle.z) * R,
                    other.x + MathUtils.cos(other.z) * R,
                    other.y + MathUtils.sin(other.z) * R);

        }
        sr.end();

    }

    public void parametricArt(float delta){

        sr.begin(ShapeRenderer.ShapeType.Line);
        for (int i = 0; i < TOTAL_LINES-1; i++) {
            float percentage = ((i*1f)/TOTAL_LINES);
            sr.setColor(Helper.HSV_to_RGB(percentage*255,255,255));
            //sr.line(x1(t + i), y1(t + i), x2(t + i), y2(t + i));
            //sr.circle(x1(t + i), y1(t + i), 3);
            //sr.circle(x2(t + i), y2(t + i), 2);
//            sr.setColor(Color.RED);
//            sr.line(x1(t - i), y1(t - i), x2(t - i), y2(t - i));

            // spirograph
            //sr.circle(x1(t + i), y1(t + i), 3);
            sr.line(x1(t + i*delta*2), y1(t + i*delta*2), x1(t + (i+1)*delta*2), y1(t + (i+1)*delta*2));
        }

        sr.end();
//        sr.begin(ShapeRenderer.ShapeType.Line);
//        sr.rect(0,0,10,10);
//        sr.end();

        t += .05f;
    }

    public float x1(float t) {
        //return MathUtils.sin(t/10) * 100 + MathUtils.sin(t/5) * 20;
        //return MathUtils.sin(t) * MathUtils.E * 10;
        return (W + r) * MathUtils.cos(t) - (r + o) * MathUtils.cos(((W+r)/r)*t);
    }

    public float y1(float t){
        //return MathUtils.cos(t/10) * 100;
        //return MathUtils.cos(t/10) * 100;
        return (W + r) * MathUtils.sin(t) - (r + o) * MathUtils.sin(((W+r)/r)*t);
    }

    public float x2(float t){
        return MathUtils.sin(t/10) * 200 + MathUtils.sin(t) * 2;
    }


    public float y2(float t){
        return MathUtils.sin(t/20) * 200 + MathUtils.sin(t/12) * 20;
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
