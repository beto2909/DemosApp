package com.mygdx.game;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by alberto on 3/21/2016.
 */
public class Assets {

    public static final String IMAGE_PATH = "badlogic.jpg";
    private static final String PARTICLE_PATH = "particle.png";
    private static final String FIRE_PATH = "fire.png";
    public static final String MENU_BUTTONS_PATH = "buttons/menu_button.pack";
    public static final String LIVE_CELL_PATH = "alive_cell.png";
    public static final String DEAD_CELL_PATH = "dead_cell.png";


    //Textures
    public static final AssetDescriptor<Texture> image =
            new AssetDescriptor<Texture>(IMAGE_PATH, Texture.class);
    public static final AssetDescriptor<Texture> particle =
            new AssetDescriptor<Texture>(PARTICLE_PATH, Texture.class);
    public static final AssetDescriptor<Texture> fire_particle =
            new AssetDescriptor<Texture>(FIRE_PATH, Texture.class);
    public static final AssetDescriptor<Texture> alive_cell =
            new AssetDescriptor<Texture>(LIVE_CELL_PATH, Texture.class);
    public static final AssetDescriptor<Texture> dead_cell =
            new AssetDescriptor<Texture>(DEAD_CELL_PATH, Texture.class);
    public static final AssetDescriptor<TextureAtlas> menuButtonPack =
            new AssetDescriptor<TextureAtlas>(MENU_BUTTONS_PATH, TextureAtlas.class);

    public static final AssetDescriptor<Pixmap> blossom =
            new AssetDescriptor<Pixmap>("blossom.jpg", Pixmap.class);

    public static AssetManager manager;

    public static void init(){
        manager = new AssetManager();
    }

    public static void load(){
        manager.load(image);
        manager.load(particle);
        manager.load(fire_particle);
        manager.load(menuButtonPack);
        manager.load(alive_cell);
        manager.load(dead_cell);
        manager.load(blossom);
    }

    public static void dispose(){
        manager.dispose();
    }

}
