package com.mygdx.game.objects;

import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.Helper;

/**
 * Created by alberto on 7/7/2017.
 */
public class Matrix {

    private int rows, cols;
    public float[][] vals;

    public Matrix(int r, int c) {
        rows = r;
        cols = c;
        vals = new float[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                vals[i][j] = 0;
            }
        }
    }

    public void randomize(){
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                vals[i][j] = MathUtils.random();
            }
        }
    }

    public void multiply(float n) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                vals[i][j] *= n;
            }
        }
    }
    
    public void add(float n) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                vals[i][j] += n;
            }
        }
    }

    public void add(Matrix m) {
        if (rows == m.getRows() && cols == m.getCols()) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    vals[i][j] += m.vals[i][j];
                }
            }
        } else {
            Helper.print("Can't add matrices of different dimensions.");
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public String toString(){
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                str.append(vals[i][j]);
                str.append(' ');
            }
            str.append('\n');
        }
        return str.toString();
    }
}
