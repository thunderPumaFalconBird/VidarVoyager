package com.vv.game.rescueMission;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.vv.game.rescueMission.entities.movable.Astronaut;


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

    public RescueMission(){
        levels = new Array<>();
        levels.add(new Level(1));
    }

    public void initPlayer(Stage stage){
        player = new Astronaut(stage,
                levels.get(currentLevelIndex).getWorld(),
                new Vector2(levels.get(currentLevelIndex).getPlayerStartPosition().x,
                        levels.get(currentLevelIndex).getPlayerStartPosition().y));
    }

    public TiledMap getMap(){ return levels.get(currentLevelIndex).getMap(); }

    public World getWorld(){ return levels.get(currentLevelIndex).getWorld(); }

    public void addActors(Stage stage){
        levels.get(currentLevelIndex).addActors(stage);
        stage.addActor(player);
    }

    public void update(){
        levels.get(currentLevelIndex).update();
        player.update();
    }

    public Vector2 getPlayerPosition(){
        return player.getBody().getPosition();
    }

    public void dispose () {
        for(int i = 0; i < levels.size; i++){
            levels.get(i).dispose();
        }
    }
}
