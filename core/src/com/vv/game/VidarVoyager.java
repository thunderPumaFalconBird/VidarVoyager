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
	public static final boolean debugging = true; // set to false when not debugging. This will control the debug renderer
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
		screenController.initMainMenu();

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
		if(gameInput.getMouseInputs() != null){
			screenController.setButtonPressed(gameInput.getMouseInputs().x, gameInput.getMouseInputs().y);
			gameController.setCurrentScreen(screenController.getCurrentScreenState());
		}

		//UPDATE
		if(!gameController.isGameOver() && !gameController.isGameWon()) {
			gameController.update();
		}

		//Check for win/game over
		if(screenController.getCurrentScreenState() == SCREEN_STATE.RESCUE_MISSION_SCREEN) {
			//TODO possibly add check for app width and height
			if(gameController.isGameOver()){
				screenController.setScreen(SCREEN_STATE.GAME_OVER);
				System.out.println("You died!!");
			}
			else if(gameController.isGameWon()){
				screenController.setScreen(SCREEN_STATE.GAME_WON);
				System.out.println("You Won The Game!!");
			}
			else{
				screenController.updateCam(gameController.getCamUpdate());
			}
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
