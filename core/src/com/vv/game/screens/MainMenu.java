package com.vv.game.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.vv.game.VidarVoyager;
import com.vv.game.utils.GameInput;

import java.io.File;

import static com.vv.game.VidarVoyager.PPM;
import static com.vv.game.VidarVoyager.debugging;

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
    private final Texture startTexture;
    private final int startX = 430;
    private final int startY = 525;
    private final int buttonWidth = 150;
    private final int buttonHeight = 50;
    private final Texture ship = new Texture("screens" + File.separator + "shipRedWithInstructions.png");
    private final Texture window1 = new Texture("screens" + File.separator + "background1.png");
    private final Texture window2 = new Texture("screens" + File.separator + "background2.png");
    private final Texture window3 = new Texture("screens" + File.separator + "background3.png");
    private final Texture window4 = new Texture("screens" + File.separator + "background4.png");
    private final Timer timer = new Timer();
    public static int window2x = 0;
    public static int window3x = 0;
    public static int window4x = 0;

    public MainMenu(){
        super();
        cam = new OrthographicCamera(VidarVoyager.APP_WIDTH, VidarVoyager.APP_HEIGHT);
        stage = new Stage(new FitViewport(VidarVoyager.APP_WIDTH, VidarVoyager.APP_HEIGHT, this.cam));
        cam.setToOrtho(false);
        stage.getViewport().apply();
        cam.position.set((float) VidarVoyager.APP_WIDTH/2, (float) VidarVoyager.APP_HEIGHT/2, 0);
        cam.update();

        startTexture = new Texture("screens" + File.separator + "StartButton.png");

        timer.scheduleTask(new Timer.Task() { @Override public void run() {MainMenu.window4x--;}},1f,1f);
        timer.scheduleTask(new Timer.Task() { @Override public void run() {MainMenu.window3x--;}},.1f,.1f);
        timer.scheduleTask(new Timer.Task() {@Override public void run() {MainMenu.window2x--;}},.06f,.06f);
        timer.start();
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
    public String getButtonPressed(float x, float y){
        String temp = "none";
        if(x > startX && x < startX + buttonWidth){
            if(y < startY - buttonHeight && y > startY - buttonHeight*2){
                temp = "start";
            }
        }
        return temp;
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
    public void updateCam(float x, float y) { cam.position.set(x, y,0); }

    @Override
    public Stage getStage() { return this.stage; }

    @Override
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }

    @Override
    public void setGameWon(boolean gameWon) { this.gameWon = gameWon; }

    @Override
    public void render(float deltaTime) {
        super.render(deltaTime);

        batch.begin();

        drawBackground();
        batch.draw(startTexture, startX, startY);

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

    @Override
    public void show() { batch.setProjectionMatrix(cam.combined); }

    @Override
    public void pause() { timer.stop(); }

    @Override
    public void resume() { timer.start(); }

    @Override
    public void hide() { timer.stop(); }
}

