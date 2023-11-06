package com.vv.game.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.vv.game.VidarVoyager;

import java.io.File;

/**
 * This is the Rescue Mission Class. It handles rendering the map and stage. It also renders box2d shapes when debugging.
 * It also updates the camera position based on the player's location.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public class RescueMissionScreen extends AbstractScreen {
    private final Stage stage;
    private final OrthographicCamera cam;
    private final Table table;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Array<TextureRegion> instructions;
    private Texture gameOverImage;
    private ImageButton newGameButton;
    private ImageButton exitButton;
    private ImageButton nextButton;

    /**
     * The Rescue Mission Screen constructor sets up the camera and stage which are used to render textures. It also
     * sets up the map render so that the map will be rendered.
     *
     */
    public RescueMissionScreen(){
        super();

        cam = new OrthographicCamera(VidarVoyager.APP_WIDTH, VidarVoyager.APP_HEIGHT);
        stage = new Stage(new FitViewport((float) VidarVoyager.APP_WIDTH,
                (float)VidarVoyager.APP_HEIGHT,
                this.cam));
        cam.setToOrtho(false);
        stage.getViewport().apply();
        cam.update();

        table = new Table();
        table.setFillParent(true);

        stage.addActor(table);

        gameOverImage = new Texture("screens" + File.separator + "GameOver.png");

        Texture textureUp = new Texture("buttons" + File.separator + "NewGameButton.png");
        TextureRegion textureRegionUp = new TextureRegion(textureUp);
        newGameButton = new ImageButton(new TextureRegionDrawable(textureRegionUp));

        textureUp = new Texture("buttons" + File.separator + "ExitButton.png");
        textureRegionUp = new TextureRegion(textureUp);
        exitButton = new ImageButton(new TextureRegionDrawable(textureRegionUp));

        textureUp = new Texture("buttons" + File.separator + "BackButton.png");
        textureRegionUp = new TextureRegion(textureUp);
        nextButton = new ImageButton(new TextureRegionDrawable(textureRegionUp));
    }

    public Stage getStage(){ return this.stage; }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
        table.setPosition(cam.position.x - (float) VidarVoyager.APP_WIDTH / 2,
                cam.position.y - ((float) VidarVoyager.APP_WIDTH / 2) - gameOverImage.getHeight());
        table.add(exitButton);
        table.add(newGameButton);
    }

    public void setGameWon(boolean gameWon) {
        this.gameWon = gameWon;
        table.setPosition(cam.position.x - (float) VidarVoyager.APP_WIDTH / 2,
                cam.position.y - ((float) VidarVoyager.APP_WIDTH / 2));
        table.add(exitButton);
        table.add(newGameButton);
    }

    @Override
    public void initMultiplexer(InputMultiplexer multiplexer){
        multiplexer.addProcessor(this);
        multiplexer.addProcessor(stage);
    }

    @Override
    public void removeMultiplexer(InputMultiplexer multiplexer){
        multiplexer.removeProcessor(this);
        multiplexer.removeProcessor(stage);
    }

    public void loadLevel(World world, TiledMap map, Array<TextureRegion> instructions){
        this.world = world;
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        this.instructions = instructions;
    }

    @Override
    public String getButtonPressed(){
        String temp = "none";

        if(newGameButton.isPressed()){
            temp = "newGame";
        }
        else if(exitButton.isPressed()){
            temp = "exit";
        }
        else if(nextButton.isPressed()){

        }

        return temp;
    }

    /**
     * This method is called and the player's position is used to update the camera position.
     * @param x
     * @param y
     */
    public void updateCam(float x, float y){
        cam.position.set(x * VidarVoyager.PPM, y * VidarVoyager.PPM, 0);
        cam.update();
    }

    /**
     * This method is called to update camera and act on the stage. It also updates the inventory textures that need to
     * be displayed and the oxygen level of the player.
     * @param deltaTime
     */
    @Override
    public void update(float deltaTime) {
        cam.update();
        mapRenderer.setView(cam);
        stage.act(deltaTime);
    }

    /**
     * This is the render method. The order that things get rendered in matters. Everything draws over the previous thing.
     * @param deltaTime The time in seconds since the last render.
     */
    @Override
    public void render(float deltaTime){
        super.render(deltaTime);

        batch.begin();

        mapRenderer.render();

        stage.draw();

        if(gameOver){
            batch.draw(gameOverImage, cam.position.x - ((float) gameOverImage.getWidth() / 2),
                    cam.position.y - ((float) gameOverImage.getHeight() / 2));
        }

        if(VidarVoyager.DEBUGGING){
            b2dr.render(world, cam.combined.cpy().scl(VidarVoyager.PPM));
        }

        batch.end();
    }

    /**
     * This method is called when the game is destroyed.
     */
    @Override
    public void dispose(){
        super.dispose();
        stage.dispose();
        mapRenderer.dispose();
        gameOverImage.dispose();
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

    public void hide() { }

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
}

