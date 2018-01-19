package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets;
import com.mygdx.game.Helper;

/**
 * Created by alberto on 4/25/2016.
 */
public class Particle extends Sprite{

    private static final int VELOCITY = 20;
    private static final float GRAVITY = 0.9f; // negative : down , positive : up
    private static final float LIFESPAN_DECREMENT = .005f;

    private Vector2 acceleration, velocity;
    private float lifespan;
    private float deltaTime;

    public Particle(float x, float y, ParticleType type){
        super(Assets.manager.get(Assets.particle));
        switch (type){
            case FIRE_PARTICLE:
                this.setTexture(Assets.manager.get(Assets.fire_particle));
                break;
            case WHITE_PARTICLE:
                this.setTexture(Assets.manager.get(Assets.particle));
                break;
        }
        setPosition(x - getWidth()/2, y - getHeight()/2);
        acceleration = new Vector2(0, GRAVITY);
        velocity = new Vector2(MathUtils.random(-VELOCITY,VELOCITY), MathUtils.random(-20,0));
        lifespan = 1;
    }

    public Particle(Particle.ParticleType type){
        super(Assets.manager.get(Assets.particle));
        switch (type){
            case FIRE_PARTICLE:
                this.setTexture(Assets.manager.get(Assets.fire_particle));
                break;
            case WHITE_PARTICLE:
                this.setTexture(Assets.manager.get(Assets.particle));
                break;
        }
        setPosition(Helper.WORLD_WIDTH/2 - getWidth()/2,
                    Helper.WORLD_HEIGHT/2 - getHeight()/2);
        acceleration = new Vector2(0, GRAVITY);
        velocity = new Vector2(MathUtils.random(-VELOCITY,VELOCITY), MathUtils.random(-20,0));
        lifespan = 1;
    }

    public void update(){
        deltaTime = Helper.getDeltaTime();

        velocity.add(acceleration);
        translate(velocity.x *deltaTime ,
                velocity.y * deltaTime);
//        translateX(velocity.x);
//        translateY(velocity.y);
        lifespan -= LIFESPAN_DECREMENT;
        setAlpha(lifespan);
    }

    public void applyForce(Vector2 force){
        acceleration.add(force);
    }

    public boolean isDead(){
        if (lifespan > 0) return false;
        else return true;
    }

    public enum ParticleType{
        WHITE_PARTICLE,
        FIRE_PARTICLE
    }

}
