package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Helper;

/**
 * Created by alberto on 8/31/2016.
 */
public class Leaf {

    private static final int R = 2;

    public Vector2 position;
    public boolean reached;

    public Leaf(){
        position = new Vector2(MathUtils.random(Helper.WORLD_WIDTH),
                MathUtils.random(Helper.WORLD_HEIGHT - 100) + 100);
        reached = false;
    }

    public void show(ShapeRenderer sr){
        if (reached) {
            sr.setColor(Color.GREEN);
            sr.circle(position.x, position.y, R);
        }
    }

    public void setReached(boolean r){
        reached = r;
    }
}
