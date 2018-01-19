package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Helper;

/**
 * Created by alberto on 5/26/2016.
 */
public class SimpleHarmonicMotion {

    Vector2 position;
    float amplitude = 200;
    float angle = 0;

    public SimpleHarmonicMotion(int x, int y){
        position = new Vector2(x,y);
    }

    public void draw(ShapeRenderer sr){
        angle += .05f;
        float x = amplitude * MathUtils.sin(angle);

        sr.line(Helper.WORLD_WIDTH/2,Helper.WORLD_HEIGHT/2, position.x +x, Helper.WORLD_HEIGHT/2);
        sr.circle(Helper.WORLD_WIDTH/2 + x ,Helper.WORLD_HEIGHT/2 ,10);
    }
}
