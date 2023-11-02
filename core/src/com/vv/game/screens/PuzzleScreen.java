package com.vv.game.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
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
public class PuzzleScreen extends AbstractScreen {
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
    public void initMultiplexer(InputMultiplexer multiplexer){

    }

    @Override
    public void removeMultiplexer(InputMultiplexer multiplexer){

    }

    @Override
    public String getButtonPressed(){
        String temp = "none";

        return temp;
    }

    /**
     * Called when a key was pressed.
     * @param keycode one of the constants in {@link Input.Keys}
     * @return
     */
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    /**
     * Called when a key was released.
     * @param keycode one of the constants in {@link Input.Keys}
     * @return
     */
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    /**
     * Called when a key was typed.
     * @param character The character
     * @return
     */
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    /**
     * Called when the screen was touched or a mouse button was pressed. The button parameter will be Input.Buttons.LEFT
     * on iOS. This is used to set UI buttons pressed.
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button the button
     * @return
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        return false;
    }

    /**
     * Called when a finger was lifted or a mouse button was released. The button parameter will be Input.Buttons.LEFT
     * on iOS.
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.
     * @param button the button
     * @return
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * Called when a finger or the mouse was dragged.
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.
     * @return
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    /**
     * Called when the mouse was moved without any buttons being pressed. Will not be called on iOS.
     * @param screenX
     * @param screenY
     * @return
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    /**
     * Called when the mouse wheel was scrolled. Will not be called on iOS.
     * @param amountX the horizontal scroll amount, negative or positive depending on the direction the wheel was scrolled.
     * @param amountY the vertical scroll amount, negative or positive depending on the direction the wheel was scrolled.
     * @return
     */
    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    /**
     * Called when this screen becomes the current screen for the Game.
     */

    public void show() { batch.setProjectionMatrix(cam.combined); }

    /**
     * This method is called when the screen is paused.
     */

    public void pause() { }

    /**
     * This method is called when resuming the screen from a paused state.
     */

    public void resume() { }

    /**
     * This method is called when the screen is no longer the current screen for the game.
     */

    public void hide() { /* TODO might want to dispose the stage here. check docs */ }
}
