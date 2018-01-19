package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Helper;

/**
 * Created by alberto on 8/14/2016.
 */
public class SolarSystem {

    private static final int DISTANCE = 0;
    private static final int DIAMETER = 1;
    private static final int PERIOD = 2;

    private Planet[] planets;
    private float[][] data = {{0, 30, 0},
                    {0.39f,     4878f,      87.96f/365.26f},
                    {0.723f,    12104f,     224.68f/365.26f},
                    {1f,        12756f,     1f},
                    {1.524f,    6787f,      686.98f/365.26f},
                    {5.203f,    142796f,    11.862f},
                    {9.539f,    120660f,    29.456f},
                    {19.18f,    51118f,     84.07f},
                    {30.06f,    48600f,     164.81f},
                    {39.53f,    2274f,      247.7f}};

    public SolarSystem(){
        planets = new Planet[10];
        planets[0] = new Planet(0, 20, 0);
        for (int i = 1; i < planets.length; i++) {
            float d, r, p;
            d = Helper.map(data[i][DISTANCE], 0, 40, 0, 500);
            r = Helper.map(data[i][DIAMETER], 0, 142000, 10, 50)/2;
            p = data[i][PERIOD];
            planets[i] = new Planet(25 + d, r, p);
        }
    }

    public void draw(ShapeRenderer sr){
        for (Planet p : planets){
            p.update();
            //p.draw(sr);
        }
    }

    public Planet[] getPlanets() {
        return planets;
    }
}
