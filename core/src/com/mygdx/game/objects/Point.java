package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Helper;

/**
 * Created by alberto on 7/3/2016.
 */
public class Point {
    private static final int RADIUS = 2;

    private float r;
    public Vector2 position, velocity;
    private Vector2 acceleration;
    private float maxSpeed;

    private float deltaTime;

    public Point(float x, float y){
        position = new Vector2(x,y);
        acceleration = new Vector2();
        velocity = new Vector2();
        r = RADIUS;
        maxSpeed = 5;
    }

    public void update(){
        deltaTime = Helper.getDeltaTime();

        velocity.add(acceleration);
        velocity.limit(maxSpeed);
        position.add(velocity);
        acceleration.scl(0);

        borders();
    }

    public void applyForce(Vector2 force){
        acceleration.add(force);
    }

    public void draw(ShapeRenderer sr){
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.BLACK);
        sr.identity();
        sr.circle(position.x, position.y, r);
        sr.end();
    }

    public void borders() {
        if (position.x < -r) position.x = Helper.WORLD_WIDTH + r;
        if (position.y < -r) position.y = Helper.WORLD_HEIGHT + r;
        if (position.x > Helper.WORLD_WIDTH + r) position.x = -r;
        if (position.y > Helper.WORLD_HEIGHT + r) position.y = - r;

    }
}
