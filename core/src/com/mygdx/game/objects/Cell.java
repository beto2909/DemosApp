package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets;

/**
 * Created by alberto on 6/11/2016.
 */
public class Cell {
    public static final int DEAD = 0;
    public static final int ALIVE = 1;

    public float a,b;
    private int state;
    private Texture[] textures;
    private Vector2 position;

    public Cell(int a, int b){
        this.a = a;
        this.b = b;
    }

    public  Cell(Vector2 pos){
        position = pos;
        state = DEAD;
        textures = new Texture[2];
        textures[0] = Assets.manager.get(Assets.dead_cell);
        textures[1] = Assets.manager.get(Assets.alive_cell);
    }

    public int getNeighborsSum(Cell[][] cells, int x, int y){
        int sum = 0;
        for (int kx = -1; kx <= 1; kx++) {
            for (int ky = -1; ky <= 1; ky++) {

                sum += cells[x + kx][y + ky].getState();

            }
        }
        sum -= cells[x][y].getState();
        return sum;
    }

    public boolean isAlive(){
        return (state == ALIVE)? true : false;
    }

    public void kill(){
        state = DEAD;
    }

    public void live(){
        state = ALIVE;
    }

    public void born(){
        live();
    }

    public int getState(){
        return state;
    }

    public void draw(SpriteBatch batch){
        batch.draw(textures[state], position.x, position.y, 10,10);
    }
}
