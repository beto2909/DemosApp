package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by alberto on 8/28/2016.
 */
public class Branch {

    public Vector2 position, direction, origDir;
    private Branch parent;
    public int count;

    public Branch(Branch p, Vector2 pos, Vector2 dir){
        position = pos;
        parent = p;
        direction = dir;
        origDir = dir.cpy();
        count = 0;
    }

    public void reset(){
        direction = origDir.cpy();
        count = 0;
    }

    public Branch next(){
        Vector2 nextDir = direction.cpy().scl(5);
        Vector2 nextPos = position.cpy().add(nextDir);
        Branch next = new Branch(this, nextPos, direction.cpy());
        return  next;
    }

    public void show(ShapeRenderer sr){
        if (parent != null){
            sr.line(position.x, position.y, parent.position.x, parent.position.y);
        }
    }

}
