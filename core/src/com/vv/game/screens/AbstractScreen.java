package com.vv.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.vv.game.VidarVoyager;

/**
 * This is the Abstract Screen Class.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public abstract class AbstractScreen implements Screen {
    protected final VidarVoyager game;
    protected Stage stage;
    protected OrthographicCamera cam;
    protected ShapeRenderer shapeRenderer;
    protected SpriteBatch batch;

    public AbstractScreen(final VidarVoyager game) {
        this.game = game;
        this.shapeRenderer = new ShapeRenderer();
        this.batch = new SpriteBatch();
        this.cam = new OrthographicCamera(VidarVoyager.APP_WIDTH, VidarVoyager.APP_HEIGHT);
        this.stage = new Stage(new FitViewport(VidarVoyager.APP_WIDTH, VidarVoyager.APP_HEIGHT, this.cam));
        this.cam.setToOrtho(false);
        stage.getViewport().apply();
        cam.position.set((float) VidarVoyager.APP_WIDTH/2, (float) VidarVoyager.APP_HEIGHT/2, 0);
        cam.update();
    }

    public abstract void update(float deltaTime);

    public abstract World getWorld();

    public abstract boolean isPaused();

    public abstract boolean isGameOver();

    public abstract void setGameOver(boolean over);

    @Override
    public void resize(int width, int height){ stage.getViewport().update(width, height, true); }

    @Override
    public void dispose(){
        this.shapeRenderer.dispose();
        this.batch.dispose();
        this.stage.dispose();
    }

    @Override
    public void render(float deltaTime){
        update(deltaTime); //child class
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}