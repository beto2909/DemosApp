package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by alberto on 3/21/2016.
 */
public class Helper {

    public static final int WORLD_WIDTH = 1000, WORLD_HEIGHT = 500;

    public static final String MENU_BUT_DOWN = "menu_down";
    public static final String MENU_BUT_UP = "menu_up";
    public static final String RIGHT_SIDE_UP = "right_handed_up";
    public static final String RIGHT_SIDE_DOWN= "right_handed_down";
    public static final String LEFT_SIDE_UP = "left_handed_up";
    public static final String LEFT_SIDE_DOWN = "left_handed_down";


    public static final int CAMERA_SPEED = 10;

    public static float getDeltaTime(){
        float dt = Gdx.graphics.getDeltaTime();
        if (dt > .15f) dt = .15f;
        return dt;
    }

    public static Vector2 subVec(Vector2 a, Vector2 b){
        return new Vector2(a.x - b.x, a.y - b.y);
    }

    public static Vector2 addVec(Vector2 a, Vector2 b){
        return new Vector2(a.x + b.x, a.y + b.y);
    }

    public static float f(float x){
        return 0.89f * x - .1f;
    }

    public static float map(float val, float input_start, float input_end, float output_start, float output_end ){
        float output;
        float slope = (output_end - output_start) / (input_end - input_start);
        output = output_start + slope * (val - input_start);
        return output;
    }

    public static float map(float val, float input_start, float input_end, float range){
        float output;
        float slope = (range) / (input_end - input_start);
        output = slope * (val - input_start);
        return output;
    }

    public enum State
    {
        PAUSE,
        RUN,
        RESUME
    }

    public static void handleCamera(OrthographicCamera camera){
        if (Gdx.input.isKeyPressed(Input.Keys.A)){
            camera.zoom += .1f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)){
            camera.zoom -= .1f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            camera.position.y += CAMERA_SPEED;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            camera.position.y -= CAMERA_SPEED;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            camera.position.x += CAMERA_SPEED;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            camera.position.x -= CAMERA_SPEED;
        }
        camera.update();
    }

    public static void positionCamera(OrthographicCamera camera, int x, int y){
        camera.position.set(x, y, 0);
        camera.update();
    }

    public static void centerCamera(OrthographicCamera camera){
        camera.position.set(0, 0, 0);
        camera.update();
    }

    public static Color HSV_to_RGB (float h, float s, float v) {
        int r, g, b;
        int i;
        float f, p, q, t;
        h = (float)Math.max(0.0, Math.min(360.0, h));
        s = (float)Math.max(0.0, Math.min(100.0, s));
        v = (float)Math.max(0.0, Math.min(100.0, v));
        s /= 100;
        v /= 100;

        h /= 60;
        i = (int)Math.floor(h);
        f = h - i;
        p = v * (1 - s);
        q = v * (1 - s * f);
        t = v * (1 - s * (1 - f));
        switch (i) {
            case 0:
                r = Math.round(255 * v);
                g = Math.round(255 * t);
                b = Math.round(255 * p);
                break;
            case 1:
                r = Math.round(255 * q);
                g = Math.round(255 * v);
                b = Math.round(255 * p);
                break;
            case 2:
                r = Math.round(255 * p);
                g = Math.round(255 * v);
                b = Math.round(255 * t);
                break;
            case 3:
                r = Math.round(255 * p);
                g = Math.round(255 * q);
                b = Math.round(255 * v);
                break;
            case 4:
                r = Math.round(255 * t);
                g = Math.round(255 * p);
                b = Math.round(255 * v);
                break;
            default:
                r = Math.round(255 * v);
                g = Math.round(255 * p);
                b = Math.round(255 * q);
        }

        return new Color(r / 255.0f, g / 255.0f, b / 255.0f, 1);
    }

    public static OrthographicCamera createCamera(){
        return new OrthographicCamera();
    }

    public static FitViewport createViewport(OrthographicCamera camera){
        return new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
    }

    public static void repositionCamera(OrthographicCamera camera){
        camera.position.set(WORLD_WIDTH/2, WORLD_HEIGHT/2, 0);
    }

    public static void resizeScreen(Viewport viewport, OrthographicCamera camera, int width, int height){
        viewport.update(width, height, true);//true to center camera;
    }

    public static void print(String message){
        System.out.println(message);
    }

}
