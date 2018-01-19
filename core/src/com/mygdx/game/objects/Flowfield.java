package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Helper;

/**
 * Created by alberto on 5/28/2016.
 */
public class Flowfield {

    private Vector2[][] field;
    private int cols, rows;
    private int resolution;
    private float[][] noise;

    public Flowfield(int r){
        resolution = r;
        cols = Helper.WORLD_WIDTH/resolution;
        rows = Helper.WORLD_HEIGHT/resolution;
        field = new Vector2[cols][rows];
        noise = PerlinNoiseGenerator.generatePerlinNoise(cols, rows, 5);

        init();
    }

    private void init(){
        float xOff = 0;
        for (int i = 0; i < cols; i++) {
            float yOff = 0;
            for (int j = 0; j < rows; j++) {
                //float theta = MathUtils.random(MathUtils.PI*2);
                //float theta = noise[i][j] * MathUtils.PI*2;
                float theta = (float) SNoise.noise(xOff,yOff) * MathUtils.PI;
                field[i][j] = new Vector2(MathUtils.cos(theta),
                        MathUtils.sin(theta));
                yOff += 0.1;
            }
            xOff += 0.1;
        }
    }

    public Vector2 lookup(Vector2 loc){
        int column = (int) MathUtils.clamp(loc.x/resolution, 0, cols -1 );
        int row = (int) MathUtils.clamp(loc.y/resolution, 0, rows - 1);
        return field[column][row].cpy();
    }
    // Draw every vector
    public void draw(ShapeRenderer sr){
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                drawVector(field[i][j],i*resolution,j*resolution,resolution-2, sr);
            }
        }
    }

    // Renders a vector object 'v' as an arrow and a location 'x,y'
    private void drawVector(Vector2 v, float x, float y, float scayl, ShapeRenderer sr) {
        float arrowsize = 4;
        sr.begin(ShapeRenderer.ShapeType.Line);
        // Translate to location to render vector
        sr.setColor(0,0,0,.5f);
        sr.identity();
        sr.translate(x,y,0);
        // Call vector heading function to get direction (note that pointing to the right is a heading of 0) and rotate
        sr.rotate(0,0,1,v.angle());
        // Calculate length of vector & scale it to be bigger or smaller if necessary
        float len = scayl;
        // Draw three lines to make an arrow (draw pointing up since we've rotate to the proper direction)
        sr.line(0,0,len,0);
        //line(len,0,len-arrowsize,+arrowsize/2);
        //line(len,0,len-arrowsize,-arrowsize/2);
        sr.end();
    }
}
