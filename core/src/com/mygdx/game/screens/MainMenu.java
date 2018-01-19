package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.Assets;
import com.mygdx.game.Helper;

/**
 * Created by alberto on 3/21/2016.
 */
public class MainMenu extends AbstractScreen {
    private static final float BUTTON_WIDTH = Helper.WORLD_WIDTH * .30f;
    private static final float BUTTON_HEIGHT = Helper.WORLD_HEIGHT/12;
    private static final float PADDING = BUTTON_HEIGHT * .2f;
    //buttons indexes
    private static final int PARTICLE_SYSTEM = 0;
    private static final int PERLIN_DEMO = 1;
    private static final int OSCILLATORS_DEMO = 2;
    private static final int SCALAR_PROJECTION = 3;
    private static final int AUTONOMOUS_AGENTS = 4;
    private static final int REACTION_DIFUSSION = 5;
    private static final int IMAGE_TESTING = 6;
    private static final int IMAGE_PROCESSING = 7;
    private static final int GAME_OF_LIFE = 8;
    private static final int POOL_PHYSICS = 9;
    private static final int FLOWFIELD_PERLIN = 10;
    private static final int BACKGROUND_PIXELS = 11;
    private static final int ART_SCREEN = 12;
    private static final int BOTANY_SCREEN = 13;
    private static final int LINEAR_REGRESSION = 14;
    private static final int PERCEPTRON = 15;
    private static final int NEURAL_NETWORK = 16;

    private MainMenu thisScreen;
    private Stage stage;

    //buttons set up---->
    final String[] demos = new String[]{
            "Particle System",
            "Perlin Noise",
            "Oscillators Demo",
            "Scalar Projection",
            "Autonomous Agents",
            "Reaction Diffusion",
            "Image Testing",
            "Image Processing",
            "Game of Life",
            "Pool Physics",
            "Perlin Flowfield",
            "Background Pixels",
            "Art",
            "Botany Screen",
            "Linear Regression",
            "Perceptron",
            "Neural Network"
    };

    public MainMenu(Game game){
        super(game);
        thisScreen = this;

    }

    @Override
    public void show() {
        super.show();

        stage = new Stage();
        stage.setViewport(viewport);

        final BitmapFont font = new BitmapFont();//Assets.manager.get(Assets.font);
        final Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);

        final Table buttonsList = new Table();

        final Label label = new Label(" Main Menu ", style);
        buttonsList.add(label).pad(PADDING);
        buttonsList.row();

        final Skin skin = new Skin();
        final TextureAtlas buttonAtlas = Assets.manager.get(Assets.menuButtonPack);
        skin.addRegions(buttonAtlas);

        final TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = skin.getDrawable(Helper.MENU_BUT_UP);
        buttonStyle.over = skin.getDrawable(Helper.MENU_BUT_DOWN);
        buttonStyle.down = skin.getDrawable(Helper.MENU_BUT_DOWN);
        buttonStyle.font = font;


        final TextButton[] buttons = new TextButton[demos.length];

        // instantiating and adding listeners
        for (int i = 0; i < buttons.length; i++) {
            final int whichButton = i;
            buttons[i] = new TextButton(demos[i], buttonStyle);
            buttonsList.add(buttons[i]).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).pad(PADDING);
            buttonsList.row();
            buttons[i].addListener(new InputListener() {

                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    switch (whichButton){
                        case PARTICLE_SYSTEM:
                            game.setScreen(new ParticleSystemDemo(game, thisScreen));
                            break;
                        case PERLIN_DEMO:
                            game.setScreen(new PerlinDemo(game, thisScreen));
                            break;
                        case OSCILLATORS_DEMO:
                            game.setScreen(new OscillatorsDemo(game, thisScreen));
                            break;
                        case SCALAR_PROJECTION:
                            game.setScreen(new ScalarProjection(game, thisScreen));
                            break;
                        case AUTONOMOUS_AGENTS:
                            game.setScreen(new AutonomousAgentsDemo(game, thisScreen));
                            break;
                        case REACTION_DIFUSSION:
                            game.setScreen(new ReactionDifussionDemo(game, thisScreen));
                            break;
                        case IMAGE_TESTING:
                            game.setScreen(new ImageTesting(game, thisScreen));
                            break;
                        case IMAGE_PROCESSING:
                            game.setScreen(new ImageProcessing(game, thisScreen));
                            break;
                        case GAME_OF_LIFE:
                            game.setScreen(new GameOfLifeDemo(game, thisScreen));
                            break;
                        case POOL_PHYSICS:
                            game.setScreen(new PoolPhysics(game, thisScreen));
                            break;
                        case FLOWFIELD_PERLIN:
                            game.setScreen(new PerlinNoiseFlowfieldDemo(game, thisScreen));
                            break;
                        case BACKGROUND_PIXELS:
                            game.setScreen(new BackgroundPixels(game, thisScreen));
                            break;
                        case ART_SCREEN:
                            game.setScreen(new ArtScreen(game, thisScreen ));
                            break;
                        case BOTANY_SCREEN:
                            game.setScreen(new BotanyScreen(game, thisScreen));
                            break;
                        case LINEAR_REGRESSION:
                            game.setScreen(new LinearRegressionScreen(game, thisScreen));
                            break;
                        case PERCEPTRON:
                            game.setScreen(new PerceptronTrainingScreen(game, thisScreen));
                            break;
                        case NEURAL_NETWORK:
                            game.setScreen(new NeuralNetworkScreen(game, thisScreen));
                            break;
                    }

                    return true;
                }

            });

        }

        final ScrollPane sp = new ScrollPane(buttonsList);
        final Table t = new Table();
        t.setFillParent(true);
        t.add(sp).fill().expand();
        stage.addActor(t);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        //Helper.handleCamera(camera);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        Helper.resizeScreen(viewport, camera, width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
    }
}
