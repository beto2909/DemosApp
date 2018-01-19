package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.Helper;


/**
 * Created by alberto on 6/17/2017.
 */
public class Circle {

    public float x, y, bias;
    public int label;

    public Circle() {
        x = MathUtils.random(-1f, 1f);
        y = MathUtils.random(-1f, 1f);
        bias = 1;

        float lineY = Helper.f(x);
        if (y > lineY) {
            label = 1;
        } else {
            label = -1;
        }
    }

    public Circle(float xx, float yy) {
        x = xx;
        y = yy;
    }

    public void draw(ShapeRenderer sr) {
        sr.begin(ShapeRenderer.ShapeType.Filled);
        if (label == 1) {
            sr.setColor(1,1,1,1);
        } else {
            sr.setColor(.2f,.2f,.2f,1);
        }

        sr.circle(pixelX(), pixelY(), 5);

        sr.end();
    }

    public float pixelX() {
        return Helper.map(x, -1, 1, 0, Helper.WORLD_HEIGHT);
    }

    public float pixelY() {
        return  Helper.map(y, -1, 1, 0, Helper.WORLD_HEIGHT);
    }

}

