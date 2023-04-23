package com.vv.game.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.EnumMap;

import static com.vv.game.VidarVoyager.debugging;

/**
 * This is the Screen Controller Class. It creates the screens that will be used and sets the proper screen. Screens
 * are stored in an enum map with the enum SCREEN_STATE as the key. It implements the InputProcessor class so that it
 * can handle UI inputs.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public class ScreenController implements InputProcessor {
    /* This enumeration is used as keys for the enumMap that holds the screens */
    public enum SCREEN_STATE {
        MAIN_MENU,
        RESCUE_MISSION_SCREEN,
        MAD_PLANETS_SCREEN,
        GAME_OVER,
        GAME_WON
    }
    private InputMultiplexer multiplexer;
    private SCREEN_STATE currentScreen = SCREEN_STATE.MAIN_MENU;
    private EnumMap<SCREEN_STATE, AbstractScreen> screens;

    /**
     * The ScreenController constructor creates the enumMap of the screens. The screens are added to the enumMap using
     * init methods below. The screenController is also added to the multiplexer so that input can be handled for UI.
     * @param multiplexer
     */
    public ScreenController(InputMultiplexer multiplexer) {
        this.multiplexer = multiplexer;
        this.screens = new EnumMap<>(SCREEN_STATE.class);
        multiplexer.addProcessor(this);
    }

    /**
     * This method initializes the RescueMissionScreen and adds it to the enumMap. The World and Map are passed from
     * the gameController.
     * @param world
     * @param map
     */
    public void initRescueMissionScreen(World world, TiledMap map){
        this.screens.put(SCREEN_STATE.RESCUE_MISSION_SCREEN, new RescueMissionScreen(map, world));
    }

    /**
     * This method initializes the Main Menu and adds it to the enumMap.
     */
    public void initMainMenu(){ this.screens.put(SCREEN_STATE.MAIN_MENU, new MainMenu()); }

    /**
     * This method sets the current screen using the SCREEN_STATE enum. It also calls the hide method for the previous
     * screen and the show method for the current screen.
     * @param screen
     */
    public void setScreen(SCREEN_STATE screen){
        if(screen == SCREEN_STATE.GAME_OVER){
            screens.get(currentScreen).setGameOver(true);
        }
        else if(screen == SCREEN_STATE.GAME_WON){
            screens.get(currentScreen).setGameWon(true);
        }
        else {
            screens.get(currentScreen).hide();
            currentScreen = screen;

            screens.get(screen).show();
            screens.get(screen).resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    /**
     * This method will update the camera for any screens that follow the player. The vector2 is the players position.
     * @param vector2
     */
    public void updateCam(Vector2 vector2){
        if(currentScreen == SCREEN_STATE.RESCUE_MISSION_SCREEN) {
            getCurrentScreen().updateCam(vector2.x, vector2.y);
        }
    }

    public AbstractScreen getCurrentScreen() { return screens.get(currentScreen); }

    public Stage getScreenStage(SCREEN_STATE screen){ return screens.get(screen).getStage(); }

    public SCREEN_STATE getCurrentScreenState(){ return currentScreen; }

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
        if(debugging) {
            System.out.println(screenX + ", " + screenY);
        }
        boolean returnValue = false;
        String temp = screens.get(currentScreen).getButtonPressed(screenX, screenY);
        switch (temp){
            case"start":
                setScreen(SCREEN_STATE.RESCUE_MISSION_SCREEN);
                returnValue = true;
                break;
            case"login":
                break;
            case"backToMain":
                setScreen(SCREEN_STATE.MAIN_MENU);
                returnValue = true;
                break;
        }
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

    /**
     * This method is called when the game is destroyed.
     */
    public void dispose(){
        for(AbstractScreen screen : screens.values()){
            if(screen != null)
                screen.dispose();
        }
    }
}

