package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Helper;

/**
 * Created by alberto on 8/12/2016.
 */
public class Canvas {

    private Pixel[] pixels;
    private int width, height;

    public Canvas(){
        width = Helper.WORLD_WIDTH;
        height =  Helper.WORLD_HEIGHT;
        pixels = new Pixel[width * height];
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = new Pixel();
        }
    }

    public Canvas(int w, int h){
        this.width = w;
        this.height = h;
        pixels = new Pixel[width * height];
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = new Pixel();
        }
    }

    public void draw(ShapeRenderer sr){
        int index;
        sr.begin(ShapeRenderer.ShapeType.Point);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                index = x + y * width;
                sr.setColor(pixels[index].getColor());
                sr.point(x, y, 0);
            }
        }
        sr.end();
    }

    public Pixel getPixel(int x, int y){
        return pixels[x + y * width];
    }

    public void setPixel(int x, int y, Pixel pixel){
        pixels[x + y * width] = pixel;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
