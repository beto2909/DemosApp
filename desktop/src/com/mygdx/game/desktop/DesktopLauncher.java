package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.DemosApp;
import com.mygdx.game.Helper;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new DemosApp(), config);
		config.width = Helper.WORLD_WIDTH;
		config.height = Helper.WORLD_HEIGHT;
	}
}
