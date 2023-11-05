package com.vv.game.rescueMission.levels;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

public interface Level {

    public abstract boolean checkForWin();

    public abstract TiledMap getMap();

    public abstract World getWorld();

    public abstract Vector2 getPlayerStartPosition();

    public abstract void addActors(Stage stage);

    public abstract void update();

    public abstract Array<TextureRegion> getInstructions();
}
