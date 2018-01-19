package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets;
import com.mygdx.game.Helper;

/**
 * Created by alberto on 6/18/2016.
 */
public class ImageProcessing extends AbstractReturnScreen {
    private static final String CURRENTPIC ="green_back.png";//"green_back.png";
    private static final char R = 'R';
    private static final char G = 'G';
    private static final char B = 'B';

    private SpriteBatch batch;
    private Texture test_resized, copyTex, editTex;
    private Pixmap copy, src, dest, edited, area;
    private int scale = 5;

    public ImageProcessing(Game game, MainMenu mainMenu){
        super(game, mainMenu);
    }

    @Override
    public void show() {
        super.show();


        batch = new SpriteBatch();

        src = Assets.manager.get(Assets.blossom);//new Pixmap(Gdx.files.internal(CURRENTPIC));
        dest = new Pixmap(src.getWidth() / scale, src.getHeight() / scale, src.getFormat());
        dest.drawPixmap(src, 0, 0, src.getWidth(), src.getHeight(), 0, 0, dest.getWidth(), dest.getHeight());
        test_resized = new Texture(dest);

        copy = new Pixmap(src.getWidth(), src.getHeight(), src.getFormat());
        for (int x = 0; x < copy.getWidth(); x++) {
            for (int y = 0; y < copy.getHeight(); y++) {
                copy.setColor(src.getPixel(x,y));
                copy.drawPixel(x,y);
            }
        }
        copyTex = new Texture(copy);

        area = new Pixmap(200,200, Pixmap.Format.RGBA8888);

        //blackAndWhite();
        //blur();
        blurColor();
        //sharpen();
        //edges();


       // Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        if (Gdx.input.isKeyPressed(Input.Keys.A)){
            camera.zoom += .1f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)){
            camera.zoom -= .1f;
        }

        if (Gdx.input.isTouched()){

            Vector2 mouse = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            mouse.y = edited.getHeight() - mouse.y;
            int startX = (int) mouse.x;
            int startY = (int) mouse.y;
            int xEnd = (int) MathUtils.clamp(mouse.x + edited.getWidth()/2, 0, edited.getWidth());
            int yEnd = (int) MathUtils.clamp(mouse.y + edited.getHeight()/2, 0, edited.getHeight());
            for (int i = startX; i < xEnd; i++) {
                for (int j = startY; j < yEnd; j++) {
                    int c = edited.getPixel(i,j);
                    area.drawPixel(i-startX,j-startY,c);
                }
            }
            copyTex.draw(area, (int)mouse.x, (int)mouse.y);
        }

        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        //batch.draw(copyTex,0,0);
        batch.draw(copyTex,0,0);
        //batch.draw(test_resized,0, copyTex.getHeight()-test_resized.getHeight());
        batch.end();

        copyTex.draw(copy,0,0);

        super.drawStage();

    }

    private void edges(){
        edited = new Pixmap(copy.getWidth(), copy.getHeight(), copy.getFormat());
        for (int x = 0; x < edited.getWidth()-1; x++) {
            for (int y = 0; y < edited.getHeight(); y++) {
                Color c = new Color(copy.getPixel(x,y));
                Color c2 = new Color(copy.getPixel(x+1,y));
                float b1 = (c.r +c.g+c.b)/3;
                float b2 = (c2.r +c2.g+c2.b)/3;
                float bright = Math.abs(b1 - b2);
                c.set(bright,bright,bright, 1);
                //Helper.print(bright + "");
                edited.setColor(c);
                edited.drawPixel(x,y);
            }
        }
        editTex = new Texture(edited);
    }

    private void sharpen(){
        edited = new Pixmap(copy.getWidth(), copy.getHeight(), copy.getFormat());

        for (int x = 1; x < edited.getWidth()-1; x++) {
            for (int y = 1; y < edited.getHeight()-1; y++) {
                float blur = getSharpen(x,y);
                Color c = new Color(blur, blur, blur, 1);
                //Helper.print(bright + "");
                edited.setColor(c);
                edited.drawPixel(x,y);
            }
        }

        editTex = new Texture(edited);
    }

    private void blur(){

        edited = new Pixmap(copy.getWidth(), copy.getHeight(), copy.getFormat());
        float[][] matrix = getMatrix(5);
        for (int x = 1; x < edited.getWidth()-1; x++) {
            for (int y = 1; y < edited.getHeight()-1; y++) {
                float blur = getBlur(x,y,matrix);
                Color c = new Color(blur, blur, blur, 1);
                //Helper.print(bright + "");
                edited.setColor(c);
                edited.drawPixel(x,y);
            }
        }

        editTex = new Texture(edited);
    }

    private void blurColor(){

        edited = new Pixmap(copy.getWidth(), copy.getHeight(), copy.getFormat());
        float[][] matrix = getMatrix(5);
        for (int x = 1; x < edited.getWidth()-1; x++) {
            for (int y = 1; y < edited.getHeight()-1; y++) {
                float r = getBlurColor(x,y,matrix, R);
                float g = getBlurColor(x,y,matrix, G);
                float b = getBlurColor(x,y,matrix, B);
                Color c = new Color(r, g, b, 1);
                //Helper.print(bright + "");
                edited.setColor(c);
                edited.drawPixel(x,y);
            }
        }

        editTex = new Texture(edited);
    }
    // returns matrix of size maskSize * maskSize (e.g. 3x3, 9x9)
    private float[][] getMatrix(int maskSize){
        float weight = 1f/(maskSize*maskSize);
        float[][] matrix = new float[maskSize][maskSize];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = weight;
            }
        }
        return matrix;
    }


    private float getBlurColor(int x, int y, float[][] matrix, char channel){
        float sum = 0;

        for (int kx = -1; kx <= matrix.length - 2; kx++) {
            for (int ky = -1; ky <= matrix.length - 2; ky++) {
                Color c = new Color(copy.getPixel(x + kx, y + ky));
                switch (channel){
                    case R:
                        sum += matrix[kx + 1][ky + 1 ] * c.r;
                        break;
                    case G:
                        sum += matrix[kx + 1][ky + 1 ] * c.g;
                        break;
                    case B:
                        sum += matrix[kx + 1][ky + 1 ] * c.b;
                        break;
                }

            }
        }
        return sum;
    }

    private float getBlur(int x, int y, float[][] matrix){
        float sum = 0;

        for (int kx = -1; kx <= matrix.length - 2; kx++) {
            for (int ky = -1; ky <= matrix.length - 2; ky++) {
                Color c = new Color(copy.getPixel(x + kx, y + ky));
                float bright = (c.r +c.g+c.b)/3;
                sum += matrix[kx + 1][ky + 1 ] * bright;
            }
        }
        return sum;
    }

    private float getSharpen(int x, int y){
        float[][] matrix = {{-1, -1, -1},
                            {-1, 9, -1},
                            {-1, -1, -1}};
        float sum = 0;

        for (int kx = -1; kx <= 1; kx++) {
            for (int ky = -1; ky <= 1; ky++) {
                Color c = new Color(copy.getPixel(x + kx, y + ky));
                float bright = (c.r +c.g+c.b)/3;
                sum += matrix[kx + 1][ky + 1 ] * bright;
            }
        }
        return sum;
    }
    private void blackAndWhite(){
        edited = new Pixmap(copy.getWidth(), copy.getHeight(), copy.getFormat());

        for (int x = 0; x < edited.getWidth(); x++) {
            for (int y = 0; y < edited.getHeight(); y++) {
                Color c = new Color(copy.getPixel(x,y));
                float bright = (c.r +c.g+c.b)/3;
                c.set(bright, bright, bright, 1);
                edited.setColor(c);
                edited.drawPixel(x,y);
            }
        }
        editTex = new Texture(edited);
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
    }
}
