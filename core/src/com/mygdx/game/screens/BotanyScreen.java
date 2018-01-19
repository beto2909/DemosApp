package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Helper;
import com.mygdx.game.objects.Br;
import com.mygdx.game.objects.Tree;


/**
 * Created by alberto on 8/28/2016.
 */
public class BotanyScreen extends AbstractReturnScreen{

    private ShapeRenderer sr;
    private float angle;
    private int count;
    private Array<Br> branches;
    private Tree tree;
    private boolean grow;


    public BotanyScreen(Game game, MainMenu mainMenu){
        super(game, mainMenu);
    }

    @Override
    public void show() {
        super.show();
        sr = new ShapeRenderer();
        angle = 45;
        count = 0;

        branches = new Array<Br>();
        Br root = new Br(new Vector2(Helper.WORLD_WIDTH/2, 0), new Vector2(new Vector2(Helper.WORLD_WIDTH/2, 150)));
        branches.add(root);

        tree = new Tree();
        grow = true;

        //Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Helper.handleCamera(camera);

        //treeFromArray();
        spaceInvasion();

        super.drawStage();

    }

    public void spaceInvasion(){
        if (Gdx.input.isKeyPressed(Input.Keys.G)) {
            grow = false;
        }
        sr.setProjectionMatrix(camera.combined);
        tree.show(sr);
        if (grow) {
            tree.grow();
        }
    }


    public void treeFromArray(){
        if (Gdx.input.isKeyPressed(Input.Keys.M)){

            if (count < 6) {

                for (int i = branches.size - 1; i >= 0; i--) {
                    if (!branches.get(i).isGrown()) {
                        branches.add(branches.get(i).branchRight());
                        branches.add(branches.get(i).branchLeft());
                    }
                    branches.get(i).setGrown(true);
                }
                count++;
            }

        }

        sr.setProjectionMatrix(camera.combined);
        for (Br b : branches){
            b.draw(sr);
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        Helper.resizeScreen(viewport, camera, width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        sr.dispose();
    }
}
