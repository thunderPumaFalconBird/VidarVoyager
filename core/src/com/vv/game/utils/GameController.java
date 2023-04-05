package com.vv.game.utils;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.vv.game.madPlanets.MadPlanets;
import com.vv.game.rescueMission.RescueMission;
import com.vv.game.screens.ScreenController.SCREEN_STATE;

/**
 * This is the Game Controller Class. It is the Model in the Model View Presenter design. It contains rescueMission and
 * MadPlanets which are two models. They contain the data for the games.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public class GameController {
    private final RescueMission rescueMission;
    private MadPlanets madPlanets;
    private SCREEN_STATE currentScreen = SCREEN_STATE.RESCUE_MISSION_SCREEN;


    public GameController(){
        rescueMission = new RescueMission();
    }


    public TiledMap getMap(SCREEN_STATE screen_state){
        TiledMap tempMap = rescueMission.getMap();;
        switch (screen_state){
            case RESCUE_MISSION_SCREEN: tempMap = rescueMission.getMap();
                break;
            case MAD_PLANETS_SCREEN: //TODO  finish this once madPlanets is done
                break;
        }
        return tempMap;
    }

    public World getWorld(SCREEN_STATE screen_state){
        World tempWorld = rescueMission.getWorld();;
        switch (screen_state){
            case RESCUE_MISSION_SCREEN: tempWorld = rescueMission.getWorld();
                break;
            case MAD_PLANETS_SCREEN: //TODO finish this once madPlanets is done
                break;
        }
        return tempWorld;
    }

    public void addActors(SCREEN_STATE screen_state, Stage stage){
        switch (screen_state){
            case RESCUE_MISSION_SCREEN:
                rescueMission.initPlayer(stage);
                rescueMission.addActors(stage);
                break;
            case MAD_PLANETS_SCREEN: //TODO finish this once madPlanets is done
                break;
        }
    }

    public void update(){
        if(currentScreen == SCREEN_STATE.RESCUE_MISSION_SCREEN){
            rescueMission.update();
        }
        else if(currentScreen == SCREEN_STATE.MAD_PLANETS_SCREEN){
            //TODO finish this once madPlanets is done
            int done = 0;
        }
    }

    public Vector2 getCamUpdate(){
        Vector2 tempVec = rescueMission.getPlayerPosition();

        if(currentScreen == SCREEN_STATE.RESCUE_MISSION_SCREEN){
            tempVec = rescueMission.getPlayerPosition();
        }
        else if(currentScreen == SCREEN_STATE.MAD_PLANETS_SCREEN){
            //TODO finish this once madPlanets is done
            int done = 0;
        }
        return tempVec;
    }

    public void setCurrentScreen(SCREEN_STATE screenState){
        currentScreen = screenState;
    }

    public void dispose(){
        rescueMission.dispose();
    }

}
