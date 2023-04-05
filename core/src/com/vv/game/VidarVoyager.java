package com.vv.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.vv.game.entities.movable.Astronaut;
import com.vv.game.entities.Level;
import com.vv.game.screens.ScreenController;
import com.vv.game.utils.GameInput;

public class VidarVoyager implements ApplicationListener {
	public static final boolean debugging = false; // set to false when not debugging. This will control the debug renderer
	public static final int APP_WIDTH = 1000;
	public static final int APP_HEIGHT = 1000;
	public static final float PPM = 100;
	public static final int APP_FPS = 60;
	public static final int VELOCITY_ITERATIONS = 7;
	public static final int POSITION_ITERATIONS = 3;
	private ScreenController screenController;
	private GameInput gameInput;
	private Astronaut player;
	private Array<Level> levels;
	private int currentLevelIndex = 0;

	@Override
	public void create () {
		levels = new Array<>();
		levels.add(new Level(1));
		screenController = new ScreenController(this);
		player = new Astronaut(screenController.getScreenStage(ScreenController.SCREEN_STATE.GAME_SCREEN),
				levels.get(currentLevelIndex).getWorld(),
				new Vector2(levels.get(currentLevelIndex).getPlayerStartPosition().x,
						levels.get(currentLevelIndex).getPlayerStartPosition().y));
		//Add actors to stage.
		Stage stage = screenController.getScreenStage(ScreenController.SCREEN_STATE.GAME_SCREEN);
		levels.get(currentLevelIndex).addActors(stage);
		stage.addActor(player);//add player last. Actors will be drawn in the order they are added.

		gameInput = GameInput.getInstance();
		Gdx.input.setInputProcessor(gameInput);
	}

	public Level getCurrentLevel() {
		return levels.get(currentLevelIndex);
	}

	@Override
	public void render () {
		//RENDER
		screenController.getCurrentScreen().render(Gdx.graphics.getDeltaTime());

		//HANDLE  UI INPUT
		//TODO create separate ui input for menu options, esc, pause, etc.
		if(screenController.getCurrentScreenState() == ScreenController.SCREEN_STATE.MAIN_MENU && Gdx.input.isTouched()){
			screenController.setScreen(ScreenController.SCREEN_STATE.GAME_SCREEN);
		}

		//UPDATE
		player.update();
		levels.get(currentLevelIndex).update();
		//TODO possibly add check for app width and height
		screenController.updateCam(player.getBody().getPosition().x*PPM, player.getBody().getPosition().y*PPM);
	}

	@Override
	public void resize(int width, int height) { screenController.getCurrentScreen().resize(width, height); }

	@Override
	public void pause() { screenController.getCurrentScreen().pause(); }

	@Override
	public void resume() { screenController.getCurrentScreen().resume(); }

	@Override
	public void dispose () {
		screenController.dispose();
		for(int i = 0; i < levels.size; i++){
			levels.get(i).dispose();
		}
	}
}
