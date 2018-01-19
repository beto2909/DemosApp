package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by alberto on 5/28/2016.
 */
public class Path{
    // A Path is an arraylist of points (PVector objects)
    ArrayList<Vector2> points;
    // A path has a radius, i.e how far is it ok for the boid to wander off
    float radius;

    public Path() {
        // Arbitrary radius of 20
        radius = 20;
        points = new ArrayList<Vector2>();
    }

    // Add a point to the path
    public void addPoint(float x, float y) {
        Vector2 point = new Vector2(x, y);
        points.add(point);
    }

    public Vector2 getStart() {
        return points.get(0);
    }

    public Vector2 getEnd() {
        return points.get(points.size()-1);
    }

    public void draw(ShapeRenderer sr){
        sr.setColor(Color.BLACK);

        for (int i = 0; i < points.size()-1; i++) {
            sr.identity();
            sr.line(points.get(i), points.get(i + 1));
        }

    }
}
