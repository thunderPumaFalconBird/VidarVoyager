package com.vv.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.vv.game.screens.ScreenController;
import com.vv.game.screens.ScreenController.SCREEN_STATE;
import com.vv.game.utils.GameController;
import com.vv.game.utils.GameInput;

/**
 * This is the Vidar Voyager Class. It is the Presenter in the Model View Presenter design. It has a screen controller
 * which contains the views and a game controller which contains the models.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public class VidarVoyager implements ApplicationListener {
	public static final boolean debugging = false; // set to false when not debugging. This will control the debug renderer
	public static final int APP_WIDTH = 1000;
	public static final int APP_HEIGHT = 1000;
	public static final float PPM = 100;
	public static final int APP_FPS = 60;
	public static final int VELOCITY_ITERATIONS = 7;
	public static final int POSITION_ITERATIONS = 3;
	private final GameInput gameInput = GameInput.getInstance();
	private ScreenController screenController;
	private GameController gameController;

	public VidarVoyager() {
	}

	@Override
	public void create () {

		gameController = new GameController();

		screenController = new ScreenController();
		screenController.initRescueMissionScreen(
				gameController.getWorld(SCREEN_STATE.RESCUE_MISSION_SCREEN),
				gameController.getMap(SCREEN_STATE.RESCUE_MISSION_SCREEN)
				);
		screenController.initMainMenu(new World(new Vector2(0f, 0f), false));

		//Add actors to stage.
		gameController.addActors(
				SCREEN_STATE.RESCUE_MISSION_SCREEN,
				screenController.getScreenStage(SCREEN_STATE.RESCUE_MISSION_SCREEN)
				);

		Gdx.input.setInputProcessor(gameInput);
	}

	@Override
	public void render () {
		//RENDER
		screenController.getCurrentScreen().render(Gdx.graphics.getDeltaTime());

		//HANDLE  UI INPUT
		//TODO create separate ui input for menu options, esc, pause, etc.
		if(screenController.getCurrentScreenState() == SCREEN_STATE.MAIN_MENU && Gdx.input.isTouched()){
			screenController.setScreen(SCREEN_STATE.RESCUE_MISSION_SCREEN);
			gameController.setCurrentScreen(SCREEN_STATE.RESCUE_MISSION_SCREEN);
		}

		//UPDATE
		gameController.update();

		if(screenController.getCurrentScreenState() == SCREEN_STATE.RESCUE_MISSION_SCREEN) {
			//TODO possibly add check for app width and height
			screenController.updateCam(gameController.getCamUpdate());
		}
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
		gameController.dispose();
	}
}
