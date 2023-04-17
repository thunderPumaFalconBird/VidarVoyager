package com.vv.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import java.util.EnumMap;

/**
 * This is the Screen Controller Class. It creates the screens that will be used and sets the proper screen. Screens
 * are stored in an enum map with the enum SCREEN_STATE as the key.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public class ScreenController {
    public enum SCREEN_STATE {
        MAIN_MENU,
        RESCUE_MISSION_SCREEN,
        MAD_PLANETS_SCREEN,
        GAME_OVER,
        GAME_WON
    }
    private SCREEN_STATE currentScreen = SCREEN_STATE.MAIN_MENU;
    private EnumMap<SCREEN_STATE, AbstractScreen> screens;

    public ScreenController() {
        this.screens = new EnumMap<>(SCREEN_STATE.class);
    }

    public void initRescueMissionScreen(World world, TiledMap map){
        this.screens.put(SCREEN_STATE.RESCUE_MISSION_SCREEN, new RescueMissionScreen(map, world));
    }
    public void initMainMenu(){ this.screens.put(SCREEN_STATE.MAIN_MENU, new MainMenu()); }

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

    public Screen getScreen(SCREEN_STATE screen){ return screens.get(screen); }

    public AbstractScreen getCurrentScreen() { return screens.get(currentScreen); }

    public Stage getScreenStage(SCREEN_STATE screen){ return screens.get(screen).getStage(); }

    public void updateCam(Vector2 vector2){ getCurrentScreen().updateCam(vector2.x,vector2.y); }

    public void setButtonPressed(float x, float y){
        String temp = screens.get(currentScreen).getButtonPressed(x, y);
        switch (temp){
            case"start": setScreen(SCREEN_STATE.RESCUE_MISSION_SCREEN);
                break;
            case"login":
                break;
            case"backToMain": setScreen(SCREEN_STATE.MAIN_MENU);
                break;
        }
    }

    public SCREEN_STATE getCurrentScreenState(){ return currentScreen; }

    public void dispose(){
        for(AbstractScreen screen : screens.values()){
            if(screen != null)
                screen.dispose();
        }
    }
}

