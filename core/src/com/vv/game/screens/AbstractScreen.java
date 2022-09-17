package com.vv.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
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

    public AbstractScreen() {
        batch = new SpriteBatch();
        b2dr = new Box2DDebugRenderer();
    }

    public abstract void update(float deltaTime);

    public abstract void updateCam(float x, float y);

    public abstract Stage getStage();

    @Override
    public void resize(int width, int height){ getStage().getViewport().update(width, height, true); }

    @Override
    public void dispose(){
        batch.dispose();
        b2dr.dispose();
    }

    @Override
    public void render(float deltaTime){
        update(deltaTime); //child class
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}