package com.vv.game.entities;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

/**
 * This is the Level Class. It contains the map and any objects that go along with the map. The map is loaded based
 * on the level number. Maps will be named after the level they belong to.
 *
 * @author thunderPumaFalconBird
 * @version 1.0
 */

public class Level {
    private int levelNumber;
    private final TmxMapLoader mapLoader;
    private final TiledMap map;

    public Level(int levelNumber){
        this.levelNumber = levelNumber;
        mapLoader = new TmxMapLoader();
        map = mapLoader.load(("maps/Level" + levelNumber + ".tmx"));

        for (int i = 0; i < map.getLayers().size(); i++) {
            String layername = map.getLayers().get(i).getName();
            //TODO add objects
        }
    }

    public TiledMap getMap() { return map; }
}
