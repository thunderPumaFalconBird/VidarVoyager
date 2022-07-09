package com.vv.game.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.vv.game.VidarVoyager;

/**
 * This is the Main Menu Screen Class. The camera and stage are in the AbstractScreen class. The world and render are
 * located in this screen class. This screen is used to display game information.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public class MainMenu extends AbstractScreen {
    private final World world;
    private final Box2DDebugRenderer b2dr;
    private final Texture background = new Texture("screens/temp.png");
    private boolean over = false;

    public MainMenu(final VidarVoyager game){
        super(game);

        world = new World(new Vector2(0f, 0f), false);
        b2dr = new Box2DDebugRenderer();
    }

    @Override
    public void update(float deltaTime) {
        world.step(1f/ VidarVoyager.APP_FPS, VidarVoyager.VELOCITY_ITERATIONS, VidarVoyager.POSITION_ITERATIONS);
        stage.act(deltaTime);

    }

    @Override
    public World getWorld() { return world; }

    @Override
    public void render(float deltaTime) {
        super.render(deltaTime);

        batch.begin();

        drawBackground();

        batch.end();

        b2dr.render(world, cam.combined.cpy().scl(VidarVoyager.PPM));
    }

    @Override
    public void dispose() {
        super.dispose();
        world.dispose();
        b2dr.dispose();
        background.dispose();
    }

    private void drawBackground(){
        batch.draw(background, 0, 0);
    }

    @Override
    public void show() {
        batch.setProjectionMatrix(cam.combined);
        shapeRenderer.setProjectionMatrix(cam.combined);
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }
}

