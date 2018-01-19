package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.screens.LoadingScreen;

public class DemosApp extends Game {
	private Game game;
	@Override
	public void create () {
		game = this;
		Assets.init();
		//setScreen(new TestScreen());
		setScreen(new LoadingScreen(game));
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose() {
		Assets.dispose();
		super.dispose();
	}
}
