package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.Helper;

/**
 * Created by alberto on 8/12/2016.
 */
public class Pixel extends Color{

    public Pixel(){
        super();
    }

    public Pixel(float r, float g, float b){
        super(r,g,b,1);
    }

    public Pixel(float c, boolean hsv){
        super();
        if (hsv){
            Color col = Helper.HSV_to_RGB(c*255, 255, 255);
            this.r = col.r;
            this.g = col.g;
            this.b = col.b;
        } else {
            this.r = c;
            this.g = c;
            this.b = c;
        }
    }

    public Color getColor(){
        return this;
    }





}
