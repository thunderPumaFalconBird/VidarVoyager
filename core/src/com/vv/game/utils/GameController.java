package com.vv.game.utils;

import com.badlogic.gdx.InputMultiplexer;
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
    private final InputMultiplexer multiplexer;
    private final RescueMission rescueMission;
    private MadPlanets madPlanets;
    private SCREEN_STATE currentScreen = SCREEN_STATE.MAIN_MENU;
    private boolean gameOver = false;
    private boolean gameWon = false;

    /**
     * The GameController constructor creates all the games. The multiplexer reference is used to add or remove input
     * processors for any playable characters.
     * @param multiplexer
     */
    public GameController(InputMultiplexer multiplexer){
        this.multiplexer = multiplexer;
        rescueMission = new RescueMission();
    }


    /**
     * This method is called to get the current map of the corresponding screen.
     * @param screen_state
     * @return
     */
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

    /**
     * This method is called to get the current world of the corresponding screen.
     * @param screen_state
     * @return
     */
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

    public boolean isGameOver() { return gameOver; }

    public boolean isGameWon() { return gameWon; }

    /**
     * This method is called to add the actors that need to be displayed to the stage of it's corresponding screen.
     * @param screen_state
     * @param stage
     */
    public void addActors(SCREEN_STATE screen_state, Stage stage){
        switch (screen_state){
            case RESCUE_MISSION_SCREEN:
                rescueMission.initPlayer(stage, multiplexer);
                rescueMission.addActors(stage);
                break;
            case MAD_PLANETS_SCREEN: //TODO finish this once madPlanets is done
                break;
        }
    }

    /**
     * This method is used to call the update method of the current game.
     */
    public void update(){
        if(currentScreen == SCREEN_STATE.RESCUE_MISSION_SCREEN){
            rescueMission.update();
            if(rescueMission.checkForDeath()){
                gameOver = true;
            }
            else if(rescueMission.checkForWin()){
                gameWon = true;
            }
        }
        else if(currentScreen == SCREEN_STATE.MAD_PLANETS_SCREEN){
            //TODO finish this once madPlanets is done
            int done = 0;
        }
    }

    /**
     * This method is called to get the player position if the camera follows the players position. It resturns the
     * player's position.
     * @return
     */
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

    /**
     * This method is used to set the current game based on UI input for the screen controller.
     * @param screenState
     */
    public void setCurrentGame(SCREEN_STATE screenState){ currentScreen = screenState; }

    /**
     * This method is called when the game is destroyed.
     */
    public void dispose(){
        rescueMission.dispose();
    }

}
