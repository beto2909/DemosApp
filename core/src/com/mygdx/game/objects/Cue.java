package com.mygdx.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Helper;

/**
 * Created by alberto on 7/3/2016.
 */
public class Cue {

    private static final int LENGTH = 250;
    private static final int THICKNESS = 10;
    private static final float LIMIT = 150;

    Vector2 pos, hitForce;
    float length, thickness, radius, angle;
    boolean hitting;

    public Cue(float x, float y, float r){
        length = LENGTH;
        thickness = THICKNESS;
        radius = r;
        angle = MathUtils.random(360);
        pos = new Vector2(x,y);
        hitForce = getHitForce();
        hitting = false;
        Helper.print("angle = " + angle);

    }

    public void update(Ball cueBall, Viewport viewport){
        // adjust angle of cue
        if (!cueBall.isMoving() && Gdx.input.isTouched()) {
            Vector2 mouse = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            angle = MathUtils.radiansToDegrees * MathUtils.atan2(mouse.y - pos.y, mouse.x - pos.x);
        }

        // handle hit force and key released
        if (!cueBall.isMoving() && Gdx.input.isKeyPressed(Input.Keys.H)) {
            if(hitForce.len2() < LIMIT) {
                hitForce.scl(1.11f);
                Helper.print(hitForce.len2() + "");
            }

        } else if (!cueBall.isMoving() && hitting) {
            cueBall.applyForce(hitForce);
            hitting = false;
            hitForce.scl(0);
        }

        if ( Gdx.input.isKeyJustPressed(Input.Keys.H)) {
            hitForce = getHitForce();
            hitForce.nor();
            hitting = true;
        }

    }

    public void draw(ShapeRenderer sr, Ball cueBall){
        //update();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.GREEN);

        if (!cueBall.isMoving()) {
            sr.line(getTipX(), getTipY(), getEndX(), getEndY());
            sr.circle(pos.x, pos.y, 2);

        } else {
            pos.x = cueBall.getPosition().x;
            pos.y = cueBall.getPosition().y;
        }

        sr.end();
    }

    private Vector2 getHitForce() {
        return new Vector2(MathUtils.cosDeg(angle - 180), MathUtils.sinDeg(angle - 180));
    }

    private float getTipX() {
        return pos.x + MathUtils.cosDeg(angle) * (radius * 2 + hitForce.len2()/2);
    }

    private float getTipY() {
        return pos.y + MathUtils.sinDeg(angle) * (radius * 2 + hitForce.len2()/2);
    }

    private float getEndX() {
        return pos.x + MathUtils.cosDeg(angle) * (LENGTH + hitForce.len2()/2);
    }

    private float getEndY() {
        return pos.y + MathUtils.sinDeg(angle) * (LENGTH + hitForce.len2()/2);
    }
}
