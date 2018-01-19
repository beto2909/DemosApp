package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Helper;
import com.mygdx.game.objects.Flowfield;
import com.mygdx.game.objects.Particle;
import com.mygdx.game.objects.Path;
import com.mygdx.game.objects.Vehicle;
import com.mygdx.game.ui.ButtonGen;

import java.util.ArrayList;

/**
 * Created by alberto on 5/28/2016.
 */
public class AutonomousAgentsDemo extends AbstractReturnScreen {

   // private Stage stage;
    private TextButton nextBut, customPathBut;
    private ButtonGen butGen;
    private Label label, behaviors;

    private ShapeRenderer sr;
    private SpriteBatch batch;

    private ArrayList<Vehicle> v;
    private Flowfield field;
    private Path path;
    private Particle separation, alignment, cohesion; //maybe maxSpeed, maxForce later
    private ArrayList<Vector2> points;
    private Vector2 currentTarget;
    private int demo, which, dx;
    private float[] weights;
    private boolean doneCustomPath, dragging;

    public  AutonomousAgentsDemo(Game game, MainMenu mainMenu){
        super(game, mainMenu);
    }

    @Override
    public void show() {
        super.show();
        demo = 0;
        // UI
        //stage = new Stage();
        //stage.setViewport(viewport);
        butGen = new ButtonGen();

        customPathBut = butGen.makeButton("Custom path");
        customPathBut.setSize(100, 50); //size
        //position
        customPathBut.setPosition(120 * 1 + 100, 30,
                Align.center);
        customPathBut.setDisabled(true);
        stage.addActor(customPathBut); //add to Stage
        nextBut = butGen.makeButton("Next");
        nextBut.setSize(100, 50); //size
        //position
        nextBut.setPosition(120 * 2 + 100, 30,
                Align.center);
        stage.addActor(nextBut); //add to Stage

        //add listeners
        nextBut.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // check if user is done selecting points for new path
                if (!doneCustomPath){
                    doneCustomPath = true;
                    return true;
                }
                demo++;
                if(demo > 3){
                    demo = 0;
                }
                switch (demo){
                    case 0:
                        label.setText("Flock");
                        behaviors.setText("");
                        customPathBut.setDisabled(true);
                        break;
                    case 1:
                        label.setText("Follow Path");
                        behaviors.setText("");
                        customPathBut.setDisabled(false);
                        break;
                    case 2:
                        label.setText("Follow Field");
                        behaviors.setText("");
                        customPathBut.setDisabled(true);
                        break;
                    case 3:
                        label.setText("Applied Behaviors");
                        behaviors.setText("Separation \nAlignment \nCohesion");
                        customPathBut.setDisabled(true);
                        break;
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        customPathBut.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!customPathBut.isDisabled()) {
                    demo = -1;
                    doneCustomPath = false;
                    nextBut.setText("Done");
                    points.clear();
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        label = new Label("Flock", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        label.setPosition(Helper.WORLD_WIDTH/2, Helper.WORLD_HEIGHT - 20, Align.center);
        stage.addActor(label);
        behaviors = new Label("", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        behaviors.setPosition(210, 430, Align.center);
        stage.addActor(behaviors);
        // end UI
        sr = new ShapeRenderer();
        batch = new SpriteBatch();

        separation = new Particle(0,0, Particle.ParticleType.WHITE_PARTICLE);
        alignment = new Particle(0,0, Particle.ParticleType.WHITE_PARTICLE);
        cohesion = new Particle(0,0, Particle.ParticleType.WHITE_PARTICLE);
        // 50, 450 - (20 * i),     200, 450 - (20 * i)
        float x = 100, y = 440;
        separation.setBounds(x,y,20,20);
        alignment.setBounds(x,y-20,20,20);
        cohesion.setBounds(x,y-40,20,20);

        v = new ArrayList<Vehicle>();
        v.add(new Vehicle(0, 0));
        v.add(new Vehicle(0, 0));
        //v.add(new Vehicle(300,40));

        currentTarget = new Vector2();
        field = new Flowfield(20);

        newPath();
        points = new ArrayList<Vector2>();
        doneCustomPath = true;
        which = 0; dx = 0;
        dragging = false;
        weights = new float[] {1,1,1};

        //Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, .49f, .56f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        if (Gdx.input.isKeyPressed(Input.Keys.A)){
            camera.zoom += .1f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)){
            camera.zoom -= .1f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.N)){
            newPath();
        }
        if (Gdx.input.isTouched(1)){    // two fingers
            Vector2 click = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()) );
            camera.zoom = Helper.map(click.y, 0, Helper.WORLD_HEIGHT, 1,3);
        }
        if (Gdx.input.isTouched(0)){
            Vector2 click = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()) );
            if (!dragging){
                if (separation.getBoundingRectangle().contains(click.x, click.y)){
                    dragging = true;
                    dx = (int) (click.x - separation.getX());
                    which = 1;
                }else if (alignment.getBoundingRectangle().contains(click.x, click.y)){
                    dragging = true;
                    dx = (int) (click.x - separation.getX());
                    which = 2;
                }else if (cohesion.getBoundingRectangle().contains(click.x, click.y)){
                    dragging = true;
                    dx = (int) (click.x - separation.getX());
                    which = 3;
                }
            }
        }
        if (Gdx.input.isTouched() && demo != -1){
            Vector2 click = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()) );
            //currentTarget = new Vector2(click.x, click.y);
            v.add(new Vehicle(click.x, click.y));
            if (v.size() > 300){
                v.remove(0);
            }

            if(dragging){
                switch (which){
                    case 1:
                        drag(separation, click);
                        break;
                    case 2:
                        drag(alignment, click);
                        break;
                    case 3:
                        drag(cohesion, click);
                        break;
                }
                updateWeights();
            }
        }
        if (!Gdx.input.isTouched()) dragging = false;


        switch (demo){
            case 0:
                flock();
                break;
            case 1:
                followPath();
                break;
            case 2:
                followField();
                break;
            case 3:
                applyBehaviors();
                break;
            case -1:
                setPath();
                break;
        }

