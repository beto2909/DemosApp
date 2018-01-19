package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Helper;

/**
 * Created by alberto on 5/26/2016.
 */
public class Pendulum {

    private static final float G = .9f;
    private Vector2 origin;
    private Vector2 bob;
    private float len;
    private float angle;
    private float aVel;
    private float aAcc;


    public Pendulum(int len){
        origin = new Vector2(Helper.WORLD_WIDTH/2,Helper.WORLD_HEIGHT);
        this.len = len;
        bob = new Vector2(Helper.WORLD_WIDTH/2, len);
        angle = MathUtils.PI/4 +.5f;
        aVel = 0;
        aAcc = 0;
    }

    public void update(){
        bob.x = origin.x + len * MathUtils.sin(angle);
        bob.y = origin.y - len * MathUtils.cos(angle);

        aAcc = (-G/len) * MathUtils.sin(angle);
        angle += aVel;
        aVel += aAcc;

        aVel *= .99f;
    }

    public void draw(ShapeRenderer sr){

        update();

        sr.line(origin.x, origin.y, bob.x, bob.y );
        sr.circle(bob.x, bob.y, 20);
    }
}
