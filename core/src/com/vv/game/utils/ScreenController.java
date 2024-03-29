package com.vv.game.utils;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.vv.game.screens.*;

import java.util.EnumMap;

/**
 * This is the Screen Controller Class. It creates the screens that will be used and sets the proper screen. Screens
 * are stored in an enum map with the enum SCREEN_STATE as the key. It implements the InputProcessor class so that it
 * can handle UI inputs.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public class ScreenController  {
    /* This enumeration is used as keys for the enumMap that holds the screens */
    public enum SCREEN_STATE {
        MAIN_MENU,
        RESCUE_MISSION_SCREEN,
        PUZZLE_SCREEN,
        GAME_OVER,
        GAME_WON,
        NEW_GAME,
        EXIT
    }
    private InputMultiplexer multiplexer;
    private SCREEN_STATE currentScreen = SCREEN_STATE.MAIN_MENU;
    private EnumMap<SCREEN_STATE, AbstractScreen> screens;

    /**
     * The ScreenController constructor creates the enumMap of the screens. The screens are added to the enumMap using
     * init methods below. This constructor is private because screenController is a singleton.
     */
    public ScreenController() {
        this.screens = new EnumMap<>(SCREEN_STATE.class);
        this.screens.put(SCREEN_STATE.MAIN_MENU, new MainMenu());
        this.screens.put(SCREEN_STATE.PUZZLE_SCREEN, new PuzzleScreen());
        this.screens.put(SCREEN_STATE.RESCUE_MISSION_SCREEN, new RescueMissionScreen());
        this.screens.put(SCREEN_STATE.GAME_WON, new KillScreen());
    }

    public AbstractScreen getCurrentScreen() { return screens.get(currentScreen); }

    public Stage getScreenStage(SCREEN_STATE screen){ return screens.get(screen).getStage(); }

    public SCREEN_STATE getCurrentScreenState(){ return currentScreen; }

    public void loadLevel(World world, TiledMap map, Array<TextureRegion> instructions){
        RescueMissionScreen rescueMissionScreen = (RescueMissionScreen) screens.get(SCREEN_STATE.RESCUE_MISSION_SCREEN);
        rescueMissionScreen.loadLevel(world, map, instructions);
    }


    /**
     *  The multiplexer is set and the screenController is also added to the multiplexer so that input can be handled
     *  for UI.
     * @param multiplexer
     */
    public void initMultiplexer(InputMultiplexer multiplexer){
        this.multiplexer = multiplexer;
        screens.get(currentScreen).initMultiplexer(multiplexer);
    }

    /**
     * This method sets the current screen using the SCREEN_STATE enum. It also calls the hide method for the previous
     * screen and the show method for the current screen.
     * @param screen
     */
    public void setScreen(SCREEN_STATE screen){
        if(screen != currentScreen) {
            if (screen == SCREEN_STATE.GAME_OVER) {
                RescueMissionScreen RMS = (RescueMissionScreen) screens.get(currentScreen);
                RMS.setGameOver(true);
            } else if (screen == SCREEN_STATE.EXIT || screen == SCREEN_STATE.NEW_GAME){
                screens.get(currentScreen).removeMultiplexer(multiplexer);
                currentScreen = screen;
            }
            else {
                screens.get(currentScreen).removeMultiplexer(multiplexer);
                screens.get(currentScreen).hide();
                currentScreen = screen;

                screens.get(currentScreen).initMultiplexer(multiplexer);
                screens.get(screen).show();
                screens.get(screen).resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            }
        }
    }

    /**
     * This method will update the controller. it updates the camera for any screens that follow the player. The
     * vector2 is the players position.
     * @param vector2
     */
    public void update(Vector2 vector2){
        if(currentScreen == SCREEN_STATE.RESCUE_MISSION_SCREEN) {
            getCurrentScreen().updateCam(vector2.x, vector2.y);
        }

        //if buttons pressed change screen.
        switch (screens.get(currentScreen).getButtonPressed()){
            case "start":
                if(currentScreen == SCREEN_STATE.MAIN_MENU) {
                    setScreen(SCREEN_STATE.RESCUE_MISSION_SCREEN);
                }
                break;
            case "back":
                if(currentScreen == SCREEN_STATE.PUZZLE_SCREEN) {
                    setScreen(SCREEN_STATE.RESCUE_MISSION_SCREEN);
                }
                else if(currentScreen == SCREEN_STATE.RESCUE_MISSION_SCREEN){
                    setScreen(SCREEN_STATE.MAIN_MENU);
                }
                break;
            case "newGame":
                setScreen(SCREEN_STATE.NEW_GAME);
                break;
            case "exit":
                setScreen(SCREEN_STATE.EXIT);
                break;
        }
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