//        stage.act();
//        stage.draw();
        super.drawStage();
    }

    private void applyBehaviors(){
        sr.begin(ShapeRenderer.ShapeType.Filled);
        for (Vehicle i:v){
            i.applyBehaviorsDemo(v,weights);
            i.draw(sr);
            i.borders();
        }
        sr.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setProjectionMatrix(camera.combined);
        sr.setColor(Color.BLACK);
        sr.identity();
        for (int i = 0; i < 3; i++) {
            sr.line( 50, 450 - (20 * i), 200, 450 - (20 * i));
        }
        sr.rect(separation.getX(), separation.getY(),
                separation.getBoundingRectangle().width,
                separation.getBoundingRectangle().height);
        sr.rect(alignment.getX(), alignment.getY(),
                alignment.getBoundingRectangle().width,
                alignment.getBoundingRectangle().height);
        sr.rect(cohesion.getX(), cohesion.getY(),
                cohesion.getBoundingRectangle().width,
                cohesion.getBoundingRectangle().height);
        sr.end();

        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        alignment.draw(batch);
        separation.draw(batch);
        cohesion.draw(batch);
        batch.end();

    }

    private void drag(Particle p, Vector2 click){
        p.setX(click.x - dx);
        if (p.getX() < 40) p.setX(40);
        if (p.getX() > 190) p.setX(190);
    }

    private void updateWeights(){
        float min = 40, max = 190;
        weights[0] = Helper.map(separation.getX(), min, max, 0, 2);
        weights[1] = Helper.map(alignment.getX(), min, max, 0, 2);
        weights[2] = Helper.map(cohesion.getX(), min, max, 0, 2);
    }

    private void setPath(){
        if (Gdx.input.isTouched()){
            Vector2 click = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()) );
            //currentTarget = new Vector2(click.x, click.y);
            points.add(new Vector2(click.x, click.y));
            Helper.print("Point added");
        }

        if (doneCustomPath){
            // clear duplicated points
            for (int i = points.size()- 1; i > 0; i--) {
                Vector2 current = points.get(i);
                Vector2 next = points.get(i - 1);
                if (current.x == next.x){
                    points.remove(i);
                }
            }
            //remove first and last points
            points.remove(0);
            points.remove(points.size() -1);

            newPath(points);
            nextBut.setText("Next");
            demo = 1;
            Helper.print(points.toString());
        }


        sr.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < points.size(); i++) {
            sr.identity();
            sr.circle(points.get(i).x, points.get(i).y, 10, 10);
        }
        sr.end();


    }

    private void flock(){
        sr.setProjectionMatrix(camera.combined);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        for (Vehicle i:v){
            i.flock(v);
            i.draw(sr);
            i.borders();
        }
        sr.end();
    }

    private void followPath(){
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setProjectionMatrix(camera.combined);
        path.draw(sr);
        sr.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);
        for (Vehicle i:v){
            i.follow(path);
            i.applyForce(i.separate(v));
            i.bordersCustom(path);
            i.draw(sr);
        }
        sr.end();
    }

    private void followField(){
        //sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setProjectionMatrix(camera.combined);
        field.draw(sr);
        //sr.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);
        for (Vehicle i:v){
            i.follow(field);
            i.draw(sr);
            //i.borders();
        }
        sr.end();
    }

    private void newPath(ArrayList<Vector2> points) {
        float height = Helper.WORLD_HEIGHT;
        float width = Helper.WORLD_WIDTH;
        // A path is a series of connected points
        // A more sophisticated path might be a curve
        Helper.print("Settign new path");
        path = new Path();
        for (int i = 0; i < points.size(); i++) {
            Vector2 p = points.get(i);
            path.addPoint(p.x, p.y);
        }
    }

    private void newPath() {
        float height = Helper.WORLD_HEIGHT;
        float width = Helper.WORLD_WIDTH;
        // A path is a series of connected points
        // A more sophisticated path might be a curve
        path = new Path();
        path.addPoint(-20, height/2);
        path.addPoint(MathUtils.random(0, width/2), MathUtils.random(0, height));
        path.addPoint(MathUtils.random(width/2, width), MathUtils.random(0, height));
        path.addPoint(width+20, height/2);
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
        stage.dispose();
    }

}
