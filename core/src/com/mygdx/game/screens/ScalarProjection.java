package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Helper;

/**
 * Created by alberto on 5/29/2016.
 */
public class ScalarProjection extends AbstractReturnScreen {

    ShapeRenderer sr;
    Vector2 a, b, mouse;


    public ScalarProjection(Game game, MainMenu mainMenu){
        super(game, mainMenu);
    }

    @Override
    public void show() {
        super.show();
        sr = new ShapeRenderer();
        a = new Vector2(20,300);
        b = new Vector2(500,250);
        mouse = new Vector2();

        //Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, .49f, .56f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        Vector2 click = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        mouse = viewport.unproject(click);

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setProjectionMatrix(camera.combined);
        sr.setColor(Color.BLACK);
        sr.line(a.x,a.y,b.x,b.y);
        sr.line(a.x,a.y,mouse.x,mouse.y);
        sr.end();

        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.ellipse(a.x,a.y,8,8);
        sr.ellipse(b.x,b.y,8,8);
        sr.ellipse(mouse.x,mouse.y,8,8);
        sr.end();

        Vector2 norm = scalarProjection(mouse,a,b);

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.line(mouse.x,mouse.y,norm.x,norm.y);
        sr.end();

        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.RED);
        sr.ellipse(norm.x,norm.y,16,16);
        sr.end();

        super.drawStage();
    }

    Vector2 scalarProjection(Vector2 p, Vector2 a, Vector2 b) {
        Vector2 ap = p.cpy().sub(a);
        Vector2 ab = b.cpy().sub(a);
        ab.nor(); // Normalize the line
        ab.scl(ap.dot(ab));
        Vector2 normalPoint = a.cpy().add(ab);
        return normalPoint;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        Helper.resizeScreen(viewport,camera,width,height);
    }

    @Override
    public void dispose() {
        super.dispose();
        sr.dispose();
    }
}
