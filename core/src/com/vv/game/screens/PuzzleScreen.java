package com.vv.game.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.vv.game.VidarVoyager;

/**
 * This is the puzzle screen class. It displays mini-games inside the Rescue Mission game.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public class PuzzleScreen extends AbstractScreen{
    private final Stage stage;
    private final OrthographicCamera cam;

    public PuzzleScreen(){
        super();
        cam = new OrthographicCamera(VidarVoyager.APP_WIDTH, VidarVoyager.APP_HEIGHT);
        stage = new Stage(new FitViewport((float) VidarVoyager.APP_WIDTH,
                (float)VidarVoyager.APP_HEIGHT,
                this.cam));
        cam.setToOrtho(false);
        stage.getViewport().apply();
        cam.update();
    }

    @Override
    public void update(float deltaTime) {
        cam.update();
        stage.act(deltaTime);
    }

    @Override
    public void render(float deltaTime){
        super.render(deltaTime);

        batch.begin();

        stage.draw();

        batch.end();
    }

    @Override
    public void updateCam(float x, float y) {
        cam.position.set(x * VidarVoyager.PPM, y * VidarVoyager.PPM, 0);
        cam.update();
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public String getButtonPressed(float x, float y) {
        return "none";
    }

    /**
     * Called when this screen becomes the current screen for the Game.
     */
    @Override
    public void show() { batch.setProjectionMatrix(cam.combined); }

    /**
     * This method is called when the screen is paused.
     */
    @Override
    public void pause() { }

    /**
     * This method is called when resuming the screen from a paused state.
     */
    @Override
    public void resume() { }

    /**
     * This method is called when the screen is no longer the current screen for the game.
     */
    @Override
    public void hide() { /* TODO might want to dispose the stage here. check docs */ }
}
