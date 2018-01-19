package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.Assets;
import com.mygdx.game.Helper;

/**
 * Created by alberto on 6/1/2016.
 */
public class ButtonGen {

    private BitmapFont font;
    private Skin skin;
    private TextureAtlas buttonAtlas;
    private TextButton.TextButtonStyle buttonStyle;
    public ButtonGen(){
        font = new BitmapFont();
        skin = new Skin();
        buttonAtlas = Assets.manager.get(Assets.menuButtonPack);
        skin.addRegions(buttonAtlas);

        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = skin.getDrawable(Helper.MENU_BUT_UP);
        buttonStyle.over = skin.getDrawable(Helper.MENU_BUT_DOWN);
        buttonStyle.down = skin.getDrawable(Helper.MENU_BUT_DOWN);
        buttonStyle.disabled = skin.getDrawable(Helper.MENU_BUT_DOWN);
        buttonStyle.font = font;
    }

    public TextButton makeButton(String text){
        return new TextButton(text, buttonStyle);
    }


}
