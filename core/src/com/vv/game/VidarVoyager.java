package com.vv.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.vv.game.utils.Database;
import com.vv.game.utils.ScreenController;
import com.vv.game.utils.ScreenController.SCREEN_STATE;
import com.vv.game.utils.GameController;
import com.vv.game.utils.User;

/**
 * This is the Vidar Voyager Class. It is the Presenter in the Model View Presenter design. It has a screen controller
 * which controls the views and a game controller which controls the models.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public class VidarVoyager implements ApplicationListener {
	// set to false when not debugging. This will control the debug renderer
	public static final boolean debugging = true;
	public static final int APP_WIDTH = 1000;
	public static final int APP_HEIGHT = 1000;
	public static final float PPM = 100;
	public static final int APP_FPS = 60;
	public static final int VELOCITY_ITERATIONS = 7;
	public static final int POSITION_ITERATIONS = 3;
	private InputMultiplexer multiplexer;
	private ScreenController screenController;
	private GameController gameController;

	public VidarVoyager() {
	}

	/** The create method is called by the applicationListener when it is created.*/
	@Override
	public void create () {
		multiplexer = new InputMultiplexer();
		gameController = GameController.getInstance();
		screenController = ScreenController.getInstance();

		//Initialize all the screens
		screenController.initRescueMissionScreen(
				gameController.getWorld(SCREEN_STATE.RESCUE_MISSION_SCREEN),
				gameController.getMap(SCREEN_STATE.RESCUE_MISSION_SCREEN)
				);
		screenController.initMainMenu();
		screenController.initPuzzleScreen();

		//Initialize multiplexer
		screenController.initMultiplexer(multiplexer);
		gameController.initMultiplexer(multiplexer);

		//Add actors to stage.
		gameController.addActors(
				SCREEN_STATE.RESCUE_MISSION_SCREEN,
				screenController.getScreenStage(SCREEN_STATE.RESCUE_MISSION_SCREEN)
				);

		Gdx.input.setInputProcessor(multiplexer);
	}

	/** The render method is the main game loop. It is called based on the APP_FPS (Frames per second) */
	@Override
	public void render () {
		//RENDER
		screenController.getCurrentScreen().render(Gdx.graphics.getDeltaTime());

		//HANDLE  UI INPUT
		gameController.setCurrentGame(screenController.getCurrentScreenState());

		//UPDATE
		gameController.update();
		screenController.update(gameController.getCamUpdate());

		//CHECK FOR WIN
		if(screenController.getCurrentScreenState() == SCREEN_STATE.PUZZLE_SCREEN && gameController.isGameWon()){
			screenController.setScreen(SCREEN_STATE.RESCUE_MISSION_SCREEN);
		}
	}

	/** Called when the application is resized. This can happen at any point during a non-paused state but will never
	 * happen before a call to create.
	 *
	 * @param width the new width in pixels
	 * @param height the new height in pixels */
	@Override
	public void resize(int width, int height) { screenController.getCurrentScreen().resize(width, height); }

	/** Called when the application is paused, usually when it's not active or visible on-screen. An Application is also
	 * paused before it is destroyed. */
	@Override
	public void pause() {
		System.out.println("Pause was called");
		screenController.getCurrentScreen().pause();
		}

	/** Called when the application is resumed from a paused state, usually when it regains focus. */
	@Override
	public void resume() {
		screenController.getCurrentScreen().resume();
	}

	/** Called when the application is destroyed. Preceded by a call to the pause method. */
	@Override
	public void dispose () {
		screenController.dispose();
		gameController.dispose();
	}
}
