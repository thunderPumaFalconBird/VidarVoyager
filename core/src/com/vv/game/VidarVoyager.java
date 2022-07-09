package com.vv.game;

import com.badlogic.gdx.Game;
import com.vv.game.screens.ScreenController;


public class VidarVoyager extends Game {
	public static final int APP_WIDTH = 1000;
	public static final int APP_HEIGHT = 1000;
	public static final float PPM = 100;
	public static final int APP_FPS = 60;
	public static final int VELOCITY_ITERATIONS = 7;
	public static final int POSITION_ITERATIONS = 3;

	ScreenController screenController;
	
	@Override
	public void create () {
		screenController = new ScreenController(this);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		screenController.dispose();
	}
}
