package com.vv.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.vv.game.rescueMission.RescueMission;
import com.vv.game.utils.ScreenController;
import com.vv.game.utils.ScreenController.SCREEN_STATE;


/**
 * This is the Vidar Voyager Class. It is the Presenter in the Model View Presenter design. It has a screen controller
 * which controls the views and RescueMission which is the model.
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
	private RescueMission rescueMission;

	public VidarVoyager() {
	}

	/** The create method is called by the applicationListener when it is created.*/
	@Override
	public void create () {
		rescueMission = new RescueMission();
		multiplexer = new InputMultiplexer();
		screenController = new ScreenController(rescueMission.getWorld(), rescueMission.getMap());

		//Add actors to stage.
		Stage stage = screenController.getScreenStage(SCREEN_STATE.RESCUE_MISSION_SCREEN);
		rescueMission.addLevelActors(stage);
		rescueMission.initPlayer(stage);

		//Initialize multiplexer
		screenController.initMultiplexer(multiplexer);
		rescueMission.addAstroMultiplexer(multiplexer);

		Gdx.input.setInputProcessor(multiplexer);
	}

	/** The render method is the main game loop. It is called based on the APP_FPS (Frames per second) */
	@Override
	public void render () {
		//RENDER
		screenController.getCurrentScreen().render(Gdx.graphics.getDeltaTime());

		//UPDATE
		rescueMission.update();
		screenController.update(rescueMission.getPlayerPosition());

		//CHECK FOR PUZZLES
		checkForActivePuzzles();

		//CHECK FOR WIN
		if(rescueMission.checkForWin()){
			System.out.println("You won the game");
			//TODO create kill screen
		}

		//CHECK FOR DEATH
		if(rescueMission.checkForDeath()){
			System.out.println("You are Dead");
			//TODO CREATE GAME OVER IMAGE
		}

	}

	/** The checkForActivePuzzles method will change the view and input to the current puzzle and check for a win. */
	private void checkForActivePuzzles(){
		//CHANGE SCREEN IF A PUZZLE IS ACTIVE
		if(rescueMission.getPuzzle() != null && screenController.getCurrentScreenState() != SCREEN_STATE.PUZZLE_SCREEN){
			screenController.setScreen(SCREEN_STATE.PUZZLE_SCREEN);
			rescueMission.removeAstroMultiplexer(multiplexer);
			rescueMission.addPuzzleMultiplexer(multiplexer);
			rescueMission.getPuzzle().setStage(screenController.getScreenStage(ScreenController.SCREEN_STATE.PUZZLE_SCREEN));
		}

		//CHECK PUZZLE FOR A WIN IF IT IS ACTIVE
		if(screenController.getCurrentScreenState() == SCREEN_STATE.PUZZLE_SCREEN && rescueMission.getPuzzle().isSolved()){
			screenController.setScreen(SCREEN_STATE.RESCUE_MISSION_SCREEN);
			rescueMission.removePuzzleMultiplexer(multiplexer);
			rescueMission.addAstroMultiplexer(multiplexer);
			rescueMission.getPuzzle().setActive(false);
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
		rescueMission.dispose();
	}
}
