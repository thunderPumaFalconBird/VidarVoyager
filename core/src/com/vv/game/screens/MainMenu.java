package com.vv.game.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.vv.game.VidarVoyager;
import com.vv.game.utils.DatabaseInterface;
import com.vv.game.utils.LogInEntry;
import com.vv.game.utils.SignUpEntry;
import com.vv.game.utils.User;

import java.io.File;
import java.sql.SQLException;


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
    private ImageButton startButton;
    private ImageButton logInButton;
    private ImageButton signUpButton;
    private ImageButton guestButton;
    private ImageButton exitButton;
    private SignUpEntry signUpEntry;
    private LogInEntry logInEntry;
    private final Table table;
    public static int window2x = 0;
    public static int window3x = 0;
    public static int window4x = 0;

    /**
     * The Main Menu constructor sets up the camera and stage which are used to render textures. It also starts 3 timers
     * to increment the x position of the window textures giving them a parallax effect.
     */
    public MainMenu() {
        super();
        cam = new OrthographicCamera(VidarVoyager.APP_WIDTH, VidarVoyager.APP_HEIGHT);
        stage = new Stage(new FitViewport(VidarVoyager.APP_WIDTH, VidarVoyager.APP_HEIGHT, this.cam));
        cam.setToOrtho(false);
        stage.getViewport().apply();
        cam.position.set((float) VidarVoyager.APP_WIDTH / 2, (float) VidarVoyager.APP_HEIGHT / 2, 0);
        cam.update();

        initButtons();

        table = new Table();
        table.center().padBottom(120);
        table.setFillParent(true);

        //CHECK DATABASE FOR CONNECTION AND ADD BUTTONS
        if (DatabaseInterface.getInstance().isConnected()) {
            table.add(logInButton);
            table.add(signUpButton);
            table.add(guestButton);
        }
        else{
            table.add(exitButton);
            table.add(startButton);
        }

        stage.addActor(table);

        signUpEntry = new SignUpEntry(stage);
        logInEntry = new LogInEntry(stage);

        timer.scheduleTask(new Timer.Task() { @Override public void run() {MainMenu.window4x--;}},1f,1f);
        timer.scheduleTask(new Timer.Task() { @Override public void run() {MainMenu.window3x--;}},.1f,.1f);
        timer.scheduleTask(new Timer.Task() {@Override public void run() {MainMenu.window2x--;}},.06f,.06f);
        timer.start();
    }

    private void initButtons(){
        Texture textureUp = new Texture("buttons" + File.separator + "StartButton.png");
        TextureRegion textureRegionUp = new TextureRegion(textureUp);
        startButton = new ImageButton(new TextureRegionDrawable(textureRegionUp));

        textureUp = new Texture("buttons" + File.separator + "LogInButton.png");
        textureRegionUp = new TextureRegion(textureUp);
        Texture textureDown = new Texture("buttons" + File.separator + "LogInButtonHighlighted.png");
        TextureRegion textureRegionDown = new TextureRegion(textureDown);
        logInButton = new ImageButton(new TextureRegionDrawable(textureRegionUp),new TextureRegionDrawable(textureRegionDown));

        textureUp = new Texture("buttons" + File.separator + "SignUpButton.png");
        textureRegionUp = new TextureRegion(textureUp);
        textureDown = new Texture("buttons" + File.separator + "SignUpButtonHighlighted.png");
        textureRegionDown = new TextureRegion(textureDown);
        signUpButton = new ImageButton(new TextureRegionDrawable(textureRegionUp),new TextureRegionDrawable(textureRegionDown));

        textureUp = new Texture("buttons" + File.separator + "GuestButton.png");
        textureRegionUp = new TextureRegion(textureUp);
        textureDown = new Texture("buttons" + File.separator + "GuestButtonHighlighted.png");
        textureRegionDown = new TextureRegion(textureDown);
        guestButton = new ImageButton(new TextureRegionDrawable(textureRegionUp),new TextureRegionDrawable(textureRegionDown));

        textureUp = new Texture("buttons" + File.separator + "ExitButton.png");
        textureRegionUp = new TextureRegion(textureUp);
        exitButton = new ImageButton(new TextureRegionDrawable(textureRegionUp));
    }

    /**
     * This method is used to set the username and check the user's password.
     * @return successful
     */
    private boolean handleLogIn() {
        boolean success = false;
        User user = User.getInstance();
        user.setUsername(logInEntry.getUserInputUsername());
        user.setPassword(logInEntry.getUserInputPassword());

        DatabaseInterface db = DatabaseInterface.getInstance();
        try {
            //db.createLogInAttempt(user);
            success = db.checkUserPassword(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(success){
            success = db.createLogInEvent(user);
        }
        return success;
    }

    /**
     * This method handles a users signing up.
     * @return successful
     */
    private boolean handleSignUp(){
        boolean success = false;
        User user = User.getInstance();
        user.setUsername(signUpEntry.getUserInputUsername());
        user.setFirstName(signUpEntry.getUserInputFirstName());
        user.setLastName(signUpEntry.getUserInputLastName());
        user.setMiddleInitial(signUpEntry.getUserInputMiddleInitial());
        user.setDateOfBirth(signUpEntry.getUserInputDateOfBirth());
        user.setEmail(signUpEntry.getUserInputEmail());
        user.setPassword(signUpEntry.getUserInputPassword());

        if(user.hasValidData()) {
            DatabaseInterface db = DatabaseInterface.getInstance();

            if (!db.checkUsernameTaken(user)) {
                success = db.insertUser(user);
            } else {
                signUpEntry.userNameTaken();
            }
        }
        else{
            System.out.println("Invalid data");
        }
        return success;
    }

    /**
     * This method is used to call the stage's act method and to reset window texture x positions so that the parallax
     * effect will continue in an infinite loop.
     * @param deltaTime
     */
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
        String temp = "";
        if(signUpEntry.isActive()) {
            temp = signUpEntry.getButtonPressed();
            if(temp.equals("back")) {
                signUpEntry.setActive(false);
                table.add(logInButton);
                table.add(signUpButton);
                table.add(guestButton);
            }
        }
        if(logInEntry.isActive()) {
            temp = logInEntry.getButtonsPressed();
            if(temp.equals("back")) {
                logInEntry.setActive(false);
                table.add(logInButton);
                table.add(signUpButton);
                table.add(guestButton);
            }
        }
        if(temp.equals("enter")){
            keyDown(Input.Keys.ENTER);
        }
    }

    /**
     * This method is called and the player's position is used to update the camera position.
     * @param x
     * @param y
     */
    @Override
    public void updateCam(float x, float y) { cam.position.set(x, y,0); }

    @Override
    public Stage getStage() { return this.stage; }

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

    /**
     * This method is used to handle buttons pressed.
     * @return the name of the button
     */
    @Override
    public String getButtonPressed(){
        String temp = "none";
        if(startButton.isPressed()){
            temp = "start";
        }
        if(exitButton.isPressed()){
            temp = "exit";
        }
        if(guestButton.isPressed()){
            table.removeActor(logInButton);
            table.removeActor(guestButton);
            table.removeActor(signUpButton);
            table.add(startButton);
        }
        if(logInButton.isPressed()){
            table.removeActor(logInButton);
            table.removeActor(guestButton);
            table.removeActor(signUpButton);
            logInEntry.setActive(true);
        }
        if(signUpButton.isPressed()){
            table.removeActor(logInButton);
            table.removeActor(guestButton);
            table.removeActor(signUpButton);
            signUpEntry.setActive(true);
        }
        return temp;
    }

    /**
     * This is the render method. The order that things get rendered in matters. Everything draws over the previous thing.
     * @param deltaTime The time in seconds since the last render.
     */
    @Override
    public void render(float deltaTime) {
        super.render(deltaTime);

        batch.begin();

        drawBackground();
        if(signUpEntry.isActive()){
            signUpEntry.draw(batch);
        }
        if(logInEntry.isActive()){
            logInEntry.draw(batch);
        }

        batch.end();
        stage.draw();
    }

    /**
     * This method is used to draw the background textures in the correct order. Everything else will be drawn after
     * this method is called.
     */
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

    /**
     * This method is called when the game is destroyed.
     */
    @Override
    public void dispose() {
        super.dispose();
        ship.dispose();
        window1.dispose();
        window2.dispose();
        window3.dispose();
        window4.dispose();
        logInEntry.dispose();
        signUpEntry.dispose();
    }

    /**
     * Called when this screen becomes the current screen for the Game.
     */

    public void show() {
        batch.setProjectionMatrix(cam.combined);
        timer.start();
    }

    /**
     * This method is called when the screen is paused.
     */

    public void pause() { timer.stop(); }

    /**
     * This method is called when resuming the screen from a paused state.
     */

    public void resume() { timer.start(); }

    /**
     * This method is called when the screen is no longer the current screen for the game.
     */

    public void hide() { timer.stop(); }

    /**
     * Called when a key was pressed.
     * @param keycode one of the constants in {@link Input.Keys}
     * @return
     */
    @Override
    public boolean keyDown(int keycode) {
        boolean temp = false;
        if(keycode == Input.Keys.ENTER){
            if(logInEntry.isActive()){
                if(handleLogIn()) {
                    logInEntry.setActive(false);
                    table.add(exitButton);
                    table.add(startButton);

                }
                else{
                    logInEntry.failedLogIn();
                }
            }
            if(signUpEntry.isActive()){
                if(signUpEntry.verifyPassword()){
                    if(handleSignUp()) {
                        signUpEntry.setActive(false);
                        table.add(logInButton);
                        table.add(signUpButton);
                        table.add(guestButton);
                    }
                    else{
                        signUpEntry.failedSignUp();
                    }
                }
                else{
                    signUpEntry.failedSignUp();
                }
            }
        }
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

