package com.mygdx.game.objects;

import com.badlogic.gdx.math.MathUtils;

/**
 * Created by alberto on 6/17/2017.
 */
public class Perceptron {

    public static final float LEARNING_RATE = 0.01f;
    public float[] weights;

    public Perceptron(int n) {
        weights =  new float[n];

        for (int i = 0; i < weights.length; i++) {
            weights[i] = MathUtils.random(-1,1);
        }
    }

    public float guessY(float x) {
//        float m = weights[0] / weights[1];
//        float b = weights[2];
//        return -m * x -b;
        float w0 = weights[0];
        float w1 = weights[1];
        float w2 = weights[2];
        return -(w2/w1) - (w0/w1) * x;
    }

    public int guess(float[] inputs) {
        float sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += inputs[i] * weights[i];
        }
        return sign(sum);
    }

    public void train(float[] inputs, int target) {
        int guess = guess(inputs);
        int error = target - guess;

        for (int i = 0; i < weights.length; i++) {
            weights[i] += error * inputs[i] * LEARNING_RATE;
        }
    }

    // activation function
    private int sign(float n) {
        if (n >= 0) return 1;
        else return -1;
    }
}
