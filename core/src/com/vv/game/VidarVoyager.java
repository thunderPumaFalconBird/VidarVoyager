package com.vv.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.sun.org.apache.bcel.internal.generic.ASTORE;
import com.vv.game.entities.Astronaut;
import com.vv.game.entities.LevelController;
import com.vv.game.screens.ScreenController;


public class VidarVoyager extends Game {
	public static final int APP_WIDTH = 1000;
	public static final int APP_HEIGHT = 1000;
	public static final float PPM = 100;
	public static final int APP_FPS = 60;
	public static final int VELOCITY_ITERATIONS = 7;
	public static final int POSITION_ITERATIONS = 3;

	private ScreenController screenController;
	private LevelController levelController;

	@Override
	public void create () {
		levelController = new LevelController();
		screenController = new ScreenController(this, levelController.getLevel());
	}

	@Override
	public void render () {
		super.render();
		if(Gdx.input.isTouched()){
			screenController.setScreen(ScreenController.SCREEN_STATE.GAME_SCREEN);
		}
	}
	
	@Override
	public void dispose () {
		screenController.dispose();
	}

}
