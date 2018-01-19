package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Helper;

import java.util.ArrayList;

/**
 * Created by alberto on 4/25/2016.
 */
public class ParticleSystem {

    private static final int INITIAL_PARTICLES = 20;

    private ArrayList<Particle> particles;
    private Vector2 pos;
    int max;
    private Particle.ParticleType type;

    public ParticleSystem(){
        this.type = Particle.ParticleType.WHITE_PARTICLE;
        particles = new ArrayList<Particle>();
        for (int i = 0; i < INITIAL_PARTICLES; i++) {
            particles.add(new Particle(type));
        }
        max = 0;
        pos = null;
    }

    public ParticleSystem(Particle.ParticleType type ){
        this.type = type;
        particles = new ArrayList<Particle>();
        for (int i = 0; i < INITIAL_PARTICLES; i++) {
            particles.add(new Particle(type));
        }
        max = 0;
        pos = null;
    }

    public ParticleSystem(float x, float y, Particle.ParticleType type){
        this.type = type;
        particles = new ArrayList<Particle>();
        for (int i = 0; i < INITIAL_PARTICLES; i++) {
            particles.add(new Particle(x,y,type));
        }
        pos = new Vector2(x,y);
        max = 0;
    }

    public void update(){
        addParticle();

        if (particles.size() > max){
            max = particles.size();
            Helper.print("" + max);
        }
        for(Particle p: particles){
            p.update();
        }
        Particle tmp;
        // reverse loop to not skip particles
        for (int i = particles.size()-1; i >= 0; i--) {
            tmp = particles.get(i);
            if (tmp.isDead()){
                particles.remove(i);
            }
        }
    }

    public void applyForce(Vector2 force){
        for (Particle p: particles){
            p.applyForce(force);
        }
    }

    public void addParticle(){
        if (pos == null)
            particles.add(new Particle(type));
        else
            particles.add(new Particle(pos.x, pos.y, type));
    }

    public void draw(SpriteBatch batch){
        for (Particle p: particles){
            p.draw(batch);
        }
    }
}
