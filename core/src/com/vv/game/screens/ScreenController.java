package com.vv.game.screens;

import com.badlogic.gdx.physics.box2d.World;
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

    public ScreenController(final VidarVoyager game) {
        this.game = game;
        init();
        setScreen(SCREEN_STATE.MAIN_MENU);
    }

    private void init(){
        this.screens = new EnumMap<>(SCREEN_STATE.class);
        this.screens.put(SCREEN_STATE.GAME_SCREEN, new GameScreen());
        this.screens.put(SCREEN_STATE.MAIN_MENU, new MainMenu());
    }

    public void setScreen(SCREEN_STATE screen){
        currentScreen = screen;
        game.setScreen(screens.get(screen));
    }

    public void dispose(){
        for(AbstractScreen screen : screens.values()){
            if(screen != null)
                screen.dispose();
        }
    }

    public World getWorld(SCREEN_STATE screen){
        return screens.get(screen).world;
    }

    public AbstractScreen getScreen() { return screens.get(currentScreen); }
}

