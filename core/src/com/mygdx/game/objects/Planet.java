package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Helper;

/**
 * Created by alberto on 8/14/2016.
 */
public class Planet {

    private static final int CENTER_X = Helper.WORLD_WIDTH/2;
    private static final int CENTER_Y = Helper.WORLD_HEIGHT/2;
    public Vector2 pos;
    public float r, d;
    public float period; // in years

    private float deltaTime;
    //angular movement
    private float a = 0;
    private float aVelocity = 0;

    public Planet(float distance, float radius, float p){
        pos = new Vector2();
        r = radius;
        d = distance;
        period = p;
        aVelocity = Helper.map(period, 0, 300, 15, 1);
        a = 0;
        Helper.print(aVelocity + "");
    }

    public void update() {
        deltaTime = Helper.getDeltaTime();
        pos.x = CENTER_X + (d * MathUtils.sin(a * MathUtils.degreesToRadians));
        pos.y = CENTER_Y + (d * MathUtils.cos(a * MathUtils.degreesToRadians));

        a += aVelocity;
    }

    public void draw(ShapeRenderer sr){
        sr.setColor(Color.WHITE);
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.circle(pos.x, pos.y, r);
        sr.end();
    }
}
