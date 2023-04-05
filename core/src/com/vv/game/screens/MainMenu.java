package com.vv.game.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.vv.game.VidarVoyager;

import java.io.File;

/**
 * This is the Main Menu Screen Class. It uses an orthographic camera and a fitViewport. There will be a simple puzzle
 * to solve to get the game screen.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public class MainMenu extends AbstractScreen {
    private final Stage stage;
    private final OrthographicCamera cam;
    private final Texture ship = new Texture("screens" + File.separator + "shipRed.png");
    private final Texture window1 = new Texture("screens" + File.separator + "background1.png");
    private final Texture window2 = new Texture("screens" + File.separator + "background2.png");
    private final Texture window3 = new Texture("screens" + File.separator + "background3.png");
    private final Texture window4 = new Texture("screens" + File.separator + "background4.png");
    private final Timer timer = new Timer();
    public static int window2x = 0;
    public static int window3x = 0;
    public static int window4x = 0;

    public MainMenu(World world){
        super();
        //this.world = world;
        cam = new OrthographicCamera(VidarVoyager.APP_WIDTH, VidarVoyager.APP_HEIGHT);
        stage = new Stage(new FitViewport(VidarVoyager.APP_WIDTH, VidarVoyager.APP_HEIGHT, this.cam));
        cam.setToOrtho(false);
        stage.getViewport().apply();
        cam.position.set((float) VidarVoyager.APP_WIDTH/2, (float) VidarVoyager.APP_HEIGHT/2, 0);
        cam.update();

        timer.scheduleTask(new Timer.Task() { @Override public void run() {MainMenu.window4x--;}},1f,1f);
        timer.scheduleTask(new Timer.Task() { @Override public void run() {MainMenu.window3x--;}},.1f,.1f);
        timer.scheduleTask(new Timer.Task() {@Override public void run() {MainMenu.window2x--;}},.06f,.06f);
        timer.start();
    }

    @Override
    public void update(float deltaTime) {
        stage.act(deltaTime);
        if(window4x < -VidarVoyager.APP_WIDTH){
            window4x = 0;
        }
        if(window3x < -VidarVoyager.APP_WIDTH){
            window3x = 0;
        }
        if(window2x < -VidarVoyager.APP_WIDTH){
            window2x = 0;
        }
    }

    @Override
    public void updateCam(float x, float y) {
        cam.position.set(x, y,0);
    }

    @Override
    public Stage getStage() { return this.stage; }

    @Override
    public void render(float deltaTime) {
        super.render(deltaTime);

        batch.begin();

        drawBackground();

        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        ship.dispose();
        window1.dispose();
        window2.dispose();
        window3.dispose();
        window4.dispose();
    }

    private void drawBackground(){

        batch.draw(window1, 0, 0);
        batch.draw(window2, window2x, 0);
        batch.draw(window2, window2x + VidarVoyager.APP_WIDTH, 0);
        batch.draw(window3, window3x, 0);
        batch.draw(window3, window3x + VidarVoyager.APP_WIDTH, 0);
        batch.draw(window4, window4x, 0);
        batch.draw(window4, window4x + VidarVoyager.APP_WIDTH, 0);
        batch.draw(ship, 0, 0);
    }

    @Override
    public void show() {
        batch.setProjectionMatrix(cam.combined);
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }
}

