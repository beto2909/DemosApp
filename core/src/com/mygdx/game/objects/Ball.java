package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Helper;

/**
 * Created by alberto on 7/3/2016.
 */
public class Ball{

    private static final int RADIUS = 10;
    private static final float RESTITUTION = .99f;

    private float r;
    private Vector2 location, velocity;
    private Vector2 acceleration;
    private float maxSpeed;
    private float mass;

    private float deltaTime;

    public Ball(float x, float y){
        location = new Vector2(x,y);
        acceleration = new Vector2();
        velocity = new Vector2();
        r = RADIUS;
        maxSpeed = 10;
        mass = 1;
    }

    public void update(){
        deltaTime = Helper.getDeltaTime();

        velocity.add(acceleration);
        velocity.limit(maxSpeed);
        location.add(velocity);
        acceleration.scl(0);
        velocity.scl(RESTITUTION);
        if (velocity.len2() <= .001) { // resolve infinite restitution
            velocity.scl(0);
        }

        borders();
    }

    public void resolveCollision(Ball b){
        Vector2 delta = location.cpy().sub(b.location);
        float d = delta.len();
        Vector2 mtd = delta.scl( ((r*2)-d)/d);


        // resolve intersection --
        // inverse mass quantities
        float im1 = 1 / mass;
        float im2 = im1;

        // push-pull them apart based off their mass
        location = location.cpy().add(mtd.scl(im1 / (im1 + im2)));
        b.location = b.location.cpy().sub(mtd.scl(im2 / (im1 + im2)));

        // impact speed
        Vector2 v = (this.velocity.cpy().sub(b.velocity));
        float vn = v.dot(mtd.nor());

        // sphere intersecting but moving away from each other already
        if (vn > 0.0f) return;

        // collision impulse
        float i = (-(1.0f + RESTITUTION) * vn) / (im1 + im2);
        Vector2 impulse = mtd.scl(i);

        // change in momentum
        this.velocity = this.velocity.add(impulse.scl(im1));
        b.velocity = b.velocity.sub(impulse.scl(im2));

    }

    public boolean collided(Ball b){
        float distance = b.location.dst(location);
        if (distance < r*2){
            return true;
        }

        return false;
    }

    public void applyForce(Vector2 force){
        acceleration.add(force);
    }

    public void draw(ShapeRenderer sr){
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.circle(location.x, location.y, r);
        sr.end();
    }

    public void borders() {
        if (location.x < -r) velocity.x *= -1;
        if (location.y < -r) velocity.y *= -1;
        if (location.x > Helper.WORLD_WIDTH+r) velocity.x *= -1;
        if (location.y > Helper.WORLD_HEIGHT+r) velocity.y *= -1;
    }

    public boolean isMoving() {
        return velocity.len2() != 0;
    }

    public Vector2 getPosition() {
        return location;
    }

    public float getRadius() {
        return r;
    }
}
