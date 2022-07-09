package com.vv.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.vv.game.VidarVoyager;
import com.vv.game.utils.CollisionHandling;

/**
 * This is the Game Screen Class. The camera and stage are in the AbstractScreen class. The world and render are located
 * in this screen class. The Game screen is used to display game play.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public class GameScreen extends AbstractScreen {
    private final World world;
    private final Box2DDebugRenderer b2dr;
    private final Texture background = new Texture("screens/temp.png");
    public boolean paused = false;
    public boolean over = false;

    public GameScreen(final VidarVoyager game){
        super(game);

        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new CollisionHandling());
        b2dr = new Box2DDebugRenderer(); //comment this out when not debugging

    }

    @Override
    public void update(float deltaTime) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            paused = !paused;
        }
        if(!paused) {
            world.step(1f / VidarVoyager.APP_FPS, VidarVoyager.VELOCITY_ITERATIONS,
                    VidarVoyager.POSITION_ITERATIONS);
            stage.act(deltaTime);
        }
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
        background.dispose();
    }

    public World getWorld(){ return this.world; }

    public boolean isPaused(){ return paused; }

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

