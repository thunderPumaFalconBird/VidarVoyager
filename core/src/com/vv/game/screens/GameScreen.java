package com.vv.game.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.vv.game.VidarVoyager;
import com.vv.game.utils.CollisionHandler;

/**
 * This is the Game Screen Class.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public class GameScreen extends AbstractScreen {
    private final Stage stage;
    private final OrthographicCamera cam;
    public boolean over = false;

    public GameScreen(){
        super();

        cam = new OrthographicCamera(VidarVoyager.APP_WIDTH, VidarVoyager.APP_HEIGHT);
        stage = new Stage(new FitViewport(VidarVoyager.APP_WIDTH, VidarVoyager.APP_HEIGHT, this.cam));
        cam.setToOrtho(false);
        stage.getViewport().apply();
        cam.position.set((float) VidarVoyager.APP_WIDTH/2, (float) VidarVoyager.APP_HEIGHT/2, 0);
        cam.update();
        world.setContactListener(new CollisionHandler());
    }

    @Override
    public void update(float deltaTime) {
        world.step(1f / VidarVoyager.APP_FPS, VidarVoyager.VELOCITY_ITERATIONS,
                VidarVoyager.POSITION_ITERATIONS);
        stage.act(deltaTime);
    }

    @Override
    public void render(float deltaTime){
        super.render(deltaTime);

        batch.begin();

        drawBackground();

        batch.end();

        b2dr.render(world, cam.combined.cpy().scl(VidarVoyager.PPM));
        stage.draw();
    }

    @Override
    public void show() {
        batch.setProjectionMatrix(cam.combined);
        shapeRenderer.setProjectionMatrix(cam.combined);
    }

    @Override
    public void dispose(){
        super.dispose();
        world.dispose();
        b2dr.dispose();
    }

    public Stage getStage(){ return this.stage; }

    public boolean isGameOver(){ return over;}

    public void setGameOver(boolean over){ this.over = over; }

    private void drawBackground() { }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }
}

