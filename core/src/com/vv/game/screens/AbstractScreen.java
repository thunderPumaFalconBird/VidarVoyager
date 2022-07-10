package com.vv.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
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
    protected World world;
    protected Box2DDebugRenderer b2dr;
    protected ShapeRenderer shapeRenderer;
    protected SpriteBatch batch;

    public AbstractScreen() {
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        world = new World(new Vector2(0f, 0f), false);
        b2dr = new Box2DDebugRenderer(); //comment this out when not debugging
    }

    public abstract void update(float deltaTime);

    public abstract Stage getStage();

    @Override
    public void resize(int width, int height){ getStage().getViewport().update(width, height, true); }

    @Override
    public void dispose(){
        this.shapeRenderer.dispose();
        this.batch.dispose();
    }

    @Override
    public void render(float deltaTime){
        update(deltaTime); //child class
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}