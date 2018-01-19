package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Helper;

/**
 * Created by alberto on 5/27/2016.
 */
public class Spring {

    private static final float K = .02f;
    private float restLength;
    private Vector2 origin;
    private Mover bob;

    public Spring(int rL){
        origin = new Vector2(Helper.WORLD_WIDTH/2,Helper.WORLD_HEIGHT);
        restLength = rL;
        bob = new Mover(Helper.WORLD_WIDTH/2,Helper.WORLD_HEIGHT - restLength - 100);
    }

    public void update(){
        Vector2 springForce = Helper.subVec(bob.location, origin);
        float currentLength = springForce.len();
        springForce.nor();
        float k = K;
        float stretch = currentLength - restLength;
        springForce.scl(-k*stretch);

        bob.applyForce(springForce);
        bob.update();
    }

    public void applyForce(Vector2 f){
        bob.applyForce(f);
    }

    public void draw(ShapeRenderer sr){
        update();

        sr.line(origin.x, origin.y, bob.location.x, bob.location.y );
        sr.circle(bob.location.x, bob.location.y, 20);
    }
}
