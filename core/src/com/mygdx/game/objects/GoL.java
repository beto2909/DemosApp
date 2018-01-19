package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by alberto on 6/19/2016.
 */
public class GoL {

    private static final int HAPPY = 3;
    private static final int LONELY = 1;
    private static final int OVERCROWDED= 4;
    private static final int CELL_WIDTH = 10;

    private Cell[][] current, next;

    public GoL(int width, int height){
        current = new Cell[width][height];
        next = new Cell[width][height];
        for (int x = 0; x < current.length; x++) {
            for (int y = 0; y < current[x].length; y++) {
                current[x][y] = new Cell(new Vector2(x * CELL_WIDTH, y * CELL_WIDTH));
                next[x][y] = new Cell(new Vector2(x * CELL_WIDTH, y * CELL_WIDTH));
                float r = MathUtils.random();
                if (r < .05f) current[x][y].live();
                else current[x][y].kill();

            }
        }

    }

    public void fill(Vector2 mouse){
        current[(int)mouse.x][(int)mouse.y].live();
    }

    public void run(){
        Cell cell, nextCell;
        for (int x = 1; x < next.length-1; x++) {
            for (int y = 1; y < next[x].length-1; y++) {
                cell = current[x][y];
                nextCell = next[x][y];
                int state = cell.getNeighborsSum(current, x, y);
                if (cell.isAlive() && state <= LONELY ){
                    nextCell.kill();
                }else if (cell.isAlive() && state >= OVERCROWDED){
                    nextCell.kill();
                } else if(!cell.isAlive() && state == HAPPY){
                    nextCell.born();
                } else {
                    if (cell.isAlive()) nextCell.live();
                    else nextCell.kill();
                }
            }
        }

        current = next;

    }

    public void draw(SpriteBatch batch) {
        for (int x = 0; x < current.length; x++) {
            for (int y = 0; y < current[x].length; y++) {
                current[x][y].draw(batch);
            }
        }
    }

    private void swap(){
        //Cell[][] tmp = next;
        current = next;
       // next = tmp;

    }

}
