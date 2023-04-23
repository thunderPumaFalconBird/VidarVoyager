package com.vv.game.rescueMission;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.vv.game.rescueMission.entities.collectable.Collectable;
import com.vv.game.rescueMission.entities.collectable.TeddyBear;
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

    public void initPlayer(Stage stage, InputMultiplexer multiplexer){
        player = new Astronaut(stage,
                levels.get(currentLevelIndex).getWorld(),
                new Vector2(levels.get(currentLevelIndex).getPlayerStartPosition().x,
                        levels.get(currentLevelIndex).getPlayerStartPosition().y));
        //add the players input processor to the multiplexer
        multiplexer.addProcessor(player);
    }

    public TiledMap getMap(){ return levels.get(currentLevelIndex).getMap(); }

    public World getWorld(){ return levels.get(currentLevelIndex).getWorld(); }

    public Astronaut getPlayer(){ return player; }

    public void addActors(Stage stage){
        levels.get(currentLevelIndex).addActors(stage);
        stage.addActor(player);
    }

    public void update(){
        levels.get(currentLevelIndex).update();
        player.update();
    }

    public Vector2 getPlayerPosition(){ return player.getBody().getPosition(); }

    public boolean checkForDeath() {
        return (player.getCurrentState() == Astronaut.STATE.dead);
    }

    //TODO fix this. The collision handler has the same code... what is this?
    public boolean checkForWin() {
        boolean temp = false;
        if(levels.get(currentLevelIndex).checkForWin()){
            boolean[] bears = {false, false, false, false, false};

            Array<Collectable> tempInventory = player.getInventory();
            for (int i = 0; i < tempInventory.size; i++){
                if(tempInventory.get(i).getBody().getUserData() instanceof TeddyBear){
                    TeddyBear tempBear = (TeddyBear) tempInventory.get(i).getBody().getUserData();
                    TeddyBear.COLOR tempColor = tempBear.getTeddyColor();
                    if(tempColor == TeddyBear.COLOR.BLUE){
                        bears[0] = true;
                    }
                    else if(tempColor == TeddyBear.COLOR.GREEN){
                        bears[1] = true;
                    }
                    else if(tempColor == TeddyBear.COLOR.ORANGE){
                        bears[2] = true;
                    }
                    else if(tempColor == TeddyBear.COLOR.PINK){
                        bears[3] = true;
                    }
                    else if(tempColor == TeddyBear.COLOR.RED){
                        bears[4] = true;
                    }
                }
            }
            if(bears[0] && bears[1] && bears[2] && bears[3] && bears[4]){
                temp = true;
            }
            else{
                levels.get(currentLevelIndex).resetWinFlag();
            }
        }
        return temp;
    }

    public void dispose () {
        for(int i = 0; i < levels.size; i++){
            levels.get(i).dispose();
        }
    }
}
