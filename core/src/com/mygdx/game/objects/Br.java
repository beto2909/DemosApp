package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by alberto on 8/31/2016.
 */
public class Br {

    private Vector2 begin, end, position;
    private boolean grown;

    public Br(Vector2 begin, Vector2 end){
        this.begin = begin;
        this.end = end;
        grown = false;
    }

    public void draw(ShapeRenderer sr){
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.WHITE);
        sr.line(begin.x, begin.y, end.x, end.y);
        sr.end();
    }

    public Br branchRight(){
        Vector2 dir = end.cpy().sub(begin);
        dir.rotate(45);
        dir.scl(.67f);
        Vector2 newEnd = end.cpy().add(dir);
        return new Br(end.cpy(), newEnd);
    }

    public Br branchLeft(){
        Vector2 dir = end.cpy().sub(begin);
        dir.rotate(-45);
        dir.scl(.67f);
        Vector2 newEnd = end.cpy().add(dir);
        return new Br(end.cpy(), newEnd);
    }

    public boolean isGrown() {
        return grown;
    }

    public void setGrown(boolean grown) {
        this.grown = grown;
    }
}
