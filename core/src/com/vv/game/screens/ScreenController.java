package com.vv.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.vv.game.VidarVoyager;
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
        GAME_SCREEN
    }
    public final VidarVoyager game;
    private SCREEN_STATE currentScreen;
    private EnumMap<SCREEN_STATE, AbstractScreen> screens;

    public ScreenController(VidarVoyager game) {
        this.game = game;
        init();
        setScreen(SCREEN_STATE.MAIN_MENU);
    }

    private void init(){
        this.screens = new EnumMap<>(SCREEN_STATE.class);
        this.screens.put(SCREEN_STATE.GAME_SCREEN, new GameScreen(game.getCurrentLevel().getMap(),
                game.getCurrentLevel().getWorld()));
        this.screens.put(SCREEN_STATE.MAIN_MENU, new MainMenu(game.getCurrentLevel().getWorld()));
    }

    public void setScreen(SCREEN_STATE screen){
        currentScreen = screen;
        game.setScreen(screens.get(screen));
    }

    public Screen getScreen(SCREEN_STATE screen){ return screens.get(screen); }

    public AbstractScreen getCurrentScreen() { return screens.get(currentScreen); }
    public Stage getScreenStage(SCREEN_STATE screen){ return screens.get(screen).getStage(); }

    public void updateCam(float x, float y){
        getCurrentScreen().updateCam(x,y);
    }

    public SCREEN_STATE getCurrentScreenState(){ return currentScreen; }

    public void dispose(){
        for(AbstractScreen screen : screens.values()){
            if(screen != null)
                screen.dispose();
        }
    }
}

