package com.vv.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * This is the Abstract Screen Class. The type of stage and camera might change, so they are created in the child class.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public abstract class AbstractScreen implements Screen {
    protected World world;
    protected Box2DDebugRenderer b2dr;
    protected SpriteBatch batch;
    protected boolean gameOver = false;
    protected boolean gameWon = false;

    public AbstractScreen() {
        batch = new SpriteBatch();
        b2dr = new Box2DDebugRenderer();
    }

    /**
     * This method is called based on APP_FPS (Frames per second). It is used to handle logical updates. Rendering will
     * be handled by the render method.
     * @param deltaTime
     */
    public abstract void update(float deltaTime);

    /**
     * This method is called to update the camera if the camera follows the player.
     * @param x
     * @param y
     */
    public abstract void updateCam(float x, float y);

    public abstract Stage getStage();

    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }

    public void setGameWon(boolean gameWon) { this.gameWon = gameWon; }

    public abstract String getButtonPressed(float x, float y);

    /**
     * This method is called when the user resizes the app screen size.
     * @param width
     * @param height
     */
    @Override
    public void resize(int width, int height){ getStage().getViewport().update(width, height, true); }

    /**
     * This method is called when the game is destroyed.
     */
    @Override
    public void dispose(){
        batch.dispose();
        b2dr.dispose();
    }

    /**
     * This method is called based on the APP_FPS (frames per second). It is used to call the child class render method.
     * It then clears the screens.
     * @param deltaTime The time in seconds since the last render.
     */
    @Override
    public void render(float deltaTime){
        update(deltaTime); //child class
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}