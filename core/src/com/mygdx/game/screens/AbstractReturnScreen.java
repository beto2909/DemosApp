package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Helper;
import com.mygdx.game.ui.ButtonGen;

/**
 * Created by alberto on 6/13/2017.
 */
public class AbstractReturnScreen extends AbstractScreen {

    private static final float RETURN_X_POS = .9f; // percentage
    private static final float RETURN_Y_POS = .9f; // percentage

    private MainMenu mainMenu;

    Stage stage;
    ButtonGen butGen;

    TextButton returnBut;
    boolean retAdjusted;

    public AbstractReturnScreen(Game game, MainMenu main) {
        super(game);
        mainMenu = main;
    }

    @Override
    public void show() {
        super.show();
        stage = new Stage();
        stage.setViewport(viewport);
        butGen = new ButtonGen();

        returnBut = butGen.makeButton("Return");
        // position button
        returnBut.setSize(100, 50);

        // add listener
        returnBut.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                returnMainMenu();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        retAdjusted = false;

        stage.addActor(returnBut);

        Gdx.input.setInputProcessor(stage);
    }

    private void returnMainMenu() {
        game.setScreen(mainMenu);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

    }

    /**
     * Sets "Return" button position based if the camera is centered or not.
     */
    public void adjustReturnButton() {
        returnBut.setPosition(RETURN_X_POS * Helper.WORLD_WIDTH - Helper.WORLD_WIDTH/2 + camera.position.x,
                RETURN_Y_POS * Helper.WORLD_HEIGHT - Helper.WORLD_HEIGHT/2 + camera.position.y, Align.center);
        retAdjusted = true;
    }

    /**
     * Needs to be called at the end of render method of every screen that implements this class.
     */
    public void drawStage() {
        if(retAdjusted) {
            stage.act();
            stage.draw();
        } else {
            adjustReturnButton();
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width, height, true);//true to center camera;
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
    }
}
