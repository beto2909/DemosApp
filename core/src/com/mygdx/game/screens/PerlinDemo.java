package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Helper;
import com.mygdx.game.objects.Particle;
import com.mygdx.game.objects.ParticleSystem;
import com.mygdx.game.objects.PerlinNoiseGenerator;

/**
 * Created by alberto on 5/7/2016.
 */
public class PerlinDemo extends AbstractReturnScreen {

    private SpriteBatch batch;
    private ShapeRenderer sr;

    private Pixmap pm;
    private Sprite noise;
    private Particle p[];
    private ParticleSystem ps;
    private float[] fireVels;
    private int count;
    private float time;

    public PerlinDemo(Game game, MainMenu mainMenu){
        super(game, mainMenu);
    }

    @Override
    public void show() {
        super.show();
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        sr = new ShapeRenderer();
        sr.setProjectionMatrix(camera.combined);

        pm = PerlinNoiseGenerator.generatePixmap(500, 500, -15, 20, 8);
        noise = new Sprite(new Texture(pm));

        float[][] noise = PerlinNoiseGenerator.generatePerlinNoise(1,50, 5);
        //byte[] n = PerlinNoiseGenerator.generateHeightMap(500,500,-1,1,3);
        Pixmap tmp = new Pixmap(500, 500, Pixmap.Format.RGBA8888);

        p = new Particle[25];
        for (int i = 0; i < p.length; i++) {
            p[i] = new Particle(Particle.ParticleType.WHITE_PARTICLE);
        }

        for (int i = 0; i < p.length; i++) {
            float t = noise[0][i ];
            t = Helper.map(t, 0,1,300);
            p[i].setPosition(i * 20, t);
        }

        ps = new ParticleSystem(200, 200, Particle.ParticleType.FIRE_PARTICLE);
        fireVels = noise[0];

        count = 0;
        time = 0;
        for (int i = 0; i < fireVels.length; i++) {
            fireVels[i] = Helper.map(fireVels[i], 0,1,-.1f, .1f);
        }

        //Gdx.input.setInputProcessor(null);    // clears previous screen buttons
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, .49f , .56f ,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        time += delta;
        //Helper.print(time + " ");

        camera.update();

        ps.update();

        Vector2 fire = new Vector2(fireVels[count], 0);
        ps.applyForce(fire);
        count++;
        if (count > fireVels.length - 1){
            count = 0;
            if (time > 5){
                time = 0;
                fireVels = getNewNoise();
                // reposition particles graph
                for (int i = 0; i < p.length; i++) {
                    float t = fireVels[i];
                    t = Helper.map(t, 0,1,300);
                    p[i].setPosition(i * 20, t);
                }
                //map velocities
                for (int i = 0; i < fireVels.length; i++) {
                    fireVels[i] = Helper.map(fireVels[i], 0,1,-.1f, .1f);
                }
            }
        }

        batch.begin();
        for (Particle tmp: p) {
            //tmp.update();
            tmp.draw(batch);
        }
        ps.draw(batch);
        //noise.draw(batch);
        batch.end();


        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.BLACK);
        sr.rect(10,10,480,480);
        sr.end();

        super.drawStage();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        Helper.resizeScreen(viewport, camera, width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        sr.dispose();
        pm.dispose();
    }

    private float[] getNewNoise(){
        float[][] noise = PerlinNoiseGenerator.generatePerlinNoise(1,50, 5);
        return noise[0];
    }
}
