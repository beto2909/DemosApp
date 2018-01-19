package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Helper;

/**
 * Created by alberto on 8/10/2016.
 */
public class Blob {

    public Vector2 pos, vel;
    public float r;

    public Blob(int x, int y){
        pos = new Vector2(x,y);
        vel = new Vector2(MathUtils.random(), MathUtils.random());
        vel.scl(MathUtils.random(4f,6f));
        r = MathUtils.random(10,150);
    }

    public  void update(){
        pos.add(vel);

        if(pos.x > Helper.WORLD_WIDTH || pos.x < 0){
            vel.x *= -1;
        }
        if(pos.y > Helper.WORLD_HEIGHT || pos.y < 0){
            vel.y *= -1;
        }
    }

    public void show(ShapeRenderer sr){
        sr.setColor(Color.BLACK);
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.circle(pos.x, pos.y, r);
        sr.end();
    }
}
