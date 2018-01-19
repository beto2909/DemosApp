package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Helper;

import java.util.ArrayList;

/**
 * Created by alberto on 5/28/2016.
 */
public class Vehicle {

    public Vector2 location, start;
    private Vector2 acceleration, velocity;
    private float r;
    private float maxSpeed;
    private float maxForce;
    private boolean loopComplete;

    public Vehicle(float x, float y){
        acceleration = new Vector2();
        velocity = new Vector2(MathUtils.random(-3,3),
                                MathUtils.random(-3,3));
        location = new Vector2(x,y);
        r = 4;
        maxSpeed = 4;
        maxForce = 0.1f;
    }

    public void applyBehaviors(ArrayList<Vehicle> vehicles, Vector2 mouse){
        Vector2 separateForce = separate(vehicles);
        Vector2 seekForce = seek(mouse);
        separateForce.scl(2);
        seekForce.scl(1);
        applyForce(separateForce);
        applyForce(seekForce);
    }

    public void applyBehaviorsDemo(ArrayList<Vehicle> vehicles, float[] weights){
        Vector2 separateForce = separate(vehicles);
        Vector2 alignmentForce = align(vehicles);
        Vector2 cohesion = cohesion(vehicles);
        separateForce.scl(weights[0]);
        alignmentForce.scl(weights[1]);
        cohesion.scl(weights[2]);
        applyForce(separateForce);
        applyForce(alignmentForce);
        applyForce(cohesion);
    }

    public void flock(ArrayList<Vehicle> vehicles){
        Vector2 separateForce = separate(vehicles);
        Vector2 align = align(vehicles);
        Vector2 coh = cohesion(vehicles);
        separateForce.scl(1.5f);
        align.scl(1);
        coh.scl(1);
        applyForce(separateForce);
        applyForce(align);
        applyForce(coh);
    }

    public Vector2 cohesion (ArrayList<Vehicle> boids) {
        float neighbordist = 50;
        Vector2 sum = new Vector2(0,0);   // Start with empty vector to accumulate all locations
        int count = 0;
        for (Vehicle other : boids) {
            float d = location.dst(other.location);
            if ((d > 0) && (d < neighbordist)) {
                sum.add(other.location); // Add location
                count++;
            }
        }
        if (count > 0) {
            sum.x /= count;
            sum.y /= count;
            return seek(sum);  // Steer towards the location
        } else {
            return new Vector2(0,0);
        }
    }

    public  Vector2 separate(ArrayList<Vehicle> vehicles){
        float safeDistance = r*5;
        Vector2 sum = new Vector2(0,0);
        Vector2 steer = new Vector2();
        int count = 0;
        for(Vehicle other: vehicles ){
            float d = location.dst(other.location);
            if (d > 0 && d < safeDistance){
                Vector2 diff = location.cpy().sub(other.location);
                diff.nor();
                diff.x /= d;
                diff.y /= d;
                sum.add(diff);
                count++;
            }
        }
        if (count > 0){
            sum.x /= count;
            sum.y /= count;
            sum.nor();
            sum.scl(maxSpeed);
            steer = sum.cpy().sub(velocity);
            steer.limit(maxForce);
        }

        return steer;
    }

    public Vector2 align(ArrayList<Vehicle> boids){
        float neighborDistance = 50;
        Vector2 sum = new Vector2(0,0);
        Vector2 steer = new Vector2();
        int count = 0;
        for(Vehicle other: boids ){
            float d = location.dst(other.location);
            if (d > 0 && d < neighborDistance){
                sum.add(other.velocity);
                count++;
            }
        }
        if (count > 0){
            sum.x /= count;
            sum.y /= count;
            sum.nor();
            sum.scl(maxSpeed);
            steer = sum.cpy().sub(velocity);
            steer.limit(maxForce);
        }

        return steer;
    }

