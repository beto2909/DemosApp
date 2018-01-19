package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Helper;

/**
 * Created by alberto on 8/31/2016.
 */
public class Tree {

    private static final int INITTIAL_LEAVES = 1500;
    private static final int MAX_DIST = 100;
    private static final int MIN_DIST = 15;

    private Array<Leaf> leaves;
    private Branch root;
    private Array<Branch> branches;


    public Tree(){
        leaves = new Array<Leaf>();
        for (int i = 0; i < INITTIAL_LEAVES; i++) {
            leaves.add(new Leaf());
        }

        // create root
        Vector2 rootPos = new Vector2(Helper.WORLD_WIDTH/2, 0);
        Vector2 dir = new Vector2(0, 1);
        root = new Branch(null, rootPos, dir);

        branches = new Array<Branch>();
        branches.add(root);

        Branch current = root;
        boolean found = false;

        //make trunk
        while (!found){
            for (int i = 0; i < leaves.size; i++) {
                float d = current.position.dst(leaves.get(i).position);
                if (d < MAX_DIST){
                    found = true;
                }
            }
            if (!found){ //create new branch
                Branch next = current.next();
                current = next;
                branches.add(next);
            }
        }

    }

    public void grow(){
        for (int i = 0; i < leaves.size; i++) {
            Leaf leaf = leaves.get(i);

            Branch closestBranch = null;
            float record = 10000000;
            for (int j = 0; j < branches.size; j++) {
                Branch b = branches.get(j);
                float d = leaf.position.dst(b.position);
                if (d < MIN_DIST){ // leaf has been reached
                    leaf.setReached(true);
                    closestBranch = null;
                    break;
                } else if(d > MAX_DIST){

                } else if(closestBranch == null || d < record){
                    closestBranch = b;
                    record = d;
                }
            }

            if (closestBranch != null){
                Vector2 dir = leaf.position.cpy().sub(closestBranch.position);
                dir.nor();
                closestBranch.direction.add(dir);
                closestBranch.count++;
            }
        }

        for (int i = leaves.size -1; i >= 0 ; i--) {
            if (leaves.get(i).reached){
                //leaves.removeIndex(i);
            }
        }

        for (int i = branches.size -1; i >= 0; i--) {
            Branch b = branches.get(i);
            if (b.count > 0){
                b.direction.x /= b.count +1;
                b.direction.y /= b.count +1;
//                Vector2 newPos = b.position.cpy().add(b.direction);
//                Branch newBranch = new Branch(b, newPos, b.direction.cpy());
//                branches.add(newBranch);
                branches.add(b.next());
            }
            b.reset();
        }
    }

    public void show(ShapeRenderer sr){
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.WHITE);
        for (int i = 0; i < leaves.size; i++) {
            leaves.get(i).show(sr);
        }
        sr.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.WHITE);
        for (int i = 0; i < branches.size; i++) {
            branches.get(i).show(sr);
        }
        sr.end();

    }
}
