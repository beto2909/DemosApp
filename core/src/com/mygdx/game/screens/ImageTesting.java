package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.Helper;

/**
 * Created by alberto on 6/16/2016.
 */
public class ImageTesting extends AbstractReturnScreen {

    /*
            anon.png - 8
            anon.jpg - 8
            einstein3.jpg - 3
            e6.png - 8
            e4.jpg - 10
            test1.jpg - 8
            test1.png - 12
            test8.jpg - 10
            test9.jpg - 11

     */

    private static final String CURRENTPIC = "logan_trailer.jpg";//"smiley.png";//"hand_heart.jpg";
    private static final int NUMBER_WIDTH = 35;
    private static final int NUMBER_HEIGHT = 51;
    private static final int FACTOR = 8;

    private SpriteBatch batch;
    private Texture tx, land,test_resized;
    private Pixmap pm, dest;
    private Sprite[][] binaries;
    private float[][] vels;
    private int scale = 16;
    private float totHeight;
    private float factor;
    private boolean debug;

    public ImageTesting(Game game, MainMenu mainMenu){
        super(game, mainMenu);
    }

    @Override
    public void show() {
        super.show();

        batch = new SpriteBatch();

        constantBinaryImage();
        //binaryImage();
        //imageSquares();
        factor = FACTOR;
        debug = false;
        //Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        Helper.handleCamera(camera);

        if (Gdx.input.isKeyPressed(Input.Keys.MINUS)){
            Helper.print("Minus " + factor);
            factor -= .05f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.PLUS)){
            Helper.print("Plus " + factor);
            factor += .05f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.U)) {
            debug = !debug;
        }


        //updateBinaries(delta);
        updateBinariesConstant(delta);

        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        batch.enableBlending();


        for (int x = 0; x < dest.getWidth(); x++) {
            for (int y = 0; y < dest.getHeight(); y++) {
               binaries[x][y].draw(batch);
            }
        }


        //zero.draw(batch);
        //batch.draw(tx,0,0);

        if (debug){
            batch.draw(test_resized,300,0);
        }
        batch.end();

        super.drawStage();
    }

    public void updateBinariesConstant(float delta){
        // update position
        for (int i = 0; i < binaries.length; i++) {
            for (int j = 0; j < binaries[i].length; j++) {
                binaries[i][j].setY(binaries[i][j].getY() + vels[i][j] * delta*50);
            }
        }
        // keep inside
        for (int i = 0; i < binaries.length; i++) {
            for (int j = 0; j < binaries[i].length; j++) {
                if (binaries[i][j].getY() < 0)
                    binaries[i][j].setY(totHeight );
            }
        }

        //update number sizes and alpha
        for (int x = 0; x < dest.getWidth(); x++) {
            for (int y = 0; y < dest.getHeight(); y++) {
                int currentC = dest.getPixel(x,y);
                Color c = new Color(currentC);
                float bright = (c.r + c.g + c.b)/3;
                //bright = Helper.map(bright, 0, 1, 1, 0);
                float currentX = x * scale,
                        currentY = totHeight - (y*scale),
                        nextY = totHeight - ((y+1)*scale);

                // iterate over all binaries and set alpha
                for (int i = 0; i < binaries.length; i++) {
                    if (binaries[i][0].getX() == currentX) {
                        for (int j = 0; j < binaries[i].length; j++) {
                            if (binaries[i][j].getY() < currentY && binaries[i][j].getY() > nextY ){
                                binaries[i][j].setAlpha(bright);
                            }
                        }
                    }
                }
            }
        }
    }

    public void updateBinaries(float delta){
        // update position
        for (int i = 0; i < binaries.length; i++) {
            for (int j = 0; j < binaries[i].length; j++) {
                binaries[i][j].setY(binaries[i][j].getY() + vels[i][j] * delta*50);
            }
        }
        // keep inside
        for (int i = 0; i < binaries.length; i++) {
            for (int j = 0; j < binaries[i].length; j++) {
                if (binaries[i][j].getY() < 0)
                    binaries[i][j].setY(totHeight + 20);
            }
        }

        //update number sizes and alpha
        for (int x = 0; x < dest.getWidth(); x++) {
            for (int y = 0; y < dest.getHeight(); y++) {
                int currentC = dest.getPixel(x,y);
                Color c = new Color(currentC);
                float bright = (c.r + c.g + c.b)/3;
                bright = Helper.map(bright, 0, 1, .2f, 1);
                float currentX = x * scale,
                        currentY = totHeight - (y*scale),
                        nextY = totHeight - ((y+1)*scale);

                // iterate over all binaries
                for (int i = 0; i < binaries.length; i++) {
                    if (binaries[i][0].getX() == currentX) {
                        for (int j = 0; j < binaries[i].length; j++) {
                            if (binaries[i][j].getY() < currentY && binaries[i][j].getY() > nextY ){
                                binaries[i][j].setAlpha(bright);
                                binaries[i][j].setSize((NUMBER_WIDTH/2)*bright,(NUMBER_HEIGHT/2)*bright);
                            }
                        }
                    }
                }
            }
        }
    }

    public  void constantBinaryImage(){

        Pixmap src = new Pixmap(Gdx.files.internal(CURRENTPIC));
        scale = src.getWidth()/100;
        Helper.print(scale+"");
        dest = new Pixmap(src.getWidth() / scale, src.getHeight() / scale, src.getFormat());
        dest.drawPixmap(src, 0, 0, src.getWidth(), src.getHeight(), 0, 0, dest.getWidth(), dest.getHeight());
        test_resized = new Texture(dest);

        binaries = new Sprite[dest.getWidth()][dest.getHeight()];
        vels = new float[dest.getWidth()][dest.getHeight()];
        for (int i = 0; i < binaries.length; i++) {
            for (int j = 0; j < binaries[i].length; j++) {
                float r = MathUtils.random();
                float v = MathUtils.random();
                vels[i][j] = -Math.abs(v);
                if (r<.51f){
                    binaries[i][j] = new Sprite(new Texture(Gdx.files.internal("0.png")));
                } else{
                    binaries[i][j] = new Sprite(new Texture(Gdx.files.internal("1.png")));
                }
            }
        }

        pm = new Pixmap(dest.getWidth()*scale, dest.getHeight()*scale, dest.getFormat());
        totHeight = binaries[0].length*scale;
        for (int x = 0; x < dest.getWidth(); x++) {
            for (int y = 0; y < dest.getHeight(); y++) {
                int currentC = dest.getPixel(x, y);
                Color c = new Color(currentC);
                float bright = (c.r + c.g + c.b) / 3;

                binaries[x][y].setPosition(x * scale, totHeight - (y * scale));
                binaries[x][y].setSize(NUMBER_WIDTH /FACTOR, NUMBER_HEIGHT / FACTOR);
                binaries[x][y].setAlpha(bright);
            }
        }

        tx = new Texture(Gdx.files.internal(CURRENTPIC));
    }

    public void binaryImage(){

        Pixmap src = new Pixmap(Gdx.files.internal(CURRENTPIC));
        dest = new Pixmap(src.getWidth() / scale, src.getHeight() / scale, src.getFormat());
        dest.drawPixmap(src, 0, 0, src.getWidth(), src.getHeight(), 0, 0, dest.getWidth(), dest.getHeight());
        test_resized = new Texture(dest);

        binaries = new Sprite[dest.getWidth()][dest.getHeight()];
        vels = new float[dest.getWidth()][dest.getHeight()];
        for (int i = 0; i < binaries.length; i++) {
            for (int j = 0; j < binaries[i].length; j++) {
                float r = MathUtils.random();
                float v = MathUtils.random();
                vels[i][j] = -Math.abs(v);
                if (r<.50f){
                    binaries[i][j] = new Sprite(new Texture(Gdx.files.internal("0.png")));
                } else{
                    binaries[i][j] = new Sprite(new Texture(Gdx.files.internal("1.png")));
                }
            }
        }
        pm = new Pixmap(dest.getWidth()*scale, dest.getHeight()*scale, dest.getFormat());
        totHeight = binaries[0].length*scale;
        float width = binaries[0][0].getWidth(), height = binaries[0][0].getHeight();
        Helper.print(width + " " + height);
        for (int x = 0; x < dest.getWidth(); x++) {
            for (int y = 0; y < dest.getHeight(); y++) {
                int currentC = dest.getPixel(x,y);
                Color c = new Color(currentC);
                float bright = (c.r + c.g + c.b)/3;
                //Helper.print(bright + "");
                binaries[x][y].setPosition(x*scale,totHeight-(y*scale));
                binaries[x][y].setSize((width/2)*bright,(height/2)*bright);
                bright = Helper.map(bright, 0, 1, .2f, 1);
                binaries[x][y].setAlpha(bright);
            }
        }
        tx = new Texture(pm);

    }

    public void imageSquares(){
        int scale = 16;
        Pixmap src = new Pixmap(Gdx.files.internal(CURRENTPIC));
        Pixmap dest = new Pixmap(src.getWidth() / scale, src.getHeight() / scale, src.getFormat());
        dest.drawPixmap(src, 0, 0, src.getWidth(), src.getHeight(), 0, 0, dest.getWidth(), dest.getHeight());
        test_resized = new Texture(dest);

        //pm = new Pixmap(dest.getWidth(), dest.getHeight(), dest.getFormat());
        pm = new Pixmap(dest.getWidth()*scale, dest.getHeight()*scale, dest.getFormat());
        for (int x = 0; x < dest.getWidth(); x++) {
            for (int y = 0; y < dest.getHeight(); y++) {
                int current = dest.getPixel(x,y);
                Color c = new Color(current);
                float bright = (c.r + c.g + c.b)/3;
                int w = MathUtils.floor(Helper.map(bright, 0,1,0,scale));
                pm.setColor(Color.WHITE);
                pm.fillRectangle(x * scale,y * scale, w, w);
            }
        }
        tx = new Texture(pm);

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