    public void follow(Path p){

        // Predict location 50 (arbitrary choice) frames ahead
        // This could be based on speed
        Vector2 predict = velocity.cpy();
        predict.nor();
        predict.scl(100);
        Vector2 predictLoc = location.cpy().add(predict);

        // Now we must find the normal to the path from the predicted location
        // We look at the normal for each line segment and pick out the closest one

        Vector2 normal = null;
        Vector2 target = null;
        float worldRecord = 1000000;  // Start with a very high record distance that can easily be beaten

        // Loop through all points of the path
        for (int i = 0; i < p.points.size()-1; i++) {

            // Look at a line segment
            Vector2 a = p.points.get(i).cpy();
            Vector2 b = p.points.get(i+1).cpy();

            // Get the normal point to that line
            Vector2 normalPoint = getNormalPoint(predictLoc, a, b);
            // This only works because we know our path goes from left to right
            // We could have a more sophisticated test to tell if the point is in the line segment or not
            if (normalPoint.x < a.x || normalPoint.x > b.x) {
                // This is something of a hacky solution, but if it's not within the line segment
                // consider the normal to just be the end of the line segment (point b)
                normalPoint = b;
            }

            // How far away are we from the path?
            float distance = predictLoc.dst( normalPoint);
            // Did we beat the record and find the closest line segment?
            if (distance < worldRecord) {
                worldRecord = distance;
                // If so the target we want to steer towards is the normal
                //normal = normalPoint;

                // Look at the direction of the line segment so we can seek a little bit ahead of the normal
                Vector2 dir = b.cpy().sub(a);
                dir.nor();
                // This is an oversimplification
                // Should be based on distance to path & velocity
                dir.scl(10);
                target = normalPoint;
                target.add(dir);
            }
        }

        // Only if the distance is greater than the path's radius do we bother to steer
        if (worldRecord > p.radius) {
            applyForce(seek(target));
        }
    }

    public void follow(Flowfield flow){
        Vector2 desired = flow.lookup(location.cpy());
        desired.scl(maxSpeed);

        Vector2 steer = desired.cpy().sub(velocity);
        steer.limit(maxForce);

        applyForce(steer);
    }

    public Vector2 seek(Vector2 target){
        Vector2 desired = target.cpy().sub(location);

        if (desired.len() == 0 ) return new Vector2();
        desired.nor();
        desired.scl(maxSpeed);

        Vector2 steer = desired.cpy().sub(velocity);
        steer.limit(maxForce);

        return steer;
    }

    public void arrive(Vector2 target){
        Vector2 desired = Helper.subVec(target, location);
        float d = desired.len();
        desired.nor();

        if(d < 100){
            float m = Helper.map(d, 0, 100, 0, maxSpeed);
            desired.scl(m);
        }else {
            desired.scl(maxSpeed);
        }

        Vector2 steer = Helper.subVec(desired, velocity);
        steer.limit(maxForce);

        applyForce(steer);
    }

    public void update(){
        velocity.add(acceleration);
        velocity.limit(maxSpeed);
        location.add(velocity);

        acceleration.scl(0);
    }

    private Vector2 getNormalPoint(Vector2 p, Vector2 a, Vector2 b) {
        // Vector from a to p
        Vector2 ap = p.cpy().sub(a);
        // Vector from a to b
        Vector2 ab = b.cpy().sub(a);
        ab.nor(); // Normalize the line
        // Project vector "diff" onto line by using the dot product
        ab.scl(ap.dot(ab));
        Vector2 normalPoint = a.cpy().add(ab);
        //Helper.print(normalPoint.toString());
        return normalPoint;
    }

    public void applyForce(Vector2 force){
        acceleration.add(force);
    }

    public void bordersCustom(Path p){

        if (location.dst(p.getEnd()) < r*4){
            loopComplete = true;
        }
        if (loopComplete){
            start = seek(p.getStart());
            start.scl(2);
            applyForce(start);
            if (location.dst(p.getStart()) < r) loopComplete = false;
        }
    }

    public void borders(Path p) {
        if (location.x > p.getEnd().x + r) {
            location.x = p.getStart().x - r;
            location.y = p.getStart().y + (location.y-p.getEnd().y);
        }
    }

    public void borders() {
        if (location.x < -r) location.x = Helper.WORLD_WIDTH+r;
        if (location.y < -r) location.y = Helper.WORLD_HEIGHT+r;
        if (location.x > Helper.WORLD_WIDTH+r) location.x = -r;
        if (location.y > Helper.WORLD_HEIGHT+r) location.y = -r;

    }

    public void draw(ShapeRenderer sr){

        update();

        float theta = velocity.angle() + 90;
        sr.setColor(Color.BLACK);
        sr.identity();
        sr.translate(location.x, location.y, 0);
        sr.rotate(0,0,1,theta);
        sr.triangle(0 , -r*2,
                    -r, r*2,
                    r, r*2);
        //sr.rect(location.x, location.y, r, r);
    }
}
