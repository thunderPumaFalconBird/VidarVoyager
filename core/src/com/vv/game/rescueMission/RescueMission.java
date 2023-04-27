package com.vv.game.rescueMission;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.vv.game.rescueMission.entities.movable.Astronaut;
import com.vv.game.rescueMission.puzzles.Puzzle;


/**
 * This is the Rescue Mission Class. It is the main game class. All information about the levels are stored in the Level
 * class. There is currently only one level.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */
public class RescueMission {
    private Astronaut player;
    private Array<Level> levels;
    private int currentLevelIndex = 0;

    /**
     * This rescue mission constructor initializes the first level.
     */
    public RescueMission(){
        levels = new Array<>();
        levels.add(new Level(1));
    }

    /**
     * This method initializes the player. It is used to place the player in the current level and activate the input
     * processor.
     * @param stage
     */
    public void initPlayer(Stage stage){
        player = new Astronaut(stage,
                levels.get(currentLevelIndex).getWorld(),
                new Vector2(levels.get(currentLevelIndex).getPlayerStartPosition().x,
                        levels.get(currentLevelIndex).getPlayerStartPosition().y));
        //add the players input processor to the multiplexer
        stage.addActor(player);
    }

    public TiledMap getMap(){ return levels.get(currentLevelIndex).getMap(); }

    public World getWorld(){ return levels.get(currentLevelIndex).getWorld(); }

    public Vector2 getPlayerPosition(){ return player.getBody().getPosition(); }

    public Puzzle getPuzzle(){ return levels.get(currentLevelIndex).getActivePuzzle(); }

    /**
     * This method adds any drawable actors in the current level that are not playable.
     * @param stage
     */
    public void addLevelActors(Stage stage){ levels.get(currentLevelIndex).addActors(stage); }

    /**
     * This method is called based on the APP_FPS (frames per second) to update the current level and player.
     */
    public void update(){
        levels.get(currentLevelIndex).update();
        player.update();
    }

    /**
     * This method checks if the players current state is dead.
     * @return
     */
    public boolean checkForDeath() { return (player.getCurrentState() == Astronaut.STATE.dead); }

    /**
     * This method is called to check if the main task of the current level is complete.
     * @return
     */
    public boolean checkForWin() { return levels.get(currentLevelIndex).checkForWin(); }

    /**
     * This method is used to remove the input processing of the player from the multiplexer.
     * @param multiplexer
     */
    public void removeAstroMultiplexer(InputMultiplexer multiplexer){ player.removeMultiplexer(multiplexer); }

    /**
     * This method is used to remove the input processing of the current puzzle from the multiplexer.
     * @param multiplexer
     */
    public void removePuzzleMultiplexer(InputMultiplexer multiplexer){
        multiplexer.removeProcessor(levels.get(currentLevelIndex).getActivePuzzle());
    }

    /**
     * This method is used to add the input processing of the player from the multiplexer.
     * @param multiplexer
     */
    public void addAstroMultiplexer(InputMultiplexer multiplexer){ multiplexer.addProcessor(player); }

    /**
     * This method is used to add the input processing of the current puzzle from the multiplexer.
     * @param multiplexer
     */
    public void addPuzzleMultiplexer(InputMultiplexer multiplexer){
        multiplexer.addProcessor(levels.get(currentLevelIndex).getActivePuzzle());
    }

    /**
     * This method is called when the game is destroyed.
     */
    public void dispose () {
        for(int i = 0; i < levels.size; i++){
            levels.get(i).dispose();
        }
    }
}
