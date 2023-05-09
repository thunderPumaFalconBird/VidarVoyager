package com.vv.game.utils;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.vv.game.madPlanets.MadPlanets;
import com.vv.game.rescueMission.RescueMission;
import com.vv.game.utils.ScreenController.SCREEN_STATE;

/**
 * This is the Game Controller Class. It is the Model in the Model View Presenter design. It contains rescueMission and
 * MadPlanets which are two models. They contain the data for the games.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public class GameController {
    private static GameController instance = new GameController();
    private final RescueMission rescueMission;
    private InputMultiplexer multiplexer;
    private MadPlanets madPlanets;
    private SCREEN_STATE currentScreen = SCREEN_STATE.MAIN_MENU;

    /**
     * The GameController constructor creates all the games. The multiplexer reference is used to add or remove input
     * processors for any playable characters.
     */
    private GameController(){
        rescueMission = new RescueMission();
    }

    /**
     * The multiplexer reference is used to add or remove input processors for any playable characters.
     * @param multiplexer
     */
    public void initMultiplexer(InputMultiplexer multiplexer) { this.multiplexer = multiplexer; }

    public static GameController getInstance() { return instance; }


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
        World tempWorld = rescueMission.getWorld();
        switch (screen_state){
            case RESCUE_MISSION_SCREEN: tempWorld = rescueMission.getWorld();
                break;
            case MAD_PLANETS_SCREEN: //TODO finish this once madPlanets is done
                break;
        }
        return tempWorld;
    }

    /**
     * This method checks the appropriate conditions for the current game being over and returns a boolean
     * @return
     */
    public boolean isGameOver() {
        boolean over = false;
        switch (currentScreen){
            case RESCUE_MISSION_SCREEN:
                over = rescueMission.checkForDeath();
                break;
            case MAD_PLANETS_SCREEN: //TODO finish this once madPlanets is done
                break;
            case PUZZLE_SCREEN:
                if(rescueMission.getPuzzle() != null) {
                    over = rescueMission.getPuzzle().isSolved();
                }
                break;
        }
        return over;
    }

    /**
     * This method checks the appropriate conditions for the current game being won and returns a boolean
     * @return
     */
    public boolean isGameWon() {
        boolean won = false;
        switch (currentScreen){
            case RESCUE_MISSION_SCREEN:
                won = rescueMission.checkForWin();
                break;
            case MAD_PLANETS_SCREEN: //TODO finish this once madPlanets is done
                break;
            case PUZZLE_SCREEN:
                if(rescueMission.getPuzzle() != null) {
                    won = rescueMission.getPuzzle().isSolved();
                }
                break;
        }
        return won;
    }

    /**
     * This method is called to add the actors that need to be displayed to the stage of it's corresponding screen.
     * @param screen_state
     * @param stage
     */
    public void addActors(SCREEN_STATE screen_state, Stage stage){
        switch (screen_state){
            case RESCUE_MISSION_SCREEN:
                rescueMission.addLevelActors(stage);
                //Add the player last. Everything in the stage is drawn in the order it was added.
                rescueMission.initPlayer(stage);
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
        }
        else if(currentScreen == SCREEN_STATE.MAD_PLANETS_SCREEN){
            //TODO finish this once madPlanets is done
            int done = 0;
        }
        else if(currentScreen == SCREEN_STATE.PUZZLE_SCREEN){
            if(rescueMission.getPuzzle() != null && !rescueMission.getPuzzle().isSolved()) {
                rescueMission.getPuzzle().update();
            }
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
    public void setCurrentGame(SCREEN_STATE screenState){
        if(currentScreen!= screenState){
            //remove old processor
            switch (currentScreen){
                case RESCUE_MISSION_SCREEN:
                    rescueMission.removeAstroMultiplexer(multiplexer);
                    break;
                case MAD_PLANETS_SCREEN: //TODO finish this once madPlanets is done
                    break;
                case PUZZLE_SCREEN:
                    rescueMission.removePuzzleMultiplexer(multiplexer);
                    break;
            }

            currentScreen = screenState;

            //add new processor
            switch (screenState){
                case RESCUE_MISSION_SCREEN:
                    rescueMission.addAstroMultiplexer(multiplexer);
                    break;
                case MAD_PLANETS_SCREEN: //TODO finish this once madPlanets is done
                    break;
                case PUZZLE_SCREEN:
                    rescueMission.addPuzzleMultiplexer(multiplexer);
                    break;
            }
        }
    }

    /**
     * This method is called when the game is destroyed.
     */
    public void dispose(){
        rescueMission.dispose();
    }

}
