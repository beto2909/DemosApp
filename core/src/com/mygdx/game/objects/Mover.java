package com.mygdx.game.objects;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Helper;

/**
 * Created by alberto on 5/23/2016.
 */
public class Mover{

    public Vector2 location;
    private Vector2 acceleration, velocity;

    private float deltaTime;
    //angular movement
    private float a = 0;
    private float aVelocity = 0;
    private float aAcceleration = 0.001f;

    public Mover(float x, float y){
        location = new Vector2(x,y);
        acceleration = new Vector2();
        velocity = new Vector2();
    }

    public void update(){
        deltaTime = Helper.getDeltaTime();

        a += aVelocity;
        aVelocity += aAcceleration;

        velocity.add(acceleration);
        location.add(velocity);
        //rotate(a);
        acceleration.scl(0);
    }

    public void applyForce(Vector2 force){
        acceleration.add(force);
    }

    public float getAngle() {
        return a;
    }

    public void setAngle(float a) {
        this.a = a;
    }

    public float getaVelocity() {
        return aVelocity;
    }

    public void setaVelocity(float aVelocity) {
        this.aVelocity = aVelocity;
    }

    public float getaAcceleration() {
        return aAcceleration;
    }

    public void setaAcceleration(float aAcceleration) {
        this.aAcceleration = aAcceleration;
    }
}
